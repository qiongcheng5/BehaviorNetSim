package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class Behavior extends ViewableAtomic{//atomic{

public double myExcitation;
public int myID, id;
public double inhibition;

EnvInterface envI;
centralRepository cRps;
entity commdEnt=null;

public Behavior(String nm){ super(nm); }
public Behavior(){ this("Behavior");  }


public void initialize(){
  addInport("selected");
  addInport("inhibitions");
  addOutport("inhib_out");
  addOutport("my_excit");
  addOutport("commd_out");

  // the interface to the environment model and central repository
  envI = (EnvInterface)getModelWithName("EnvInterface");
  cRps = (centralRepository)getModelWithName("centralRepository");

  holdIn("nextStep",0);
}

public void deltext(double e,message x){
  Continue(e);
  if(phaseIs("waitInhibition")){// wait for inhibition from other models
    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort(x, "inhibitions", i)) {
        id = ( (int_double_Ent) x.getValOnPort("inhibitions", i)).getX();
        inhibition = ( (int_double_Ent) x.getValOnPort("inhibitions", i)).getY();
        reCalcuExcitation(id, inhibition);
      }
    }
    holdIn("sendMyExcit",0);
  }
  else if(phaseIs("nextStep")){ //waiting for next step after sending output
    for (int i = 0; i < x.getLength(); i++) { // there should be only one message
      if (messageOnPort(x, "selected", i)){
          prepareAction();
          updateEnergy();
          cRps.setCurrentBehavior(myID); // this is an ad-hoc approach, need to be changed later
          holdIn("sendActionCommd",0);
      }
    }
  }
}

public void deltint(){
  if(phaseIs("sendInhibition")) passivateIn("waitInhibition"); // wait for inhibition from other models
  else if(phaseIs("sendMyExcit")||phaseIs("sendActionCommd"))
    holdIn("nextStep",configData.timeStep);
  else if(phaseIs("nextStep")){
    calcuExcitation();
    capStrength();
    holdIn("sendInhibition",0);
  }
}

public message out(){
message m = new message();
if(phaseIs("sendInhibition"))
    m.add(makeContent("inhib_out", new int_double_Ent(myID,myExcitation)));
else if(phaseIs("sendMyExcit"))
    m.add(makeContent("my_excit", new int_double_Ent(myID,myExcitation)));
else if(phaseIs("sendActionCommd")){
    if(commdEnt!=null)  m.add(makeContent("commd_out", commdEnt));
  }
  return m;
}


//-----------------------------------------------------------------------
public void reCalcuExcitation(int id, double inhibition){
  if(inhibition>=1.0)
    myExcitation = myExcitation - configData.KIN[myID][id]*inhibition;
}

public void capStrength(){ // Cap strength of excitation
   if (myExcitation > 20) myExcitation = 20;
}

public void updateEnergy(){
// Update energy
double energy = cRps.Energy + configData.ECOST[myID];
cRps.setEnergy(energy);  // update energy to the central repository
if (energy <= 0){
        System.out.println("crayfish starved at time:"+this.getSimulationTime());
        envI.deActivateCrayfish();
}
  }

public void calcuExcitation(){}  // to be implemented by the child model

public void prepareAction(){}  // to be implemented by the child model

}