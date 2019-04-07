package genDevs.simulation.special;

import java.awt.*;
import java.lang.*;
import java.lang.Math;
import java.io.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;
import java.util.*;

public class minSel extends entity{

public boolean root;
protected minEnvironment de;
protected Pair whichMin;
protected int id;
protected int numImminents =0;
protected int receivedImmi = 0;

protected minSel superior;
int sCount = 0;

public minSel(String nm){
 super(nm);
 de = new minEnvironment(nm);
 root = false;
 whichMin = new Pair();
 }


public minSel(int id){
 this(""+id);
}

public minSel(String nm, ensembleSet ch){
this(nm);
//System.out.println("XXXXXXXXXXXXXX "+nm+" "+ch);
Iterator it = ch.iterator();
while(it.hasNext())
  addChild(((minSel)it.next()));
}

public minSel(String nm, ensembleSet ch,boolean pull){
this(nm);
//System.out.println("XXXXXXXXXXXXXX "+nm+" "+ch);
Iterator it = ch.iterator();
while(it.hasNext()){
  Object m = it.next();
  de.put(m,new Pair());
  ensembleSet s = new ensembleSet();
  s.add(m);
  ((minSel)m).superior = this;   // Xiaolin Hu
  de.replace(m, new Pair(s,new doubleEnt(Double.POSITIVE_INFINITY)));
  }
}

public void addChild(minSel child){
de.addName(child.getName());
child.superior = this;
}

public double getMin(){
doubleEnt d = (doubleEnt)whichMin.getValue();
if (d == null)return 0;
return d.getv();
}


public double pullGetMin(){
//whichMin = de.pullWhichMin();   //Xiaolin Hu
doubleEnt d = (doubleEnt)whichMin.getValue();
if (d == null)return 0;
return d.getv();
}

public ensembleSet getImms(){
if (whichMin.getKey()==null) return new ensembleSet();
return (ensembleSet)whichMin.getKey();
}

public ensembleSet pullGetImms(){
if (whichMin == null)
  whichMin = de.pullWhichMin();
if (whichMin.getKey()==null) return new ensembleSet();
return (ensembleSet)whichMin.getKey();
}

public void  sendUp(String nm,Pair p)
{
//System.out.println("SSSSSSSSSSSSSSSSS " +nm + " "+p);
//sCount++;
//System.out.println("" +name + "  number of send up is "+sCount);
de.setPair(nm,p);
whichMin = de.whichMin();
if (!root){
superior.sendUp(name,whichMin);
}
}

public void  sendUp_once(String nm,Pair p){
  de.setPair(nm,p);
  receivedImmi++;
//  System.out.println("" +name + "  number of numImminents and receivedImmi is "+numImminents+"__"+receivedImmi);
  if(receivedImmi==numImminents){
    receivedImmi=0; //reset the value for the next cycle
    numImminents=0; //reset the value for the next cycle
    whichMin = de.whichMin();
    if (!root) superior.sendUp_once(name,whichMin);
  }
}

public void  atomicPullSendUp(String ms,Pair p)
{
//System.out.println("SSSSSSSSSSSSSSSSS " +nm + " "+p);
de.setPair(ms,p);
whichMin = null;//////de.whichMin();
if (!root){
//superior.pullSendUp(this,whichMin);  //Xiaolin Hu
superior.pullSendUp(this,new Pair(null,new doubleEnt(-1)));
}
}

public void  pullSendUp(minSel ms,Pair p)
{
//System.out.println("SSSSSSSSSSSSSSSSS " +nm + " "+p);
de.setPair(ms,p);
whichMin = null;//////de.whichMin();
if (!root){
//superior.pullSendUp(this,whichMin);  //Xiaolin Hu
superior.pullSendUp(this,new Pair(null,new doubleEnt(-1)));
}
}

public void  informChange(){
if(numImminents==0&&!root) superior.informChange();  //the first imminent, thus need to pass the message upward
numImminents++;
}

public static minSel createChildren(Function f,String nm,int level,int base){
//System.out.println("minSelCreatTree---"+nm);
minSel m = null;
if (level == 0){
int i = Integer.parseInt(nm); //rid initial zeros
m = new minSel(""+i);
f.put(""+i,m);
}
else{ //if (level >0)
 ensembleSet s = new ensembleSet();
for (int i=0; i<base; i++)
  s.add(createChildren(f,nm+i,level-1,base));
 m= new minSel(nm,s);
 }
return m;
}

public static minSel createChildren(Function f,String nm,int level,int base, int leafBase){
//System.out.println("minSelCreatTree---"+nm);
minSel m = null;
if (level == 0){
int i = Integer.parseInt(nm); //rid initial zeros
m = new minSel(""+i);
f.put(""+i,m);
}
else if(level ==1){
 ensembleSet s = new ensembleSet();
 for (int i=0; i<leafBase; i++)
  s.add(createChildren(f,nm+i,level-1,base,leafBase));
 m= new minSel(nm,s);
}
else{ //if (level >1)
 ensembleSet s = new ensembleSet();
for (int i=0; i<base; i++)
  s.add(createChildren(f,nm+i,level-1,base,leafBase));
 m= new minSel(nm,s);
 }
return m;
}

public static minSel pullCreateChildren(Function f,String nm,int level,int base){
minSel m = null;
if (level == 0){
int i = Integer.parseInt(nm); //rid initial zeros
m = new minSel(""+i);
f.put(""+i,m);
}
else{ //if (level >0)
 ensembleSet s = new ensembleSet();
for (int i=0; i<base; i++)
  s.add(pullCreateChildren(f,nm+i,level-1,base));
 m= new minSel(nm,s,true);
 }
return m;
}

public static void cc(String nm,int level,int base){
System.out.println(nm);
if (level >0)
for (int i=0; i<base; i++)
    cc(nm+i,level-1,base);
}


public static void main(String args[]){

Function f = new Function();
//minSel r = createChildren(f,"",2,12);
minSel r = createChildren(f,"",2,3,5);
r.root = true;
//f.print();
/*
minSel l = (minSel)f.assoc("22");
System.out.println("NNNNNNNNNNN "+l.getName());
minSel par = l.superior;
System.out.println("NNNNNNNNNNN "+par.getName());
minSel rt = par.superior;
System.out.println(rt.root+" NNNNNNNNNNN "+rt.getName());

ensembleSet s = new ensembleSet();
s.add("22");

l.sendUp("22",new Pair(s,new doubleEnt(9)));

System.out.println(r.whichMin);


ensembleSet es = r.getImms();
System.out.println(es);

activeStack s = new activeStack();

ensembleSet a = new ensembleSet();
a.add("a");
ensembleSet b = new ensembleSet();
b.add("b");
ensembleSet c = new ensembleSet();
c.add("c");

s.pushPair("a",new Pair(a,new doubleEnt(0)));
s.pushPair("b",new Pair(b,new doubleEnt(1)));
s.pushPair("c",new Pair(c,new doubleEnt(-1)));
System.out.println(s.indexFromtop("a"));
//s.replacePair("a",new Pair(a,new doubleEnt(2)));
System.out.println(s.indexFromtop("a"));
System.out.println(s.indexFromtop("b"));
System.out.println(s.whichMin());
*/
}
}

//////////////////////////////////////////

//top of stack is likely to have min value

class activeStack extends Stack{

public activeStack(){
super();
}

public void pushPair(Object key,Object val){//add to top of stack
push(new Pair(key,val));
}


public synchronized int indexFromtop(Object key) {//search from top
    	if (elementCount == 0)
	    throw new NoSuchElementException();
	else if (key == null) return -1;
        else
        for (int i = 0 ; i<elementCount ; i++){
                Pair p = (Pair)elementData[elementCount - i-1];
		if (key.equals(p.getKey()))
		    return i;
                    }
	return -1;
    }


public void replacePair(Object key,Object val){
    int i = this.indexFromtop(key);
    if (i == 0){
     Pair p = (Pair)elementData[elementCount -1];
     p.value = val;
     }
     else{
      removeElementAt(elementCount -1-i);
      pushPair(key,val);
    }
}

double timeGranule = .000001;
int howFar = 1;

public Pair whichMin(){//imminents and tN
ensembleSet which = new ensembleSet();
double min = Double.POSITIVE_INFINITY;
for (int i = 0 ; i<Math.min(howFar,elementCount) ; i++){//going from top down
   Pair p = (Pair)elementData[elementCount - i-1];
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

}



