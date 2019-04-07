package genDevs.modeling;

import genDevs.simulation.*;
import GenCol.*;

import java.util.*;

public abstract class HILActivity extends abstractActivity implements Runnable, ActivityInterface {

protected  Thread myThread;

public HILActivity(String name){
 super(name);
 myThread = new Thread(this);
}

public void kill(){
myThread.interrupt();
}

public void start(){myThread.start();}


public void putInstantInput(String inputPort, entity ent){} // not implemented in HILActivity

/**
 * put message on the output port and immediately send the message to
 * other models based on the abstractActivity coupling
 * note that mySim should be a real time simulator because this is HIL simulation
 */
public void sendInstantOutput(String outputPort, entity ent){
  content ct = makeContent(outputPort,ent);
  message m = new message();
  m.add(ct);
  ((coupledSimulator)mySim).setOutput(m);
  mySim.sendMessages();
}

}
