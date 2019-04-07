/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Category;

import ai.sim.analysis.common.CommonDefinition;

/**
 * @author Qiong Cheng
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ConfigFile {

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
        	FileInputStream input = new FileInputStream( CommonDefinition.getFullPath(CommonDefinition.CurDir, CommonDefinition.configFile ) );
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

	private static String hasAnalysisModule = "hasAnalysisModule";
    public static boolean hasAnalysisModule(){
    	boolean bRet = false ;
    	String sDebug = getProperty( hasAnalysisModule );

    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;

    	return bRet;
    }

	private static String XAxisLabel = "XAxisLabel";
    public static String getXAxisLabel(){
    	String sLabel = getProperty( XAxisLabel );

    	return sLabel;
    }

	private static String AnalysisModuleConfig = "AnalysisModule.Config";
    public static String getAnalysisModuleConfig(){
    	String sConfig = getProperty( AnalysisModuleConfig );

    	return sConfig;
    }

	private static String ActOutDefaultTimeStep = "ActivityOutputDefaultTimeStep";
    public static int getActOutDefaultTimeStep(){
    	String sCount = getProperty( ActOutDefaultTimeStep );
    	try{
    		if ( sCount == null || sCount != null && sCount.trim().equals( "" ))
    			return -1;

    		int iCount = CommonDefinition.parseInt( sCount );
    		return iCount;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.properties file ] ActOutDefaultTimeStep item is wrong.");
    		return -1;
    	}
    }

	private static String RingBufferSize = "RingBufferSize";
    public static int getRingBufferSize(){
    	String sCount = getProperty( RingBufferSize );
    	try{
    		if ( sCount == null || sCount != null && sCount.trim().equals( "" ))
    			return -1;

    		int iCount = CommonDefinition.parseInt( sCount );
    		return iCount;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.properties file ] ActOutDefaultTimeStep item is wrong.");
    		return -1;
    	}
    }
}
