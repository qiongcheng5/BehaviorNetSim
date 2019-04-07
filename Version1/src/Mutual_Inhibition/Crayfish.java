package Mutual_Inhibition;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

class Crayfish extends ViewableDigraph{//digraph{

  public Crayfish(){
    this("crayFish");
  }

public Crayfish(String nm){
super(nm);

Behavior behaviorModel = new Behavior(nm+"_behaviorModel");
add(behaviorModel);

/*
addCoupling(avo,"distanceData",wan,"distanceData");
addCoupling(avo,"moveComplete",wan,"moveComplete");
addCoupling(wan,"move",avo,"move");
*/
}

}
