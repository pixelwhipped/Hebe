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
package legacy.resource.codecs.image;

/**
 * PNGCodec.class codec for encoding and decoding the Protable Network Graphics Format
 * @author Benjamin Tarrant
 */
public class PNGCodec extends ImageIOCodec {    
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getExtension returns an extension of a known ImageIO type
     * @return the file extension associated with a type
     */
    public String getExtension(){
        return "png";
    }
    // </editor-fold>    

}
