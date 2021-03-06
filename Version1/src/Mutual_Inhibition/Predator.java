package Mutual_Inhibition;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;
import java.math.*;

public class Predator extends ViewableAtomic{//atomic{
int i,j;
EnvActivity envA;

public Predator(String nm){
super(nm);
}

public Predator(){
this("Predator");
}

public void initialize(){
    iniBehavior();
    envA = new EnvActivity("predator_EnvA");
    startActivity(envA);
    holdIn("active",0);
}

public void deltext(double e,message x){
Continue(e);
for (i=0; i< x.getLength();i++){
}
behaviorSelect();
holdIn("act",0);
}

public void deltint(){
behaviorSelect();
holdIn("active",1);
}

public message out(){
message m = new message();
return m;
}

double VisibilityRange = 100.0;
double wanderingSp = 2.0;
double chasingSp = 4.0;
double direction, vel;

protected void iniBehavior(){
    direction=0;
    vel = wanderingSp;
  }

protected void behaviorSelect(){
 Vect v = envA.predator_getCrayFRelPos();
 if(v.mag()<VisibilityRange){ //starting chasing
   direction = v.angle();
   vel = chasingSp;
   envA.movePredator(vel,direction); // adjust the angle and speed and then check if there is obstacles ahead
   if(envA.predator_ObstacleAhead()) //changing direction
     direction = direction + Math.PI/2;
 }
 else{  //keeping wandering
   if(envA.predator_ObstacleAhead()){ //changing direction
     direction = direction+ Math.PI*2/3;
   }
   else direction = direction; // maintain current angle
   vel = wanderingSp;
 }

 envA.movePredator(vel,direction);
}

}
