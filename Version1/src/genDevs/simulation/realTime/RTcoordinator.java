/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package genDevs.simulation.realTime;

import GenCol.*;

import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;




public class RTcoordinator extends coordinator
                  implements RTCoordinatorInterface{
protected int numIter,count;
protected Thread myThread;
protected double observeTime;
protected RTCoupledinjectThread injThread;
protected boolean started = false;

public long timeInSecs() {
    return (timeInMillis()/1000);
}
public long timeInMillis() {
    return System.currentTimeMillis();
}
public RTcoordinator(coupledDevs c){
super(c);
myThread = new Thread(this);
}

public RTcoordinator(coupledDevs c,boolean minimal){
super(c,minimal);
myThread = new Thread(this);
}

public void addSimulator(IOBasicDevs comp){
coupledRTSimulator s = new coupledRTSimulator(comp);
simulators.add(s);
s.setRTRootParent(this);      // set the parent
modelToSim.put(comp.getName(),s);
internalModelTosim.put(comp.getName(),s);
}

public void addCoordinator(Coupled comp){
RTcoupledCoordinator s = new RTcoupledCoordinator(comp);
s.setRTRootParent(this);       // set the parent
simulators.add(s);
modelToSim.put(comp.getName(),s);
internalModelTosim.put(comp.getName(),s);
// later will download modelToSim to its children and then will be updated by its parents
// so after initialization, modelToSim store the brother models and simulators
// internalModelTosim store its children models and simulators
}

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model is
        coupledRTSimulator s = new coupledRTSimulator(iod);
        s.setRTRootParent(this);
        internalModelTosim.put(iod.getName(),s);
        s.initialize();
        if(!started)  newSimulators.add(s);  //the model is created by a model's initilize()
        else{ // created at runtime, so start the model directly
          simulators.add(s);
          //download the new ModtoSim info to all the simulators
          Class [] classes = {ensembleBag.getTheClass("GenCol.Function")};
          Object [] args  = {internalModelTosim};
          simulators.tellAll("setModToSim",classes,args);
          classes = null; args = null;  // release memory
          s.simulate(numIter);
        }
    }
    else if(iod instanceof digraph){
        RTcoupledCoordinator s = new RTcoupledCoordinator((Coupled) iod);
        s.setRTRootParent(this);
        internalModelTosim.put(iod.getName(),s);
        s.initialize();
        if(!started) newSimulators.add(s);
        else{
          simulators.add(s);
          //download the new ModtoSim info to all the simulators
          Class [] classes = {ensembleBag.getTheClass("GenCol.Function")};
          Object [] args  = {internalModelTosim};
          simulators.tellAll("setModToSim",classes,args);
          classes = null; args = null;  // release memory
          s.simulate(numIter);
        }
    }
}

public void removeModel(IODevs model){
       String modelName = model.getName();
       if(internalModelTosim.get(modelName) instanceof CoupledRTSimulatorInterface){
           CoupledRTSimulatorInterface sim = (CoupledRTSimulatorInterface)internalModelTosim.get(modelName);
           simulators.remove(sim);
           internalModelTosim.remove(modelName);
           ((coupledRTSimulator)sim).kill();
       }
       else if(internalModelTosim.get(modelName) instanceof RTcoupledCoordinator){
           RTcoupledCoordinator sim = (RTcoupledCoordinator)internalModelTosim.get(modelName);
           simulators.remove(sim);
           internalModelTosim.remove(modelName);
           sim.kill();
      }
      Class [] classes = {ensembleBag.getTheClass("GenCol.Function")};
      Object [] args  = {internalModelTosim};
      simulators.tellAll("setModToSim",classes,args);
      classes = null; args = null;  // release memory
}

//bpz
public void  simulate(int numIter){
simulate(numIter,DevsInterface.INFINITY);
}

public void  simulate(int numIter,double observeTime)
{
  this.numIter = numIter;
  this.observeTime = observeTime;
  started = true;
  tL = timeInSecs();
  tN = nextTN();
  tellAllSimulate(numIter);
  myThread.start();

  }

public void tellAllSimulate(int numIter){
Class [] classes  = {ensembleBag.getTheClass("java.lang.Integer")};
Object [] args  = {new Integer(numIter)};
simulators.tellAll("simulate",classes,args);
}

public void tellAllStop(){
simulators.tellAll("stopSimulate");
}


public void run(){

 try {
  Thread.currentThread().sleep((long)observeTime);
} catch (Exception e) {}
tellAllStop();
System.out.println("Coordinator Terminated Normally at time: " + timeInMillis());

}

public void wrapDeltFunc(double time) {   // used for external input inject
sendDownMessages();
input = new message();
output = new message();
}

public void sendDownMessages() {
  if(!input.isEmpty()){
    Relation r = convertInput(input);
    Iterator rit = r.iterator();
    while (rit.hasNext()){
       Pair p = (Pair)rit.next();
       Object ds = p.getKey();
       content co = (content)p.getValue();
       if(internalModelTosim.get(ds) instanceof CoupledRTSimulatorInterface){
           CoupledRTSimulatorInterface sim = (CoupledRTSimulatorInterface)internalModelTosim.get(ds);
           sim.putMessages(co);
       }
       else if(internalModelTosim.get(ds) instanceof RTcoupledCoordinator){
           RTcoupledCoordinator sim = (RTcoupledCoordinator)internalModelTosim.get(ds);
           sim.putMessages(co);
       }
    }
  }
}

public void simInject(double e,String portName,entity value){
simInject(e,new port(portName),value);
}

public void simInject(double e,PortInterface p,EntityInterface value){
MessageInterface m = makeMessage();
m.add(myModel.makeContent(p,value));
simInject(e,m);
}

public void simInject(double e,MessageInterface m){
injThread = new RTCoupledinjectThread(this,e,m);
}

}

class RTCoupledinjectThread extends Thread{
protected RTcoordinator sim;
protected double e;
protected MessageInterface m;
public RTCoupledinjectThread(RTcoordinator sim,double e,MessageInterface m){
this.sim = sim;
this.e = e;
this.m = m;
start();
}

public void run(){
try {
 sleep((long)e*1000);
} catch (Exception e) {}
sim.input = m;
sim.wrapDeltFunc(e);
System.out.println("Time: " + sim.timeInMillis() +" ,input injected: " );
m.print();
}

}

