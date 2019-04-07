/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Category;

import ai.sim.analysis.common.CommonDefinition;

/**
 * @author weijp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfigFile {
	private static String configFileName = "config/config.properties";
	
	static private Category log = Category.getInstance(ConfigFile.class.getName());

    private static boolean init = false;
    static Properties props = new Properties();


    protected ConfigFile() {
    }
    
    /**
     * Get the instance and get the config information.
     *
     */
    public static void init() {
        if (init) {
            return;
        }
      
        try {
        	FileInputStream input = new FileInputStream( CommonDefinition.getFullPath(CommonDefinition.CurDir, configFileName ) );
        	if ( input != null ){
        		props.load( input );
                init = true;
        	}
        } catch (IOException e) {
        	e.printStackTrace() ;
 
            log.error("[ config.analysis.module.properties file ] file doesnot exist.");
        }
    }
   
    private static String getProperty(String str) {
        init();
        String value = props.getProperty(str);
        
        if ( value != null ) value = value.trim();
        
        return value;
    }
    
	private static String Title = "Title";
    public static String getTitle(){
    	String sLabel = getProperty( Title );
    	
    	return sLabel;
    }
    
	public static void main(String[] args) {
	}
}
