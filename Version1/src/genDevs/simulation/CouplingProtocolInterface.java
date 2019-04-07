package genDevs.simulation;

import GenCol.*;
import java.util.*;
import genDevs.modeling.*;

//public
interface CouplingProtocolInterface {
public void putMessages(ContentInterface c);
public void sendMessages();
public void setModToSim(Function mts);
public void addPair(Pair cs,Pair cd);   //add coupling pair
public void removePair(Pair cs, Pair cd); //remove coupling pair
public void showCoupling();
}

