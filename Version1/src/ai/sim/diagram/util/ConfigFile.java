/*
 * Created on Dec 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.util;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Category;

import ai.sim.diagram.common.CommonDefinition;

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
        	FileInputStream input = new FileInputStream( CommonDefinition.getFullPath(CommonDefinition.CurDir, CommonDefinition.ConfigFile ) );
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
    
	private static String GraphWindowTitle = "GraphWindowTitle";
    public static String getGraphWindowTitle(){
    	return getProperty( GraphWindowTitle );
    }
    
	private static String TimeXAxisLabel = "TimeXAxisLabel";
    public static String getTimeXAxisLabel(){
    	return getProperty( TimeXAxisLabel );
    }
    
	private static String ChartBackgroundPaint = "ChartBackgroundPaint";
    public static Color getChartBackgroundPaint(){
    	return Color.getColor( getProperty( ChartBackgroundPaint ) );
    }	
    
	private static String ExitYAxisLabel = "ExitYAxisLabel";
    public static String getExitYAxisLabel(){
    	return getProperty( ExitYAxisLabel );
    }
    
	private static String ChartBorderPaint = "ChartBorderPaint";
    public static Color getChartBorderPaint(){
    	return Color.getColor( getProperty( ChartBorderPaint ) );
    }	
    
	private static String PlotBackgroundPaint = "PlotBackgroundPaint";
    public static Color getPlotBackgroundPaint(){
    	return Color.getColor( getProperty( PlotBackgroundPaint ) );
    }
    
	private static String PlotDomainGridlinePaint = "PlotDomainGridlinePaint";
    public static Color getPlotDomainGridlinePaint(){
    	return Color.getColor( getProperty( PlotDomainGridlinePaint ) );
    }
    
	private static String PlotRangeGridlinePaint = "PlotDomainGridlinePaint";
    public static Color getPlotRangeGridlinePaint(){
    	return Color.getColor( getProperty( PlotRangeGridlinePaint ) );
    }
    
	private static String GraphHistoryCount = "GraphSaveHistoryCount";
    public static int getGraphHistoryCount(){
    	String sCount = getProperty( GraphHistoryCount );
    	try{
    		if ( sCount == null || sCount != null && sCount.trim().equals( "" ))
    			return -1;
    		
    		int iCount = CommonDefinition.parseInt( sCount );
    		return iCount;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] GraphSaveHistoryCount item is wrong.");
    		return -1;
    	}
    }
    
	private static String DefaultSubplots = "DefaultSubplots";
    public static int getDefaultSubplots(){
    	String sCount = getProperty( DefaultSubplots );
    	try{
    		if ( sCount == null || sCount != null && sCount.trim().equals( "" ))
    			return -1;
    		
    		int iCount = CommonDefinition.parseInt( sCount );
    		return iCount;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] DefaultSubplots item is wrong.");
    		return -1;
    	}
    }	
	
	private static String XAxisFixedAutoRange = "XAxisFixedAutoRange";
    public static double getXAxisFixedAutoRange(){
    	String sSize = getProperty( XAxisFixedAutoRange );
    	try{
    		if ( sSize == null || sSize != null && sSize.trim().equals( "" ))
    			return -1;
    		
    		double dSize = CommonDefinition.parseDouble( sSize );
    		return dSize;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] XAxisFixedAutoRange item is wrong.");
    		return -1;
    	}
    }
    
	private static String AutoRangeMinimumSize = "AutoRangeMinimumSize";
    public static double getAutoRangeMinimumSize(){
    	String sSize = getProperty( AutoRangeMinimumSize );
    	try{
    		if ( sSize == null || sSize != null && sSize.trim().equals( "" ))
    			return -1;
    		
    		double dSize = CommonDefinition.parseDouble( sSize );
    		return dSize;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] AutoRangeMinimumSize item is wrong.");
    		return -1;
    	}
    }
    
	private static String YAxisLowerBound = "YAxisLowerBound";
    public static double getYAxisLowerBound(){
    	String sSize = getProperty( YAxisLowerBound );
    	try{
    		if ( sSize == null || sSize != null && sSize.trim().equals( "" ))
    			return -1;
    		
    		double dSize = CommonDefinition.parseDouble( sSize );
    		return dSize;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] YAxisLowerBound item is wrong.");
    		return -1;
    	}
    }
    
	private static String YAxisUpperBound = "YAxisUpperBound";
    public static double getYAxisUpperBound(){
    	String sSize = getProperty( YAxisUpperBound );
    	try{
    		if ( sSize == null || sSize != null && sSize.trim().equals( "" ))
    			return -1;
    		
    		double dSize = CommonDefinition.parseDouble( sSize );
    		return dSize;
    	}catch ( NumberFormatException e ){
        	e.printStackTrace() ;
            System.out.println("[ config.analysis.module.properties file ] YAxisUpperBound item is wrong.");
    		return -1;
    	}
    }    
    
	private static String TimeAxisAutoRange = "TimeAxisAutoRange";
    public static boolean isTimeAxisAutoRange(){
    	boolean bRet = false ;
    	String sDebug = getProperty( TimeAxisAutoRange );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
	private static String SimTimeOfX = "isSimTimeOfX";
    public static boolean isSimTimeOfX(){
    	boolean bRet = false ;
    	String sDebug = getProperty( SimTimeOfX );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
	private static String YAxisRangeFixed = "isYAxisRangeFixed";
    public static boolean isYAxisRangeFixed(){
    	boolean bRet = false ;
    	String sDebug = getProperty( YAxisRangeFixed );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
	private static String YAxisIncludeZero = "doesYAxisIncludeZero";
    public static boolean doesYAxisIncludeZero(){
    	boolean bRet = false ;
    	String sDebug = getProperty( YAxisIncludeZero );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
    //XAXIS
	private static String SimulateTime = "isSimulateTime";
    public static boolean isSimulateTime(){
    	boolean bRet = false ;
    	String sDebug = getProperty( SimulateTime );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
	private static String RealTime = "isRealTime";
    public static boolean isRealTime(){
    	boolean bRet = false ;
    	String sDebug = getProperty( RealTime );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
    private static String TickLabelsVisible = "TickLabelsVisible";
    public static boolean isTickLabelsVisible(){
    	boolean bRet = false ;
    	String sDebug = getProperty( TickLabelsVisible );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
    private static String ChartVerticalZoom = "ChartVerticalZoom";
    public static boolean isChartVerticalZoom(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ChartVerticalZoom );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	
    
    private static String ChartHorizontalZoom = "ChartHorizontalZoom";
    public static boolean isChartHorizontalZoom(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ChartHorizontalZoom );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	
    
    private static String ChartHorizontalAxisTrace = "ChartHorizontalAxisTrace";
    public static boolean isChartHorizontalAxisTrace(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ChartHorizontalAxisTrace );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	
    
    private static String ChartVerticalAxisTrace = "ChartVerticalAxisTrace";
    public static boolean isChartVerticalAxisTrace(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ChartVerticalAxisTrace );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	    
	
    private static String ChartBorderVisible = "ChartBorderVisible";
    public static boolean isChartBorderVisible(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ChartBorderVisible );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
    private static String AutoRangeIncludesZero = "AutoRangeIncludesZero";
    public static boolean doesAutoRangeIncludeZero(){
    	boolean bRet = false ;
    	String sDebug = getProperty( AutoRangeIncludesZero );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	
    
    private static String PlotShapes = "PlotShapes";
    public static boolean doesPlotShapes(){
    	boolean bRet = false ;
    	String sDebug = getProperty( PlotShapes );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	
    
    private static String ShapesFilled = "ShapesFilled";
    public static boolean isShapesFilled(){
    	boolean bRet = false ;
    	String sDebug = getProperty( ShapesFilled );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }	    
    
    private static String RangeCrosshairVisible = "RangeCrosshairVisible";
    public static boolean isRangeCrosshairVisible(){
    	boolean bRet = false ;
    	String sDebug = getProperty( RangeCrosshairVisible );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }
    
    private static String DomainCrosshairVisible = "DomainCrosshairVisible";
    public static boolean isDomainCrosshairVisible(){
    	boolean bRet = false ;
    	String sDebug = getProperty( DomainCrosshairVisible );
    	
    	if ( sDebug != null && sDebug.equalsIgnoreCase( "true" ) )
    		return bRet = true;
    		
    	return bRet;
    }		
}

