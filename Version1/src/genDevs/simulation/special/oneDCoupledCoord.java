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

public class oneDCoupledCoord extends coupledCoordinator {
protected minSel root;
protected int base;
protected ensembleSet imminents,influencees;
protected Function fullTree;
protected minSel superior;
int numCells;

public oneDCoupledCoord(coupledDevs c){
this(c,10);
}

public oneDCoupledCoord(coupledDevs c, int base){
  super((Coupled)c,false);
  this.base = base;
  imminents = new ensembleSet();
  influencees = new ensembleSet();
  if(c instanceof IDSpaceModel) numCells = ((IDSpaceModel)c).getNumOfModels();
//  else if(c instanceof oneDimCellSpace) numCells = ((oneDimCellSpace)c).numCells;
  else{
    numCells=0;
    componentIterator cit = ((digraph)c).getComponents().cIterator();
    while (cit.hasNext()){
      IOBasicDevs iod = cit.nextComponent();
      numCells++;
    }
  }
  if(numCells<=1) numCells=2;  // to avoid causing exceptions when numCells=0 or numCells=1
  int levels =(int)Math.ceil(Math.log(numCells)/Math.log(base));
  createFullTree(levels,base);
  setSimulators();
  informCoupling();
}

public oneDCoupledCoord(coupledDevs c, int base, int numCells){
  super((Coupled)c,false);
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

public void setImminents() {
imminents =  root.getImms();
}

public double nextTN() {
ensembleInterface result = new ensembleSet();
Class [] classes  = {};
Object [] args  = {};
simulators.AskAll(result,"nextTNDouble",classes,args);
TreeSet t = new TreeSet(result);
Double d = (Double)t.first();   // get the smallest tN
double nextT = d.doubleValue();
//int modelNum = simulators.size();
//System.out.println("-----number of started models: " + modelNum);
if(nextT<DevsInterface.INFINITY){
  ensembleSet hs = new ensembleSet();
  hs.add(this);
  superior.sendUp(""+myModelId(),new Pair(hs,new doubleEnt(nextT)));
}
return nextT;
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

public void sendTNUp(){
  ensembleSet hs = new ensembleSet();
  hs.add(this);
  superior.sendUp(""+myModelId(),new Pair(hs,new doubleEnt(shortNextTN())));
}

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model is
        oneDSim s = new oneDSim(iod);
        s.setParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
    else if(iod instanceof digraph){
        oneDCoupledCoord s = new oneDCoupledCoord((Coupled)iod,base);
        s.setParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
}

public void putMessages(ContentInterface c){
input.add(c);
if(getRootParent()!=null)
  ((oneDCoord)getRootParent()).addInfluencee(this);
else if(getParent()!=null)
  ((oneDCoupledCoord)getParent()).addInfluencee(this);
}

public void addSimulator(IOBasicDevs comp){
oneDSim s = new oneDSim(comp);
s.setParent(this);      // set the parent
simulatorCreated(s, comp);
imminents.add(s);
String baseRep = baseRep(s.myModelId(),base);
s.superior =(minSel)fullTree.assoc(baseRep);
if (s.superior == null){
//System.out.println("NOT FOUND "+s.myModelId()+"  -- "+comp.getName());
minSel m = new minSel(s.myModelId());
m.de.addName(""+s.myModelId());
root.addChild(m);
s.superior = m;
}
}

public void addCoordinator(Coupled comp){
oneDCoupledCoord s = new oneDCoupledCoord(comp,base);
s.setParent(this);       // set the parent
simulatorCreated(s, comp);
imminents.add(s);
String baseRep = baseRep(s.myModelId(),base);
s.superior =(minSel)fullTree.assoc(baseRep);
if (s.superior == null){
//System.out.println("NOT FOUND "+s.myModelId()+"  -- "+comp.getName());
minSel m = new minSel(s.myModelId());
m.de.addName(""+s.myModelId());
root.addChild(m);
s.superior = m;
}
}

public int myModelId(){
//return hashCode();
if(myModel instanceof IDModel) return ((IDModel)myModel).getId();
else return hashCode();
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
// inform change of TN
superior.informChange();  // inform that its tN has been changed
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
