package genDevs.simulation;

import java.util.*;
import GenCol.*;
import genDevs.modeling.*;

//what is the core requirement on legacy models if they
//are just required to have a message interface
//but not necessarily a Devs structure

//standalone simulator

public interface coreSimulatorInterface{

public void initialize();
public Double nextTNDouble();
public void computeInputOutput(Double d);
public void DeltFunc(Double d);
public MessageInterface getOutput();
public void simulate(int numIter);
}






