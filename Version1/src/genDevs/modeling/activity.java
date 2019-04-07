package genDevs.modeling;

import GenCol.*;
import genDevs.simulation.*;
import java.util.*;

public class activity extends Thread implements ActivityInterface {

protected CoupledSimulatorInterface modelSim;

public activity(String name){
 super(name);
}

public void setActivitySimulator(CoupledSimulatorInterface sim){
this.modelSim = sim;
}

public void returnTheResult(entity myresult) {
//System.out.println("return result in Activity");
 modelSim.returnResultFromActivity(myresult);
}

/*
public void run(){
   try {
 sleep((long)getProcessingTime()*1000);
} catch (InterruptedException e) {return;}
returnTheResult();
}
*/

public void kill(){
interrupt();
}


}