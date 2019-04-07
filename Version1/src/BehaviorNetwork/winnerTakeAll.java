package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class winnerTakeAll extends ViewableAtomic{//atomic{
  public int id, winner;
  public double excitation, minval;

public winnerTakeAll(String nm){ super(nm); }
public winnerTakeAll(){ this("winnerTakeAll");  }

public void initialize(){
  addInport("in");
  //addOutport("out");
  for(int i=0;i<configData.numOfBehavior;i++)
    addOutport("out"+i);
  passivate();
}

public void deltext(double e,message x){
  Continue(e);
  winner = configData.REST;
  minval = configData.MINTHRESHHOLD;
  for (int i = 0; i < x.getLength(); i++) {
    if (messageOnPort(x, "in", i)) {
      id = ( (int_double_Ent) x.getValOnPort("in", i)).getX();
      excitation = ( (int_double_Ent) x.getValOnPort("in", i)).getY();
      if (excitation >= minval){
         winner = id;
         minval = excitation;
      }
    }
  }
  holdIn("sendOutput",0);
}

public void deltint(){
  passivate();
}

public message out(){
message m = new message();
if(phaseIs("sendOutput"))
    m.add(makeContent("out"+winner, new entity("winner")));
return m;
}

}
