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

package legacy;

/**
 * Arrays.class 
 * @author Benjamin Tarrant
 */
public class Arrays<T> {

    public void insert(T[] src, T[] dest, int offset, int len) throws IndexOutOfBoundsException,NullPointerException{
        for (int i = offset; i < len + offset; i++) {
            dest[i] = src[i - offset];
        }
    }

    public void insert(T[] src, T[] dest, int offset)  throws IndexOutOfBoundsException,NullPointerException{
        int end = src.length + offset;
        for (int i = offset; i < end; i++) {
            dest[i] = src[i];
        }
    }

    public void insert(T[] src, T[] dest)  throws IndexOutOfBoundsException,NullPointerException{
        for (int i = 0; i < src.length; i++) {
            dest[i] = src[i];
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    // </editor-fold>    
}
