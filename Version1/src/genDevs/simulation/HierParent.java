package genDevs.simulation;

import GenCol.*;
import java.util.*;
import genDevs.modeling.*;

interface HierParent {
public void setParent( CoupledCoordinatorInterface p);  //bpz
public void setRootParent(CoordinatorInterface p);
public CoupledCoordinatorInterface getParent();
public CoordinatorInterface getRootParent();
}
