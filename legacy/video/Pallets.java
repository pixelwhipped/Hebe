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
package legacy.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import static legacy.video.Colors.*;
import legacy.Helpers;
import legacy.Common;

/**
 * Pallets.class contains a list of standard predifined pallets or swatches generaly used by
 * various systems.</br>
 * <b>WARNING</b><i> No PalletFactory contained withing this class performs any checks and will forcably
 * terminate the application if an attempt to apply a pallet to a null RGB pal or RGB pal of invalid length.</br>
 * </i><b>WARNING</b><i> During constuction a call to the PalletFactory exented by Legacy.class and is parsed an
 * pal of a predetmind size.  this pal cannot be reinitialized only altered as the VideoAdapter expects this object
 * to be used for the cration of itself.  No other pallet can be used.
 * @see legacy.Configuration
 * @author Benjamin Tarrant
 */
public class Pallets {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Common">
    /**
     * PalletFactory for classic CGA pallet
     */
    public static final PalletFactory CGA = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{BLACK, DARK_RED, DARK_GREEN, DARK_YELLOW, DARK_BLUE, DARK_MAGENTA, DARK_CYAN, LIGHT_GREY, DARK_GREY, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE}, pal);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for classic EGA pallet
     */
    public static final PalletFactory EGA = CGA;
    /**
     * PalletFactory for classic Windows 16 color pallet
     */
    public static final PalletFactory WINDOWS_16C = CGA;
    /**
     * PalletFactory for extended Windows 20 color pallet
     */
    public static final PalletFactory WINDOWS_20C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                RGB[] cl = {BLACK, DARK_RED, DARK_GREEN, DARK_YELLOW, DARK_BLUE, DARK_MAGENTA, DARK_CYAN, LIGHT_GREY, MONEY_GREEN, SKY_BLUE};
                RGB[] ch = {CREAM, MEDIUM_GREY, DARK_GREY, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE};
                Arrays.fill(pal, BLACK);
                System.arraycopy(cl, 0, pal, 0, cl.length);
                System.arraycopy(ch, 0, pal, pal.length - ch.length, ch.length);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for classic Ric 16 color pallet
     */
    public static final PalletFactory RISC_16C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{WHITE, new RGB(221, 221, 221), new RGB(187, 187, 187), new RGB(153, 153, 153), new RGB(119, 119, 119), new RGB(85, 85, 85), new RGB(51, 51, 51), BLACK, new RGB(0, 68, 153), new RGB(238, 238, 0), new RGB(0, 204, 0), new RGB(221, 0, 0), new RGB(238, 238, 187), new RGB(85, 136, 0), new RGB(255, 187, 0), new RGB(0, 187, 255)}, pal);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for standard Teletext 8 color pallet
     */
    public static final PalletFactory TELETEXT = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{BLACK, BLUE, GREEN, CYAN, RED, MAGENTA, YELLOW, WHITE}, pal);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for classic Apple II 16 color pallet
     */
    public static final PalletFactory APPLEII_16C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{BLACK, new RGB(114, 38, 64), new RGB(64, 51, 127), new RGB(228, 52, 254), new RGB(14, 89, 64), DARK_GREY, new RGB(27, 154, 255), new RGB(191, 179, 255), new RGB(64, 76, 0), new RGB(228, 101, 1), DARK_GREY, new RGB(241, 166, 191), new RGB(27, 203, 1), new RGB(191, 204, 128), new RGB(141, 217, 191), WHITE}, pal);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for classic Vic20  16 color pallet
     */
    public static final PalletFactory VIC20_16C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{BLACK, WHITE, new RGB(141, 62, 55), new RGB(114, 193, 200), new RGB(128, 52, 139), new RGB(85, 160, 73), new RGB(64, 49, 141), new RGB(170, 185, 93), new RGB(139, 84, 41), new RGB(213, 159, 116), new RGB(184, 105, 98), new RGB(135, 214, 221), new RGB(170, 95, 182), new RGB(148, 224, 137), new RGB(128, 113, 204), new RGB(191, 206, 114)}, pal);

            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for classic Commodore64 16 color pallet
     */
    public static final PalletFactory C64_16C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                Helpers.getRGBArray().insert(new RGB[]{BLACK, WHITE, new RGB(136, 57, 50), new RGB(103, 182, 189), new RGB(139, 63, 150), new RGB(85, 160, 73), new RGB(64, 49, 141), new RGB(191, 206, 114), new RGB(139, 84, 41), new RGB(87, 66, 0), new RGB(184, 105, 98), new RGB(80, 80, 80), new RGB(120, 120, 120), new RGB(148, 224, 137), new RGB(120, 105, 196), new RGB(159, 159, 159)}, pal);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for standard 256 greyscale color pallet
     */
    public static final PalletFactory GREYSCALE_256C = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                for (int i = 0; i < 255; i++) {
                    pal[i] = new RGB(i, i, i);
                }
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for standard Web Safe 216 color pallet
     */
    public static final PalletFactory WEB_SAFE = new PalletFactory() {

        public void createPallet(RGB[] pal) {
            try {
                int i = 0;
                for (int r = 255; r > 0; r -= 51) {
                    for (int g = 255; g > 0; g -= 51) {
                        for (int b = 255; b > 0; b -= 51) {
                            pal[i] = new RGB(r, g, b);
                            i++;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    /**
     * PalletFactory for custom 256 color pallet
     */
    public static final PalletFactory DEFAULT_PALLET = new PalletFactory() {

        public void createPallet(RGB[] pal) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("legacy/video/pallets/default.pal")));
                reader.readLine();
                StringTokenizer tokens;
                for (int i = 0; i < pal.length; i++) {
                    tokens = new StringTokenizer(reader.readLine());
                    pal[i] = new RGB(Integer.parseInt(tokens.nextToken()),
                            Integer.parseInt(tokens.nextToken()),
                            Integer.parseInt(tokens.nextToken()));
                }
                reader.close();
            } catch (IOException IOE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOE);
            } catch (IndexOutOfBoundsException IOOBE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), IOOBE);
            } catch (NullPointerException NPE) {
                throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), NPE);
            }
        }
    };
    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * reduce method is the internal kulling loop for the reducePallet method.
     * @todo overhaul
     * @param pal pallet to reduce
     * @return the ruslling pallet after kulling.
     */
    private static final RGB[] reduce(RGB[] pal) {
        RGB ret[] = new RGB[pal.length - 1];
        int cx = 0;
        int cy = 1;
        int y = 0;
        int cd = getDistance(pal[cx], pal[cy]);
        if (cd != 0) {
            for (int x = y; x < pal.length - 1; x++) {
                for (y = x; y < pal.length; y++) {
                    if (x != y) {
                        if (pal[x] == pal[y]) {
                            for (int i = 0; i < x; i++) {
                                ret[i] = pal[i];
                            }
                            for (int i = x + 1; i < y; i++) {
                                ret[i - 1] = pal[i];
                            }
                            for (int i = y + 1; i < pal.length; i++) {
                                ret[i - 2] = pal[i];
                            }
                            ret[ret.length - 1] = pal[x];
                            return ret;
                        } else if (cd > getDistance(pal[x], pal[y])) {
                            cx = x;
                            cy = y;
                            cd = getDistance(pal[cx], pal[cy]);
                        }
                    }
                }
            }
            for (int i = 0; i < cx; i++) {
                ret[i] = pal[i];
            }
            for (int i = cx + 1; i < cy; i++) {
                ret[i - 1] = pal[i];
            }
            for (int i = cy + 1; i < pal.length; i++) {
                ret[i - 2] = pal[i];
            }
            ret[ret.length - 1] = new RGB((pal[cx].r + pal[cy].r) >> 1,
                    (pal[cx].g + pal[cy].g) >> 1,
                    (pal[cx].b + pal[cy].b) >> 1);
            return ret;
        } else {
            ret[0] = pal[0];
            for (int i = 1; i < ret.length; i++) {
                ret[i] = pal[i + 1];
            }
            return ret;
        }

    }

    /**
     * reducePallet reduces an array of colors to fit a destination array of a given size.</br>
     * <b>Note</b><i> very costly to use continually reduced an arry by 1 until the desired size is met.</br>
     * this calls a mehod wich will find first mathces and the closest in wich case it will interpolate the results.</i>
     * @param source array to reduce
     * @param dest out put array
     */
    public static final void reducePallet(RGB[] source, RGB dest[]) {
        if (source.length > dest.length) {
            RGB ret[] = new RGB[source.length - 1];
            while (ret.length != dest.length) {
                ret = reduce(ret);
            }
            for (int i = 0; i < dest.length; i++) {
                dest[i] = ret[i];
            }
        } else if (source.length == dest.length) {
            for (int i = 0; i < source.length; i++) {
                dest[i] = source[i];
            }
        } else {
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_DATA_FAILURE), new java.lang.ArrayStoreException());
        }

    }
    // </editor-fold>
}
