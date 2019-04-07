package genDevs.modeling;

import GenCol.*;

import java.util.*;



public abstract class devs extends entity implements IODevs{
//IOBasicDevs,EntityInterface{

//protected
public messageHandler mh;
protected IODevs myParent;

public devs(String name){
super(name);
mh = new messageHandler();
}

public void setParent(IODevs p) { myParent = p;}
public IODevs getParent() { return myParent;}

public void addInport(String portName){mh.addInport(portName);}
public void addOutport(String portName){mh.addOutport(portName);}
public ContentInterface makeContent(PortInterface port,EntityInterface value)
              {return mh.makeContent(port, value); }
public content makeContent(String p,entity value){  //for use in usual devs
return new content(p,value);
}
public boolean   messageOnPort(MessageInterface x, PortInterface port, ContentInterface c)
            {return mh.messageOnPort(x,port,c);}

public boolean   messageOnPort(message x,String p, int i){ //for use in usual devs
return x.onPort(p,i);
}
abstract public  void initialize();

public devs findModelWithName(String nm){  //Xiaolin Hu, Nov 18, 2003
  if(nm.compareTo(getName())==0) return this;
  else return null;
}

}


