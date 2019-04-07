package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Forage extends Behavior{
double odour, hunger, dfood;
Vect fooddir;

public behavior_Forage(){
    this("FORAGE");
  }

public behavior_Forage(String nm){
    super(nm);
    myID = configData.FORAGE;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
odour = envI.getFoodAmount()/(dfood+1);
dfood = envI.getFoodDist();
hunger = cRps.getHunger();
myExcitation = configData.KEX[myID] * odour * hunger / (hunger + 4);
  }

public void doAction(){
    fooddir = envI.getFoodRelPos();
    // Move in the direction of the food source
    double direction = fooddir.angle();
    double vel = configData.MOVE[myID];
    // no point overshooting the food
    if ( dfood < vel/2 ) vel = 0;
    // Update position and energy
    envI.moveCrayFish(vel,direction);
    updateEnergy();
  }

}