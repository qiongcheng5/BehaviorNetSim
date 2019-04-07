

package genDevs.simulation;

import GenCol.*;

import java.util.*;
import genDevs.modeling.*;


public interface CoordinatorInterface extends AtomicSimulatorInterface{
public void setSimulators();
public void addSimulator(IOBasicDevs comp);
public void addCoordinator(Coupled comp);
public coupledDevs getCoupled();
public void addExtPair(Pair cs,Pair cd) ;
public void informCoupling();
public void showCoupling();
public Relation convertInput(MessageInterface x);
public void putMyMessages(ContentInterface c);
public void sendDownMessages();
//public void decrement();


}