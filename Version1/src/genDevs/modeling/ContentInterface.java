package genDevs.modeling;

import GenCol.*;

import java.util.*;



public interface ContentInterface {
public PortInterface getPort();
public String getPortName();
public Object getValue();
public boolean onPort(PortInterface port);
}



