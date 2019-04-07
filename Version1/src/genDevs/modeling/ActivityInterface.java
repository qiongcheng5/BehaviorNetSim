package genDevs.modeling;

import GenCol.*;

import java.util.*;
import genDevs.simulation.*;

public interface ActivityInterface{
public void setActivitySimulator(CoupledSimulatorInterface sim);
public String getName();
public void kill();
public void start();
public void returnTheResult(entity myresult);
}