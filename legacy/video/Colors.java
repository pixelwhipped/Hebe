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
package legacy.video;

/**
 * Colors.class contains a list of standard predifined colors generaly used by
 * various systems.
 * @author Benjamin Tarrant
 */
public class Colors {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Common">
    /**
     * Standard Black color
     */
    public static final RGB BLACK = new RGB(0, 0, 0);
    /**
     * Standard Dark Red color
     */
    public static final RGB DARK_RED = new RGB(128, 0, 0);
    /**
     * Standard Dark Green color
     */
    public static final RGB DARK_GREEN = new RGB(0, 128, 0);
    /**
     * Standard Dark Yellow color
     */
    public static final RGB DARK_YELLOW = new RGB(128, 128, 0);
    /**
     * Standard Dark Blue color
     */
    public static final RGB DARK_BLUE = new RGB(0, 0, 128);
    /**
     * Standard Dark Magenta color
     */
    public static final RGB DARK_MAGENTA = new RGB(128, 0, 128);
    /**
     * Standard Dark Cyan color
     */
    public static final RGB DARK_CYAN = new RGB(0, 128, 128);
    /**
     * Standard Light Grey color
     */
    public static final RGB LIGHT_GREY = new RGB(192, 192, 192);
    /**
     * Standard Money Green color
     */
    public static final RGB MONEY_GREEN = new RGB(192, 220, 192);
    /**
     * Standard Sky Blue color
     */
    public static final RGB SKY_BLUE = new RGB(166, 202, 240);
    /**
     * Standard Cream color
     */
    public static final RGB CREAM = new RGB(255, 251, 540);
    /**
     * Standard Medium Grey color
     */
    public static final RGB MEDIUM_GREY = new RGB(160, 160, 164);
    /**
     * Standard Dark Grey color
     */
    public static final RGB DARK_GREY = new RGB(128, 128, 128);
    /**
     * Standard Red color
     */
    public static final RGB RED = new RGB(255, 0, 0);
    /**
     * Standard Green color
     */
    public static final RGB GREEN = new RGB(0, 255, 0);
    /**
     * Standard Yellow color
     */
    public static final RGB YELLOW = new RGB(255, 255, 0);
    /**
     * Standard Blue color
     */
    public static final RGB BLUE = new RGB(0, 0, 255);
    /**
     * Standard Magenta color
     */
    public static final RGB MAGENTA = new RGB(255, 0, 255);
    /**
     * Standard Cyan color
     */
    public static final RGB CYAN = new RGB(0, 255, 255);
    /**
     * Standard White color
     */
    public static final RGB WHITE = new RGB(255, 255, 255);
    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getDistance calculates an effective destance between to individual colors
     * @param rgbx RGB triplet x
     * @param rgby RGB triplet y
     * @return effective distance
     */
    public static final int getDistance(RGB rgbx, RGB rgby) {
        int RedDistance = rgby.r - rgbx.r;
        int GreenDistance = rgby.g - rgbx.g;
        int BlueDistance = rgby.b - rgbx.b;

        return (RedDistance * RedDistance) +
                (GreenDistance * GreenDistance) +
                (BlueDistance * BlueDistance);

    }
    // </editor-fold>
}
