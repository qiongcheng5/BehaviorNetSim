/*      Copyright 1999 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA2.6
 *  Date       : 04-15-00
 */

package GenCol;

import java.lang.*;

public class intEnt extends entity {

	int v;

public intEnt(int t)
{
  v = t;

}

public boolean greaterThan(entity ent){
    return (this.v > ((intEnt)  ent).getv());
}

public void setv(int t){v = t;}

public int getv(){return v;}

public void print(){System.out.print( v);}

public boolean equal(entity ent){
//System.out.println(v + " " + ((intEnt)ent).getv());
    return this.v ==((intEnt)  ent).getv();
}

public boolean equals(Object ent){ //needed for Relation
    return  equal((entity)ent);
}

public entity  copy(){
     intEnt  ip = new intEnt(getv());
     return (entity)ip;
}

public String getName(){
     return Integer.toString(v);
}

public int hashCode(){  //bpz needed to make work with Collections
return v;
}
}
