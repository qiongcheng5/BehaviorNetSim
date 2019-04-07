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


public class oneDSim  extends coupledSimulator{
protected minSel superior;
protected boolean tNChanged=false;

public oneDSim(IOBasicDevs cell){
super(cell);
}

public int myModelId(){
/*
if (!(myModel instanceof oneDCell))
return hashCode();
oneDCell oc = (oneDCell)myModel;
int i = oc.getId();
return i;
*/
if(myModel instanceof IDModel) return ((IDModel)myModel).getId();
//else if(myModel instanceof oneDCell) return ((oneDCell)myModel).getId();
else return hashCode();
}

public double nextTN(){
if(tN<DevsInterface.INFINITY){
  ensembleSet hs = new ensembleSet();
  hs.add(this);
  superior.sendUp(""+myModelId(),new Pair(hs,new doubleEnt(tN)));
}
return tN;
}

public boolean  equalTN(double t){return t == tN;}

public double getTN(){
return tN;
}

public double getTL(){
return tL;
}
public void DeltFunc(double t) {
   wrapDeltfunc(t,input);
   input = new message();
}

public void putMessages(ContentInterface c){
input.add(c);
if(getRootParent()!=null) ((oneDCoord)getRootParent()).addInfluencee(this);
else if(getParent()!=null) ((oneDCoupledCoord)getParent()).addInfluencee(this);
}

public void sendTNUp(){
  if(tNChanged){
    tNChanged = false;
    ensembleSet hs = new ensembleSet();
    hs.add(this);
    superior.sendUp_once(""+myModelId(),new Pair(hs,new doubleEnt(tN)));
  }
}

public  synchronized void  wrapDeltfunc(double t,MessageInterface x){
 if(x == null){
    System.out.println("ERROR RECEIVED NULL INPUT  " + myModel.toString());
    return;
  }
  if (x.isEmpty() && !equalTN(t)) {
    return;
  }
  else if((!x.isEmpty()) && equalTN(t)) {
    double e = t - tL;
    myModel.deltcon(e,x);
  }
  else if(tN <= t) {// relax to enable revival
    myModel.deltint();
  }
  else if(!x.isEmpty()) {
    double e = t - tL;
    myModel.deltext(e,x);
  }
  wrapDeltfuncHook2();
  tL = t;
  tN = tL + myModel.ta();
  System.out.println(getModel().getName()+"  " +tL+"  "+tN);

  tNChanged = true;
  superior.informChange();  // inform that its tN has been changed
}


}