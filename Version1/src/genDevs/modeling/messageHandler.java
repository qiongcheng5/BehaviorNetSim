package genDevs.modeling;

import GenCol.*;

import java.util.*;


public class messageHandler implements IODevs{
protected ports inports,outports;
protected message input,output;

public ports getInports(){
return inports;
}
public ports getOutports(){
return outports;
}

public messageHandler(){
inports = new ports();
outports = new ports();
}

public ExternalRepresentation getExtRep(){return new ExternalRepresentation.ByteArray();}
public String getName(){return "messageHandler";}
public Object equalName(String name){return null; }
public void addInport(String portName){inports.add(new port(portName));}
public void addOutport(String portName){outports.add(new port(portName));}

public ContentInterface makeContent(PortInterface p,EntityInterface value){
return new content((port)p,(entity)value);
}
public content makeContent(String p,entity value){  //for use in usual devs
return new content(p,value);
}
public boolean   messageOnPort(MessageInterface x,PortInterface p, ContentInterface c){
return  x.onPort(p,c);
}
public boolean   messageOnPort(message x,String p, int i){ //for use in usual devs
return x.onPort(p,i);
}

}