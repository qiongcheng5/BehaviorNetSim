package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Escape extends Behavior{
double dpred;

public behavior_Escape(){
    this("ESCAPE");
  }

public behavior_Escape(String nm){
    super(nm);
    myID = configData.ESCAPE;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    dpred = envI.getPredDist();
    double dpred1 = !envI.CrayfishIsHidden()? dpred : Double.MAX_VALUE;
    myExcitation = configData.KEX[myID] * Math.exp(-dpred1 / configData.KLENGTH_ESC);
  }

public void doAction(){
    double vel = configData.MOVE[myID];
    double direction = envI.getCrayfishAngle();
    // Set direction to forwards or backwards, depending on predator pos
    Vect v = envI.getPredRelPos();
    Vect dir = new Vect(Math.cos(direction),Math.sin(direction));
    if (v.dot(dir) > 0)
    {
            direction = direction + Math.PI;
            if (direction > 2 * Math.PI)  direction -= 2 * Math.PI;

    }
    // Update position and energy
    envI.moveCrayFish(vel,direction);
    updateEnergy();

    // update the escape time, which is used by the Swim bebavior model
    cRps.setEscape(true);
    cRps.setCTescape(myExcitation);
    cRps.setlastEscTime(getSimulationTime());

  }

}