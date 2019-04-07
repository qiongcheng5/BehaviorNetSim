package Mutual_Inhibition;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class Behavior extends ViewableAtomic{//atomic{
int i,j;
EnvActivity envA;

public Behavior(String nm){
super(nm);
}

public Behavior(){
this("Behavior");
}

public void initialize(){
    iniBehavior();
    envA = new EnvActivity();
    startActivity(envA);
    holdIn("active",0);
}

public void deltext(double e,message x){
Continue(e);
for (i=0; i< x.getLength();i++){
  if (messageOnPort(x,"Energy",i)) {
    energy = ((doubleEnt)x.getValOnPort("Energy",i)).getv();
  }
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

//------------------------------------------------------------------------------

  // Constants
  public static final int NBEHAVIOURS = 10;
  public static final int NONE = 0;
  public static final int ESCAPE = 1;
  public static final int RETREAT = 2;
  public static final int DEFEND = 3;
  public static final int HIDE = 4;
  public static final int EAT = 5;
  public static final int FORAGE = 6;
  public static final int SWIM = 7;
  public static final int APPROACH = 8;
  public static final int ATTACK = 9;
  public static final int DEAD = -1;

  public static final String[] BEHAVIOUR_NAMES = {
          "None", "Escape", "Retreat", "Defend", "Hide",
          "Eat", "Forage", "Swim", "Approach", "Attack"
  };
  private static final double[] MOVE = { 0, 50, 2, 0, 0, 0, 2, 25, 2, 0 };

  // Default behaviour strengths, energy, movement rates etc.
  private static final double[] KEX = { 0, 45, 15, 8, 6, 500, 800, 1, 20, 20};
  private static final double[][] KIN = {
          {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0},
          {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.2, 1.0, 0, 0},
          {0.0, 1.0, 0.0, 0.5, 0.5, 0.5, 0.2, 0.5, 0.5, 0},
          {0.0, 1.0, 0.5, 0.0, 0.0, 0.5, 0.2, 0.5, 0.5, 0},
          {0.0, 1.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5},
          {0.0, 1.0, 0.5, 0.5, 0.0, 0.0, 0.0, 0.5, 0, 0},
          {0.0, 1.0, 0.5, 0.5, 0.5, 1.0, 0.0, 0.5, 0, 0},
          {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0, 0},
          {0.0, 1.0, 0.5, 0.5, 0.2, 0.5, 0.5, 0.0, 0, 1.0},
          {0.0, 1.0, 0.0, 0.0, 0.2, 0.5, 0.5, 0.0, 0, 0}
  };

  private static final double KLENGTH_DEF = 135;
  private static final double KLENGTH_RET = 45;
  private static final double KLENGTH_ESC = 15;
  private static final double KLENGTH_APP = 45;
  private static final double KLENGTH_ATT = 10;
  private static final double CRAYRETCOEFF = 0.25;
  private static final double DEF_ENERGY = 2;
  private static final double NIBBLE_AMT = 0.05;
  private static final double L_CRES = KLENGTH_RET;
  public static final double MINTHRESHHOLD = 4.0;
  protected static final double RETREAT_SENSITIVITY = 0.1;
  private static final double[] ECOST =
          { -0.002, -0.02, -0.004, -0.002, -0.002,
             NIBBLE_AMT, -0.004, -0.01, -0.004, -0.02 };

double[] ex = new double [NBEHAVIOURS], bs= new double [NBEHAVIOURS];
boolean inescape;
double ATTACK_DIST=10;
double CTescape,escapetime, confidence;
private int behaviour;
double vel, direction;
Vect targetvel,shelterdir,preddir,fooddir;
double energy;

protected void iniBehavior(){
    inescape = false;
    CTescape =0;
    confidence=1;
    energy = DEF_ENERGY;
  }

double dfood,dshelter,dpred,dcray;
protected void behaviorSelect(){
// Evaluate excitation of competing behaviours
// Common subexpressions
double hunger = 100 * Math.exp(-4 * energy);
preddir = envA.getPredRelPos();
 dpred = envA.getPredDist();
//if (world.isHidden(this))
//	dpred = Double.MAX_VALUE;
shelterdir = envA.getShelterRelPos();
 dshelter = envA.getShelterDist();
fooddir = envA.getFoodRelPos();
 dfood = envA.getFoodDist();
 dcray = Double.MAX_VALUE;  // only consider one crayfish at this point

// calculate resource coefficient
double cshel = 1 / (1 + Math.exp((dshelter-L_CRES)*10/L_CRES));
double cfood = 1 / (1 + Math.exp((dfood-L_CRES)*10/L_CRES));
double cres = Math.sqrt(cshel*cshel + cfood*cfood);

// Foraging
double odour;
odour = envA.getFoodAmount()/(dfood+1);
//System.err.println("odour=" + odour);
ex[FORAGE] = KEX[FORAGE] * odour * hunger / (hunger + 4);

// Eating
if (dfood <= 10)  ex[EAT] = KEX[EAT] * odour * hunger / (hunger + 4);
else ex[EAT] = 0;

// Defence
ex[DEFEND] = KEX[DEFEND] * Math.exp(-dpred / KLENGTH_DEF);
//ex[DEFEND] += KEX[DEFEND] * Math.exp(-dcray / KLENGTH_DEF);

// Retreat
ex[RETREAT] = KEX[RETREAT] * Math.exp(-dpred / KLENGTH_RET) + 3
                                        + 5 * Math.exp(-dshelter / 200)
                                        - 3 * Math.exp(-dshelter / 50);
//ex[RETREAT] += KEX[RETREAT] * crayretcoeff * (1/confidence) * Math.exp(-dcray / klength_ret);

// Escape
// hack to avoid escape when hidden in the shelter
double dpred1 = !envA.CrayfishIsHidden()? dpred : Double.MAX_VALUE;
ex[ESCAPE] = KEX[ESCAPE] * Math.exp(-dpred1 / KLENGTH_ESC);

// Swim
if (inescape)
        ex[SWIM] = KEX[SWIM] * CTescape
                             * Math.exp( -(getSimulationTime() - escapetime) / 3 );
else
        ex[SWIM] = 0.0;

// Hide
if (envA.CrayfishIsHidden())  ex[HIDE] = KEX[HIDE];
else  ex[HIDE] = 0;

// Approach
ex[APPROACH] = cres * KEX[APPROACH] * confidence * Math.exp(-dcray / KLENGTH_APP);

// Attack
if (dcray <= ATTACK_DIST)
        ex[ATTACK] = KEX[ATTACK]* Math.exp(-dcray / (confidence*KLENGTH_ATT));
else
        ex[ATTACK] = 0;

// Cap strength of excitation
for (i = 1; i < NBEHAVIOURS; i++)
        if (ex[i] > 20) ex[i] = 20;

for (i = 1; i < NBEHAVIOURS; i++)
        bs[i] = ex[i];
// Inhibition
for (i = 1; i < NBEHAVIOURS; i++)
        if (bs[i] >= 1.0)
                for (j = 1; j < NBEHAVIOURS; j++)
                {
                        double bs1 = bs[i];
                        //double bs2 = (time > 1) ? bs[i][time-2] : 0;
                        //double bs2 = ex[i][time];
//                        bs[j] -= kin[j][i] * (0.7 * bs1 + 0.3 * bs2);
                        bs[j] -= KIN[j][i] *  bs1;
                }

// Keep behaviours above zero
for (i = 1; i < NBEHAVIOURS; i++)
        if (bs[i] < 0.0)
                bs[i] = 0.0;

// Choose behaviour with greatest excitation
double minval = MINTHRESHHOLD;
this.behaviour = NONE;
for (i = 1; i < NBEHAVIOURS; i++)
{
        if (bs[i] >= minval)
        {
                this.behaviour = i;
                minval = bs[i];
        }
}

int idx = behaviour;
// Set target velocity magnitude, but give behaviours an opportunity
// to modify it
vel = MOVE[idx];

//System.out.println("crayfish's behavior ----  "+BEHAVIOUR_NAMES[idx]+ "     energy  : "+energy);
// Go off to appropriate behavioural subroutine
switch (idx)
{
case NONE: 		doNone(); break;
case ESCAPE:	doEscape(); break;
case RETREAT: 	doRetreat(); break;
case DEFEND:	doDefend(); break;
case HIDE:		doHide(); break;
case EAT:		doEat(); break;
case FORAGE:	doForage(); break;
case SWIM:		doSwim(); break;
case APPROACH:	doApproach(); break;
case ATTACK:	doAttack(); break;
default:
        // Error
        throw new RuntimeException("Crayfish set in undefined behaviour");
}

// Update position
envA.moveCrayFish(vel,direction);
//targetvel = new Vect(vel * Math.cos(direction),
//                                     vel * -Math.sin(direction));

// Calculate acceleration
//accel = targetvel.diff(getVelocity()).unit().scalarMultiply(max_accel);

// rebound confidence
//confidence += (1 - confidence) * confr;

    // Update energy
    energy = energy + ECOST[behaviour];
    if (energy <= 0)
    {
            System.out.println("crayfish starved at time:"+this.getSimulationTime());
            envA.deActivateCrayfish();
    }

}

  private void doNone(){}
  private void doEscape()
  {
          direction = envA.getCrayfishAngle();
          // Set direction to forwards or backwards, depending on predator pos
          Vect v = envA.getPredRelPos();
//          Vect dir = new Vect(Math.cos(direction),-Math.sin(direction));
          Vect dir = new Vect(Math.cos(direction),Math.sin(direction));
          //System.err.println("pred vect=" + v.x + "," + v.y);
          //System.err.println("dir=" + dir.x + "," + dir.y);
          if (v.dot(dir) > 0)
          {
                  direction = direction + Math.PI;
                  if (direction > 2 * Math.PI)  direction -= 2 * Math.PI;

          }
          this.inescape = true;
          this.escapetime = this.getSimulationTime();
  }

  private void doRetreat()
  {
          double preDirection = envA.getCrayfishAngle();
          Vect v_sum = calcRetreatDir2(shelterdir, preddir, null, Vect.ZERO,1, KLENGTH_RET);
          double vmag = v_sum.mag();
          direction = v_sum.angle();
          Vect delta = new Vect(-vel * Math.cos(direction),
                                                    vel * Math.sin(direction));
          Vect v_sum1 = calcRetreatDir2(shelterdir, preddir, null, delta, 1, KLENGTH_RET);
          if ( vmag < RETREAT_SENSITIVITY || v_sum1.dot(v_sum) < 0 )
          {
                          vel = 0;
                          direction = preDirection;
          }
  }

  private static final Vect calcRetreatDir2(Vect sdir, Vect pdir,
                                Vect cdir,
                                Vect delta,
                                double confidence,
                                double klength_ret)
  {
          final double A = 10;
          final double B = 1;
          final double E0 = 5;
          Vect v_sum = new Vect(0, 0);
          if (sdir != null)
          {
                  double sx = sdir.x + delta.x;
                  double sy = sdir.y + delta.y;
                  double smag = Math.sqrt(sx*sx + sy*sy);
                  v_sum.x += sx / smag;
                  v_sum.y += sy / smag;
          }
          if (pdir != null)
          {
                  double px = pdir.x + delta.x;
                  double py = pdir.y + delta.y;
                  double pmag = Math.sqrt(px*px + py*py);
                  double f = Math.exp(-pmag / (A * klength_ret));
                  v_sum.x += -f * px / pmag;
                  v_sum.y += -f * py / pmag;
          }
          if (cdir != null)
          {
                  double cx = cdir.x + delta.x;
                  double cy = cdir.y + delta.y;
                  double cmag = Math.sqrt(cx*cx + cy*cy);
                  double f = (B*klength_ret/(confidence*confidence));
                  double a = Math.exp(-(cmag + 10) / f);
                  v_sum.x += -a * cx / cmag;
                  v_sum.y += -a * cy / cmag;
          }
          return v_sum;
  }

  private void doDefend()
  {
          // Put up a defensive posture, hopefully it will scare the predator away.
          // crayfish should face predator or other crayfish
          Vect face = (Vect)Vect.ZERO.clone();
          if (preddir != null)
          {
                  double dpred = preddir.mag();
                  Vect vpred = preddir.unit().scalarMultiply(Math.exp(-dpred));
                  face.incr(vpred);
          }
          if (!face.equals(Vect.ZERO))
                  direction = face.angle();
          else
                  direction = direction;  // maintain previous direction
  }

  private void doHide()
  {
          direction = direction; // maintain previous direction
  }

  private void doEat()
  {
          direction = direction; // maintain previous direction
//          envA.FoodEatenUpdate(NIBBLE_AMT);
          envA.FoodEatenUpdate(NIBBLE_AMT/5);
  }
  private void doForage()
  {
          // Move in the direction of the food source
          //direction[time] = Vect.ZERO.angle(getFoodPos(this, world));
          direction = fooddir.angle();
          // no point overshooting the food
          if ( dfood < vel/2 )
                  vel = 0;
  }
  private void doSwim()
  {
    direction = direction; // maintain previous direction
  }
  private void doApproach()
  {
//          if (craydir != null)
//                  direction[time] = craydir.angle();
 //         else
                  direction = direction; // maintain previous direction
  }

  private void doAttack()
  {
          direction = direction; // maintain previous direction
//          Integer otherid = world.findClosest(this, "crayfish", Double.MAX_VALUE);
//          world.takeAction(this, otherid, CrayAttack.class, null);
  }


}
