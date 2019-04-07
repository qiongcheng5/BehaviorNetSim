/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish.command;

import ai.sim.crayfish.IGUI;


/**
 * @author Qiong Cheng
 *
 * StopSimCommand
 */
public class StopSimCommand implements ICommand {
	protected IGUI gui = null;
	
	/**
	 * Constructor
	 * @param gui
	 */
	public StopSimCommand( IGUI gui ){
		this.gui = gui;
	}
	
	/* (non-Javadoc)
	 * @see ai.sim.crayfish.command.ICommand#Execute()
	 */
	public void Execute() {
		if ( this.gui != null && this.gui.getMediator() != null )
			this.gui.getMediator().doStopSim();
	}

	public static void main(String[] args) {
	}
}
