package BehaviorNetwork;


import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class int_double_Ent  extends entity {
public int x;
public double y;

public int_double_Ent(int x, double y){
super("int_double_Ent");
this.x = x;
this.y = y;
}

public int getX(){ return this.x;}
public double getY() { return this.y;}

}