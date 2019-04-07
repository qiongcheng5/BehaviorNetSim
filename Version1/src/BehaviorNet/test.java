package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import java.util.*;

public class test  {

protected static digraph testDig;

public static void main (String[ ] args){

testDig = new crayfishSys("crayfishSys");
/*
coordinator cs = new coordinator(testDig);
cs.initialize();
cs.simulate(15000);
*/

TunableCoordinator cs = new TunableCoordinator(testDig,0.04);
cs.initialize();
cs.simulate(8000);
/**/
}

}