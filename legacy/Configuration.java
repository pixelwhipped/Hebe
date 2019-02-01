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
package legacy;

import legacy.video.PalletFactory;
import legacy.video.RGB;

/**
 * Configuration.class Allows the user define the enviroment it wishes to run in.
 * @author Benjamin Tarrant
 */
public abstract class Configuration implements PalletFactory {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">    

    /**
     * getVideoAdaptor() should return the required video adaptor.
     * @return desired video adaptor
     */
    public abstract VideoAdapter getVideoAdaptor();

    /**
     * getFrameRate() return the disird framerate or frames per second to lock.</br>
     * <b>Recommend</b><i> viewing description in VideoAdapter.class.</i>
     * @see legacy.VideoAdapter
     * @return desired frame rate
     */
    public abstract int getFrameRate();

    /**
     * This must be set to allow the proceeding createPallet(RGB[] pal) method to be correctly called
     * and applied to the active VideaAdapter</br>
     * example </br><pre>                   
     * public Object getVideoMemoryModel(){
     *      return adaptor.MemoryModelPalletized1Bit;
     * }
     * </pre>
     * @return the memory model defined in VideoAdapter
     */
    public abstract Object getVideoMemoryModel();

    /**
     * createPallet to allow user to define a pallet. This pallet will be defined by size
     * and checked by the Video Adapter befor initialization of the Graphics object.</br>
     * <b>Correct usage</b><pre>
     * public void createPallet(RGB[] pal){
     *    Pallets.GREYSCALE_256C.createPallet(pal);
     *    pal[1] = Colors.MONEY_GREEN; 
     * }
     * </pre></br>
     * <b>Incorrect usage</b><pre>
     * public void createPallet(RGB[] pal){
     *    pal = new RGB[256];   //Illegal cannot change object!!!
     *    Pallets.GREYSCALE_256C.createPallet(pal);
     *    pal[1] = Colors.MONEY_GREEN; 
     * }
     * </pre>
     * @param pal array pre determinied to be defined.
     */
    public abstract void createPallet(RGB[] pal);
    // </editor-fold> 
}
