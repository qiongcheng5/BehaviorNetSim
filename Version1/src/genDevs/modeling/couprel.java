package genDevs.modeling;

import GenCol.*;

import java.util.*;



public class couprel extends Relation{

public  synchronized Object add(Object key, Object value){
return put(key,value);
}

  public   synchronized void add(entity c1,port p1,entity c2,port p2){
    Pair coup1 = new Pair(c1.getName(),p1.getName());
    Pair coup2 = new Pair(c2.getName(),p2.getName());
    add(coup1,coup2);
  }


  public   synchronized void remove(entity c1,port p1,entity  c2,port p2){
    Pair coup1 = new Pair(c1.getName(),p1.getName());
    Pair coup2 = new Pair(c2.getName(),p2.getName());
      remove(coup1,coup2);
  }


  public   synchronized HashSet translate(String srcName,String ptName){
      Pair cp = new Pair(srcName,ptName);
       return (HashSet)getSet(cp);
  }


  public   synchronized Set assocPair(Pair cpr) {
    return getSet(cpr);
    }
}