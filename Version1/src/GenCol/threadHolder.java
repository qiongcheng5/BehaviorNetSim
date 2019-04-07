package GenCol;


import java.util.*;
import java.lang.reflect.*;

public class threadHolder extends Thread{
Object o;
Method m;
Class[] classes;
Object[] args;
ensembleCollection result;
countCoord coordinator;
boolean One = false;

static Class Entity  = ensembleBag.getTheClass("GenCol.entity");
//System.out.println(Entity.toString() +
//     " " +Entity.isInstance(new String()));

public threadHolder (Object O,String MethodNm,Class[] Classes,Object[] Args){
o = O;
classes = Classes;
args = Args;
Class cl = o.getClass();
result = null;
coordinator = null;

try{
      m = cl.getMethod(MethodNm,classes);
}
catch(Exception e1) {
System.out.println(e1);
    }
}
public threadHolder (ensembleCollection Result,Object O,String MethodNm,
              Class[] Classes,Object[] Args){
this(O,MethodNm,Classes,Args);
result = Result;
}

public threadHolder (ensembleCollection Result,Object O,String MethodNm,
             Class[] Classes,Object[] Args,countCoord Coordinator){
this(O,MethodNm,Classes,Args);
result = Result;
coordinator = Coordinator;
}

public threadHolder (ensembleCollection Result,Object O,String MethodNm,
             Class[] Classes,Object[] Args,countCoord Coordinator,
                                boolean one){
this(O,MethodNm,Classes,Args);
result = Result;
coordinator = Coordinator;
One = true;
}

public threadHolder (Object O,String MethodNm,
             Class[] Classes,Object[] Args,countCoord Coordinator){
this(O,MethodNm,Classes,Args);
coordinator = Coordinator;
}


public void runOne(){
//
if (!coordinator.isAlive()) return;
                     //to kill threads in whichOne
                     //need to make sure coord has been started
                     //so test result for non-empty

try{
Object out = m.invoke(o,args);
if (out != null){
    result.add(out);
    //
    coordinator.decrement();
    coordinator.allDecrement();
    return;
 }
}
catch(Exception e1) {System.out.println(e1); }
//
coordinator.allDecrement();
}

public void run(){
if (One)runOne();
else{
try{
Object out = m.invoke(o,args);
 if (result != null)
   if (out != null){
  result.add(out);
   }
 // else  System.out.println("null");
   }
catch(Exception e1) {System.out.println(e1); }
if (coordinator != null) coordinator.allDecrement();
}
}



public threadHolder (ensembleCollection e,Object O,Class cl){
try{
     Object nw =  cl.newInstance();
     wrapObject w = (wrapObject)nw;
     w.kernel = O;
     e.add(nw);
}
catch(Exception e1) {
System.out.println(e1);
    }
}

}
/*
class wrapObject extends Object{
public Object kernel;

public Boolean isContained(ensembleBag e){
return new Boolean(e.contains(kernel));
}


public synchronized void addSelf(ensembleBag e){
e.add(kernel);
}


public synchronized void  removeSelf(ensembleBag e){
e.remove(kernel);
}

public  synchronized  void removeSelf(ensembleBag source,ensembleBag criterion){
if (!criterion.contains(kernel))
      source.remove(kernel);
}

public void print(){
System.out.println(kernel.toString());
}
}
*/


