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

import util.Logging;
import genDevs.modeling.*;
import genDevs.simulation.*;


public class coupledRTSimulator extends coupledSimulator
                              implements CoupledRTSimulatorInterface {

protected double startTime;
protected int numIter;
protected  Thread myThread;
protected boolean inputReady = false;
protected boolean Elapsed = false;
protected long timeToSleep;
protected RTCoordinatorInterface myRTRootParent;
protected RTcoupledCoordinator myRTParent;
protected simTimer timer;

public coupledRTSimulator(IOBasicDevs devs){
super(devs);
}

public coupledRTSimulator(){
super();
}

public  synchronized void initialize(){
 myModel.initialize();
 startTime = timeInMillis();
 tL = startTime;
 tN = tL + myModel.ta()*1000;
 myModel.showState();
 myThread = new  Thread(this);

}

public  synchronized void initialize(double sTime){
 myModel.initialize();
 startTime = sTime;
 tL = startTime;
 tN = tL + myModel.ta()*1000;
 myModel.showState();
 myThread = new  Thread(this);

}

public void setTN(){
   tN = timeInMillis() + myModel.ta()*1000;
}

public double getTN(){
return tN;
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

public void run(){
     Thread.currentThread().setName(myModel.getName());
     setTN();
     int iter = 0;
     int iPause = 0;
//     while( iter < numIter ) {
     while( true ) {
        while(timeInMillis() < getTN() - 10){
            timeToSleep = (long)(getTN() - timeInMillis());
            if (timeToSleep < DevsInterface.INFINITY){
                timer = new simTimer(this,timeToSleep);
                Elapsed = false;
            }
            waitForNextEvent();
            if (inputReady) break;
        } // out of the while loop becuase of getting an external input or time elapsed
        try{Thread.sleep(100);} //time granule --- wait for other input
             catch (Exception e){}
        if(timeInMillis() >= getTN() - 10) Elapsed = true;

        if(Elapsed){   // time elapsed
           computeInputOutput(getTN());
//           showOutput();
           sendMessages();
           wrapDeltfunc(getTN());
        }
        else if(inputReady){ // get external input
            double externalEventTime = timeInMillis();
            if (externalEventTime > getTN())  externalEventTime = getTN();
            wrapDeltfunc(externalEventTime);
        }
//        showModelState();
        if(timer!=null) timer.interrupt();
        inputReady = false;
        Elapsed = false;
        tL = timeInMillis();
        tN = tL + myModel.ta()*1000;
        iter ++;
        
        //<-- Added by Qiong Cheng
        /*
        try{
	        while ( ( ! stop ) && ( pause ) ){
	        	iPause = (int) (Math.random() * 1000);
	            Thread.sleep(iPause);
	        }
	        
	        if ( stop ){
	        	System.out.println(this.myModel.getName() + " finished!");
	        	return;
	        }
        }catch(InterruptedException exception){
            System.out.println(exception.toString());
        }
        */
        //<-- Added by Qiong Cheng   
     }
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

public  synchronized void waitForNextEvent(){
while(!inputReady && !Elapsed){
try{
wait();
   }
catch (InterruptedException e)
  {}
}
}

public synchronized void notifyElapsed(){
Elapsed = true;
notify();
}

public void simulate(Integer i){
simulate(i.intValue());
}

public void kill(){myThread.stop();}

public void  simulate(int NumIter)
{
	Logging.log(this.myModel.getName() + " start simulator.");	
  int i=1;
  numIter = NumIter;
  tN = nextTN();
  myThread.start();
}

public void stopSimulate(){
numIter = 0;
myThread.interrupt();
}

public void  wrapDeltfunc(double t){
 wrapDeltfunc(t,getInput()); //changed to work with activity
 input = new message();
}

public   void  wrapDeltfunc(double t,MessageInterface x){
 if(x == null){
    System.out.println("ERROR RECEIVED NULL INPUT  " + myModel.toString());
    return;
  }

  if (x.isEmpty() && !equalTN(t)) {
    return;
  }
  else if((!x.isEmpty()) && equalTN(t)) {
    double e = t - tL;
    myModel.deltcon(e/1000,x);
  }

 else if(equalTN(t)) {
     myModel.deltint();
  }
  else if(!x.isEmpty()) {
    double e = t - tL;
    myModel.deltext(e/1000,x);
  }
}

public void startActivity(ActivityInterface a){  // changed by xiaolin Hu
  a.setActivitySimulator(this);
  if(a instanceof activity) a.start(); // activity case
  else{
    if(a instanceof HILActivity){  //HILActivity case
      ((atomic)myModel).addModel((HILActivity)a);
      a.start();
    }
    else if(a instanceof abstractActivity) ( (atomic) myModel).addModel( (abstractActivity) a);
  }
}

public synchronized void putMessages(ContentInterface c){
    if(c == null) return;
    System.out.flush();
    input.add(c);
    inputReady = true;
    notify();
}

//<-- Added by Qiong Cheng   
/**
 * DeInitializes this atomic simulator.
 * Aim to handle objects initialized by special atomic model, 
 * for example, Environment initionalizes Display thread.
 * Here transfer the full control to concrete subclass so as to make it clear.
 */
public void deInitialize(){
    Logging.log(this.myModel.getName() + " deinitialize simulator.");		
}

/**
 * Start simulators
 * Aim to stop threads specially in some atomic model.
 * @param i
 */
public void startSimulator(Integer i){
	stop = false;
	simulate(i.intValue());
}
/**
 * Stop simulators
 * Aim to stop threads specially in some atomic model.
 */
private boolean stop = false;
public void stopSimulator(){
    Logging.log(this.myModel.getName() + " stop simulator.");
    stop = true;
    //myThread.interrupt();
}

/**
 * Pause simulators
 * Aim to pause threads specially in some atomic model.
 */
private boolean pause = false;
public void pauseSimulator(){
    Logging.log(this.myModel.getName() + " pause simulator.");	
    pause = true;
    //myThread.yield();
}

/**
 * Resume simulators
 * Aim to resume threads specially in some atomic model.
 */
public void resumeSimulator(){
    Logging.log(this.myModel.getName() + " resume simulator.");
    pause = false;
    //myThread.resume();
}    
public static void main(String[] args) {
	atomicSimulator test = new atomicSimulator(new atomic());
	test.initialize() ;
	test.simulate( 200 );
}	
//Added by Qiong Cheng -->    

}
class simTimer extends Thread{
coupledRTSimulator sim;
long timeOut;

public simTimer (coupledRTSimulator Sim,long Time){
sim = Sim;
timeOut = Time;
start();
}


public void run(){
  try
        {
          Thread.sleep(timeOut);
        }
        catch (Exception e)
        {
        }
sim.notifyElapsed();
}


}




