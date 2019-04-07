package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Swim extends Behavior{

public behavior_Swim(){
    this("SWIM");
  }

public behavior_Swim(String nm){
    super(nm);
    myID = configData.SWIM;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    if (cRps.getEscape())
            myExcitation = configData.KEX[myID] * cRps.getCTescape()
                * Math.exp( -(getSimulationTime() - cRps.getlastEscTime()) / 3 );
    else    myExcitation = 0.0;
  }

public void doAction(){
    double direction = envI.getCrayfishAngle();
    double vel = configData.MOVE[myID];
    // Update position and energy
    envI.moveCrayFish(vel,direction);
    updateEnergy();
  }

}