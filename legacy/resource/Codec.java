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
import java.io.OutputStream;

/**
 * Codec is deigned to handle resource types and the loading of.
 * calls to encode and decode should encode from a native type or decode to a native type.</br>
 * <b>Note</b><i> native types are a format recognized by legacy and any new implementaion of a type will require
 * adding coding to the legacy and resource management classes.
 * @author Benjamin Tarrant
 */
public abstract class Codec {

    /**
     * Internal reference for a image type
     */
    public static final Object IMAGE_TYPE = new Object();
    /**
     * Internal reference for a pallet type
     */
    public static final Object PALLET_TYPE = new Object();
    /**
     * Internal reference for a font type
     */
    public static final Object FONT_TYPE = new Object();
    /**
     * Internal reference for a midi type
     */
    public static final Object MIDI_TYPE = new Object();
    /**
     * Internal reference for a sound type
     */
    public static final Object SOUND_TYPE = new Object();

    /**
     * validate is a method wich is called to check if a resource is actualy associated with this Codec
     * @param resource file to test
     * @return true on success false otherwise
     */
    public abstract boolean validate(String resource);

    /**
     * isDecodable check if the ablility to read an input stream is infact possible
     * @return true on success false otherwise
     */
    public abstract boolean isDecodable();

    /**
     * decodeNative returns the native version of the resource from a given inputStream
     * @param in input to transcode if needed
     * @return an input stream for the file type
     * @throws  IOException if an unforseen issue reading a file has occoured
     */
    public abstract InputStream decodeNative(InputStream in) throws IOException;

    /**
     * isEncodable check if the ablility to write an output stream is infact possible
     * @return true on success false otherwise
     */
    public abstract boolean isEncodeable();

    /**
     * encodeNative writes to a given output stream from native version of the resource
     * @param out the output
     * @param format the file format to encode from
     * @param src the Object it self
     * @throws  IOException if an unforseen issue reading a file has occoured
     */
    public abstract void encodeNative(OutputStream out, Class format, Object src) throws IOException;

    /**
     * getType returns the known native type.
     * @return an object of a known type
     */
    public abstract Object getType();

    /**
     * getExtension returns an extension of a knwon type
     * @return the file extension associated with a type
     */
    public abstract String getExtension();
}
