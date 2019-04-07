package GenCol;

import java.util.*;

interface ZFunctionInterface extends FunctionInterface{

public boolean empty();
public int getLength();
public boolean keyIsIn(Object key);
public boolean valueIsIn(Object value);
public boolean isIn(Object key, Object value);
public Object assoc(Object key);
public void add(Object key, Object value);
public void Remove(Object key);  //to avoid clash
public void replace(Object key, Object value);
public Set domainObjects();
public Set rangeObjects();
}


