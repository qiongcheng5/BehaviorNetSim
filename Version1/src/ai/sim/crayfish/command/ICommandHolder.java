/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish.command;

/**
 * @author Qiong Cheng
 *
 * Command Pattern
 * All implemented class should include a hashtable to keep the command list
 */
public interface ICommandHolder {
	public void setCommand( ICommand comd );
	public ICommand getCommand( );
}
