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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import legacy.Common;
import legacy.Helpers;
import legacy.resource.codecs.image.*;

/**
 * CodecLibrary.class contains codec maps for use with resource management classes
 * @author Benjamin Tarrant
 */
public class CodecLibrary {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    private static Codec[] ImageCodecs = {new BMPCodec(),new GIFCodec()};
    private static Codec[] PalletCodecs = {};
    private static Codec[] MidiCodecs = {};
    private static Codec[] SoundCodecs = {};
    private static Codec[] FontCodecs = {};
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * installCodecs(Properties) allows the end user to add additional codecs which the
     * ResourceManager can use load resoruces for use.</br>
     * the codex list allows is a properties list and sould be formated
     * as follows</br><pre>
     * //Codex Properties File for my game
     * mygame.codex.worldmap = "codec to read a map as an image"
     * mygame.codex.tile = "codec to read a tile" 
     * @param codex a properties file containg a list of classes to use.
     */
    public static final void installCodecs(Properties codex) {
        // <editor-fold defaultstate="collapsed" desc="Prepait Temporary List">
        List<Codec> imageCodex = new ArrayList<Codec>();
        List<Codec> palletCodex = new ArrayList<Codec>();
        List<Codec> midiCodex = new ArrayList<Codec>();
        List<Codec> soundCodex = new ArrayList<Codec>();
        List<Codec> fontCodex = new ArrayList<Codec>();
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Traverese Codecs List">    
        Enumeration keys = codex.keys();
        boolean validExtension = true;
        while (keys.hasMoreElements()) {
            try {
                // <editor-fold defaultstate="collapsed" desc="Validate Cast">
                String clazzName = (String) keys.nextElement();
                validExtension = Helpers.classExtends(clazzName, Codec.class.getCanonicalName());
                Class clazz = Class.forName(clazzName);
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Append Codec">
                if (validExtension) {
                    Codec cast = (Codec) clazz.newInstance();
                    if (cast.getType() == Codec.IMAGE_TYPE) {
                        imageCodex.add((Codec) clazz.newInstance());
                    } else if (cast.getType() == Codec.PALLET_TYPE) {
                        palletCodex.add((Codec) clazz.newInstance());
                    } else if (cast.getType() == Codec.MIDI_TYPE) {
                        midiCodex.add((Codec) clazz.newInstance());
                    } else if (cast.getType() == Codec.SOUND_TYPE) {
                        soundCodex.add((Codec) clazz.newInstance());
                    } else if (cast.getType() == Codec.FONT_TYPE) {
                        fontCodex.add((Codec) clazz.newInstance());
                    } else {
                        throw new InstantiationException(); //Unknown Codec Type
                    }
                    cast = null;
                } else {
                    throw new ClassCastException(); //Invalid Extension(cast of)
                }
            // </editor-fold>
            } catch (ClassCastException CCE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
            } catch (ClassNotFoundException CNFE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
            } catch (InstantiationException IE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_CONSTRUCTION_FAILURE));
            } catch (IllegalAccessException IAE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_CONSTRUCTION_FAILURE));
            }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Update Codec Library">
        for (Codec c : ImageCodecs) {
            imageCodex.add(c);
        }
        ImageCodecs = (Codec[]) imageCodex.toArray();
        for (Codec c : PalletCodecs) {
            palletCodex.add(c);
        }
        PalletCodecs = (Codec[]) palletCodex.toArray();
        for (Codec c : MidiCodecs) {
            midiCodex.add(c);
        }
        MidiCodecs = (Codec[]) midiCodex.toArray();
        for (Codec c : SoundCodecs) {
            soundCodex.add(c);
        }
        SoundCodecs = (Codec[]) soundCodex.toArray();
        for (Codec c : FontCodecs) {
            fontCodex.add(c);
        }
        FontCodecs = (Codec[]) fontCodex.toArray();
        imageCodex = null;
        palletCodex = null;
        midiCodex = null;
        soundCodex = null;
        fontCodex = null;
        System.gc();
    // </editor-fold>
    }

    /**
     * getImageCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public static final Codec[] getImageCodecs() {
        return ImageCodecs;
    }

    /**
     * getPalletCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public static final Codec[] getPalletCodecs() {
        return PalletCodecs;
    }

    /**
     * getMidiCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public static final Codec[] getMidiCodecs() {
        return MidiCodecs;
    }

    /**
     * getSoundCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public static final Codec[] getSoundCodecs() {
        return SoundCodecs;
    }

    /**
     * getFontCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public static final Codec[] getFontCodecs() {
        return FontCodecs;
    }    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    // </editor-fold>    
}
