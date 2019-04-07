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
 * A real-time simulator that can interface with a remote coordinator-server
 * as part of a distributed real-time simulation.
 */
public class RTCoupledSimulatorClient extends coupledRTSimulator
{
    /**
     * The client aspect of this class, which allows for communication
     * with a server.
     */
    protected Client client;

    /**
     * Constructs an object of this class.
     *
     * @param   devs            The devs component upon which this simulator
     *                          will operate.
     * @param   serverAddress   The server's IP address.
     * @param   serverPort      The server's port number.
     */
    public RTCoupledSimulatorClient(IOBasicDevs devs, String serverAddress,
        int serverPort)
    {
        super(devs);

        // create the client aspect of this simulator
        client = new Client(devs.getName(), serverAddress, serverPort);
    }

    /**
     * Overriding the parent class behavior, this sends this simulator's
     * current output message to the server, rather than to other local
     * simulators or coordinators.
     */
    public void sendMessages()
    {
        // if this simulator's output message is non-empty
        if (!output.isEmpty()) {
            // send the output message to the server
            client.sendMessageToServer(output.toString());

            // clear the output message
            output = new message();
        }
    }

    /**
     * add coupling dynamically
     * use : to seperate the addCouplingSymbol, src, p1, dest, and p2
    */
    public void addDistributedCoupling(String src, String p1, String dest, String p2){
      String dcc = Constants.addCouplingSymbol+":"+src+":"+p1+":"+dest+":"+p2;
      System.out.println("RTCoupledSimulatorClient add Coupling---"+dcc);
      client.sendMessageToServer(dcc);
    }

    /**
     * remove coupling dynamically
     * use : to seperate the addCouplingSymbol, src, p1, dest, and p2
    */
    public void removeDistributedCoupling(String src, String p1, String dest, String p2){
      String dcc = Constants.removeCouplingSymbol+":"+src+":"+p1+":"+dest+":"+p2;
      System.out.println("RTCoupledSimulatorClient remove Coupling---"+dcc);
      client.sendMessageToServer(dcc);
    }

    /**
     * See parent class.
     */
    protected class Client extends genDevs.simulation.distributed.Client
    {
        /**
         * See parent constructor.
         */
        public Client(String devsName, String serverAddress, int serverPort)
        {
            super(devsName, serverAddress, serverPort);
        }

        /**
         * See parent method.
         */
        protected void initializeMessageReceived(String message) {initialize();}

        /**
         * See parent method.
         */
        protected void startSimulateMessageReceived(int numIterations)
        {
            simulate(numIterations);
        }

        /**
         * See parent method.
         */
        protected void inputReceived(message message)
        {
            // make the given message this simulator's input message
//            input = message;   //Xiaolin Hu  Jun 21, 2004
            ContentIteratorInterface cit = message.mIterator();
            while (cit.hasNext()){
             content co = (content)cit.next();
             input.add(co);
            }

            // wake up the parent class run() method, which has been waiting
            // for an external input, so it can handle the message
            inputReady = true;
            synchronized (RTCoupledSimulatorClient.this) {
              RTCoupledSimulatorClient.this.notify();
            }
        }
    }
}
