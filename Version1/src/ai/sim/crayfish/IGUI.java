/*
 * Created on 2005-2-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish;

import ai.sim.crayfish.command.Mediator;
import genDevs.modeling.IOBasicDevs;
import genDevs.simulation.realTime.RTcoupledCoordinator;
import genDevs.simulation.realTime.TunableCoordinator;

/**
 * @author Qiong Cheng
 *
 * Interface for GUI
 */
public interface IGUI {
	/**
	 * getModel
	 * @return
	 */
	public IOBasicDevs getModel();
	
	/**
	 * getSimulator
	 * @return
	 */
	public TunableCoordinator getSimulator();
	
	/**
	 * getMediator
	 * @return
	 */
	public Mediator getMediator();
}
