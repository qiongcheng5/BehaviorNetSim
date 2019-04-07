package BehaviorNetwork;

import java.lang.Math;

public class Vect
	implements Cloneable
{
	// Constants
	public static final Vect ZERO = new Vect(0, 0);
	public static final Vect UNIT_X = new Vect(1, 0);
	public static final Vect UNIT_Y = new Vect(0, 1);
	public static final Vect RANDOM = new Vect(-1, -1);

	// Data
	public double x;
	public double y;

	public Vect()
	{
	}

	public Vect(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double dot(Vect v)
	{
		return (this.x * v.x + this.y * v.y);
	}

	public static Vect add(Vect u, Vect v)
	{
		return new Vect(u.x + v.x, u.y + v.y);
	}

	public Vect add(Vect v)
	{
		return new Vect(this.x + v.x, this.y + v.y);
	}

	public static Vect polar(double r, double angle)
	{
		Vect v = new Vect();
		v.x = r * Math.cos(angle);
		v.y = r * -Math.sin(angle);
		return v;
	}

	public void incr(Vect v)
	{
		this.x += v.x;
		this.y += v.y;
	}

	public void incr(Vect v, Vect max)
	{
		double fx;
		double fy;
		double mdx = max.x - this.x;
		if (mdx != 0)
			fx = Math.min(v.x/mdx, 1.0);
		else
			fx = 0.0;
		double mdy = max.y - this.y;
		if (mdy != 0)
			fy = Math.min(v.y/mdy, 1.0);
		else
			fy = 0.0;
		this.x += fx * mdx;
		this.y += fy * mdy;
	}

	public Vect addm(Vect v, Vect max)
	{
		Vect n = (Vect)clone();
		n.incr(v, max);
		return n;
	}

	public Vect diff(Vect v)
	{
		return new Vect(this.x - v.x, this.y - v.y);
	}

	public double dist(Vect v)
	{
		double x = this.x - v.x;
		double y = this.y - v.y;
		return Math.sqrt(x*x + y*y);
	}

    /**
     *  Returns the angle (in radians) of the vector between two
     *  points.
     */
    public double angle(Vect pt)
    {
        double angle = 0.0;
        double dx = pt.x - this.x;
//        double dy = -(pt.y - this.y);       // lousy top-left origin crap
        double dy = (pt.y - this.y);       // lousy top-left origin crap
        try {
            angle = Math.atan2(dy, dx);
        } catch (ArithmeticException e) { angle = 0.0; }
        return angle;
    }


	public Vect min(Vect v)
	{
		return new Vect(Math.min(x, v.x), Math.min(y, v.y));
	}

	public double mag()
	{
		return Math.sqrt(x*x + y*y);
	}

	public double angle()
	{
//		return Math.atan2(-y, x);
                return Math.atan2(y, x);
	}

	public Vect unit()
	{
		double mag = this.mag();
		if (mag == 0.0)
			return ZERO;
		else
			return new Vect(x/mag, y/mag);
	}

	public Vect orthog()
	{
		return new Vect(-y, x);
	}

	/**
     * Project this vector onto vector v
     */
	public double proj(Vect v)
	{
		return (this.x * v.x + this.y + this.y) / v.mag();
	}

	public Vect scalarMultiply(double a)
	{
		return new Vect(a*x, a*y);
	}

	public java.awt.Point toPoint()
	{
		return new java.awt.Point((int)x, (int)y);
	}

	public Object clone()
	{
		return new Vect(this.x, this.y);
	}

	public String toString()
	{
		return "(x=" + x + ", y=" + y + ")";
	}

	public boolean equals(Object o)
	{
		Vect v;
		return (v = (Vect)o) != null &&
			    v.x == x &&
			    v.y == y;
	}
}
