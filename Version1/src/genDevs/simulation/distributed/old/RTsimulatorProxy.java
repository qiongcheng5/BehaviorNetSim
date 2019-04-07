

package genDevs.simulation.distributed.old;

import GenCol.*;

import java.util.*;
import java.io.*;
import java.net.*;
import genDevs.modeling.*;
import genDevs.simulation.*;

public class RTsimulatorProxy extends simulatorProxy {
             //   implements CoupledRTSimulatorInterface{



public RTsimulatorProxy( Socket s, RTcoordServer srvr) {
super(s,srvr);
}



   public  synchronized void sendMessages(message output) {
     srvr.done();
  if (output!=null && !output.isEmpty()) {
   Relation r = convertMsg(output);//assume computeInputOutput done first
   //r.print();
   Iterator rit = r.iterator();
   while (rit.hasNext()){
   Pair p = (Pair)rit.next();
   Object ds = p.getKey();
   content co = (content)p.getValue();
   //co.print();
   RTsimulatorProxy cn  = (RTsimulatorProxy)modelToSim.get(ds);
   if(cn!=null) cn.putMessages(co);
   else srvr.putMyMessages(co);     // this goes to the root server
   }
  }
  }


public long timeInSecs() {
    return (timeInMillis()/1000);
}
public long timeInMillis() {
    return System.currentTimeMillis();
}


}

