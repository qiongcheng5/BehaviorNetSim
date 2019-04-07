
package GenCol;



import java.util.*;

public class Queue extends LinkedList{  //add is to end

public Object remove(){
if (size()>0){
	return remove(0);
}
else 
	return null;
}

public Object first(){
return get(0);
}


static public Queue set2Queue(Set s){
Queue q = new Queue();
Iterator it = s.iterator();
while (it.hasNext())q.add(it.next());
return q;
}

public Bag Queue2Bag(){
Bag b = new  Bag();
Iterator it = iterator();
while (it.hasNext())b.add(it.next());
return b;
}

}


