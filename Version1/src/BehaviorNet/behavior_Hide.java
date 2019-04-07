package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Hide extends Behavior{

public behavior_Hide(){
    this("HIDE");
  }

public behavior_Hide(String nm){
    super(nm);
    myID = configData.HIDE;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    if (envI.CrayfishIsHidden())  myExcitation = configData.KEX[myID];
    else  myExcitation = 0;
  }

public void prepareAction(){
    double direction = envI.getCrayfishAngle();
    double vel = configData.MOVE[myID];
    // Update position and energy
    commdEnt = new double_double_Ent(vel,direction);
    //envI.moveCrayFish(vel,direction);
  }

}