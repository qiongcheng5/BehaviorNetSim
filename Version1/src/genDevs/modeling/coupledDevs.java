package genDevs.modeling;

import GenCol.*;

import java.util.*;
import java.awt.Color;
import genDevs.simulation.*;




public interface coupledDevs extends EntityInterface{

public void add(IODevs d);
public void addCoupling(IODevs src, String p1, IODevs dest, String p2);

public IODevs withName(String nm);
public ComponentsInterface getComponents();
public couprel getCouprel();
public void setCoordinator(coordinator coordinator);
public coordinator getCoordinator();
}



