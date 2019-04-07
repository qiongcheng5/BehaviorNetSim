/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

package genDevs.simulation.realTime;

import java.util.*;
import GenCol.*;
import genDevs.modeling.*;
import genDevs.simulation.*;

public class RTCentralCoord extends coordinator
                  implements RTCoordinatorInterface{
protected int numIter;
protected Thread myThread;
protected long timeToSleep;

public long timeInSecs() {
    return (timeInMillis()/1000);
}
public long timeInMillis() {
    return System.currentTimeMillis();
}


public RTCentralCoord(){ //for ease of inheritance
}

public RTCentralCoord(coupledDevs c){this(c, true, null);}

public RTCentralCoord(coupledDevs c, boolean setSimulators, Object dummyParameter){
super(c, setSimulators, dummyParameter);
myThread = new Thread(this);
}

public RTCentralCoord(coupledDevs c,boolean minimal){
super(c,minimal);
myThread = new Thread(this);

}

public void initialize(){
tL = timeInMillis()/1000;
tN = tL;  // initially set tN = tL, it is used when add a model in a model's initialize()  --- Xiaolin Hu
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {new Double(tL)};
simulators.tellAll("initialize",classes,args);
//check and update new or deleted simulators
//This is because we allow to add new Model (including simActivity model) in model's initialize()
updateChangedSimulators();
}

public void  simulate(int numIter)
{
  this.numIter = numIter;
  myThread.start();
}

public void tellAllStop(){
simulators.tellAll("stopSimulate");
}

public void run(){
  int i=1;
  tN = nextTN();
  while( (tN < INFINITY) && (i<=numIter) ) {
    while(timeInMillis() < getTN()*1000 - 10){
      timeToSleep = (long)(getTN()*1000 - timeInMillis());

      if(timeToSleep >= 0) {
          try {
               myThread.sleep(timeToSleep);
          } catch (Exception e) { continue; }
      }
    }
    computeInputOutput(tN);
    wrapDeltFunc(tN);
    tL = tN;
    tN = nextTN();
    showModelState();
    i++;
  }

  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

}
