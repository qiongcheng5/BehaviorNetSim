package BehaviorNetwork;

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

public void doAction(){
    double direction = envI.getCrayfishAngle();
    double vel = configData.MOVE[myID];
    // Update position and energy
    envI.moveCrayFish(vel,direction);
    updateEnergy();
  }

}