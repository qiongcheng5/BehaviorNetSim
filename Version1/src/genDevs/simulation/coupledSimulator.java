package genDevs.simulation;

import GenCol.*;
import genDevs.modeling.*;
import java.util.*;
import util.*;


public class coupledSimulator extends atomicSimulator
                              implements CoupledSimulatorInterface {
protected Function modelToSim;
protected couprel coupInfo;
protected ActivityInterface myActivity = null;
protected boolean activityDue = false;
protected CoupledCoordinatorInterface myParent;
protected CoordinatorInterface myRootParent;

public coupledSimulator(){
this(new atomic());
}

public coupledSimulator(IOBasicDevs devs){
super(devs);
modelToSim = new Function();
coupInfo = new couprel();
myActivity = myModel.getActivity();
myModel.setSimulator(this);
}

public void setParent( CoupledCoordinatorInterface p){
myParent = p;
}
public void setRootParent( CoordinatorInterface p){
myRootParent = p;
}

public CoupledCoordinatorInterface getParent(){
return myParent;
}
public CoordinatorInterface getRootParent(){
return myRootParent;
}


public Double nextTNDouble(){
return new Double(nextTN());
}

public void DeltFunc(Double d){
DeltFunc(d.doubleValue());
}

public void addPair(Pair cs,Pair cd) {
coupInfo.add(cs,cd);
}

public void removePair(Pair cs,Pair cd) {
coupInfo.remove(cs,cd);
}

public void showCoupling(){
System.out.println("The coupling is: ");
coupInfo.print();
}

public void setModToSim(Function mts){
modelToSim = mts;
}

public synchronized Relation convertMsg(MessageInterface x) {
  // for each content in the given message
  Relation r = new Relation();
  message  msg = new message();
  if(x.isEmpty()) return r;
  ContentIteratorInterface cit = ((message)x).mIterator();
  while (cit.hasNext()){
     content co = (content)cit.next();

     // get destination, might more than one
     // for each coupling to this content's outport
     HashSet s = coupInfo.translate(myModel.getName(), co.getPort().getName());
     Iterator it = s.iterator();
     while(it.hasNext()){
        Pair cp = (Pair) it.next();
        content tempco = new content((String)cp.getValue(),
            (entity)co.getValue());
        r.put(cp.getKey(),tempco);

        convertMsgHook1(co, cp, tempco, myModel.getName(), (String)cp.getKey());
     }
  }
  return r;
}

/*  changed by xiaolin Hu  -- Feb 15th, 2003
public void startActivity(ActivityInterface a){
  a.setSimulator(this);
  myActivity = a;
  double completionTime = Math.random()*2*a.getProcessingTime();
  //this should be replaced by a user specifyable distribution
  // and way of determinng completionTime
  if (myModel instanceof atomic){
       if (completionTime < ((atomic)myModel).getSigma()){
             ((atomic)myModel).setSigma(completionTime);
              activityDue = true;
              }
    }
        else return;
}
*/

public void startActivity(ActivityInterface a){
  if(a instanceof HILActivity)
    System.out.println("ERROR: fast mode or centralized real time simulation only works with abstractActivity!");
  else if(a instanceof abstractActivity){
    ((atomic)myModel).addModel((abstractActivity)a);
    a.setActivitySimulator(this);
  }
  else
    System.out.println("ERROR: fast mode or centralized real time simulation only works with abstractActivity!");
}

public void returnResultFromActivity(EntityInterface result) {
    content c = new content("outputFromActivity",(entity)result);
    putMessages(c);
}

public void putMessages(ContentInterface c){
input.add(c);
}

public void sendMessages() {
/*  // comment out by xiaolin Hu
 if (activityDue){
     returnResultFromActivity(myActivity.computeResult());
     activityDue = false;
  }
*/
  MessageInterface o = getOutput();
  if( o!= null && !o.isEmpty()) {
    Relation r = convertMsg((message)getOutput());//assume computeInputOutput done first
    Iterator rit = r.iterator();
    while (rit.hasNext()){
       Pair p = (Pair)rit.next();
       content co = (content)p.getValue();
       Object ds = p.getKey();
       if(modelToSim.get(ds) instanceof CoupledSimulatorInterface){
           CoupledSimulatorInterface sim = (CoupledSimulatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
       }
       else if(modelToSim.get(ds) instanceof CoupledCoordinatorInterface){
           CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
       }
       else{            // this is an internal output coupling
           CoupledCoordinatorInterface cci = getParent();
           CoordinatorInterface ci = getRootParent();
           if(cci != null) myParent.putMyMessages(co);
           else if(ci != null)  myRootParent.putMyMessages(co);
       }
    }
  }
}

public void sendInstantMessages(ContentInterface c) {
    message m = new message();
    m.add(c);
    Relation r = convertMsg(m);//assume computeInputOutput done first
    Iterator rit = r.iterator();
    while (rit.hasNext()){
       Pair p = (Pair)rit.next();
       content co = (content)p.getValue();
       Object ds = p.getKey();
       if(modelToSim.get(ds) instanceof CoupledSimulatorInterface){  // we found the destination
           CoupledSimulatorInterface sim = (CoupledSimulatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
           sim.DeltFunc(new Double(getCurrentTime()));
       }
       else if(modelToSim.get(ds) instanceof CoupledCoordinatorInterface){   // we found the destination
           CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)modelToSim.get(ds);
           sim.putMessages(co);
           sim.DeltFunc(new Double(getCurrentTime()));
       }
       else{            // the message goes one level upper and is handeled by the parent
           CoupledCoordinatorInterface cci = getParent();
           CoordinatorInterface ci = getRootParent();
           if(cci != null) ((coupledCoordinator)myParent).sendInstantMessages(co);
           else if(ci != null)  System.out.println("the destination of the instant message is not found!");
       }
    }
}

public void DeltFunc(double t) {
   wrapDeltfunc(t,input);
   input = new message();
}

public double getCurrentTime(){
   double t = 0;
   coupledCoordinator cci = (coupledCoordinator)getParent();
   coordinator ci = (coordinator)getRootParent();
   if(cci != null) t=cci.getCurrentTime();
   else if(ci != null)  t=ci.getCurrentTime();
   return t;
}

public void  simulate(int num_iter)    // compare to atomicSimulator, add the sendMessages()
{
  int i=1;
  tN = nextTN();
  while( (tN < DevsInterface.INFINITY) && (i<=num_iter) ) {
     Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);
     computeInputOutput(tN);
     showOutput();
     sendMessages();
     DeltFunc(tN);
     tL = tN;
     tN = nextTN();
     showModelState();
     i++;
  }
  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

    /**
     * Returns a list of the ports (along with their components) to which
     * the given port is a source.
     *
     * @param   portName    The source port name on the source component.
     * @return              The list of couplings to the source port's
     *                      desination ports.
     */
    public List getCouplingsToSourcePort(String portName)
    {
        return AtomicSimulatorUtil.getCouplingsToSourcePort(portName,
            myModel.getName(), coupInfo, null, modelToSim,
            null, (atomicSimulator)myRootParent);
    }

    protected void convertMsgHook1(content oldContent, Pair coupling,
        content newContent, String sourceComponentName,
        String destComponentName) {}
}
