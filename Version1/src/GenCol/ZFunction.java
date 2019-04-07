/*
/*  ZFunction is same as relation in ZContainers
/*  can't define relation and Function due to Java restriction
*/

package GenCol;


import java.util.*;


public class ZFunction  extends Function implements ZFunctionInterface{


public ZFunction(){
super();
}

public boolean empty(){
return isEmpty();
}

public int getLength(){
return size();
}

public Object assoc(Object key){
return get(key);
}

public boolean keyIsIn(Object key){
return containsKey(key);
}

public boolean valueIsIn(Object value){
return containsValue(value);
}

public boolean isIn(Object key, Object value){
return contains(key, value);
}

public Set domainObjects(){
return keySet();
}

public Set rangeObjects(){
return valueSet();
}


public synchronized void add(Object key, Object value){
put(key,value);
}

public synchronized void Remove(Object key){
remove(key);
}

public synchronized void replace(Object key, Object value){
put(key,value);
}

}