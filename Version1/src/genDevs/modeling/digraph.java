package genDevs.modeling;

import GenCol.*;

import java.util.*;
import genDevs.simulation.*;
import genDevs.simulation.distributed.*;
import genDevs.modeling.basicModels.*;

public class digraph extends devs implements Coupled{
protected coordinator myCoordinator;
protected ComponentsInterface components;
protected couprel cp;

public digraph(String nm){
super(nm);
components = new Components();
cp = new couprel();
}

public void add(IODevs iod){
components.add(iod);
((devs)iod).setParent(this);
}

/**
 * the method to add couplings with delay. A delay model is created and added into the coupling
 * @author  Xiaolin Hu
 */
public void addCouplingWithDelay(IODevs src, String p1, IODevs dest, String p2, double delay){
  // find a unique ID for the DelayModel
  int UID = 0;
  componentIterator cit = getComponents().cIterator();
  while (cit.hasNext()){
    IOBasicDevs iod = cit.nextComponent();
    if((iod.getName()).startsWith("DelayModel_")) UID++;
  }
  delayModel dM = new delayModel("DelayModel_"+UID, delay);
  add(dM);
  addCoupling(src,p1, dM, "in");
  addCoupling(dM, "out", dest, p2);
}

public void addCoupling(IODevs src, String p1, IODevs dest, String p2){
cp.add((entity)src,new port(p1),(entity)dest,new port(p2));
}

public void addPair(Pair cs, Pair cd){
cp.add(cs,cd);
}

public void removePair(Pair cs, Pair cd){
cp.remove(cs,cd);
}

public void remove(IODevs d){
components.remove(d);
}

//------------------------------------------------------------------------------
/**
 *  This method adds couplings between any two models belonging to the hierarchical
 *  tree of this coupled model.
 */
public void addCoupling_AnyTwoSubModel(String src, String p1, String dest, String p2){
   devs srcModel, destModel;
   //first try to find the src model
   srcModel = findModelWithName(src);
   destModel = srcModel.findModelWithName(dest);  // check if the dest is "child" of src model
   if(destModel!=null)  // destination model is "child" of the source model
     ((digraph)(destModel.getParent())).addUpCouplingPath(srcModel.getName(),p1,dest,p2);
   else
     ((digraph)(srcModel.getParent())).findDestToAddCoupling(srcModel,p1,dest,p2);
}

public void findDestToAddCoupling(devs srcModel, String p1, String dest, String p2){
  devs destModel;
  // check destination model is child of this model
  destModel = findModelWithName(dest);
  if(destModel!=null){
    if(destModel==this) //this means the origianl srcModel actually belongs to destModel
      addCoupling(srcModel,p1,this, p2);
    else  // the destModel belongs to a model which is brother model of the srcModel
      ((digraph)(destModel.getParent())).addUpCouplingPath(srcModel.getName(),p1,dest,p2);
  }
  else{ // first adds a coupling path from the srcModel to this model and then goes one layer up
    addCoupling(srcModel,p1,this, srcModel.getName()+"_"+p1);
    ((digraph)(getParent())).findDestToAddCoupling(this,srcModel.getName()+"_"+p1,dest,p2);
  }
}

public void addUpCouplingPath(String src, String p1, String dest, String p2){
    devs srcM = (devs)withName(src);
    devs destM = (devs)withName(dest);
    if(srcM!=null){    //this is the output coupling
      if(destM!=null) addCoupling(srcM,p1,destM,p2); // we found the destination
      else{
        if(dest.compareTo(getName())==0) addCoupling(srcM,p1,this,p2);// this model is the destination model
        else{// doesn't find the destination, needs to go one layer upper
          addCoupling(srcM,p1,this,src+"_"+p1);
          digraph P = (digraph)getParent();
          if(P!=null) P.addUpCouplingPath(getName(), src+"_"+p1, dest, p2);
          else System.out.println("the source or the destination of the coupling cannot be found!");
        }
      }
    }
    else if(destM!=null){  //this is the input coupling
      if(src.compareTo(getName())==0) addCoupling(this,p1,destM,p2);// this model is the src model
      else { // doesn't find the source, needs to go one layer upper
        addCoupling(this,dest+"_"+p2,destM,p2);
        digraph P = (digraph)getParent();
        if(P!=null) P.addUpCouplingPath(src, p1, getName(), dest+"_"+p2);
        else System.out.println("the source or the destination of the coupling cannot be found!");
      }
    }
    else System.out.println("the source or the destination of the coupling cannot be found!");
}

public devs findModelWithName(String nm){
  if(nm.compareTo(this.getName())==0) return this;
  else{
      componentIterator cit = getComponents().cIterator();
      while (cit.hasNext()){
        devs iod = (devs)(cit.nextComponent());
        devs m = iod.findModelWithName(nm);
        if(m!=null) return m;
      }
      return null;
  }
}

//------------------------------------------------------------------------------
/**
 * this method adds coupling between a model and another model. The relationship between
 * these two models is that the latter model is the brother of the first model's parent/grandparent...
 * This method starts from the bottom model and goes up layer by layer until it
 * finds the top model which is the source or destination of the coupling
 * During this process, internal couplings are added so a hierarchical path is formed
 * @author  Xiaolin Hu
 */
public void addHierarchicalCoupling(String src, String p1, String dest, String p2){
    coordinator PCoord = getCoordinator();
    if(withName(src)!=null){    //this is the output coupling
      if(withName(dest)!=null){ // we found the destination
        Pair cs = new Pair(src,p1);
        Pair cd = new Pair(dest,p2);
        addPair(cs,cd);
        PCoord.addCoupling(src,p1,dest,p2);
      }
      else{  // doesn't find the destination, needs to go one layer upper
        String myName = getName();
        String myPort = src+"_"+p1; // output port = modelName + portName
        Pair cs = new Pair(src,p1);
        Pair cd = new Pair(myName,myPort);
        addPair(cs,cd);
        PCoord.addCoupling(src,p1,myName,myPort);
        digraph P = (digraph)getParent();
        if(P!=null) P.addHierarchicalCoupling(myName, myPort, dest, p2);
        else{     //   this could be the distributed coupling case
            if(PCoord instanceof RTCoordinatorClient)
              ((RTCoordinatorClient)PCoord).addDistributedCoupling(myName, myPort, dest, p2);
            else
              System.out.println("the source or the destination of the coupling couldn't be found!");
        }
      }
    }
    else if(withName(dest)!=null){  //this is the input coupling
        String myName = getName();
        String myPort = dest+"_"+p2; // input port = modelName + portName
        Pair cs = new Pair(myName,myPort);
        Pair cd = new Pair(dest,p2);
        addPair(cs,cd);
        PCoord.addCoupling(myName,myPort,dest,p2);
        digraph P = (digraph)getParent();
        if(P!=null) P.addHierarchicalCoupling(src, p1, myName, myPort);
        else{     //   this could be the distributed coupling case
            if(PCoord instanceof RTCoordinatorClient)
              ((RTCoordinatorClient)PCoord).addDistributedCoupling(src, p1, myName, myPort);
            else
              System.out.println("the source or the destination of the coupling couldn't be found!");
        }
    }
    else System.out.println("the source or the destination of the coupling couldn't be found!");
}

/**
 * get the reference of the environmet model --- Xiaolin Hu, August 8th, 2003
 */
public IODevs getModelWithName(String name){
  digraph P = (digraph)getParent();
  if(P!=null) return P.getModelWithName(name);
  else return withName(name);
}

public IODevs withName(String nm){
Class [] classes  = {ensembleBag.getTheClass("java.lang.String")};
Object [] args  = {nm};
return (IODevs)components.whichOne("equalName",classes,args);
}

public ComponentsInterface getComponents(){
return components;
}

public couprel getCouprel(){
return cp;
}

public String toString(){
String s = "";
componentIterator cit = getComponents().cIterator();
while (cit.hasNext()){
IOBasicDevs iod = cit.nextComponent();
s += " " + iod.toString();
}
return s;
}

public void showState(){
components.tellAll("showState");
}


public void initialize(){
components.tellAll("initialize");
}


public void setSimulator(CoupledSimulatorInterface sim){}
public ActivityInterface getActivity(){return new activity("name");}
public void deltext(double e,MessageInterface x){}
public void deltcon(double e,MessageInterface x){}
public void deltint(){}
public MessageInterface Out(){return new message();}
public double ta(){return 0;}

 public componentIterator iterator(){
 return getComponents().cIterator();
 }

    public void setCoordinator(coordinator coordinator_) {myCoordinator = coordinator_;}
    public coordinator getCoordinator() {return myCoordinator;}
}



