package genDevs.modeling;

import GenCol.*;
import java.util.*;

interface atomicDevs {

public void Continue(double e);
public void passivate();
public void passivateIn(String phase);
public void holdIn(String phase, double time);
public void holdIn(String phase, double time, ActivityInterface a);
public boolean phaseIs(String phase);
//bpz
public double getSigma();
public void setSigma(double sigma);
}

