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

import legacy.video.RGB;

/**
 * VideoCodecToolkit.class allows access to functions wich would not otherwize be accessable to Codecs
 * @author Benjamin Tarrant
 */
public interface VideoCodecToolkit {

       // <editor-fold defaultstate="collapsed" desc="Methods">
        /**
     * setPalletIndex(int, RGB) sets a color in this pallet at index to a specific color
     * @param index index of color
     * @param color color to set
     */
    public void setPalletIndex(int index, RGB color);
    /**
     * getPalletIndex(int) returns the color currently set for the pallet at index of
     * @param index of color
     * @return the RGB for a pallet index
     */
    public RGB getPalletIndex(int index);

    /**
     * Returns the closest color in the existing pallet
     * @param rgb the RGB triplet to match
     * @return the index in this pallet of the closest color
     */
    public int getClosetColor(RGB rgb);

    /**
     * Returns the closest color in the existing pallet
     * @param r Red component to match
     * @param g Green component to match
     * @param b Blue component to match
     * @return the index in this pallet of the closest color
     */
    public int getClosetColor(int r, int g, int b);
    // </editor-fold>    
}
