/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.common;

import ai.sim.analysis.util.ConfigFile;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommonDefinition {
	//======== For Constant Variables =========
	public static String configFile = "config/config.analysis.properties";
	public static String DisplayModConfigFile = "config/" + ai.sim.analysis.util.ConfigFile.getAnalysisModuleConfig();
	public static String CurDir = System.getProperty("user.dir");
	
	public static String getFullPath( String dir, String file ){
		if ( dir == null ) return file;
		if ( file == null ) return dir;
		
		if (  ( dir.lastIndexOf( "/")==dir.length()-1 ) && ( file.lastIndexOf( "/")==0 ) )
			return dir + file.substring( 1 );
		else if (  ( dir.lastIndexOf( "/")!=dir.length()-1 ) && ( file.lastIndexOf( "/")!=0 ) )
				return dir + "/" + file;
		else
			return dir + file;
			
	}
    public static int parseInt(String argument) throws NumberFormatException{
        int value = 0;
        try {
            value = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw e;
        }
        return value;
    }
    
    public static double parseDouble(String argument) throws NumberFormatException{
    	double value = 0;
        try {
            value = Double.parseDouble(argument);
        } catch (NumberFormatException e) {
            throw e;
        }
        return value;
    }    
	public static void main(String[] args) {
	}
}
