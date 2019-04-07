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
public class SimulatorProxy extends coupledSimulator
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
    protected RTCoordinatorServer server;

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
    public SimulatorProxy(Socket socket, RTCoordinatorServer server_)
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

            s.s("SimulatorProxy terminated");
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
        if(string.startsWith(Constants.addCouplingSymbol)||
            string.startsWith(Constants.removeCouplingSymbol)) //this is a dynamic coupling change string
                DynamicCouplingStrReceived(string);
        else{     // this is a regular DEVS message
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
                SimulatorProxy proxy = (SimulatorProxy)
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
    }

    /**
     * process the dynamic coupling change string
     * : is the sepetator to seperate the CouplingSymbol, src, p1, dest, and p2
     */
    protected void DynamicCouplingStrReceived(String dccString){
        String[] dcc = new String[4];
        int index1, index2;
        index1 = dccString.indexOf(":")+1;
        for(int j =0;j<4;j++){
          index2 = dccString.indexOf(":",index1);
          if(index2==-1) index2=dccString.length();
          dcc[j]=dccString.substring(index1,index2);
          index1 = index2+1;
        }
        if(dccString.startsWith(Constants.addCouplingSymbol)){
          System.out.println("DCCADD:"+dcc[0]+":"+dcc[1]+":"+dcc[2]+":"+dcc[3]);
          server.addCoupling(dcc[0],dcc[1],dcc[2],dcc[3]);
        }
        else if(dccString.startsWith(Constants.removeCouplingSymbol)){
          System.out.println("DCCRMV:"+dcc[0]+":"+dcc[1]+":"+dcc[2]+":"+dcc[3]);
          server.removeCoupling(dcc[0],dcc[1],dcc[2],dcc[3]);
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
            //s.s("proxy for " + myModel.getName() + " sendInput:" + string);
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
