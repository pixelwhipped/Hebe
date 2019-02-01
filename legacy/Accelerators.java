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
 * Accelerators.class conatins various methods for generation accelerated method, funcion and class code
 * @author Benjamin Tarrant
 */
public class Accelerators {
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * optimizeScanlineShift converts a simple expresion such as pix[x+(y*width] = color to and equivical code
     * segment using shift operators instread of multiplication. </br>
     * <b>example</b></br><pre>
     * y*320 equates to ((y<<8)+(y<<6))
     * </pre>
     * @param scanline width of a scanline or graphical object
     * @param param paramiter to be shifted
     * @return the augmented code.
     */
    public static final String optimizeScanlineShift(int scanline, String param) {
        boolean go = true;
        int m1 = 0;
        int m2 = 0;
        String algorithm = "(" + param + "*" + scanline + ")";
        for (int i = 0; i < 32; i++) {
            if ((1 << i) == scanline) {
                return "(" + param + "<<" + i + ")";
            }
        }
        while (go) {
            for (int x = 0; x < m2; x++) {
                if (scanline == ((1 << m1) + (1 << x))) {
                    return "((" + param + "<<" + m1 + ")+(" + param + "<<" + x + "))";
                } else if (x == 0 && (((1 << m1) + (1 << x)) > scanline)) {
                    go = false;
                }
            }
            m1++;
            m2 = m1 - 1;
        }
        return algorithm;
    }
    
    // </editor-fold>    
}
    
