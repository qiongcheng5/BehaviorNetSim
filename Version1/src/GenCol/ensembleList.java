package GenCol;

import java.util.*;

public class ensembleList extends HashSet implements ensembleInterface{

private ensembleWrapper.ensemble el;

public ensembleList(){
el = ensembleWrapper.make(this);
}

public ensembleList(Collection c){
addAll(c);
el = ensembleWrapper.make(this);
}

public void tellAll(String MethodNm,Class[] classes,Object[] args){
el.tellAll(MethodNm,classes,args);
}

public void tellAll(String MethodNm){
Class [] classes = {};
Object [] args  = {};
el.tellAll(MethodNm,classes,args);
}


public void AskAll(ensembleInterface result,String MethodNm,Class[] classes,Object[] args){
el.AskAll(result,MethodNm,classes,args);
}

public void which(ensembleInterface result,String MethodNm,Class[] classes,Object[] args){
 //MethodNm method must return this if condition is true and null otherwise
el.AskAll(result,MethodNm,classes,args);
}

public Object whichOne(String MethodNm,Class[] classes,Object[] args){
return el.whichOne(MethodNm,classes,args);
}

public  ensembleInterface copy(ensembleInterface c){
return el.copy(c);                             
}

public  void wrapAll(ensembleInterface Result,Class cl){
el.wrapAll(Result,cl);
}

public boolean none(String MethodNm,Class[] classes,Object[] args){
return el.none(MethodNm,classes,args);
}
public boolean all(String MethodNm,Class[] classes,Object[] args){
return el.all(MethodNm,classes,args);
}

public void print(){
el.print();
}

}