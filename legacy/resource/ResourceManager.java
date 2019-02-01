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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import legacy.Annotations.Restricted;
import legacy.Common;
import legacy.Helpers;
import legacy.resource.codecs.font.NativeFontFormat;
import legacy.resource.codecs.image.NativeImageFormat;
import legacy.resource.codecs.midi.NativeMidiFormat;
import legacy.resource.codecs.pallet.NativePalletFormat;
import legacy.resource.codecs.sound.NativeSoundFormat;

/**
 * ResourceManager.class 
 * @author Benjamin Tarrant
 */
public class ResourceManager {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    /**
     * Allows for access to otherwise in accessable methods
     */
    private static VideoCodecToolkit videoToolkit = null;    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    /**
     * Singleton instance for this class
     */
    private static ResourceManager manager = null;
    /**
     * container for groups of resources.
     */
    private Hashtable<String, ResourceGroup> resourceGroups = null;
    /**
     * Internal locking object used to prevent execution until a synchronized block has executed
     */
    private static final Object RESOURCE_LOAD_LOCK = new Object();
    /**
     * representation of the fraction of resources prepaired for use.
     */
    private float loaded = 0;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Resource manager is private and only allows for one instance to be created via getResourceManager
     * @see ResourceManager#getResourceManager() 
     */
    private ResourceManager() {
        resourceGroups = new Hashtable<String, ResourceGroup>();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getResourceManager allows for acces to a singlular instance or singlton ResourceManage
     * @return the usable ResourceManager
     */
    public static final ResourceManager getResourceManager() {
        if (manager == null) {
            return new ResourceManager();
        }
        return manager;
    }

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
    public void installCodecs(Properties codex) {
        CodecLibrary.installCodecs(codex);
    }

    /**
     * setVideoCodecToolkit(VideoCodecToolkit) applies a toolkit to make availible for classes which may
     * not in general have acccess to it.
     * @param toolkit Video Adapter Toolit
     */
    @Restricted
    public static void setVideoCodecToolkit(VideoCodecToolkit toolkit) {
        try {
            Class t = Class.forName(Helpers.getCallerClass(1));
            while (!t.getName().equals("java.lang.Object")) {
                if (t.getName().equals("legacy.VideoAdapter")) {
                    videoToolkit = toolkit;
                }
                t = t.getSuperclass();
            }
        } catch (ClassNotFoundException CNFE) {
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_ILLEGAL_USAGE));
        }
    }

    /**
     * getVideoCodecToolkit() allows access to the initialized VideoAdapter for codec use</br>
     * <b>Note</b><i> has been left not protected or not Restricted as access to functions as yet have not been deemed detrimental
     * to use by any class as should always be set before access to can be called except for instances where an attempt to 
     * access has been made from a legacy.Legacy.getConfiguration() call.
     * @return the toolkit
     */
    public static VideoCodecToolkit getVideoCodecToolkit() {
        return videoToolkit;
    }

    /**
     * addGroup is called from inside the ResourceGroup class to additself to the singleton ResourceManager</br>
     * <b>Warning</b><i> this will not allow groups of the same name to be added.</i>
     * @param group the group to add.
     */
    protected void addGroup(ResourceGroup group) {
        if (!resourceGroups.containsKey(group.getName())) {
            System.out.println("Adding " + group.getName());
            if (validateGroup(group)) {
                resourceGroups.put(group.getName(), group);
            } else {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
            }
        }
    }

    /**
     * validateGroup tests if a group of resources is valid and compatible.
     * @param group the group to validate
     * @return true on validation false otherwise;
     */
    private boolean validateGroup(ResourceGroup group) {
        // <editor-fold defaultstate="collapsed" desc="Image Validation">
        Hashtable<Integer, String> res = group.getImageResources();
        Enumeration<Integer> keys = res.keys();
        while (keys.hasMoreElements()) {
            String element = res.get(keys.nextElement());
            String extension = Helpers.getExtension(element);
            boolean valid = false;
            Codec[] codecs = getImageCodecsFor(extension);
            for (Codec codex : codecs) {
                if (codex.validate(element)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Pallet Validation">
        res = group.getPalletResources();
        keys = res.keys();
        while (keys.hasMoreElements()) {
            String element = res.get(keys.nextElement());
            String extension = Helpers.getExtension(element);
            boolean valid = false;
            Codec[] codecs = getPalletCodecsFor(extension);
            for (Codec codex : codecs) {
                if (codex != null) {
                    if (codex.validate(element)) {
                        valid = true;
                    }
                }
            }
            if (!valid) {
                return false;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Midi Validation">
        res = group.getMidiResources();
        keys = res.keys();
        while (keys.hasMoreElements()) {
            String element = res.get(keys.nextElement());
            String extension = Helpers.getExtension(element);
            boolean valid = false;
            Codec[] codecs = getMidiCodecsFor(extension);
            for (Codec codex : codecs) {
                if (codex.validate(element)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sound Validation">
        res = group.getSoundResources();
        keys = res.keys();
        while (keys.hasMoreElements()) {
            String element = res.get(keys.nextElement());
            String extension = Helpers.getExtension(element);
            boolean valid = false;
            Codec[] codecs = getSoundCodecsFor(extension);
            for (Codec codex : codecs) {
                if (codex.validate(element)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Font Validation">
        res = group.getFontResources();
        keys = res.keys();
        while (keys.hasMoreElements()) {
            String element = res.get(keys.nextElement());
            String extension = Helpers.getExtension(element);
            boolean valid = false;
            Codec[] codecs = getFontCodecsFor(extension);
            for (Codec codex : codecs) {
                if (codex.validate(element)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        // </editor-fold>

        group.setValid(true);
        return true;
    }

    /**
     * setResources(List<String>) defines the resource groups required for use.</br>
     * <b>Note</b><i> set is used as resources may already be loadedSize and tells the manager that
     * these resources will be required.
     * @param groups to mark as required and load.
     */
    public void setResources(final List<String> groups) {
        // <editor-fold defaultstate="collapsed" desc="Thead Loading to allow other tasks to operate">
        new Thread(new Runnable() {

            public void run() {

                // <editor-fold defaultstate="collapsed" desc="Block additional Loading Operations">
                synchronized (RESOURCE_LOAD_LOCK) {
                    // <editor-fold defaultstate="collapsed" desc="Load Management Variables">
                    int loadedSize = 0;
                    int bundleSize = 0;
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc="Clean up Memory">
                    Enumeration<String> grouplist = resourceGroups.keys();
                    while (grouplist.hasMoreElements()) {
                        ResourceGroup group = resourceGroups.get(grouplist.nextElement());
                        System.out.println("grp name "+group.getName());
                        if (group != null) {
                            if (!groups.contains(group.getName())) {
                                if (group.isCached()) {
                                    group.images.clear();
                                    group.images = null;
                                    group.pallets.clear();
                                    group.pallets = null;
                                    group.midis.clear();
                                    group.midis = null;
                                    group.sounds.clear();
                                    group.sounds = null;
                                    group.fonts.clear();
                                    group.fonts = null;
                                    System.gc();
                                    group.images = new Hashtable<Integer, NativeImageFormat>();
                                    group.pallets = new Hashtable<Integer, NativePalletFormat>();
                                    group.midis = new Hashtable<Integer, NativeMidiFormat>();
                                    group.sounds = new Hashtable<Integer, NativeSoundFormat>();
                                    group.fonts = new Hashtable<Integer, NativeFontFormat>();
                                }
                            }
                        }
                    }
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc="Determin new Resource Bundle size">
                    for (String s : groups) {
                        ResourceGroup group = resourceGroups.get(s);
                        if (group != null) {
                            bundleSize += group.getResources();
                        }
                    }
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc="Load required unchached resources and update load status">
                    for (String s : groups) {                       
                        ResourceGroup group = resourceGroups.get(s);
                        if (group != null) {
                            if (!group.isCached()) {
                                int size = group.getResources();
                                int currentSize = 0;
                                Hashtable<Integer, String> imageCollection = group.getImageResources();
                                Hashtable<Integer, String> palletCollection = group.getPalletResources();
                                Hashtable<Integer, String> midiCollection = group.getMidiResources();
                                Hashtable<Integer, String> soundCollection = group.getSoundResources();
                                Hashtable<Integer, String> fontCollection = group.getFontResources();

                                try {

                                    // <editor-fold defaultstate="collapsed" desc="Image Loading">                                    
                                    Enumeration<Integer> keys = imageCollection.keys();
                                    while (keys.hasMoreElements()) {
                                        Integer key = keys.nextElement();
                                        String element = soundCollection.get(key);
                                        String extension = Helpers.getExtension(element);
                                        boolean valid = false;
                                        Codec[] codecs = getImageCodecsFor(extension);
                                        Codec requiredCodec = null;
                                        for (Codec codex : codecs) {
                                            if (codex.validate(element)) {
                                                valid = true;
                                                requiredCodec = codex;
                                            }
                                        }
                                        if (valid) {
                                            group.images.put(key, new NativeImageFormat(requiredCodec.decodeNative(Helpers.getInputStreamProtocol(element))));
                                            currentSize++;
                                            loadedSize++;
                                            loaded = bundleSize / loadedSize;
                                            group.setLoadStatus(size / currentSize);
                                        } else {
                                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                        }
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Pallet Loading">
                                    keys = palletCollection.keys();
                                    while (keys.hasMoreElements()) {
                                        Integer key = keys.nextElement();
                                        String element = soundCollection.get(key);
                                        String extension = Helpers.getExtension(element);
                                        boolean valid = false;
                                        Codec[] codecs = getPalletCodecsFor(extension);
                                        Codec requiredCodec = null;
                                        for (Codec codex : codecs) {
                                            if (codex.validate(element)) {
                                                valid = true;
                                                requiredCodec = codex;
                                            }
                                        }
                                        if (valid) {
                                            group.pallets.put(key, new NativePalletFormat(requiredCodec.decodeNative(Helpers.getInputStreamProtocol(element))));
                                            currentSize++;
                                            loadedSize++;
                                            loaded = bundleSize / loadedSize;
                                            group.setLoadStatus(size / currentSize);
                                        } else {
                                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                        }
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Midi Loading">
                                    keys = midiCollection.keys();
                                    while (keys.hasMoreElements()) {
                                        Integer key = keys.nextElement();
                                        String element = soundCollection.get(key);
                                        String extension = Helpers.getExtension(element);
                                        boolean valid = false;
                                        Codec[] codecs = getMidiCodecsFor(extension);
                                        Codec requiredCodec = null;
                                        for (Codec codex : codecs) {
                                            if (codex.validate(element)) {
                                                valid = true;
                                                requiredCodec = codex;
                                            }
                                        }
                                        if (valid) {
                                            group.midis.put(key, new NativeMidiFormat(requiredCodec.decodeNative(Helpers.getInputStreamProtocol(element))));
                                            currentSize++;
                                            loadedSize++;
                                            loaded = bundleSize / loadedSize;
                                            group.setLoadStatus(size / currentSize);
                                        } else {
                                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                        }
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Sound Loading">
                                    keys = soundCollection.keys();
                                    while (keys.hasMoreElements()) {
                                        Integer key = keys.nextElement();
                                        String element = soundCollection.get(key);
                                        String extension = Helpers.getExtension(element);
                                        boolean valid = false;
                                        Codec[] codecs = getSoundCodecsFor(extension);
                                        Codec requiredCodec = null;
                                        for (Codec codex : codecs) {
                                            if (codex.validate(element)) {
                                                valid = true;
                                                requiredCodec = codex;
                                            }
                                        }
                                        if (valid) {
                                            group.sounds.put(key, new NativeSoundFormat(requiredCodec.decodeNative(Helpers.getInputStreamProtocol(element))));
                                            currentSize++;
                                            loadedSize++;
                                            loaded = bundleSize / loadedSize;
                                            group.setLoadStatus(size / currentSize);
                                        } else {
                                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                        }
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Font Loading">
                                    keys = fontCollection.keys();
                                    while (keys.hasMoreElements()) {
                                        Integer key = keys.nextElement();
                                        String element = soundCollection.get(key);
                                        String extension = Helpers.getExtension(element);
                                        boolean valid = false;
                                        Codec[] codecs = getFontCodecsFor(extension);
                                        Codec requiredCodec = null;
                                        for (Codec codex : codecs) {
                                            if (codex.validate(element)) {
                                                valid = true;
                                                requiredCodec = codex;
                                            }
                                        }
                                        if (valid) {
                                            group.fonts.put(key, new NativeFontFormat(requiredCodec.decodeNative(Helpers.getInputStreamProtocol(element))));
                                            currentSize++;
                                            loadedSize++;
                                            loaded = bundleSize / loadedSize;
                                            group.setLoadStatus(size / currentSize);
                                        } else {
                                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                        }
                                    }
                                // </editor-fold>
                                } catch (IOException IOE) {
                                    throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                                }

                            } else {
                                loadedSize += group.getResources();
                                loaded = bundleSize / loadedSize;
                            }
                        } else {
                            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE));
                        }
                    }
                    Format.getFormatLoadLock(); //await any latent loads.
                // </editor-fold>
                }
            // </editor-fold>
            }
        }).start();
    // </editor-fold>
    }

    /**
     * getAllResources() simply returns all known resource groups.
     * @return a list of known resource groups
     */
    public List<String> getAllResources() {
        List<String> list = new ArrayList<String>();
        Enumeration<String> groups = resourceGroups.keys();
        while (groups.hasMoreElements()) {
            list.add(groups.nextElement());
        }
        return list;
    }

    /**
     * getChachedResources() simply returns all chached or ready resource groups.
     * @return a list of chached or ready  resource groups
     */
    public List<String> getChachedResources() {
        List<String> list = new ArrayList<String>();
        Enumeration<String> groups = resourceGroups.keys();
        while (groups.hasMoreElements()) {
            String key = groups.nextElement();
            if (resourceGroups.get(key).isCached()) {
                list.add(key);
            }
        }
        return list;
    }

    /**
     * isReady returns weather or not required resources are ready to use
     * @return true if valid and loadedSize false otherwise
     */
    public boolean isReady() {
        return loaded == 100.00f;
    }

    /**
     * Returns the load status as a fraction of 100% of loadedSize items in required group
     * @return percentile of loadedSize resources.
     */
    public float getLoadStatus() {
        return loaded;
    }

    /**
     * getImageCodecsFor(String ext) returns a list of codecs which may be associated with this extension
     * @param ext to find codecs for
     * @return a list of codecs.
     */
    public Codec[] getImageCodecsFor(String ext) {
        Vector<Codec> codex = new Vector<Codec>();
        Codec[] codecs = getImageCodecs();
        for (Codec c : codecs) {
            if (c.getExtension().equalsIgnoreCase(ext)) {
                codex.add(c);
            }
        }
        Codec[] usables = new Codec[codex.size() + 1];
        int cdx = 0;
        for (Codec c : codecs) {
            usables[cdx++] = c;
        }

        return usables;
    }

    /**
     * getImageCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public Codec[] getImageCodecs() {
        return CodecLibrary.getImageCodecs();
    }

    /**
     * getPalletCodecsFor(String ext) returns a list of codecs which may be associated with this extension
     * @param ext to find codecs for
     * @return a list of codecs.
     */
    public Codec[] getPalletCodecsFor(String ext) {
        Vector<Codec> codex = new Vector<Codec>();
        Codec[] codecs = getPalletCodecs();
        for (Codec c : codecs) {
            if (c.getExtension().equalsIgnoreCase(ext)) {
                codex.add(c);
            }
        }
        Codec[] usables = new Codec[codex.size() + 1];
        int cdx = 0;
        for (Codec c : codecs) {
            usables[cdx++] = c;
        }

        return usables;
    }

    /**
     * getPalletCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public Codec[] getPalletCodecs() {
        return CodecLibrary.getPalletCodecs();
    }

    /**
     * getMidiCodecsFor(String ext) returns a list of codecs which may be associated with this extension
     * @param ext to find codecs for
     * @return a list of codecs.
     */
    public Codec[] getMidiCodecsFor(String ext) {
        Vector<Codec> codex = new Vector<Codec>();
        Codec[] codecs = getMidiCodecs();
        for (Codec c : codecs) {
            if (c.getExtension().equalsIgnoreCase(ext)) {
                codex.add(c);
            }
        }
        Codec[] usables = new Codec[codex.size() + 1];
        int cdx = 0;
        for (Codec c : codecs) {
            usables[cdx++] = c;
        }

        return usables;
    }

    /**
     * getMidiCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public Codec[] getMidiCodecs() {
        return CodecLibrary.getMidiCodecs();
    }

    /**
     * getSoundCodecsFor(String ext) returns a list of codecs which may be associated with this extension
     * @param ext to find codecs for
     * @return a list of codecs.
     */
    public Codec[] getSoundCodecsFor(String ext) {
        Vector<Codec> codex = new Vector<Codec>();
        Codec[] codecs = getSoundCodecs();
        for (Codec c : codecs) {
            if (c.getExtension().equalsIgnoreCase(ext)) {
                codex.add(c);
            }
        }
        Codec[] usables = new Codec[codex.size() + 1];
        int cdx = 0;
        for (Codec c : codecs) {
            usables[cdx++] = c;
        }

        return usables;
    }

    /**
     * getSoundCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public Codec[] getSoundCodecs() {
        return CodecLibrary.getSoundCodecs();
    }

    /**
     * getFontCodecsFor(String ext) returns a list of codecs which may be associated with this extension
     * @param ext to find codecs for
     * @return a list of codecs.
     */
    public Codec[] getFontCodecsFor(String ext) {
        Vector<Codec> codex = new Vector<Codec>();
        Codec[] codecs = getFontCodecs();
        for (Codec c : codecs) {
            if (c.getExtension().equalsIgnoreCase(ext)) {
                codex.add(c);
            }
        }
        Codec[] usables = new Codec[codex.size() + 1];
        int cdx = 0;
        for (Codec c : codecs) {
            usables[cdx++] = c;
        }

        return usables;
    }

    /**
     * getFontCodecsFor(String ext) returns a list of codecs for this type
     * @return a list of codecs.
     */
    public Codec[] getFontCodecs() {
        return CodecLibrary.getFontCodecs();
    }    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    // </editor-fold>    
}
