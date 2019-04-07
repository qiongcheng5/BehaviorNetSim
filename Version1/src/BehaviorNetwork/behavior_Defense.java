package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Defense extends Behavior{
double dpred;

public behavior_Defense(){
    this("DEFENSE");
  }

public behavior_Defense(String nm){
    super(nm);
    myID = configData.DEFEND;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    dpred = envI.getPredDist();
    myExcitation = configData.KEX[myID] * Math.exp(-dpred / configData.KLENGTH_DEF);
  }

public void doAction(){
    // Put up a defensive posture, hopefully it will scare the predator away.
    // crayfish should face predator or other crayfish
    Vect face = (Vect)Vect.ZERO.clone();
    Vect preddir = envI.getPredRelPos();
    if (preddir != null){
            double dpred = preddir.mag();
            Vect vpred = preddir.unit().scalarMultiply(Math.exp(-dpred));
            face.incr(vpred);
    }
    double direction ;
    double vel = configData.MOVE[myID];
    if (!face.equals(Vect.ZERO)){  // need to turn to that direction
      direction = face.angle();
    }
    else direction = envI.getCrayfishAngle();  //maintain previous direction

    envI.moveCrayFish(vel,direction);
    updateEnergy();
  }

}