package genDevs.simulation.distributed;

import java.io.*;
import java.net.*;
import java.util.*;
import GenCol.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import util.*;

/**
 * A server-side proxy for a remote client simulator, on which the server
 * in a distributed simulation may make calls.
 */
public class TunableSimulatorProxy extends coupledSimulator
{
    /**
     * The input stream for receiving input from the client simulator.
     */
    protected DataInputStream inputStream;

    /**
     * The print stream for sending output to the client simulator.
     */
    protected PrintStream printStream;

    /**
     * The server to which this object acts as a proxy for a
     * remote client simulator.
     */
    protected TunableCoordinatorServer server;

    /**
     * Whether this proxy should quit listening for input to pass along from
     * the client simulator to the server.
     */
    protected boolean quit = false;

    /**
     * Constructs an object of this class.
     *
     * @param   socket      The server's connection to this proxy's client
     *                      simulator.
     * @param   server_     The server to which this object is to act as a
     *                      proxy for a client simulator.
     */
    public TunableSimulatorProxy(Socket socket, TunableCoordinatorServer server_)
    {
        server = server_;

        try {
            // open input and output streams on the given socket
            inputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(
                new DataOutputStream(socket.getOutputStream()));
        } catch (IOException e) {s.e(e);}

        // get this proxy's run() method executed
        (new ListenForClientMessagesThread()).start();
    }

    /**
     * Tells this proxy to stop listening for messages from the client.
     */
    public void stop()
    {
        quit = true;
    }

    /**
     * A thread that listens for messages from the client simulator.
     */
    protected class ListenForClientMessagesThread extends Thread
    {
        public void run()
        {
            waitForSimulatorName();

            // keep passing along messages from the client simulator to the
            // server
            while (!quit) {
                waitForMessageFromClient();
            }

            s.s("TunableSimulatorProxy terminated");
        }
    }

    /**
     * Reads in the name of the client simulator's model, then associates
     * this proxy with the corrsponding model in the server.
     */
    protected void waitForSimulatorName()
    {
        // read in the next message from the client simulator, which is
        // assumed to be the name of the simulator's model
        s.s("waiting for name");
        String name = readMessageFromClient();

        // associate this proxy with the corresponding model being run
        // on the server side
        myModel = (IOBasicDevs)server.getCoupled().withName(name);
        server.registerSimulatorProxy(this, name);
        s.s("name is : " + name);
    }

    /**
     * Waits for a message from the client simulator, then passes that
     * message on to the server or to other proxies.
     */
    protected void waitForMessageFromClient()
    {
        // wait for the next message from the client simulator
        String string = readMessageFromClient();
        //s.s("proxy for " + myModel.getName() + " received message from client: " + string);
        message message = Util.interpret(string);

        // if the message is non-empty
        if (!message.isEmpty()) {
            // for each content-destination pair in the output message
            Iterator i = convertMsg(message).iterator();
            while (i.hasNext()) {
                Pair pair = (Pair)i.next();

                // if we can find the proxy of the simulator of the
                // destination component of this content
                content content = (content)pair.getValue();
                TunableSimulatorProxy proxy = (TunableSimulatorProxy)
                    modelToSim.get(pair.getKey());
                if (proxy != null) {
                    // pass the proxy this content
                    proxy.putMessages(content);
                }

                // otherwise
                else {
                    // pass the content to the server
                    server.putMyMessages(content);
                }
            }
        }
    }

    /**
     * Overrides the parent class method's behavior to send this proxy's
     * current input message, augmented with the given content, to
     * the client simulator.
     *
     * @param   content     The content to add to the input message before
     *                      sending that message off.
     */
    public void putMessages(ContentInterface content)
    {
        // send this proxy's current output message, augmented
        // with the given content, to the client simulator
        input.add(content);
        sendInputToClient();

        // reset the input message
        input = new message();
    }

    /**
     * Sends this proxy's current input message off to its client simulator.
     */
    protected void sendInputToClient()
    {
        // if this proxy's input message is non-empty
        if (!input.isEmpty()) {
            // send the input message off to the client simulator
            String string = input.toString();
            //s.s("sendInput:" + string);
            sendMessage(string);
        }
    }

    /**
     * A shorthand method. It is public since the server makes reflection
     * calls on it.
     */
    public void sendMessage(String message) {printStream.println(message);}

    /**
     * Returns the next line read from this proxy's input-stream coming from
     * the client simulator.
     */
    protected String readMessageFromClient()
    {
        try {
            return inputStream.readLine();
        } catch (IOException e) {s.e(e); return "";}
    }
}
