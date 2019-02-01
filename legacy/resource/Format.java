// <editor-fold defaultstate="collapsed" desc="Licenses">
/**
 * Hêbê (Ήβη) Goddess of youth 
 * Project hebe, the rebirth of classic games
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
package legacy.resource;

import java.io.IOException;
import java.io.InputStream;
import legacy.Annotations.Restricted;
import legacy.Common;
import legacy.Helpers;

/**
 * Format.class 
 * @author Benjamin Tarrant
 */
public abstract class Format {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    /**
     * Internal locking object used to prevent execution until a synchronized block has executed
     */
    private static final Object FORMAT_LOAD_LOCK = new Object();
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Format is requred to be able to be read directly from an InputStream
     * @param stream
     */
    public Format(InputStream stream) throws IOException{
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getFormatLoadLock() is used to prevent race conditions.  </br>
     * <b>Warning</b><i> no method other than a Format constructor or ResourceManager may call this class.</i>
     * The correct and only possible usage is as follows.</br>
     * <pre>
     * class myGame extends Format{
     *     public NativeImageFormat(InputStream stream){
     *         super(stream);
     *         synchronized(this.getFormatLoadLock()){
     *     }
     * }
     * </pre>
     * @return format lock object
     */
    @Restricted
    protected static final Object getFormatLoadLock() {
        try {
            Class t = Class.forName(Helpers.getCallerClass(1));
            while (!t.getName().equals("java.lang.Object")) {
                if (t.getName().equals("legacy.resource.Format") || t.getName().equals("legacy.resource.ResourceManager")) {
                    return FORMAT_LOAD_LOCK;
                }
                t = t.getSuperclass();
            }
        } catch (ClassNotFoundException CNFE) {

        }
        throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_ILLEGAL_USAGE));
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    // </editor-fold>    
}
