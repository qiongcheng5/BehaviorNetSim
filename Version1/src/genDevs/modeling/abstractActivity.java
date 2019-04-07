package genDevs.modeling;

import GenCol.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import simView.*;

import java.util.*;

/**
 * an abstract Activity to imitate the behavior and interface of an Activity
 * It's typically used for the purpose of simulation-based test and model continuity
 *
 * @author      Xiaolin Hu
 * Created time   February 19th, 2003
 */

public class abstractActivity extends ViewableAtomic /*atomic*/ implements ActivityInterface{

protected CoupledSimulatorInterface modelSim;

public abstractActivity(String nm){
super(nm);
}

/**
 * return  result to the atomic model who starts this abstractActivity
 */
public void returnTheResult(entity myresult) {
  modelSim.returnResultFromActivity(myresult);
}

/**
 * set the simulator of the model who starts the this abstractActivity
 */
public void setActivitySimulator(CoupledSimulatorInterface sim){
this.modelSim = sim;
}

public void kill(){
removeModel(getName());
}

public void start(){}  // not implemented because the model is automatically started when it is added into the system

/**
 * add coupling between this abstractActivity and other models, typically the environment model
 * The coupling added will follow the "hierarchical rule"
 */
public void addActivityCoupling(String src, String p1, String dest, String p2){
  if(src.compareTo(getName())==0||dest.compareTo(getName())==0){
    digraph P = (digraph)getParent();
    if(P!=null) P.addHierarchicalCoupling(src, p1, dest, p2);
  }
}

/**
 * put message on the input port of this abstractActivity
 * and ask the simulator to handle the input immediately
 */
public void putInstantInput(String inputPort, entity ent){
  content ct = makeContent(inputPort,ent);
  if(mySim instanceof CoupledRTSimulatorInterface) mySim.putMessages(ct);
  else if(mySim instanceof CoupledSimulatorInterface){
    mySim.putMessages(ct);
    mySim.DeltFunc(new Double(((coupledSimulator)mySim).getCurrentTime()));
  }
}

/**
 * put message on the output port and immediately send the message to
 * other models based on the abstractActivity coupling
 */
public void sendInstantOutput(String outputPort, entity ent){
  content ct = makeContent(outputPort,ent);
  if(mySim instanceof CoupledRTSimulatorInterface){
    message m = new message();
    m.add(ct);
    ((coupledSimulator)mySim).setOutput(m);
    mySim.sendMessages();
  }
  else if(mySim instanceof CoupledSimulatorInterface)
    ((coupledSimulator)mySim).sendInstantMessages(ct);
}

}