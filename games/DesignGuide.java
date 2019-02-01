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
package games;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import legacy.Configuration;
import legacy.Legacy;
import legacy.VideoAdapter;
import legacy.resource.ResourceGroup;
import legacy.resource.ResourceManager;
import legacy.video.Pallets;
import legacy.video.RGB;

/**
 * DesignGuide.class is used during creation of the Hêbê to ensure that a satifactory
 * archetecture is created.
 * @author Benjamin Tarrant
 */
public  class DesignGuide extends Legacy {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    ResourceManager manager;
    ResourceGroup mainResources;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Only required constructor to instanciate the Hêbê(legacy) engine
     * @param args argument to handle and parse on to configuration methods
     */
    public DesignGuide(String[] args) {
        super(args);        
        synchronized(getConstructionLock()){
            Properties resources = new Properties();
            resources.setProperty("1", "file://d:/Legacy/Resources/cursorup.gif");
            List<String> list = new ArrayList<String>();
            list.add("Mouse");
            
            mainResources = new ResourceGroup("Mouse",resources);            
            manager = ResourceManager.getResourceManager();            
            manager.setResources(list);   
            
            //Do anything here that may be required for use before render(VideoAdapter v) or updateGame(long gameTime) is called
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * render(Video v) handles all rendering
     * @see legacy.Legacy#getConfiguration(java.lang.String[])
     * @see legacy.Configuration
     * @param v Video output class
     */
    public void render(VideoAdapter v) {

    }

    /**
     * updateGame(long gameTime) is called to allow the game to update its state
     * @param gameTime time in Nano Seconds since last call
     * @return true to request to be recalled false otherwize
     */
    public boolean updateGame(long gameTime) {
        return true;
    }

    /**
     * getConfiguration() is called once and is used to prepair the enviroment
     * @see legacy.Configuration
     * @param args arguments eventualy parsed from super(args) construction
     * @return Configuration to be used.
     */
    public Configuration getConfiguration(String[] args) {
        final VideoAdapter adaptor = VideoAdapter.getStandardFor(320, 200);
        if (adaptor != null) {
            return new Configuration() {

                /**
                 * getVideoAdaptor() returns the required video adaptor.
                 * @return desired video adaptor
                 */
                public VideoAdapter getVideoAdaptor() {
                    return adaptor; //Standard CGA [8:5]
                }

                /**
                 * This must be set to allow the proceeding createPallet(RGB[] pal) method to be correctly called
                 * and applied to the active VideaAdapter</br>
                 * @return the memory model defined in VideoAdapter
                 */
                public Object getVideoMemoryModel() {
                    return VideoAdapter.MemoryModelPalletized8Bit;  //ModeX or 13H vidoe mode note pallet colors are triplets of 255 not 63 in classical systems
                }

                /**
                 * getFrameRate() return the disird framerate or frames per second to lock.</br>
                 * @see legacy.VideoAdapter
                 * @return desired frame rate
                 */
                public int getFrameRate() {
                    return 30;  //Respectale framerate fo classic ModeX game or 13H
                }

                /**
                 * createPallet is used to apply the required pallet for use.
                 * @param pal array pre determinied to be defined.
                 */
                public void createPallet(RGB[] pal) {
                    Pallets.DEFAULT_PALLET.createPallet(pal);   //Use the default Pallet
                }
            };
        }
        return null;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    /**
     * Application entry point.  This methods main method acts as a test bed
     * for the Hêbê system and creates the DesignGuide class which extends the Legacy class.
     * @param args argments to be handled by DesignGuide(String[] args)
     */
    public static void main(String[] args) {
        new DesignGuide(args);      //Begin
    }
    // </editor-fold>    
}
