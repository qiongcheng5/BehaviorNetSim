package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class centralRepository extends abstractActivity{

  // define the variables shared by all models
  double Energy, Hunger;
  int currentBehavior;
  double lastEscTime, CTescape;
  boolean escape;


  public centralRepository(String nm){
  super(nm);
  }

  public centralRepository(){
  this("centralRepository");
  }

  public void initialize(){
    passivate();
    //initialize some global variables
    Energy = configData.DEF_ENERGY;
    Hunger = 100 * Math.exp(-4 * Energy);
  }

  public void setCurrentBehavior(int b){currentBehavior = b;}
  public int getCurrentBehavior(){return currentBehavior;}

  public void setEnergy(double e){
    Energy = e;
    Hunger = 100 * Math.exp(-4 * Energy);
  }

  public double getEnergy(){ return Energy;}
  public double getHunger(){ return Hunger;}

  // the following functions are used by Swim behavior
  public void setlastEscTime(double t){lastEscTime = t;}
  public double getlastEscTime(){return lastEscTime;}

  public void setEscape(boolean e){escape = e;}
  public boolean getEscape(){return escape;}

  public void setCTescape(double c){CTescape = c;}
  public double getCTescape(){return CTescape;}

}