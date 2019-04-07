package genDevs.modeling.basicModels;

import java.lang.*;
import java.util.*;
import GenCol.*;
import genDevs.modeling.*;
import simView.*;

/**
 * a network delay model which hold on any received messages before the messages are
 * sent out. The order of messages is preserved.
 * @author      Xiaolin Hu
 */
public class delayModel extends ViewableAtomic{//atomic{
private double  delay, lastMessageTime, nextTime;
private Pair M_T_pair;
private entity ent, nextMessage;
private messageQueue mq = new messageQueue(10);


public delayModel(String nm, double d){
super(nm);
addInport("in");
addOutport("out");
delay = d;
}

public delayModel(){
this("delayModel",0);
}

public void initialize(){
passivate();
}

public void deltext(double e,message x){
Continue(e);
for (int i=0; i< x.getLength();i++){
  if (messageOnPort(x,"in",i)){
    ent =  x.getValOnPort("in",i);
//    System.out.println(getName()+" get input message: "+ent.toString());
    if (phaseIs("passive")){
      lastMessageTime = 0;
      nextMessage = ent;
      holdIn("active",delay);
    }
    else if(phaseIs("active")){
      M_T_pair = new Pair(ent,new Double(e-lastMessageTime));
      mq.addMessage(M_T_pair);
      lastMessageTime = 0;
    }
  }
}
}

public void deltint(){
if (mq.getFirst()!=null){ // there are other messages
M_T_pair = (Pair)mq.removeFirst();
ent = (entity) M_T_pair.getKey();
nextTime = ((Double)M_T_pair.getValue()).doubleValue();
lastMessageTime = lastMessageTime+ (0-getSigma());
nextMessage = ent;
holdIn("active",nextTime);
}
else passivate();
}

public message out(){
message m = new message();
m.add(makeContent("out",nextMessage));
//System.out.println(getName()+" -------------------send output message: "+nextMessage.toString());
return m;
}

public void showState(){
   super.showState();
  }

// implement a message queue
class messageQueue extends Vector{
Vector vq;
Object sObject;

public messageQueue(int length){
vq = new Vector(length);
}

public void addMessage(Object ms){
vq.addElement(ms);
}

public Object removeFirst(){
if(vq.size()>0){
  sObject = vq.elementAt(0);
  vq.removeElementAt(0);
  return sObject;
}
else return null;
}

public Object getFirst(){
if(vq.size()>0) return vq.elementAt(0);
else return null;
}

public Object getElementAt(int index){
if(index>=0 && index<vq.size()) return vq.elementAt(index);
else return null;
}

} // end of class messageQueue

}




