/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;

import org.jfree.data.time.Millisecond;

/**
 * @author weijp
 *
 * BaseXYValuePair's subclass, just for realtime and value pair
 */
public class TimeCurveValuePair extends BaseXYValuePair {
	public TimeCurveValuePair( Millisecond mSecond, double value ){
		super( (Object)mSecond, new Double( value ) );
	}
	
	public Millisecond getXValue(){
		return (Millisecond)super.xValue ;
	}
	public void setXValue( Millisecond mSecond ){
		super.setXValue( mSecond );
	}
	
	public double getYValue(){
		return ((Double)(super.yValue) ).doubleValue(); 
	}
	
	public void setYValue( double value ){
		super.setYValue( new Double( value ) );
	}
}
