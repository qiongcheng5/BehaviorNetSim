package genDevs.modeling;

import GenCol.*;

import java.util.*;



public interface IODevs extends EntityInterface{

public void addInport(String portName);
public void addOutport(String portName);
//public void removeInport(String portName);;
//public void removeOutport(String portName);
public ContentInterface makeContent(PortInterface port,EntityInterface value);
public boolean   messageOnPort(MessageInterface x, PortInterface port, ContentInterface c);
}