/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;

import org.jfree.data.time.Millisecond;

/**
 * @author Qiong Cheng
 *
 * BaseXYValuePair's subclass, just for simulation-time and value pair
 */
public class SimtimeCurveValuePair extends BaseXYValuePair {
	/**
	 * Constructor
	 * @param iTimeStep
	 * @param value
	 */
	public SimtimeCurveValuePair( double iTimeStep, double value ){
		super( new Double(iTimeStep), new Double( value ) );
	}
	
	/**
	 * Get the time-step value
	 * @return
	 */
	public double getXValue(){
		return ((Double)(super.xValue) ).doubleValue();
	}
	
	/**
	 * Set the time-step value
	 * @param iTimeStep
	 */
	public void setXValue( int iTimeStep ){
		super.setXValue( new Integer(iTimeStep) );
	}
	
	/**
	 * Get the y-axis value
	 * @return
	 */
	public double getYValue(){
		return ((Double)(super.yValue) ).doubleValue(); 
	}
	
	/**
	 * Set the y-axis value
	 * @param value
	 */
	public void setYValue( double value ){
		super.setYValue( new Double( value ) );
	}
}
