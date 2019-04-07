package genDevs.simulation.special;

import java.lang.*;
import java.io.*;
import GenCol.*;
import java.util.*;

public class minEnvironment extends Function{
public double timeGranule = .000001;//0.0001;//0.001;//.01; //.05; //.000001;//
protected String name;
protected TreeSet ts;

public minEnvironment(){
   super();
   ts = new TreeSet();
}

public minEnvironment(String nm){
   super();
   name=nm;
   ts = new TreeSet();
}

//replace String by Object
public void addName(Object name){
put(name,new Pair());
setVal(name,Double.POSITIVE_INFINITY);
}

public void setVal(Object name, double val){//atomic imms = self
ensembleSet s = new ensembleSet();
s.add(name);
replace(name, new Pair(s,new doubleEnt(val)));
//ts.add(new Double(val));
}

public void setPair(Object name, Pair p){//Pair(imms,tN)
replace(name, p);
//doubleEnt d = (doubleEnt)p.getValue();
//ts.add(new Double(d.getv()));
}



public void reset(){

FunctionIterator itr = (FunctionIterator)iterator();
    while(itr.hasNext())
      {
        Pair p = (Pair)itr.next();
        Object ent = p.getKey();
        String inf = ent.toString();
        Pair pp = (Pair)p.getValue();
        doubleEnt dent = (doubleEnt)pp.getValue();
        dent.setv(Double.NEGATIVE_INFINITY);
      }
}
/*
public Pair whichMin(){//imminents and tN
ensembleSet which = new ensembleSet();
if (isEmpty())return new Pair(which,new doubleEnt(Double.POSITIVE_INFINITY));
ts = new TreeSet();
FunctionIterator itr = (FunctionIterator)iterator();

while(itr.hasNext())
       {
   Pair p = (Pair)itr.next(); //name, Pair
   Pair pp = (Pair)p.getValue(); //imms, tN
   doubleEnt dent = (doubleEnt)pp.getValue();
   ts.add(new Double(dent.getv()));
    }

 double min = ((Double)ts.first()).doubleValue();

//FunctionIterator
itr = (FunctionIterator)iterator();

//System.out.println("starting whichMin "+name+" doing "+this.keySet().size()+" comparisons");
 while(itr.hasNext())
       {
   Pair p = (Pair)itr.next(); //name, Pair
   Pair pp = (Pair)p.getValue(); //imms, tN
   doubleEnt dent = (doubleEnt)pp.getValue();
   if (Math.abs(dent.getv()- min)< timeGranule)
    which.addAll((ensembleSet)pp.getKey());
    }
//System.out.println("finishing whichMin "+name);
return new Pair(which,new doubleEnt(min));
}

*/

public Pair whichMin(){//imminents and tN
ensembleSet which = new ensembleSet();
double min = Double.POSITIVE_INFINITY;
FunctionIterator itr = (FunctionIterator)iterator();
//System.out.println("starting whichMin "+name+" doing "+this.keySet().size()+" comparisons");
 while(itr.hasNext())
       {
   Pair p = (Pair)itr.next(); //name, Pair
   Pair pp = (Pair)p.getValue(); //imms, tN
   doubleEnt dent = (doubleEnt)pp.getValue();
   if (Math.abs(dent.getv()- min)< timeGranule)
    which.addAll((ensembleSet)pp.getKey());
   else if (dent.getv() < min){
      which = new ensembleSet();
      min = dent.getv();
     which.addAll((ensembleSet)pp.getKey());
     }
    }
//System.out.println("finishing whichMin "+name);
return new Pair(which,new doubleEnt(min));
}

public Pair pullWhichMin(){//imminents and tN
ensembleSet which = new ensembleSet();
double min = Double.POSITIVE_INFINITY;
FunctionIterator itr = (FunctionIterator)iterator();
//System.out.println("starting whichMin "+name+" doing "+this.keySet().size()+" comparisons");
 while(itr.hasNext())
       {
   Pair p = (Pair)itr.next(); //name, Pair
   Pair pp;
//   if (p.getValue() == null){//if not reported, go get it
   if (((doubleEnt)((Pair)p.getValue()).getValue()).getv() == -1){//Xiaolin Hu
     minSel child = (minSel)p.getKey();
     pp = child.de.pullWhichMin();
    }
   else pp = (Pair)p.getValue(); //imms, tN

   doubleEnt dent = (doubleEnt)pp.getValue();
   if (Math.abs(dent.getv()- min)< timeGranule)
    which.addAll((ensembleSet)pp.getKey());
   else if (dent.getv() < min){
      which = new ensembleSet();
      min = dent.getv();
     which.addAll((ensembleSet)pp.getKey());
     }
    }
//System.out.println("finishing whichMin "+name);
return new Pair(which,new doubleEnt(min));
}
public static void main(String[] args){
Double d1 = new Double(1);
Double d2 = new Double(2);
Double d3 = new Double(4);
Pair  p1 = new Pair (new entity("a"),d1);
Pair  p2 = new Pair  (new entity("b"),d2);
Pair  p3 = new Pair  (new entity("c"),d3);
Function f = new Function();
f.put(new entity("a"),d1);
f.put(new entity("b"),d2);
f.put(new entity("c"),d3);
System.out.println(f.valueSet());


TreeSet ts = new TreeSet(new minComparator());
//TreeSet ts = new TreeSet();
ts.add(p1);
//ts.add(d1);
ts.add(p2);
//ts.add(d2);
System.out.println(ts.first());
ts.add(p3);
//ts.add(d3);
System.out.println(ts.first());
ts.remove(p1);
System.out.println(ts.first());
/*
minEnvironment d = new minEnvironment();

System.out.println(d.whichMin().toString());
d.addName("o1");
d.setVal("o1",3);
d.addName("o2");
d.setVal("o2",5);
System.out.println(d.whichMin().toString());
d.setVal("o3",3.000000000001);
System.out.println(d.whichMin().toString());
d.setVal("o1",7);
System.out.println(d.whichMin().toString());
d.setVal("o3",7);
System.out.println(d.whichMin().toString());
ensembleSet s = new ensembleSet();
s.add("a1");
s.add("a2");
d.setPair("o4",new Pair(s,new doubleEnt(5)));
System.out.println(d.whichMin().toString());
d.setVal("o3",4.99999999);
System.out.println(d.whichMin().toString());
*/
}
}
class minComparator implements Comparator{


public minComparator(){
}

public boolean equals(Object obj){
return false;
}

public int compare(Object o1, Object o2){
if (!(o1 instanceof Pair))
return -1;
if (!(o2 instanceof Pair))
return -1;
Pair p1 = (Pair)o1;
Double val1 = (Double)p1.getValue();
Pair p2 = (Pair)o2;
Double val2 = (Double)p2.getValue();
return -compare(val1,val2);
}
}
