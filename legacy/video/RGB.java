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
package legacy.video;

/**
 * RGB.class is a container for RGBA color composites with values ranging from 
 * 0 to 255 for each.</br>
 * <b>Warning</b><i> access to componets r,g,b,a are public but may not effect
 * the value used by any rendition service or class, these feild are availble for
 * optimiziation considerations only. modify at your own risk.</i></br>
 * <b>Reccomend</b><i> read only.</i>
 * @author Benjamin Tarrant
 */
public class RGB {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Internal">
    /**
     * red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * <br><b>Recommend</b><i> read only.</i>
     */
    public int r;
    /**
     * green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * <br><b>Recommend</b><i> read only.</i>
     */
    public int g;
    /**
     * blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * <br><b>Recommend</b><i> read only.</i>
     */
    public int b;
    /**
     * alpha component ranging from 0 to 255 where 0 is considered to be fully translucent
     * <br><b>Recommend</b><i> read only.</i>
     */
    public int a;
    /**
     * alpha, red, green and blue components packed as a single int for use with display adaptor
     * <br><b>Recommend</b><i> read only.</i>
     */
    public int value;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * RGB(int rgb) creates a a record of all three components plus alpha extracted from 0xAARRGGBB
     * @param rgb red green blue and alpha component ranging from 0 to 255 where 255 is
     * considered to be at it's higest luminace for red green and blue and the alpha
     * range from 0 to 255 where 0 is considered to be fully translucent
     */
    public RGB(int rgb) {
        this.a = (rgb >> 24) & 255;
        this.r = (rgb >> 16) & 255;
        this.g = (rgb >> 8) & 255;
        this.b = (rgb) & 255;
        this.value = rgb;
    }

    /**
     * RGB(int r, int g, int b) creates a a record of all three components plus alpha
     * by default set to 255 and it's real value calulated as 0xAARRGGBB
     * @param r red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param g green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param b blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public RGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }

    /**
     * RGB(int r, int g, int b, int a) creates a a record of all three components plus alpha
     * and it's real value calulated as 0xAARRGGBB
     * @param r red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param g green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param b blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param a alpha component ranging from 0 to 255 where 0 is considered to be fully translucent
     */
    public RGB(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getter for red component
     * @return red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public int getR() {
        return r;
    }

    /**
     * safe setter for the red component, will adjust value;
     * @param r red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public void setR(int r) {
        this.r = r;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }

    /**
     * getter for green component
     * @return green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public int getG() {
        return g;
    }

    /**
     * safe setter for the green component, will adjust value;
     * @param g green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public void setG(int g) {
        this.g = g;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }

    /**
     * getter for blue component
     * @return blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public int getB() {
        return b;
    }

    /**
     * safe setter for the blue component, will adjust value;
     * @param b blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     */
    public void setB(int b) {
        this.b = b;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }

    /**
     * getter for alpha component
     * @return alpha component ranging from 0 to 255 where 0 is considered to be fully translucent
     */
    public int getA() {
        return a;
    }

    /**
     * safe setter for the alpha component, will adjust value;
     * @param a blue component ranging from 0 to 255 where 0 is considered to be fully translucent.
     */
    public void setA(int a) {
        this.a = a;
        this.value = getColor(this.r, this.g, this.b, this.a);
    }

    /**
     * getter for alpha, red, green and blue components packed as a single int for use with display adaptor</br>
     * <b>Warning</b><i>may be inacurate if any component has been directly altered.</i>
     * @return packed value of compaonents as 0xAARRGGBB
     */
    public int getColor() {
        return value;
    }

    /**
     * safe setter for the alpha, red, greed and blue components by packed value, will adjust components.;
     * @param value in the rgba format as 0xAARRGGBB
     */
    public void setValue(int value) {
        this.a = (value >> 24) & 255;
        this.r = (value >> 16) & 255;
        this.g = (value >> 8) & 255;
        this.b = (value) & 255;
        this.value = value;
    }

    /**
     * getColor(int r, int g, int b, int a) packs the RGBA components into a single int for use with the display adaptor
     * @param r red component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param g green component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param b blue component ranging from 0 to 255 where 255 is considered to be at it's higest luminace.
     * @param a alpha component ranging from 0 to 255 where 0 is considered to be fully translucent
     * @return packed value of compaonents as 0xAARRGGBB
     */
    public static final int getColor(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
    }

    /**
     * blendColor blends to colors togeather using the second colors alpha value as the quantity to blend. 
     * @param baseRGB the RGB triplet to blend
     * @param colorRGB the RGB quad to blend
     * @return the Composite color
     */
    public static final int blendColor(RGB baseRGB, RGB colorRGB) {
        //r = (r1 * (a * 256 / 255) + r2 * (256 - (a * 256 / 255))) / 256;
        int a = colorRGB.a;
        return (255 << 24) |
                ((((colorRGB.r * a + baseRGB.r * (256 - a)) >> 8) & 0xFF) << 16) |
                ((((colorRGB.g * a + baseRGB.g * (256 - a)) >> 8) & 0xFF) << 8) |
                ((((colorRGB.b * a + baseRGB.b * (256 - a)) >> 8) & 0xFF));

    }

    /**
     * blendColor blends to colors togeather using the second colors alpha value as the quantity to blend. 
     * @param baseRGB the packed RGB triplet to blend
     * @param colorRGB the packed RGB quad to blend
     * @return the Composite color
     */
    public static final int blendColor(int baseRGB, int colorRGB) {
        int a = (colorRGB >> 24) & 255;
        return (255 << 24) |
                ((((((colorRGB >> 16) & 255) * a + ((baseRGB >> 16) & 255) * (256 - a)) >> 8) & 0xFF) << 16) |
                ((((((colorRGB >> 8) & 255) * a + ((baseRGB >> 8) & 255) * (256 - a)) >> 8) & 0xFF) << 8) |
                (((((colorRGB & 255) * a + (baseRGB & 255) * (256 - a)) >> 8) & 0xFF));

    }    // </editor-fold>    
    
}
