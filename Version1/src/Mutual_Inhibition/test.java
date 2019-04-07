
package Mutual_Inhibition;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import java.util.*;

public class test  {
protected RTcoordinator cs;
protected static digraph testDig;


public static void main (String[ ] args){

testDig = new MutualInib_Study("test");

//testDig = new MultiRobot("MultiRobot");

/*
RTcoordinator cs = new RTcoordinator(testDig);
cs.initialize();
cs.simulate(80,200000);


coordinator cs = new coordinator(testDig);
cs.initialize();
cs.simulate(150);
*/

TunableCoordinator cs = new TunableCoordinator(testDig,0.04);
cs.initialize();
cs.simulate(8000);
/**/
}

}