/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.handlers.dataobj.impl;

import ai.sim.analysis.handlers.dataobj.AbstractHandledDataObj;

/**
 * @author Qiong Cheng
 *
 * Just for simulation time and excitation data pair
 */
public class SimtimeExcitHandledDataObj extends AbstractHandledDataObj {
	/**
	 * Constructor method
	 * @param id
	 * @param timestamp
	 * @param value
	 */
	public SimtimeExcitHandledDataObj( int id, int timestamp, double value ){
		super( id, new Integer(timestamp), new Double(value) );
	}

	/**
	 * Get the value of excitation
	 * @return
	 */
	public double getValue(){
		Object obj = super.getOutValue();

		if ( obj != null ){
			return ((Double)obj).doubleValue();
		}else
			return -1;
	}

	/**
	 * Set the value of excitation
	 * @param value
	 */
	public void setValue( double value ){
		super.setOutValue( new Double( value ) );
	}

	/**
	 * Get the value of simulation-time stamp
	 */
	public int getTimestamp(){
		Object obj = super.getOutTimestamp();

		if ( obj != null ){
			return ((Integer)obj).intValue();
		}else
			return 0;
	}

	/**
	 * Set the value of simulation-time stamp
	 * @param value
	 */
	public void setTimestamp( int value ){
		super.setOutTimestamp( new Integer(value) );
	}
}
