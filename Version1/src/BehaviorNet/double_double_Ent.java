package BehaviorNet;


import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class double_double_Ent  extends entity {
public double x;
public double y;

public double_double_Ent(double x, double y){
super("double_double_Ent");
this.x = x;
this.y = y;
}

public double getX(){ return this.x;}
public double getY() { return this.y;}

}