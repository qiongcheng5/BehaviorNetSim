package BehaviorNet;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;
import java.math.*;
import java.io.*;

public class Environment extends ViewableAtomic{//atomic{

Display rgui;
double timeStep = configData.timeStep;
int i;

// for the space
int WallX=440;
int WallY=330;
int MON =2;//10,
int SON=2;//10;
// for crayfish and predator
int CRAYFISH=0, PREDATOR=1;
MovingObject_Struct MovingObj[] = new MovingObject_Struct[10];
//for Food and shelter
int FOOD=0, SHELTER=1;
StaticObject_Struct StaticObj[] = new StaticObject_Struct[10];

//////////////////////////////////////////////////////////////
// data structs to be used by the environment model

// MovingObj_Struct
class MovingObject_Struct{
  boolean alive = false;
  double startingTime = 0;
  double Width, Length; // we only consider rectange-shape MovingObj so far
  double X, Y, Angle;  // the position and direction of the object
  MovingPara_Struct MovingPara;
  public MovingObject_Struct(boolean alv, double st, double w, double l, double x, double y, double a, MovingPara_Struct mvp){
    alive= alv; startingTime = st; Width =w; Length=l; X=x; Y=y; Angle=a; MovingPara=mvp;
  }
}

class MovingPara_Struct{
  int movingType;  //1--forward, 2--backward, 3--rotatecw, 4--rotateccw, 5--accel Motion
  double speed=0; //  m/s for forward and backward moving; Radian/s for rotation
  double maxSpeed = DevsInterface.INFINITY;
  double noiseFactor=0;
  public MovingPara_Struct(int mt, double s, double ms){
    movingType=mt; speed = s; maxSpeed=ms;
  }
}

//StaticObject_Struct  ---- more environment entities such as roads, moving entities, etc can be defined later
class StaticObject_Struct{
double Width, Length; // we only consider rectange-shape obstacle
double X, Y, Angle;  // the position and direction of the obstacle
double value = 0;  // may need to define a changeRate later
public StaticObject_Struct(double w, double l, double x, double y, double a){
  Width=w; Length=l; X=x; Y=y; Angle=a;
}
public StaticObject_Struct(double w, double l, double x, double y, double a, double v){
  Width=w; Length=l; X=x; Y=y; Angle=a; value = v;
}
}
//----------------------------------------------------------------------------------


public Environment(String nm){
this(nm,false);
}

public Environment(String nm, boolean central){
super(nm);
}

public Environment(){
this("Environment");
}

public void initialize(){
  MovingObj[CRAYFISH] = new MovingObject_Struct(true,0,15,15,250,150,Math.PI/2,new MovingPara_Struct(0,0,100));
  MovingObj[PREDATOR] = new MovingObject_Struct(false,100,20,20,0,250,Math.PI*2/3,new MovingPara_Struct(0,0,100));
  StaticObj[FOOD] = new StaticObject_Struct(10,10,200,200,0,6);
  StaticObj[SHELTER] = new StaticObject_Struct(50,50,120,120,0);

  rgui = new Display(this, (int)WallX, (int)WallY);
  rgui.start();
  holdIn("active",timeStep);
}

public void deltint(){
  // envDynamic();  // for a dynamic environment
  if(phaseIs("active")) holdIn("active",timeStep);
}

public message out(){
  message m = new message();
  return m;
}



/**
 * update all the moving objects' new position and angle
 * There are 3 issues needs to be work further:
 * 1. this fucntion only considers robot, not moving objects
 * 2. there is no mechanism to check if collision happens in the rotation movement
 * 3. when moving forward and backward, the function assumes the moving object's
 *    angle (direction) doesn't change -- because of variance, this may not be true in reality
 */
double msp, nf, mType;
public void updatePosAngle(){
  for(i=0;i<MON;i++){
    if(i==PREDATOR && !MovingObj[i].alive){
      if (getSimulationTime() >MovingObj[i].startingTime) MovingObj[i].alive =true;
    }
    if(MovingObj[i].alive){
        mType= MovingObj[i].MovingPara.movingType;
        msp = MovingObj[i].MovingPara.speed;
        nf = 0;//noise factor     =(ran.nextDouble()*2-1)*MovingObj[i].MovingPara.noiseFactor;
        switch(MovingObj[i].MovingPara.movingType){
          case 1:  // moving forward or backward
              MovingObj[i].X = MovingObj[i].X + msp*timeStep*Math.cos(MovingObj[i].Angle)*(1-nf);
              MovingObj[i].Y = MovingObj[i].Y + msp*timeStep*Math.sin(MovingObj[i].Angle)*(1-nf);
//            else System.out.println("MovingObj"+i+" collide!! when moving forward");
            break;
          case 2: // moving backward
              MovingObj[i].X = MovingObj[i].X - msp*timeStep*Math.cos(MovingObj[i].Angle)*(1-nf);
              MovingObj[i].Y = MovingObj[i].Y - msp*timeStep*Math.sin(MovingObj[i].Angle)*(1-nf);
//            else System.out.println("MovingObj"+i+" collide!! when moving backward");
            break;
          case 3: // rotate clockwise
            MovingObj[i].Angle = MovingObj[i].Angle - msp*timeStep*(1-nf);
            break;
          case 4: // rotate counter clockwise
            MovingObj[i].Angle = MovingObj[i].Angle + msp*timeStep*(1-nf);
            break;
        }
    }

//    System.out.println("x y angle is:"+MovingObj[i].X+"__"+MovingObj[i].Y+"__"+MovingObj[i].Angle);
  }
//  System.out.println("distance :"+getPredDist());
  if(getPredDist()<17) MovingObj[CRAYFISH].alive=false;  // crayfish is eaten
}

public void updatePosAngle(int objID){
    i = objID;
    if(i==PREDATOR && !MovingObj[i].alive){
      if (getSimulationTime() >MovingObj[i].startingTime) MovingObj[i].alive =true;
    }
    if(MovingObj[i].alive){
        mType= MovingObj[i].MovingPara.movingType;
        msp = MovingObj[i].MovingPara.speed;
        nf = 0;//noise factor     =(ran.nextDouble()*2-1)*MovingObj[i].MovingPara.noiseFactor;
        switch(MovingObj[i].MovingPara.movingType){
          case 1:  // moving forward or backward
              MovingObj[i].X = MovingObj[i].X + msp*timeStep*Math.cos(MovingObj[i].Angle)*(1-nf);
              MovingObj[i].Y = MovingObj[i].Y + msp*timeStep*Math.sin(MovingObj[i].Angle)*(1-nf);
//            else System.out.println("MovingObj"+i+" collide!! when moving forward");
            break;
          case 2: // moving backward
              MovingObj[i].X = MovingObj[i].X - msp*timeStep*Math.cos(MovingObj[i].Angle)*(1-nf);
              MovingObj[i].Y = MovingObj[i].Y - msp*timeStep*Math.sin(MovingObj[i].Angle)*(1-nf);
//            else System.out.println("MovingObj"+i+" collide!! when moving backward");
            break;
          case 3: // rotate clockwise
            MovingObj[i].Angle = MovingObj[i].Angle - msp*timeStep*(1-nf);
            break;
          case 4: // rotate counter clockwise
            MovingObj[i].Angle = MovingObj[i].Angle + msp*timeStep*(1-nf);
            break;
        }
    }

//    System.out.println("x y angle is:"+MovingObj[i].X+"__"+MovingObj[i].Y+"__"+MovingObj[i].Angle);
//  System.out.println("distance :"+getPredDist());
  if(getPredDist()<17) MovingObj[CRAYFISH].alive=false;  // crayfish is eaten
}

//////////////////////////////////////////////////////////////////
/**
 * below are the interface functions for motor's moving
 */
public void moveCrayFish(double speed, double angle){
MovingObj[CRAYFISH].MovingPara.movingType=1;
MovingObj[CRAYFISH].MovingPara.speed=speed;
MovingObj[CRAYFISH].Angle=angle;
updatePosAngle(CRAYFISH);
}

public void movePredator(double speed, double angle){
  if (MovingObj[PREDATOR].alive) {
    MovingObj[PREDATOR].MovingPara.movingType = 1;
    MovingObj[PREDATOR].MovingPara.speed = speed;
    MovingObj[PREDATOR].Angle = angle;
  }
  updatePosAngle(PREDATOR);
}

public void StopMoving(int index){
MovingObj[index].MovingPara.speed=0;
  updatePosAngle(index);
}

public double getCrayfishAngle(){
  return MovingObj[CRAYFISH].Angle;
}

public double getPredDist(){
if(MovingObj[PREDATOR].alive){
    return Math.sqrt((MovingObj[PREDATOR].X-MovingObj[CRAYFISH].X)*(MovingObj[PREDATOR].X-MovingObj[CRAYFISH].X)+
                     (MovingObj[PREDATOR].Y-MovingObj[CRAYFISH].Y)*(MovingObj[PREDATOR].Y-MovingObj[CRAYFISH].Y));
  }
return Double.POSITIVE_INFINITY;
}

public Vect getPredRelPos(){
if(MovingObj[PREDATOR].alive){
    return new Vect((MovingObj[PREDATOR].X-MovingObj[CRAYFISH].X),(MovingObj[PREDATOR].Y-MovingObj[CRAYFISH].Y));
  }
else return null;
}

public Vect predator_getCrayFRelPos(){
  if(MovingObj[CRAYFISH].alive && ! CrayfishIsHidden()){
    return new Vect((MovingObj[CRAYFISH].X-MovingObj[PREDATOR].X),(MovingObj[CRAYFISH].Y-MovingObj[PREDATOR].Y));
  }
  else return new Vect(Double.MAX_VALUE,Double.MAX_VALUE);
}

public double getFoodDist(){
    return Math.sqrt((StaticObj[FOOD].X-MovingObj[CRAYFISH].X)*(StaticObj[FOOD].X-MovingObj[CRAYFISH].X)+
                     (StaticObj[FOOD].Y-MovingObj[CRAYFISH].Y)*(StaticObj[FOOD].Y-MovingObj[CRAYFISH].Y));
}

