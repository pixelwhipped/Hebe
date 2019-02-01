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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import legacy.resource.Format;
import legacy.resource.ResourceManager;
import legacy.resource.VideoCodecToolkit;
import legacy.video.RGB;

/**
 * NativeImageFormat.class is the base for image files as reads files as defined</br>
 * readInt() = width</br>
 * readInt() = height</br>
 * readBoolean() = has Alpha </br>
 * readBoolean() = has Transparency </br>
 * readInt() = Transparency should be indicated above and -1 is read as indiction of no transparency</br>
 * readInt()...<b>width</><i> times <b>height</b> read a packed in containg RGB in the format AARRGGBB</br>
 * 14bytes+(width*height*4)bytes
 * This is a very simple form as is makes things simpler when reading a image and transcoding it.  it is also important
 * to note what is read becomes irrelevent quickly as images are adjusted to suit the active VideoAdapter when read.
 * @author Benjamin Tarrant
 */
public class NativeImageFormat extends Format {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    private static int BEST_BLACK = RGB.getColor(0, 0, 0, 0);
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    private int width = 0;
    private int height = 0;
    private int pixelMap[] = null;
    private int alphaMap[] = null;
    private boolean hasAlpha = false;
    private boolean hasTransparency = false;
    private int transparency = -1;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Constructor for Image Storage.  
     * @param stream the stram to read.
     * @throws java.io.IOException
     */
    public NativeImageFormat(InputStream stream) throws IOException {
        super(stream);
        synchronized (getFormatLoadLock()) {
            VideoCodecToolkit toolkit = ResourceManager.getVideoCodecToolkit();
            BEST_BLACK = toolkit.getClosetColor(0, 0, 0);
            DataInputStream datastream = new DataInputStream(stream);
            width = datastream.readInt();
            height = datastream.readInt();
            hasTransparency = datastream.readBoolean();
            transparency = toolkit.getClosetColor(new RGB(datastream.readInt()));
            if (width < 0 && height < 0) {
                int size = width * height;
                pixelMap = new int[size];
                alphaMap = new int[size];
                for (int x = 0; x < size; x++) {
                    RGB rgb = new RGB(datastream.readInt());
                    if (rgb.getA() != 255) {
                        hasAlpha = true;
                    }
                    pixelMap[x] = toolkit.getClosetColor(rgb);
                    alphaMap[x] = rgb.getA();
                }
                if (!hasAlpha) {
                    alphaMap = null;
                    System.gc();
                }
                datastream.close();
                datastream = null;
            } else {
                throw new IOException();
            }

        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasAlphaMap() {
        return hasAlpha;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public int getTransparency() {
        return transparency;
    }    // </editor-fold>    

    /**
     * getPixel returns  pixel at the given coordinaters
     * @param x offset
     * @param y offset
     * @return indexed color
     */
    public int getPixel(int x, int y) {
        return pixelMap[x + (y * width)];
    }

    /**
     * getPixel returns a pixels alpha level at the given coordinaters
     * @param x offset
     * @param y offset
     * @return lpha level
     */
    public int getAlpha(int x, int y) {
        if (alphaMap != null) {
            return alphaMap[x + (y * width)];
        }
        return 0;
    }

    /**
     * getPixelRegion copys an area to a array filled with index colors
     * @param x offset
     * @param y offet
     * @param width bounding width
     * @param height bounding height
     * @return the index array
     */
    public int[] getPixelRegion(int x, int y, int width, int height) {
        int pix[] = new int[width * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(getPixelScanline(x, y + i, width), 0, pix, width * i, width);
        }
        return pix;
    }

    /**
     * getAlphaRegion copys an area to a array filled with alpha levels
     * @param x offset
     * @param y offet
     * @param width bounding width
     * @param height bounding height
     * @return the alpha map array
     */
    public int[] getAlphaRegion(int x, int y, int width, int height) {
        int pix[] = new int[width * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(getAlphaScanline(x, y + i, width), 0, pix, width * i, width);
        }
        return pix;
    }

    /**
     * getPixelScanline return a copy of this image and allows for after x offset and overrun checking. 
     * scanline shoud represent a line in an image ie from the horizontal posistion 0 to its width at the verticly y.
     * this method on adds the paramity x to reduce complexities later.
     * @param x offset
     * @param y offset 
     * @param width width to read.
     * @return and array filled in with index colors
     */
    public int[] getPixelScanline(int x, int y, int width) {
        int line[] = new int[width];
        int copy = ((x + width) < this.width) ? width : ((x + width) - this.width);
        if (y < height) {
            for (int i = 0; i < copy; i++) {
                line[i] = pixelMap[(x + i) + (y * width)];
            }
            if (copy < width) {
                if (hasTransparency()) {
                    for (int i = copy + 1; i < width; i++) {
                        line[i] = transparency;
                    }
                    return line;
                } else {
                    for (int i = copy + 1; i < width; i++) {
                        line[i] = BEST_BLACK;
                    }
                    return line;
                }
            }
            return line;

        }
        if (hasTransparency()) {
            for (int i = 0; i < width; i++) {
                line[i] = transparency;
            }
            return line;
        } else {
            for (int i = 0; i < width; i++) {
                line[i] = BEST_BLACK;
            }
            return line;
        }
    }

    /**
     * getAlphaScanline return a copy of this image and allows for after x offset and overrun checking. 
     * scanline shoud represent a line in an image ie from the horizontal posistion 0 to its width at the verticly y.
     * this method on adds the paramity x to reduce complexities later.
     * @param x offset
     * @param y offset 
     * @param width width to read.
     * @return and array filled in with alpha levels
     */
    public int[] getAlphaScanline(int x, int y, int width) {
        int line[] = new int[width];
        if (alphaMap != null) {
            int copy = ((x + width) < this.width) ? width : ((x + width) - this.width);
            if (y < height) {
                for (int i = 0; i < copy; i++) {
                    line[i] = alphaMap[(x + i) + (y * width)];
                }
                if (copy < width) {
                    for (int i = copy + 1; i < width; i++) {
                        line[i] = 0;
                    }
                    return line;
                }
                return line;

            }
        }
        for (int i = 0; i < width; i++) {
            line[i] = 0;
        }
        return line;
    }

    /**
     * putPixel(int x, int y, int color) places a  pixel on the active video buffer
     * @param x coordinate
     * @param y coordinate
     * @param color as pallet index
     */
    public void putPixel(int x, int y, int color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixelMap[x + (y * width)] = (color & 255) % 256;
        }
    }

    /**
     * putAlpha(int x, int y, int level) places a  pixel on the active video buffer
     * @param x coordinate
     * @param y coordinate
     * @param level alpha level
     */
    public void putAlpha(int x, int y, int level) {
        if (alphaMap != null) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                alphaMap[x + (y * width)] = (level & 255) % 256;
            }
        }
    }

    /**
     * putPixel(int x, int y, RGB rgb) places a  pixel on the active video buffer
     * @param x coordinate
     * @param y coordinate
     * @param rgb color
     */
    public void putPixel(int x, int y, RGB rgb) {
        putPixel(x, y, rgb.getColor());
        putAlpha(x, y, rgb.getA());
    }
    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    // </editor-fold>    
}
