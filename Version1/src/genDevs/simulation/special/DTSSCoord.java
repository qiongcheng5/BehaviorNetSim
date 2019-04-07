package genDevs.simulation.special;

import genDevs.modeling.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
import GenCol.*;
import util.*;
import simView.*;
import genDevs.simulation.*;

public class DTSSCoord extends coordinator {

public DTSSCoord (){
super();
} //added for use with SimView


public DTSSCoord(coupledDevs c){
super(c);
}

public void addSimulator(IOBasicDevs comp){
coupledSimulator s = new DTSSSim(comp);
s.setRootParent(this);      // set the parent
simulatorCreated(s, comp);
// later will download modelToSim to its children and then will be updated by its parents
// so after initialization, modelToSim store the brother models and simulators
// internalModelTosim store its children models and simulators
}

Double dum = new Double(0);

public void wrapDeltFunc(double time) {
sendDownMessages();
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {dum};
simulators.tellAll("DeltFunc",classes,args);
input = new message();
output = new message();
}



public void  simulate(int num_iter)
{
  int i=1;

  while( i<=num_iter ) {
    Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);

    computeInputOutput(1);
//showOutput();
    wrapDeltFunc(1);
 // showModelState();
    i++;
  }
  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

public static void main(String args[]){

/*
DTSSCoord c = new DTSSCoord(new earthquakeCellSpace( ));
c.initialize();
c.simulate(40);
*/
}
}



