/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.handlers;

/**
 * @author Qiong Cheng
 *
 * In self-life-circle, the class aims to handle collected data
 */
public abstract class AbstractCollectedDataHandler extends Thread{

	public AbstractCollectedDataHandler(){

	}

	public void run(){
		while (true){
			if ( isEmpty() ){
				try{
					Thread.sleep( 800 );
				}catch (Exception e){}
			}
			handleOutput();
		}
	}

	/**
	 * The function aims at how to handle with the output. It depends on different output
	 * parameters and different structures of collected output data
	 *
	 */
	public abstract void handleOutput();

	public abstract boolean isEmpty();

}
