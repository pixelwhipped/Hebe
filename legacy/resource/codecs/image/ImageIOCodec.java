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

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import legacy.Helpers;
import legacy.resource.Codec;

/**
 * ImageIOCodec.class 
 * @author Benjamin Tarrant
 */
public abstract class ImageIOCodec extends Codec {

    /**
     * validate checks if the resource is comaptible with ImageIO and this codec
     * @param resource file to test
     * @return true on success false otherwise
     */
    public final boolean validate(String resource) {
        try {
            ImageIO.read(Helpers.getInputStreamProtocol(resource)); //No Reference required 
            System.gc();
            return true;
        } catch (IOException IOE) {
            return false;
        }
    }

    /**
     * isDecodable returns true as allowed ImageIO types are decodeble.
     * @return true
     */
    public final boolean isDecodable() {
        return true;
    }

    /**
     * decodeNative returns the native version of the resource from a given inputStream
     * @param in input to transcode if needed
     * @return an input stream for the file type
     * @throws  IOException if an unforseen issue reading a file has occoured
     * @todo implemet code
     */
    public final InputStream decodeNative(InputStream in) throws IOException {
        return new InputStream() {

            int pos = 0;
            int mark = 0;
            Integer byteStream[];

            public InputStream getInstance(InputStream in) throws IOException {
                BufferedImage input = ImageIO.read(in);
                IndexColorModel colorModel = (IndexColorModel) input.getColorModel();
                byteStream = new Integer[17 + ((input.getWidth() * input.getHeight()) * 4)];
                int[] v = Helpers.unpack(input.getWidth());
                Helpers.getIntegerArray().insert(new Integer[]{v[0], v[1], v[2], v[3]}, byteStream, 0); //add width      
                v = Helpers.unpack(input.getHeight());
                Helpers.getIntegerArray().insert(new Integer[]{v[0], v[1], v[2], v[3]}, byteStream, 4); //add height 
                Helpers.getIntegerArray().insert(new Integer[]{-1}, byteStream, 8); //add hasAlpha
                Helpers.getIntegerArray().insert(new Integer[]{((colorModel.getTransparentPixel() == -1) ? -1 : 1)}, byteStream, 9); //add hasTransparency   
                v = Helpers.unpack(colorModel.getTransparentPixel());
                Helpers.getIntegerArray().insert(new Integer[]{v[0], v[1], v[2], v[3]}, byteStream, 10); //add transpaent pixel color
                for (int y = 0; y < input.getHeight(); y++) {
                    for (int x = 0; x < input.getWidth(); x++) {
                        v = Helpers.unpack(input.getHeight());
                        if (v[0] != 0xff) {
                            Helpers.getIntegerArray().insert(new Integer[]{1}, byteStream, 8); //Alpha = true
                        }
                        Helpers.getIntegerArray().insert(new Integer[]{v[0], v[1], v[2], v[3]}, byteStream, x + (y * input.getWidth())); //add pixel
                    }
                }
                in.close();
                return this;
            }

            public int read() throws IOException {
                return byteStream[pos++];
            }

            @Override
            public int read(byte b[]) throws IOException {
                return read(b, 0, b.length);
            }

            @Override
            public int read(byte b[], int off, int len) throws IOException {
                System.arraycopy(byteStream, pos, b, off, len);
                pos += len;
                return len;
            }

            @Override
            public long skip(long n) throws IOException {
                return pos += n;
            }

            @Override
            public int available() throws IOException {
                return byteStream.length;
            }

            @Override
            public void close() throws IOException {
                this.byteStream = null;
            }

            @Override
            public synchronized void mark(int readlimit) {
                mark = pos;
            }

            @Override
            public synchronized void reset() throws IOException {
                pos = mark;
            }

            @Override
            public boolean markSupported() {
                return true;
            }
        }.getInstance(in);
    }

    /**
     * isEncodable returns true as allowed ImageIO types are encodeble.
     * @return true
     */
    public final boolean isEncodeable() {
        return true;
    }

    /**
     * encodeNative writes to a given output stream from native version of the resource
     * @param out the output
     * @param format the file format to encode from
     * @throws  IOException if an unforseen issue reading a file has occoured
     */
    public final void encodeNative(OutputStream out, Class format, Object src) throws IOException {
        if (Helpers.classExtends(format, NativeImageFormat.class)) {
            NativeImageFormat fmt = (NativeImageFormat) src;
            int pix[] = new int[fmt.getWidth() * fmt.getHeight()];
            for (int x = 0; x < fmt.getWidth(); x++) {
                for (int y = 0; y < fmt.getHeight(); y++) {
                    pix[x+(y*fmt.getWidth())] = fmt.getPixel(x, y);
                }
            }
            MemoryImageSource source = new MemoryImageSource(fmt.getWidth(), fmt.getHeight(), ColorModel.getRGBdefault(), pix, 0, fmt.getWidth());
            BufferedImage bufImage = new BufferedImage(fmt.getWidth(), fmt.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufImage.createGraphics();
            g.drawImage(Toolkit.getDefaultToolkit().createImage(source), 0, 0, null);
            g.dispose();
            ImageIO.write(bufImage, getExtension(), out);
        } else {
            throw new IOException();
        }
    }

    /**
     * getType returns the known ImageIO type in this case Codec.IMAGE_TYPE.
     * @return Codec.IMAGE_TYPE
     */
    public final Object getType() {
        return Codec.IMAGE_TYPE;
    }

    /**
     * getExtension returns an extension of a known ImageIO type
     * @return the file extension associated with a type
     */
    public abstract String getExtension();
}
