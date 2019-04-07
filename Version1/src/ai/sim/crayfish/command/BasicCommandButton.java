/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish.command;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import ai.sim.crayfish.common.CommonDefine;

/**
 * @author Qiong Cheng
 *
 * Abstract Command Button
 */
public class BasicCommandButton extends JButton implements ICommandHolder,ActionListener {

	protected ICommand command;

	/**
	 * Constructor Function
	 * @param med
	 * @param key
	 */
	public BasicCommandButton(String key ){
		super( CommonDefine.getResourceString(key + CommonDefine.LabelSuffix) );

		init(key);
		addActionListener( this );
	}

	//<----------Implement ICommandHolder-----------
	/**
	 * getCommand
	 */
	public ICommand getCommand(){
		return this.command;
	}

	/**
	 * setCommand
	 */
	public void setCommand( ICommand comm ){
		this.command = comm;
	}
	//----------Implement ICommandHolder----------->

	/**
	 * To Impelement ActionListener
	 */
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if ( obj instanceof ICommandHolder ){
			(( ICommandHolder )obj).getCommand().Execute();
		}
	}

	/**
	 * Initiate the button command
	 *
	 */
	private void init(String key){
		//TODO
		URL url = this.getClass().getClassLoader().getResource(CommonDefine.getResourceString(key + CommonDefine.ImageSuffix));
		if ( url != null ){
			setIcon(new ImageIcon(url));
		}

		setMargin(new Insets(10, 10, 10, 10));
		setRequestFocusEnabled(false);

		setActionCommand(CommonDefine.getResourceString(key + CommonDefine.LabelSuffix));
		this.setBackground( Color.WHITE );
    	setEnabled( false );

		String tip = CommonDefine.getResourceString(key + CommonDefine.TipSuffix);
		tip = (tip != null) ? tip : CommonDefine.getResourceString(key + CommonDefine.LabelSuffix);
		String accel = CommonDefine.getResourceString(key + CommonDefine.AccelSuffix);
		accel = (accel != null) ? " (" + accel + ")" : "";
		setToolTipText(tip + accel);

	}

	public static void main(String[] args) {
	}
}
