package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class action_Eat extends Action{
double odour, hunger, dfood;

public action_Eat(){
    this("action_Eat");
  }

public action_Eat(String nm){
    super(nm);
}

public void initialize(){
super.initialize();
}

public void doAction(){
    double am=0;
    for (int ci =0; ci<cNum; ci++){  // winner takes all
      if(((doubleEnt)command[ci]).getv()>am)
        am = ((doubleEnt)command[ci]).getv();
    }
    envI.FoodEatenUpdate(am);
}

}