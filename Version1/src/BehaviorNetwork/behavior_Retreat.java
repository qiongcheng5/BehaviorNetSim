package BehaviorNetwork;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import simView.*;
import java.awt.*;

public class behavior_Retreat extends Behavior{
double dpred, dshelter;
Vect shelterdir,preddir;

public behavior_Retreat(){
    this("RETREAT");
  }

public behavior_Retreat(String nm){
    super(nm);
    myID = configData.RETREAT;
  }

public void initialize(){
super.initialize();
  }

public void calcuExcitation(){
    dpred = envI.getPredDist();
    dshelter = envI.getShelterDist();
    myExcitation = configData.KEX[myID] * Math.exp(-dpred / configData.KLENGTH_RET) + 3
                                            + 5 * Math.exp(-dshelter / 200)
                                            - 3 * Math.exp(-dshelter / 50);
  }

public void doAction(){
    double preDirection = envI.getCrayfishAngle();
    preddir = envI.getPredRelPos();
    shelterdir = envI.getShelterRelPos();

    Vect v_sum = calcRetreatDir2(shelterdir, preddir, null, Vect.ZERO,1, configData.KLENGTH_RET);
    double vmag = v_sum.mag();
    double vel = configData.MOVE[myID];
    double direction = v_sum.angle();
    Vect delta = new Vect(-vel * Math.cos(direction),
                                              vel * Math.sin(direction));
    Vect v_sum1 = calcRetreatDir2(shelterdir, preddir, null, delta, 1, configData.KLENGTH_RET);
    if ( vmag < configData.RETREAT_SENSITIVITY || v_sum1.dot(v_sum) < 0 )
    {
                    vel = 0;
                    direction = preDirection;
    }
    envI.moveCrayFish(vel, direction);
    updateEnergy();
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

}