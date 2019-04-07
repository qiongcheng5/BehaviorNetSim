package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class Action extends ViewableAtomic{//atomic{

EnvInterface envI;
centralRepository cRps;

int cNum;
entity[] command = new entity[configData.numOfBehavior]; //set to the largest capacity

public Action(String nm){ super(nm); }
public Action(){ this("Action");  }

public void initialize(){
  addInport("In");

  // the interface to the environment model and central repository
  envI = (EnvInterface)getModelWithName("EnvInterface");
  cRps = (centralRepository)getModelWithName("centralRepository");

  passivate();
}

public void deltext(double e,message x){
  Continue(e);
  cNum=0;
  for (int i = 0; i < x.getLength(); i++) {
    if (messageOnPort(x, "In", i)) {
      command[cNum] = x.getValOnPort("In", i);
      cNum++;
    }
  }
  doAction();
}

public void deltint(){
}

public message out(){
message m = new message();
return m;
}


//-----------------------------------------------------------------------
public void doAction(){}  // to be implemented by the child model

}