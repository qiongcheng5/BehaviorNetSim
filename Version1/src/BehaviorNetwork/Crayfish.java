package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class Crayfish extends ViewableDigraph{//digraph{

  public Crayfish(){
    this("crayFish");
  }

public Crayfish(String nm){
super(nm);

BehaviorNet behaviorNet = new BehaviorNet("BehaviorNet");
add(behaviorNet);

//behaviorNet.setBlackBox(true);

}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(707, 368);
        ((ViewableComponent)withName("BehaviorNet")).setPreferredLocation(new Point(55, 68));
    }
}
