
package genDevs.simulation.special;

import genDevs.modeling.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
import GenCol.*;
import util.*;
import simView.*;
import genDevs.simulation.*;


public class DTSSSim  extends coupledSimulator{//to work with 1 dim
protected boolean amIminent;

public DTSSSim(IOBasicDevs d){
super(d);
amIminent = false;
}

public  void computeInputOutput(double t){
   if (myModel.ta() >= Double.POSITIVE_INFINITY)
    amIminent = false;
    else
    amIminent = true;
//System.out.println(myModel.getName()+" Imm "+amIminent);
      if(amIminent) {
          output = myModel.Out();
      }
      else{
        output = new message();//bpz
      }

      computeInputOutputHook1();
}
public  synchronized void  wrapDeltfunc(double t,MessageInterface x){
 if(x == null){
    System.out.println("ERROR RECEIVED NULL INPUT  " + myModel.toString());
    return;
  }
  if (x.isEmpty() && !amIminent) {
    return;
  }
  else if((!x.isEmpty()) && amIminent) {
    double e = t - tL;
    myModel.deltcon(e,x);
  }
  else if(amIminent) {
    myModel.deltint();
  }
  else if(!x.isEmpty()) {
    double e = t - tL;
    myModel.deltext(e,x);
  }
  wrapDeltfuncHook2();
}
}
