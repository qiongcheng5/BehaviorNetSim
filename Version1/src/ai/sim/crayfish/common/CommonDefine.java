/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish.common;

import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Qiong Cheng
 *
 * Common Definition
 */
public class CommonDefine {
	public static String title = "title";
	
	public static String LabelSuffix = "Label";
	public static String MnemonicSuffix = "Mnemonic";
	public static String AccelSuffix = "Accel";
	public static String ImageSuffix = "Image";	
	public static String TipSuffix = "Tip";
	
	// External resource
	protected static ResourceBundle resources;
	static {
    	try {
	        resources = ResourceBundle.getBundle( "resources.crayfishgui", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("resources/crayfishgui.properties not found");
			System.exit(1);
		}
	} 
	
	/**
	 * getResourceString
	 * @param nm
	 * @return
	 */
	public static String getResourceString(String nm) {
		try{
			return resources.getString(nm);
		}catch ( MissingResourceException ex ){
			return null;
		}
	}
	
	/**
	 * getResource
	 * @param key
	 * @return
	 */
	public URL getResource(String key) {
		String name = getResourceString(key);
		if (name != null) {
			URL url = this.getClass().getClassLoader().getResource(name);
			return url;
		}
		return null;
	}
	
	/**
	 * 
	 */
	public CommonDefine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
	}
}
