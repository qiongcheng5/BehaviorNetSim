/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;


/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseXYValuePair implements IValuePair{
	Object xValue;
	Object yValue;
	public BaseXYValuePair( Object xValue, Object yValue){
		this.xValue = xValue;
		this.yValue = yValue;
	}
	
	public void setXValue( Object xValue ){
		this.xValue = xValue;
	}
	
	public void setYValue( Object yValue ){
		this.yValue = yValue;
	}
}
