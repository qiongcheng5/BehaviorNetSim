package Mutual_Inhibition;


import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class vect2DEnt  extends entity {
public double x,y;

public static vect2DEnt ZERO = new vect2DEnt(0,0);

public vect2DEnt(double x, double y){
super("vect2DEnt");
this.x = x;
this.y = y;
}

public double getX(){ return this.x;}
public double getY() { return this.y;}

public vect2DEnt add(double x, double y){
return new vect2DEnt(this.x+x,this.y+y);
}

public vect2DEnt add(vect2DEnt v){
return add(v.x,v.y);
}

public void addSelf(vect2DEnt v){
x += v.x;
y += v.y;
}

public vect2DEnt subtract(vect2DEnt v){
return add(-v.x,-v.y);
}

public double norm(){
return Math.sqrt(x*x + y*y);
}

public vect2DEnt normalize(){
double norm = norm();
if (norm == 0)
return ZERO;
else return new vect2DEnt(x/norm,y/norm);
}

public vect2DEnt perpendicular(){
return new vect2DEnt(-y,x);
}

public vect2DEnt scalarMult(double d){
return new vect2DEnt(this.x*d,this.y*d);
}

public String toString(){
return doubleFormat.niceDouble( x )+
 ","+doubleFormat.niceDouble(y);
}


public static vect2DEnt toObject(String nm){
int commaIndex = nm.indexOf(",");
String xs = nm.substring(0,commaIndex);
String ys = nm.substring(commaIndex+1,nm.length());
return new vect2DEnt(Double.parseDouble(xs),Double.parseDouble(ys));
}
public static vect2DEnt toObject(entity ent){
return toObject(ent.getName());
}

public String getName(){
return toString();
}

public boolean equals(Object o){    //overrides pointer equality of Object
if  (o instanceof vect2DEnt)
{
vect2DEnt ov = (vect2DEnt)o;
return ov.x == this.x && ov.y == this.y;
}
else return false;
}


public vect2DEnt maxLimit(double max){
if (norm() <= max)
return this;
else{
vect2DEnt na = normalize();
return na.scalarMult(max);
}
}

public static void main(String[] args){
vect2DEnt v = new vect2DEnt(Math.PI,-666.66666);
System.out.println(toObject(v.getName()).getName());
System.out.println(toObject(ZERO.getName()).getName());
double z = Double.POSITIVE_INFINITY;
System.out.println(Double.toString(z));
//System.out.println(Double.parseDouble("Infinity"));

/*
correct to allow using INFINITY also E notation
System.out.println(toObject(new vect2DEnt(Math.PI,Double.POSITIVE_INFINITY)//Double.NEGATIVE_INFINITY)
            .getName()).getName());
            */
}
}