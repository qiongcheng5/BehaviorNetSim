/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Category;

import ai.sim.diagram.common.CommonDefinition;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimResource {
	static private Category log = Category.getInstance(SimResource.class.getName());
	
	// External resource
	protected static ResourceBundle resources;
	static {
    	try {
	        resources = ResourceBundle.getBundle( "resources.SimGraph", Locale.getDefault());
		} catch (MissingResourceException mre) {
			log.error("resources/SimGraph.properties not found");
			System.exit(1);
		}
	}
	
	public static String appTitle = resources.getString( CommonDefinition.AppTitle );
	public static String xAxisLabel = resources.getString( CommonDefinition.XAxisLabel);
}
