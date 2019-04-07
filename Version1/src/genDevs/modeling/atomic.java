package genDevs.modeling;

import GenCol.*;
import java.util.*;
import java.awt.Color;
import genDevs.simulation.*;
import genDevs.simulation.distributed.*;
import util.*;

//public abstract
public class atomic extends devs implements AtomicInterface{
 protected double sigma;
 protected String phase;
 protected ActivityInterface a;
 protected double  INFINITY  = DevsInterface.INFINITY;
 protected CoupledSimulatorInterface mySim;

public CoupledSimulatorInterface getSim(){
return mySim;
}

public atomic(){
this("atomic");
}

public atomic(String name){
super(name);
sigma = INFINITY;
}
public atomic(ActivityInterface a){
this(a.getName());
this.a = a;
}
public void setSimulator(CoupledSimulatorInterface mySim){
this.mySim = mySim;
}

public String getPhase(){
return phase;
}

public void setSigma(double sigma){

  this.sigma = sigma;
}

public double getSigma(){
return sigma;
}

public double getSimulationTime(){   //Xiaolin Hu, Jun 16, 2004
  return ((coupledSimulator)getSim()).getCurrentTime();
}

public void initialize(){
}

public void Continue(double e){
 if (sigma < INFINITY)
    setSigma(sigma - e);
}




    public void passivateIn(String phase) {holdIn(phase, INFINITY);}
    public void passivate() {passivateIn("passive");}
    public void holdIn(String phase, double sigma) {holdIn(phase, sigma, null);}

    public void holdIn(String phase, double sigma,ActivityInterface a)
    {
        this.phase = phase;
        setSigma(sigma);
        this.startActivity(a);
    }

    public void startActivity(ActivityInterface a){
      if(a!=null){
        this.a = a;
        mySim.startActivity(a);
      }
    }

  public boolean phaseIs(String phase){
  return this.phase.equals(phase);
  }


  public double ta(){
      return sigma;
  }

  public void deltint(){}
  public void deltext(double e,MessageInterface x){deltext(e,(message)x);}
  public void deltext(double e,message x){} //usual devs
  public void deltcon(double e,MessageInterface x){deltcon(e,(message)x);}
  public void deltcon(double e,message x){ //usual devs
   deltint();
   deltext(0,x);
}

 public MessageInterface Out(){

 return out();
}

 public message out(){
 return new message();
}

 public String toString(){
 return "phase :"+ phase + " sigma : " + sigma ;
 }

  public void showState(){
    Logging.log("Model "+ getName() + "'s state: ", Logging.full);
    Logging.log("phase :"+ phase + " sigma : " + sigma, Logging.full);
  }

  public ActivityInterface getActivity(){
  return a;
  }

    public String stringState(){
    return "";
    }

// the code below is for variable structure DEVS
 public void addCoupling(String src, String p1, String dest, String p2){
    digraph P = (digraph)getParent();
    if(P!=null){
      //update its parent model's coupling info database
      P.addPair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = P.getCoordinator();
      PCoord.addCoupling(src,p1,dest,p2);
    }
    else{   // this is for the distributed simulation situation
      if (mySim instanceof RTCoupledSimulatorClient)
        ((RTCoupledSimulatorClient)mySim).addDistributedCoupling(src,p1,dest,p2);
    }
  }

 public void removeCoupling(String src, String p1, String dest, String p2){
    digraph P = (digraph)getParent();
    if(P!=null){
      //update its parent model's coupling info database
      P.removePair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = P.getCoordinator();
      PCoord.removeCoupling(src,p1,dest,p2);
    }
    else{   // this is for the distributed simulation situation
      if (mySim instanceof RTCoupledSimulatorClient)
        ((RTCoupledSimulatorClient)mySim).removeDistributedCoupling(src,p1,dest,p2);
    }
 }

 public void addModel(IODevs iod){
   digraph P = (digraph)getParent();
   if(P!=null){
     P.add(iod);
     coordinator PCoord = P.getCoordinator();
     PCoord.setNewSimulator((IOBasicDevs)iod);
   }
 }

 public void removeModel(String modelName){
   digraph P = (digraph)getParent();
   if(P!=null){
     coordinator PCoord = P.getCoordinator();
     // first remove all the coupling related to that model
     PCoord.removeModelCoupling(modelName);
     // then remove the model
     IODevs iod = P.withName(modelName);
     P.remove(iod);
     PCoord.removeModel(iod);
   }
 }

 /**
 * get the reference of the environmet model --- Xiaolin Hu, Nov 30th, 2004
 */
public IODevs getModelWithName(String name){
  digraph P = (digraph)getParent();
  if(P!=null)
    return P.getModelWithName(name);
  else return null;
}

}