/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.handlers.dataobj.impl;

import org.jfree.data.time.Millisecond;

import ai.sim.analysis.handlers.dataobj.AbstractHandledDataObj;

/**
 * @author Qiong Cheng
 *
 * Excitation output value
 * 
 */
public class RealtimeExcitHandledDataObj extends AbstractHandledDataObj{
	/**
	 * Constructor method
	 * @param id
	 * @param timestamp
	 * @param value
	 */
	public RealtimeExcitHandledDataObj( int id, Object timestamp, double value ){
		super( id, timestamp, new Double(value) );
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
	 * Get the value of realtime stamp
	 */
	public Object getTimestamp(){
		Object obj = super.getOutTimestamp();
		
		return obj;
	}
	
	/**
	 * Set the value of realtime stamp 
	 * @param value
	 */
	public void setTimestamp( Millisecond value ){
		super.setOutTimestamp( value );
	}
}
