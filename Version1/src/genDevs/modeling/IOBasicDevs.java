
package genDevs.modeling;

import GenCol.*;

import java.util.*;
import genDevs.simulation.*;

interface basicDevs {

public void deltext(double e,MessageInterface x);
public void deltcon(double e,MessageInterface x);
public void deltint();
public MessageInterface Out();  //to allow out:message for usual devs
public double ta();
public void initialize();
public void  showState();
}

public interface IOBasicDevs extends basicDevs,IODevs{
public ActivityInterface getActivity();
public void setSimulator(CoupledSimulatorInterface sim);
/* removed these from here and digraph */
//public  void setSigma(double sigma);
//public double getSigma();

}
