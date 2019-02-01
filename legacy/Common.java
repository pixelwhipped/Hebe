// <editor-fold defaultstate="collapsed" desc="Licenses">
/**
 * Hêbê (Ήβη) Goddess of youth 
 * Project legacy, the rebirth of classic games
 * Copyright© 2008 Benjamin J. Tarrant
 *
 * Hêbê (Ήβη) and it's named package are designed to use and or emulate existing
 * games which may or may not be copyrighted and/or/with registered trademark(s),
 * as such no materials are distributed or o be distributed with this package. No
 * materials are to be used with this package unless you're legally entitled to 
 * do so.
 *
 * Warning distributed ABSOLUTELY NO WARRANTY. Under no circumstances is/are the
 * Developer(s) responsible for and damage of any type incurred but the code.
 * If you disagree with this please remove this package from your storage medium.
 */
// </editor-fold>
package legacy;

/**
 * Common.class contains all references which may be hardcoded for system use.
 * This class contains Feilds and should be altered in accordance with execution
 * settings on compile.  No binarys are distributed and should not be.
 * @author Benjamin Tarrant
 */
public class Common {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    /**
     * APPLICATION_NAME refers to the name of this project.
     */
    public static final String APPLICATION_NAME = "Legacy™";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="System Internals">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="System Codes">
    // <editor-fold defaultstate="collapsed" desc="Return Codes">
    /**
     * NORMAL_TERMINATION indicated normal termination ansi compliant return for normal termination
     */
    public static final int NORMAL_TERMINATION = 0;
    /**
     * ABNORMAL_TERMINATION indicated abnormal termination ansi compliant return for abnormal termination
     */
    public static final int ABNORMAL_TERMINATION = 1;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Fatal Error Codes">
    /**
     * ERROR_FATAL_INVALID_DISPAY_ADAPTER return for invalid display adapter request
     */
    public static final int ERROR_FATAL_INVALID_DISPAY_ADAPTER = 2;
    /**
     * ERROR_FATAL_INVALID_CONFIGURATION return for invalid configuration or null configuration
     */
    public static final int ERROR_FATAL_INVALID_CONFIGURATION = 4;
    /**
     * ERROR_FATAL_CONSTRUCTION_FAILURE return when a method involved in constuction has failed
     */
    public static final int ERROR_FATAL_CONSTRUCTION_FAILURE = 8;
    /**
     * ERROR_FATAL_DATA_FAILURE return when a variable was incorrectly initialized or incompatible to configuration</br>
     * <b>IE</b><i> an attemp to apply a pallet to an invallid system pallet size</i>
     */
    public static final int ERROR_FATAL_DATA_FAILURE = 16;
    
    /**
     * ERROR_FATAL_ILLEGAL_USAGE return when the end user has attempted to operate in a manor which has been defined
     * as illegal or unsafe
     */
    public static final int ERROR_FATAL_ILLEGAL_USAGE = 32;
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>
}
