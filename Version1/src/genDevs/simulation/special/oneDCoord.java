package genDevs.simulation.special;


import genDevs.simulation.*;
import genDevs.modeling.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
import GenCol.*;
import util.*;
import simView.*;
//import oneDCellSpace.*;
import java.util.*;

public class oneDCoord extends coordinator {
protected minSel root;
protected int base;
protected ensembleSet imminents,influencees;
protected Function fullTree;

public oneDCoord (){
super();
} //added for use with SimView

public oneDCoord(coupledDevs c, int base, int numCells){ // for pdes
  super(c,false,null);
  this.base = base;
  imminents = new ensembleSet();
  influencees = new ensembleSet();
  int levels =(int)Math.ceil(Math.log(numCells)/Math.log(base));
  createFullTree(levels,base);
  setSimulators();
  informCoupling();
}

public void createFullTree(int levels,int base){
fullTree = new Function();
root = minSel.createChildren(fullTree,"",levels, base);
root.root = true;
//System.out.println(fullTree);
}

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model it is
        oneDSim s = new oneDSim(iod);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
    else if(iod instanceof digraph){
        oneDCoupledCoord s = new oneDCoupledCoord((Coupled)iod,base);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
}

public void  simulate(int num_iter){
  int i=1;
  tN = nextTN();
  while( (tN < DevsInterface.INFINITY) && (i<=num_iter) ) {
    Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);
    System.out.println(" ------------ ITERATION " + i +" tN "+tN);
    computeInputOutput(tN);
    //showOutput();
    wrapDeltFunc(tN);
    tL = tN;
    tN = shortNextTN();
    //showModelState();
    i++;
  }
//  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

public void setImminents() {
imminents =  root.getImms();
}

public double shortNextTN() {
sendUpChange();
setImminents();
return root.getMin();
}

public void sendUpChange(){
  //System.out.println("-------size-----"+imminents.size());
  Iterator t = imminents.iterator();
  while(t.hasNext()){
    Object o = t.next();
    if(o instanceof oneDSim) ((oneDSim)o).sendTNUp();
    else if(o instanceof oneDCoupledCoord) ((oneDCoupledCoord)o).sendTNUp();
  }
}

public void addSimulator(IOBasicDevs comp){
oneDSim s = new oneDSim(comp);
s.setRootParent(this);      // set the parent
simulatorCreated(s, comp);
imminents.add(s);
int mid = s.myModelId();
String baseRep = baseRep(mid,base);
s.superior =(minSel)fullTree.assoc(baseRep);
if (s.superior == null){
//System.out.println("NOT FOUND "+mid+"  -- "+comp.getName());
minSel m = new minSel(mid);
m.de.addName(m);//(""+mid);
root.addChild(m);
s.superior = m;
}
}

public void addCoordinator(Coupled comp){
oneDCoupledCoord s = new oneDCoupledCoord(comp,base);
s.setRootParent(this);       // set the parent
simulatorCreated(s, comp);
imminents.add(s);
int mid = s.myModelId();
String baseRep = baseRep(mid,base);
s.superior =(minSel)fullTree.assoc(baseRep);
if (s.superior == null){
//System.out.println("NOT FOUND "+mid+"  -- "+comp.getName());
minSel m = new minSel(mid);
m.de.addName(""+mid);
root.addChild(m);
s.superior = m;
}
}

public void addInfluencee(coupledSimulator sim){
influencees.add(sim);
}

public void addInfluencee(coupledCoordinator sim){
influencees.add(sim);
}

public void computeInputOutput(double time) {
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {new Double(time)};
//System.out.println("size of imminents is:"+imminents.size());
imminents.tellAll("computeInputOutput",classes,args);
// send output to the corresponding model based on the coupling information
imminents.tellAll("sendMessages");
}

public void wrapDeltFunc(double time) {
sendDownMessages();
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {new Double(time)};
imminents.addAll(influencees);
imminents.tellAll("DeltFunc",classes,args);
influencees = new ensembleSet();
input = new message();
output = new message();
//for variable structure models
updateChangedSimulators();
}

public void updateChangedSimulators(){  // for variable structure capability
  //check if there are added or removed simulators
  Iterator nsit = newSimulators.iterator();
  Iterator dsit = deletedSimulators.iterator();
  int mid;
  if(nsit.hasNext()||dsit.hasNext()){
      // need to update the simulators and download the internalModelTosim to simulators
      while (nsit.hasNext()){
        Object o = nsit.next();
        simulators.add(o);
        if(o instanceof oneDSim){
          oneDSim s = (oneDSim)o;
          mid = s.myModelId();
          String baseRep = baseRep(mid,base);
          s.superior =(minSel)fullTree.assoc(baseRep);
          if (s.superior == null){
            //System.out.println("NOT FOUND "+mid+"  ++  "+s.getModel().getName());
            minSel m = new minSel(mid);
            m.de.addName(""+mid);
            root.addChild(m);
            s.superior = m;
          }
          s.nextTN();  // to send up its tN to the tree
        }
      }
      while (dsit.hasNext()) simulators.remove(dsit.next());
      //download the new ModtoSim info to all the simulators
      Class [] sclasses = {ensembleBag.getTheClass("GenCol.Function")};
      Object [] sargs  = {internalModelTosim};
      simulators.tellAll("setModToSim",sclasses,sargs);
  }
  // reset newSimulators and deletedSimulators to empty
  newSimulators = new ensembleSet();
  deletedSimulators = new ensembleSet();
}

public static String baseRep(int number,int base){
String s = "";
int num = number;
while (true){
 s = (num%base)+s;
 num = (num-num%base)/base;
 if (num < base)break;
 }
 s = num%base ==0?s:num%base+s;
return s;
}

}
