package Mutual_Inhibition;

import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;
import GenCol.*;
import java.util.*;


class MutualInib_Study extends ViewableDigraph{//digraph{

  public MutualInib_Study(){
    this("MI_Study");
  }

public MutualInib_Study(String nm){
super(nm);

Environment env = new Environment("Environment");
add(env);

    Crayfish crayF = new Crayfish(nm+"_Crayfish");
    add(crayF);

    Predator pred = new Predator(nm+"_predator");
    add(pred);
}

}