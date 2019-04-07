package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Eat extends Behavior{
double odour, hunger, dfood;

public behavior_Eat(){
    this("EAT");
  }

public behavior_Eat(String nm){
    super(nm);
    myID = configData.EAT;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    odour = envI.getFoodAmount()/(dfood+1);
    dfood = envI.getFoodDist();
    hunger = cRps.getHunger();
    if (dfood <= 10)  myExcitation = configData.KEX[myID] * odour * hunger / (hunger + 4);
    else myExcitation = 0;
  }

public void doAction(){
    double vel = configData.MOVE[myID];
    double direction = envI.getCrayfishAngle();  //maintain previous direction
    // Update position and energy
    envI.moveCrayFish(vel, direction);
    updateEnergy();

    //update food in environment
    //envI.FoodEatenUpdate(NIBBLE_AMT);
    envI.FoodEatenUpdate(configData.NIBBLE_AMT/5);
  }

}