  public Vect getFoodRelPos(){
    return new Vect((StaticObj[FOOD].X-MovingObj[CRAYFISH].X),(StaticObj[FOOD].Y-MovingObj[CRAYFISH].Y));
  }

public double getShelterDist(){
    return Math.sqrt((StaticObj[SHELTER].X-MovingObj[CRAYFISH].X)*(StaticObj[SHELTER].X-MovingObj[CRAYFISH].X)+
                     (StaticObj[SHELTER].Y-MovingObj[CRAYFISH].Y)*(StaticObj[SHELTER].Y-MovingObj[CRAYFISH].Y));
}

  public Vect getShelterRelPos(){
    return new Vect((StaticObj[SHELTER].X-MovingObj[CRAYFISH].X),(StaticObj[SHELTER].Y-MovingObj[CRAYFISH].Y));
  }

public double getFoodAmount(){
    return StaticObj[FOOD].value;
  }

public boolean CrayfishIsHidden(){
    double LhalfSize,WhalfSize;
    double fa,fb,fc,ba,bb,bc,la,lb,lc,ra,rb,rc;  // (front, back, left, right) edges, each edge is described by a*x+b*y+c=0
    double carX, carY, carA;
    double  X, Y;

    X = MovingObj[CRAYFISH].X;  // the x coordination of crayfish
    Y = MovingObj[CRAYFISH].Y;  // the y coordination of crayfish

   // check shelters  -- only consider one shelter at this point
      carA = StaticObj[SHELTER].Angle; carX = StaticObj[SHELTER].X; carY = StaticObj[SHELTER].Y;
      LhalfSize = StaticObj[SHELTER].Length/2; WhalfSize = StaticObj[SHELTER].Width/2;
      //get the parameters describing the line of the robot's front edge
      fa = Math.cos(carA);
      fb = Math.sin(carA);
      fc = -1*LhalfSize-carX*Math.cos(carA)-carY*Math.sin(carA);
      //get the parameters describing the line of the robot's back edge
      ba = Math.cos(carA);
      bb = Math.sin(carA);
      bc = LhalfSize-carX*Math.cos(carA)-carY*Math.sin(carA);
      //get the parameters describing the line of the robot's left edge
      la = Math.sin(carA);
      lb = -1* Math.cos(carA);
      lc = WhalfSize-carX*Math.sin(carA)+carY*Math.cos(carA);
      //get the parameters describing the line of the robot's right edge
      ra = Math.sin(carA);
      rb = -1* Math.cos(carA);
      rc = -1* WhalfSize-carX*Math.sin(carA)+carY*Math.cos(carA);
      // check if the point (X, Y) falls into the rectangle shape of the shelter
      if( (fa*X+fb*Y+fc)*(ba*X+bb*Y+bc)<0 && (la*X+lb*Y+lc)*(ra*X+rb*Y+rc)<0 ) return true;
      else return false;
  }

public void FoodEatenUpdate(double amount){
    StaticObj[FOOD].value = StaticObj[FOOD].value - amount;
  }

public void deActivateCrayfish(){
    MovingObj[CRAYFISH].alive=false;
  }

public boolean predator_ObstacleAhead(){
    double CollisionRange =5;  // an arbitrary number
    if (getIRSensorData(PREDATOR,1,0)<CollisionRange) return true;
    else return false;
  }


//-------------------------------------------------------
//////////////////////////////////////////////////////////////
/**
 * interface functions for the IR sensor to get distance data
 * parameters (dx, dy) are used to indicate the position of the IR sensor on the robot
 * (1,0)--Front; (1,1)--FrontLeft; (0,1)--Left; (-1,1)--BackLeft;
 * (-1,0)--Back; (-1,-1)--BackRight; (0,-1)--Right; (1,-1)--FrontRight
 */
double Sx, Sy, Sa, Sdistance;
double tempR, tempA;
public double getIRSensorData(int robotIndex, int dx, int dy){
  tempR = Math.sqrt(dx*dx*MovingObj[robotIndex].Length*MovingObj[robotIndex].Length/4 + dy*dy*MovingObj[robotIndex].Width*MovingObj[robotIndex].Width/4);
  tempA = Math.atan2(dy*MovingObj[robotIndex].Width,dx*MovingObj[robotIndex].Length);
  Sa = MovingObj[robotIndex].Angle + tempA;  // the angle (direction) of this IR sensor
  Sx = MovingObj[robotIndex].X + tempR*Math.cos(Sa);  // the x coordination of this IR sensor
  Sy = MovingObj[robotIndex].Y + tempR*Math.sin(Sa);  // the y coordination of this IR sensor
  //  System.out.println("sa, sx, sy = "+Sa*180/Math.PI+"  "+Sx+"   "+Sy);

  Sdistance = checkDistance(robotIndex,Sx,Sy,Sa);
  return Sdistance;
}


