package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class EnvInterface extends abstractActivity{

Environment env;

public EnvInterface(String nm){
super(nm);
}

public EnvInterface(){
this("EnvActivity");
}

public void initialize(){
env = (Environment)getModelWithName("Environment");
passivate();
}

public double getPredDist(){
return env.getPredDist();
}

public double getFoodDist(){
  return env.getFoodDist();
}

public double getShelterDist(){
    return env.getShelterDist();
}

public double getFoodAmount(){
    return env.getFoodAmount();
  }

public boolean CrayfishIsHidden(){
    return env.CrayfishIsHidden();
  }

public Vect getPredRelPos(){
    return env.getPredRelPos();
  }

public Vect getShelterRelPos(){
      return env.getShelterRelPos();
    }

public Vect getFoodRelPos(){
   return env.getFoodRelPos();
}

double getCrayfishAngle(){
    return env.getCrayfishAngle();
  }

public void moveCrayFish(double sp, double ang){
    env.moveCrayFish(sp,ang);
  }

public void FoodEatenUpdate(double amount){
 env.FoodEatenUpdate(amount);
}
public void deActivateCrayfish(){
 env.deActivateCrayfish();
  }

public Vect predator_getCrayFRelPos(){
    return env.predator_getCrayFRelPos();
  }

public boolean predator_ObstacleAhead(){
    return env.predator_ObstacleAhead();
  }

public void movePredator(double speed, double angle){
    env.movePredator(speed,angle);
  }
}

