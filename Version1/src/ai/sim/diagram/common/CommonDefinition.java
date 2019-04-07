/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.common;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommonDefinition {
	public static String AppTitle = "Title";
	public static String XAxisLabel = "XAxisLabel";

	
	//======== For Constant Variables =========
	public static String ConfigFile = ai.sim.analysis.common.CommonDefinition.DisplayModConfigFile;
	public static String CurDir = ai.sim.analysis.common.CommonDefinition.CurDir;
	
	public static String getFullPath( String dir, String file ){
		return ai.sim.analysis.common.CommonDefinition.getFullPath( dir, file );
			
	}
	
    public static int parseInt(String argument) throws NumberFormatException{
        return ai.sim.analysis.common.CommonDefinition.parseInt( argument );
    }
    
    public static double parseDouble(String argument) throws NumberFormatException{
    	return ai.sim.analysis.common.CommonDefinition.parseDouble( argument );
    }    
}
