
package GenCol;


import java.util.*;


public class FunctionIterator implements Iterator{
private Function f;
private Queue keys;
private Object curKey = null;


public FunctionIterator (Function F){
f = F;
Set keyset = F.keySet();
keys = Queue.set2Queue(keyset);
}

private Pair Next(){
if (keys.isEmpty())return null;
curKey = keys.first();
return new Pair(curKey,f.get(curKey));
}


public Object next(){
Object ret = Next();
removeNext();
return ret;
}



private void removeNext(){
keys.remove();
}

public boolean hasNext(){
return Next() != null;
}



public void remove(){
}

}