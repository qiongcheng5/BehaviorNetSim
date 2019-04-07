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


public class RTcoupledCoordinator extends coupledCoordinator implements RTCoupledCoordinatorInterface{

protected int numIter;
//protected  Thread myThread;
protected RTCoordinatorInterface myRTRootParent;
protected RTcoupledCoordinator myRTParent;
protected boolean started = false;

public RTcoupledCoordinator(Coupled c){
super(c);
}

public long timeInSecs() {
    return (timeInMillis()/1000);
}
public long timeInMillis() {
    return System.currentTimeMillis();
}
 public void setRTRootParent(RTCoordinatorInterface r){
myRTRootParent = r;
}

public RTCoordinatorInterface getRTRootParent(){
return myRTRootParent;
}

public void setRTParent(RTcoupledCoordinator r){
myRTParent = r;
}

public RTCoupledCoordinatorInterface getRTParent(){
return myRTParent;
}

public void addSimulator(IOBasicDevs comp){
coupledRTSimulator s = new coupledRTSimulator(comp);
simulators.add(s);
s.setRTParent(this);      // set the parent
modelToSim.put(comp.getName(),s);
internalModelTosim.put(comp.getName(),s);
}

public void addCoordinator(Coupled comp){
RTcoupledCoordinator s = new RTcoupledCoordinator(comp);
s.setRTParent(this);       // set the parent
simulators.add(s);
modelToSim.put(comp.getName(),s);
internalModelTosim.put(comp.getName(),s);
}

public void removeModel(IODevs model){
       String modelName = model.getName();
//      System.out.println("initially Simulators --->"+simulators.toString());
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
//      System.out.println("after remove Simulators --->"+simulators.toString());
      Class [] classes = {ensembleBag.getTheClass("GenCol.Function")};
      Object [] args  = {internalModelTosim};
      simulators.tellAll("setModToSim",classes,args);
      classes = null; args = null;  // release memory
}

public void kill(){
simulators.tellAll("kill");
}

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model is
        coupledRTSimulator s = new coupledRTSimulator(iod);
        s.setRTParent(this);
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
        s.setRTParent(this);
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

public void  simulate(int numIter)
{
  this.numIter = numIter;
  started = true;
  tL = timeInSecs();
  tN = nextTN();
  tellAllSimulate(numIter);
  System.out.println( "start to simulate !");
//  myThread.start();

  }

public synchronized void putMessages(ContentInterface c){
input.add(c);
sendDownMessages();
input = new message();
}

public synchronized void putMyMessages(ContentInterface c){
output.add(c);
sendMessages();
output = new message();
}

public void sendMessages() {    //extend so they send message to its parent also
  MessageInterface o = getOutput();
  if( o!= null && !o.isEmpty()) {
    Relation r = convertMsg((message)getOutput());//assume computeInputOutput done first
    Iterator rit = r.iterator();
    while (rit.hasNext()){
       Pair p = (Pair)rit.next();
       content co = (content)p.getValue();
       Object ds = p.getKey();
       if(modelToSim.get(ds) instanceof CoupledRTSimulatorInterface){
           CoupledRTSimulatorInterface sim = (CoupledRTSimulatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
       }
       else if(modelToSim.get(ds) instanceof RTCoupledCoordinatorInterface){
           RTCoupledCoordinatorInterface sim = (RTCoupledCoordinatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
       }
       else{            // this is an internal output coupling
           RTCoupledCoordinatorInterface cci = getRTParent();
           RTCoordinatorInterface ci = getRTRootParent();
           if(cci != null) myRTParent.putMyMessages(co);
           else if(ci != null)  myRTRootParent.putMyMessages(co);
       }
    }
  }
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
       else if(internalModelTosim.get(ds) instanceof RTCoupledCoordinatorInterface){
           RTCoupledCoordinatorInterface sim = (RTCoupledCoordinatorInterface)internalModelTosim.get(ds);
           sim.putMessages(co);
       }
    }
  }
}

public void tellAllSimulate(int numIter){
Class [] classes  = {ensembleBag.getTheClass("java.lang.Integer")};
Object [] args  = {new Integer(numIter)};
simulators.tellAll("simulate",classes,args);
}

public void stopSimulate(){
simulators.tellAll("stopSimulate");
//myThread.interrupt();
}
/*
public void run(){
long observeTime = 10000;
 try {
  Thread.currentThread().sleep((long)observeTime);
} catch (Exception e) {}
tellAllStop();
System.out.println("Coordinator Terminated Normally at time: " + timeInMillis());

}
*/
public void tellAllStop(){
simulators.tellAll("stopSimulate");
}

//<-- Added by Qiong Cheng 
/**
 * DeInitializes this coordinator.
 * Aim to handle objects initialized by special atomic model, 
 * for example, Environment initionalizes Display thread.
 * Here transfer the full control to concrete subclass so as to make it clear.
 */
public void deInitialize(){
    simulators.tellAll( "deInitialize" );
}

/**
 * Start simulators
 * Aim to start threads specially in some atomic model.
 */
public void startSimulator(int numIter){
	Class [] classes  = {ensembleBag.getTheClass("java.lang.Integer")};
	Object [] args  = {new Integer(numIter)};
	simulators.tellAll("simulate",classes,args);
}

/**
 * Stop simulators
 * Aim to stop threads specially in some atomic model.
 */
public void stopSimulator(){
	simulators.tellAll( "stopSimulator" );
}

/**
 * Pause simulators
 * Aim to pause threads specially in some atomic model.
 */
public void pauseSimulator(){
	simulators.tellAll( "pauseSimulator" );
}

/**
 * Resume simulators
 * Aim to resume threads specially in some atomic model.
 */
public void resumeSimulator(){
	simulators.tellAll( "resumeSimulator" );
}    
//Added by Qiong Cheng -->
}
