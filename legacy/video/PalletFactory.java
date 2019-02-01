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
 * PalletFactory.class is an interface and used for defining pallets.
 * Pallets should but are not limited to the the maximum possible colors of
 * a specific display adaptor without exceeding 256 colors.
 * exceeding this limit may cause undisired effects as orperated within the kernal 
 * rely on using a pallet which has been defined and limits use to original dimensions.
 * This is because the Hêbê project is designed to emulate classical games which
 * usualy operated on a fixed pallet.</br>
 * <b>Note</b><i> pallets should be resticted to arrays of 2,4,16,64,256 colors.
 * @author Benjamin Tarrant
 */
public interface PalletFactory {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * createPallet to allow user to define a pallet. This pallet will be defined by size
     * and checked by the Video Adapter befor initialization of the Graphics object.
     * @param pal array pre determinied to be defined.
     */
    public void createPallet(RGB[] pal) ;
    // </editor-fold>    
}