  public double checkDistance(int index, double Fx, double Fy, double Fa){
     double Fdistance = DevsInterface.INFINITY, temp, LhalfSize,WhalfSize;
     double a1, b1, c1, a2, b2, c2;  // each line is described by a*x+b*y+c=0
     double XBs, XBl, YBs, YBl; //boundry of a segment of a line
     double carX, carY, carA;

     a1 = Math.sin(Fa);
     b1 = -1*Math.cos(Fa);
     c1 = Fy*Math.cos(Fa)-Fx*Math.sin(Fa);

     //check the distance between the sensor and the Left wall
     temp = calcuDist(a1,b1,c1,1,0,0,0,0,0,WallY,Fx,Fy,Fa);
     if(temp<Fdistance) Fdistance = temp;
     //check the distance between the sensor and Top wall
     temp = calcuDist(a1,b1,c1,0,1,-1*WallY,0,WallX,WallY,WallY,Fx,Fy,Fa);
     if(temp<Fdistance) Fdistance = temp;
     //check the distance between the sensor and Right wall
     temp = calcuDist(a1,b1,c1,1,0,-1*WallX,WallX,WallX,0,WallY,Fx,Fy,Fa);
     if(temp<Fdistance) Fdistance = temp;
     //check the distance between the sensor and Bottom wall
     temp = calcuDist(a1,b1,c1,0,1,0,0,WallX,0,0,Fx,Fy,Fa);
     if(temp<Fdistance) Fdistance = temp;

    // check distance to Shelter
//    for(int i=0;i<ON;i++){
       carA = StaticObj[SHELTER].Angle; carX = StaticObj[SHELTER].X; carY = StaticObj[SHELTER].Y;
       LhalfSize = StaticObj[SHELTER].Length/2; WhalfSize = StaticObj[SHELTER].Width/2;
       //check the distance between the sensor and Obstacle's front edge
       a2 = Math.cos(carA);
       b2 = Math.sin(carA);
       c2 = -1*LhalfSize-carX*Math.cos(carA)-carY*Math.sin(carA);
       XBs = carX + LhalfSize*Math.cos(carA) - WhalfSize* Math.abs(Math.sin(carA));
       XBl = carX + LhalfSize*Math.cos(carA) + WhalfSize* Math.abs(Math.sin(carA));
       YBs = carY + LhalfSize*Math.sin(carA) - WhalfSize* Math.abs(Math.cos(carA));
       YBl = carY + LhalfSize*Math.sin(carA) + WhalfSize* Math.abs(Math.cos(carA));
       temp = calcuDist(a1,b1,c1,a2,b2,c2,XBs,XBl,YBs,YBl,Fx,Fy,Fa);
       if(temp<Fdistance) Fdistance = temp;
       //check the distance between the sensor and Obstacle's back edge
       a2 = Math.cos(carA);
       b2 = Math.sin(carA);
       c2 = LhalfSize-carX*Math.cos(carA)-carY*Math.sin(carA);
       XBs = carX - LhalfSize*Math.cos(carA) - WhalfSize* Math.abs(Math.sin(carA));
       XBl = carX - LhalfSize*Math.cos(carA) + WhalfSize* Math.abs(Math.sin(carA));
       YBs = carY - LhalfSize*Math.sin(carA) - WhalfSize* Math.abs(Math.cos(carA));
       YBl = carY - LhalfSize*Math.sin(carA) + WhalfSize* Math.abs(Math.cos(carA));
       temp = calcuDist(a1,b1,c1,a2,b2,c2,XBs,XBl,YBs,YBl,Fx,Fy,Fa);
       if(temp<Fdistance) Fdistance = temp;
       //check the distance between the sensor and Obstacle's right edge
       a2 = Math.sin(carA);
       b2 = -1* Math.cos(carA);
       c2 = -1* WhalfSize-carX*Math.sin(carA)+carY*Math.cos(carA);
       XBs = carX + WhalfSize*Math.sin(carA) - LhalfSize* Math.abs(Math.cos(carA));
       XBl = carX + WhalfSize*Math.sin(carA) + LhalfSize* Math.abs(Math.cos(carA));
       YBs = carY - WhalfSize*Math.cos(carA) - LhalfSize* Math.abs(Math.sin(carA));
       YBl = carY - WhalfSize*Math.cos(carA) + LhalfSize* Math.abs(Math.sin(carA));
       temp = calcuDist(a1,b1,c1,a2,b2,c2,XBs,XBl,YBs,YBl,Fx,Fy,Fa);
       if(temp<Fdistance) Fdistance = temp;
       //check the distance between the sensor and the Obstacle's left edge
       a2 = Math.sin(carA);
       b2 = -1* Math.cos(carA);
       c2 = WhalfSize-carX*Math.sin(carA)+carY*Math.cos(carA);
       XBs = carX - WhalfSize*Math.sin(carA) - LhalfSize* Math.abs(Math.cos(carA));
       XBl = carX - WhalfSize*Math.sin(carA) + LhalfSize* Math.abs(Math.cos(carA));
       YBs = carY + WhalfSize*Math.cos(carA) - LhalfSize* Math.abs(Math.sin(carA));
       YBl = carY + WhalfSize*Math.cos(carA) + LhalfSize* Math.abs(Math.sin(carA));
       temp = calcuDist(a1,b1,c1,a2,b2,c2,XBs,XBl,YBs,YBl,Fx,Fy,Fa);
       if(temp<Fdistance) Fdistance = temp;
//   }

   return Fdistance;
 }

 public double calcuDist(double a1, double b1, double c1, double a2, double b2, double c2,
                           double XBs, double XBl, double YBs, double YBl, double Sx, double Sy, double Sa){
  double intectX, intectY, D;
  if((a1*b2-a2*b1) == 0) return DevsInterface.INFINITY;    // two lines are parral
  else{
     intectX = (b1*c2-c1*b2)/(a1*b2-a2*b1);
     intectY = (c1*a2-a1*c2)/(a1*b2-a2*b1);
     D = Math.sqrt((intectX-Sx)*(intectX-Sx)+(intectY-Sy)*(intectY-Sy));
     // check the intersection point is at "front" or "back"
     if((intectX-Sx)*Math.cos(Sa)<0 || (intectY-Sy)* Math.sin(Sa)<0)
       return DevsInterface.INFINITY;
     else{ // check if the intersection point is inside the boundary
         if((int)(intectX+0.5)<(int)XBs||(int)(intectX+0.5)>(int)XBl||(int)(intectY+0.5)<(int)YBs||(int)(intectY+0.5)>(int)YBl)
           return DevsInterface.INFINITY;
         else return D;
     }
  }
 }


}
