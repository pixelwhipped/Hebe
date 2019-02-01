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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import legacy.Common;
import legacy.Helpers;
import legacy.resource.codecs.sound.NativeSoundFormat;
import legacy.resource.codecs.font.NativeFontFormat;
import legacy.resource.codecs.image.NativeImageFormat;
import legacy.resource.codecs.midi.NativeMidiFormat;
import legacy.resource.codecs.pallet.NativePalletFormat;

/**
 * ResourceGroup.class is a container for lists of specific resouces defined in a group of resources.
 * This includes lists of images, midi, sound, fonts and pallets which are required for use.</bt>
 * <b>Warning</b><i> This class relies heavily on the ResourceManager to set it status paramiter and should not
 * be modifed lightly. </i>
 * @author Benjamin Tarrant
 * @see legacy.resource.ResourceManager
 */
public class ResourceGroup {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Internal">
    /**
     * Container for image references
     */
    private Hashtable<Integer, String> imageCollection;
    /**
     * Container for images
     */
    protected Hashtable<Integer, NativeImageFormat> images;
    /**
     * Container for pallet references
     */
    private Hashtable<Integer, String> palletCollection;
    /**
     * Container for pallets
     */
    protected Hashtable<Integer, NativePalletFormat> pallets;
    /**
     * Container for midi audio references
     */
    private Hashtable<Integer, String> midiCollection;
    /**
     * Container for Midis
     */
    protected Hashtable<Integer, NativeMidiFormat> midis;
    /**
     * Container for sound audio references
     */
    private Hashtable<Integer, String> soundCollection;
    /**
     * Container for sound
     */
    protected Hashtable<Integer, NativeSoundFormat> sounds;
    /**
     * Container for font references
     */
    private Hashtable<Integer, String> fontCollection;
    /**
     * Container for fonts
     */
    protected Hashtable<Integer, NativeFontFormat> fonts;
    /**
     * Name for this group.
     */
    private String name = null;
    /**
     * A resource can only be valid after the Resource manager has validated this group
     */
    private boolean valid = false;
    /**
     * Cached refers to weather this group is activly stored in memory and availible for use.
     */
    private boolean cached = false;
    /**
     * representation of the fraction of resources prepaired for use.
     */
    private float loaded = 0;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Resource group is an arrangements of resouces in a given group. </br>
     * <b>Usages for resources(properties file)</b></br><pre>
     * 1 = images/hud/face01.bmp
     * 2 = images/hud/face02.bmp
     * 3 = images/hud/face03.bmp
     * 4 = images/hud/bar.bmp
     * 5 = fonts/hud.fnt
     * ...
     * </pre>
     * @param group Nmae of the group
     * @param resources a properties file as described above.
     */
    public ResourceGroup(String group, Properties resources) {
        // <editor-fold defaultstate="collapsed" desc="Initialize Containers">
        images = new Hashtable<Integer, NativeImageFormat>();
        imageCollection = new Hashtable<Integer, String>();
        pallets = new Hashtable<Integer, NativePalletFormat>();
        palletCollection = new Hashtable<Integer, String>();
        midis = new Hashtable<Integer, NativeMidiFormat>();
        midiCollection = new Hashtable<Integer, String>();
        sounds = new Hashtable<Integer, NativeSoundFormat>();
        soundCollection = new Hashtable<Integer, String>();
        fonts = new Hashtable<Integer, NativeFontFormat>();
        fontCollection = new Hashtable<Integer, String>();
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Initialize Internal Data">
        name = group;
        Enumeration<String> props = (Enumeration<String>) resources.propertyNames();
        while (props.hasMoreElements()) {
            String key = props.nextElement();
            String value = resources.getProperty(key);
            boolean found = false;
            Codec[] codecs = ResourceManager.getResourceManager().getImageCodecsFor(Helpers.getExtension(value));
            for (Codec c : codecs) {
                if (c.validate(value)) {
                    found = true;
                }
            }
            if (found) {
                imageCollection.put(Integer.parseInt(key), value);
            } else {
                codecs = ResourceManager.getResourceManager().getPalletCodecsFor(Helpers.getExtension(value));
                for (Codec c : codecs) {
                    if (c.validate(value)) {
                        found = true;
                    }
                }
                if (found) {
                    palletCollection.put(Integer.parseInt(key), value);
                } else {
                    codecs = ResourceManager.getResourceManager().getMidiCodecsFor(Helpers.getExtension(value));
                    for (Codec c : codecs) {
                        if (c.validate(value)) {
                            found = true;
                        }
                    }
                    if (found) {
                        midiCollection.put(Integer.parseInt(key), value);
                    } else {

                        codecs = ResourceManager.getResourceManager().getSoundCodecsFor(Helpers.getExtension(value));
                        for (Codec c : codecs) {
                            if (c.validate(value)) {
                                found = true;

                            }
                        }
                        if (found) {
                            soundCollection.put(Integer.parseInt(key), value);
                        } else {

                            codecs = ResourceManager.getResourceManager().getFontCodecsFor(Helpers.getExtension(value));
                            for (Codec c : codecs) {
                                if (c.validate(value)) {
                                    found = true;

                                }
                            }
                            if (found) {
                                fontCollection.put(Integer.parseInt(key), value);
                                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                            }
                        }
                    }
                }
            }

        }
// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Give Control to The Manager">
        ResourceManager.getResourceManager().addGroup(this);
    // </editor-fold>

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getImageResource returns the physical location for a file
     * @param definition defined name of resource
     * @return the location of required resource.
     */
    public String getImageResource(int definition) {
        return imageCollection.get(definition);
    }

    /**
     * getImageResources returns a list of resources and their definitions.
     * @return a disposable clone of the internal resource stucture.
     */
    public Hashtable<Integer, String> getImageResources() {
        Hashtable<Integer, String> clone = new Hashtable<Integer, String>(imageCollection.size());
        Enumeration<Integer> keys = imageCollection.keys();
        while (keys.hasMoreElements()) {
            int k = keys.nextElement();
            clone.put(k, imageCollection.get(k));
        }
        return clone;
    }

    /**
     * getImages returns how many images are defined in this group
     * @return size of image list
     */
    public int getImages() {
        return imageCollection.size();
    }

    /**
     * getPalletResource returns the physical location for a file
     * @param definition defined id of resource
     * @return the location of required resource.
     */
    public String getPalletResource(int definition) {
        return palletCollection.get(definition);
    }

    /**
     * getPalletResources returns a list of resources and their definitions.
     * @return a disposable clone of the internal resource stucture.
     */
    public Hashtable<Integer, String> getPalletResources() {
        Hashtable<Integer, String> clone = new Hashtable<Integer, String>(palletCollection.size());
        Enumeration<Integer> keys = palletCollection.keys();
        while (keys.hasMoreElements()) {
            int k = keys.nextElement();
            clone.put(k, palletCollection.get(k));
        }
        return clone;
    }

    /**
     * getImages returns how many pallets are defined in this group
     * @return size of pallet list
     */
    public int getPallets() {
        return palletCollection.size();
    }

    /**
     * getMidiResource returns the physical location for a file
     * @param definition defined id of resource
     * @return the location of required resource.
     */
    public String getMidiResource(int definition) {
        return midiCollection.get(definition);
    }

    /**
     * getImageResources returns a list of resources and their definitions.
     * @return a disposable clone of the internal resource stucture.
     */
    public Hashtable<Integer, String> getMidiResources() {
        Hashtable<Integer, String> clone = new Hashtable<Integer, String>(midiCollection.size());
        Enumeration<Integer> keys = midiCollection.keys();
        while (keys.hasMoreElements()) {
            int k = keys.nextElement();
            clone.put(k, midiCollection.get(k));
        }
        return clone;
    }

    /**
     * getImages returns how many midi's are defined in this group
     * @return size of midi list
     */
    public int getMidis() {
        return midiCollection.size();
    }

    /**
     * getSoundResource returns the physical location for a file
     * @param definition defined id of resource
     * @return the location of required resource.
     */
    public String getSoundResource(int definition) {
        return soundCollection.get(definition);
    }

    /**
     * getSoundResources returns a list of resources and their definitions.
     * @return a disposable clone of the internal resource stucture.
     */
    public Hashtable<Integer, String> getSoundResources() {
        Hashtable<Integer, String> clone = new Hashtable<Integer, String>(soundCollection.size());
        Enumeration<Integer> keys = soundCollection.keys();
        while (keys.hasMoreElements()) {
            int k = keys.nextElement();
            clone.put(k, soundCollection.get(k));
        }
        return clone;
    }

    /**
     * getImages returns how many sounds are defined in this group
     * @return size of sound list
     */
    public int getSounds() {
        return soundCollection.size();
    }

    /**
     * getFontesource returns the physical location for a file
     * @param definition defined id of resource
     * @return the location of required resource.
     */
    public String getFontesource(int definition) {
        return fontCollection.get(definition);
    }

    /**
     * getFontResources returns a list of resources and their definitions.
     * @return a disposable clone of the internal resource stucture.
     */
    public Hashtable<Integer, String> getFontResources() {
        Hashtable<Integer, String> clone = new Hashtable<Integer, String>(fontCollection.size());
        Enumeration<Integer> keys = fontCollection.keys();
        while (keys.hasMoreElements()) {
            int k = keys.nextElement();
            clone.put(k, fontCollection.get(k));
        }
        return clone;
    }

    /**
     * getFonts returns how many fonts are defined in this group
     * @return size of font list
     */
    public int getFonts() {
        return fontCollection.size();
    }

    /**
     * returns the name of this set of resources or group
     * @return the name associated with this group
     */
    public String getName() {
        return name;
    }

    /**
     * setValid is set by the ResourceManager after it has vilidated locations and files
     * @param validation sets true if this group has been determined to be Valid false otherwise.
     */
    protected void setValid(boolean validation) {
        this.valid = validation;
    }

    /**
     * isValid returns weather or not the ResourceManager has validated this group of resources
     * @return true if valid false otherwise.  set to false until the resource manager has validated resources
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * isReady returns weather or not resources are ready to use
     * @return true if valid and loaded false otherwise
     */
    public boolean isReady() {
        return valid && (loaded == 100.00f);
    }

    /**
     * setLoadStatus is only called by ResourceManager to set the currently loaded state of resources.
     * @param loaded percentage of loaded itrems in this group.
     */
    protected void setLoadStatus(float loaded) {
        if (loaded == 100.00f) {
            cached = true;
        } else {
            cached = false;
        }
        this.loaded = loaded;
    }

    /**
     * Returns the load status as a fraction of 100% of loaded items in this group
     * @return percentile of loaded resources.
     */
    public float getLoadStatus() {
        return loaded;
    }

    /**
     * isCached is essentialy equal to getLoadStatus()==100.00f
     * @return true if this group is determined to be cached by the ResourceManager
     */
    public boolean isCached() {
        return cached;
    }

    /**
     * getResources returns the amount of uniqure rsouces in totality.
     * @return accumulated size of internal resouce references.
     */
    public int getResources() {
        return getImages() + getPallets() + getMidis() + getSounds() + getFonts();
    }
    // </editor-fold>    
}
