package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Rest extends Behavior{

public behavior_Rest(){
    this("REST");
  }

public behavior_Rest(String nm){
    super(nm);
    myID = configData.REST;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    myExcitation = 0; // this is the default behavior
  }

public void prepareAction(){
    double direction = envI.getCrayfishAngle();
    double vel = configData.MOVE[myID];
    // Update position and energy
    //commdEnt = null;
    commdEnt = new double_double_Ent(vel,direction);
    //envI.moveCrayFish(vel,direction);
  }

}