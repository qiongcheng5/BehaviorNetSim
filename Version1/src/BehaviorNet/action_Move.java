package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class action_Move extends Action{
double vel;
double direction;

public action_Move(){
    this("action_Move");
  }

public action_Move(String nm){
    super(nm);
  }

public void initialize(){
super.initialize();
  }

public void doAction(){
  Vect v = new Vect(0,0);
  for (int ci =0; ci<cNum; ci++){  // vector sum
    vel = ((double_double_Ent)command[ci]).getX();
    direction = ((double_double_Ent)command[ci]).getY();
    if(vel!=0)
      v.incr(new Vect(vel* Math.cos(direction), vel*Math.sin(direction)));
  }
  if(!v.equals(Vect.ZERO)){
    vel = v.mag();
    direction = v.angle();
  }
  envI.moveCrayFish(vel,direction);
}

}