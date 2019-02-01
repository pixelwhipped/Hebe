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

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javax.swing.JFrame;
import legacy.Annotations.Accelerated;
import legacy.resource.ResourceManager;
import legacy.resource.VideoCodecToolkit;
import legacy.video.Colors;
import legacy.video.RGB;

/**
 * VideoAdapter.class 
 * 
 * VideoAdapter handles graphics and graphics device functions and prepiration.
 * Hêbê is deigned to allow operation comparible to classical games and operates 
 * on a maximin bit resolution of 8Bits or 256 Colors.  
 * This mode operates on initial tests at over 40% faster than a 32bit resolution.
 * in fact initial tests clocked a simple test at opertating at a peak of 1288fps
 * (frames per second). Considering this one should have ample power to rebith 
 * classic titles on the java platform.
 * <b>Note.</b> refresh rates are set to operate at 60Htz by new standard models 
 * this would be considerd 60p(Progresive) not 60i(interlaced) or 30P(Progresive)
 * and equates to 60fps so reaching this rate would be running in excess of 
 * the actual hardwares capabilites and not recomended. In addition 
 * Verticle Syncronization (VSync) is not adheard to so operating at this limit will
 * produce Atrifacts.
 * @see legacy.video.RGB
 * @author Benjamin Tarrant
 */
public abstract class VideoAdapter implements VideoCodecToolkit{

    // <editor-fold defaultstate="collapsed" desc="Variables">    
    // <editor-fold defaultstate="collapsed" desc="Common">  
    private static boolean isActive = false;
    //activePage
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
    /**
     * ratio for this VideoAdapter
     */
    private float ratio;
    /**
     * standardized name for this VideoAdapter
     */
    private String standard;
    /**
     * Singular operations for constructor VideoAdapter();
     */
    private static boolean registered = false;
    /**
     * List of known standards
     */
    private static List<VideoAdapter> standards = new ArrayList<VideoAdapter>();
    /**
     * List of compatible standards
     */
    private static List<VideoAdapter> compatible = new ArrayList<VideoAdapter>();
    // <editor-fold defaultstate="collapsed" desc="Build Internal Standards Reference">
    /**
     * Video standards availible for use if compatible
     */
    

    static {
        standards.add(blankAdapter(320, 200, "CGA [8:5]"));
        standards.add(blankAdapter(320, 240, "QVGA [4:3]"));
        standards.add(blankAdapter(640, 480, "VGA [4:3]"));
        standards.add(blankAdapter(720, 480, "NTSC [4:2]"));
        standards.add(blankAdapter(768, 576, "PAL [4:3]"));
        standards.add(blankAdapter(800, 480, "WVGA [5:3]"));
        standards.add(blankAdapter(852, 480, "HD480 [71:40]"));
        standards.add(blankAdapter(854, 480, "WVGA [16:9]"));
        standards.add(blankAdapter(1024, 600, "WSVGA [128:75]"));
        standards.add(blankAdapter(1024, 768, "XGA [4:3]"));
        standards.add(blankAdapter(1280, 720, "HD720 [16:9]"));
        standards.add(blankAdapter(1280, 768, "WXGA [5:3]"));
        standards.add(blankAdapter(1280, 800, "WXGA [8:5]"));
        standards.add(blankAdapter(1280, 1024, "SXGA [5:4]"));
        standards.add(blankAdapter(1400, 1050, "SXGA+ [4:3]"));
        standards.add(blankAdapter(1600, 1200, "UGA [4:3]"));
        standards.add(blankAdapter(1680, 1050, "WSXGA+ [8:5]"));
        standards.add(blankAdapter(1920, 1080, "HD1080 [16:9]"));
        standards.add(blankAdapter(1920, 1200, "WUXGA [8:5]"));
        standards.add(blankAdapter(2048, 1080, "2K [17:9]"));
        standards.add(blankAdapter(2048, 1536, "QXGA [4:3]"));
        standards.add(blankAdapter(2560, 2048, "QSXGA [5:4]"));
    }
    // </editor-fold>
    /**
     * Internal Pallet ColorModel calss for MemoryImageSource references.
     */
    private static class PalletModel extends ColorModel implements Transparency {

        /**
         * Reference structure;
         */
        @Accelerated("ByteCode")
        public RGB[] pal;

        /**
         * Default constructor required for implementation.</br>
         * <b>Note</b><i> theoreticly redundant.</i>
         * @param pixel_bits   Requireds futher reasearch for optimization
         * @param bits bits per pixel shuld always be 8
         * @param cspace   Requireds futher reasearch for optimization
         * @param hasAlpha Requireds futher reasearch for optimization
         * @param isAlphaPremultiplied Requireds futher reasearch for optimization
         * @param transparency Requireds futher reasearch for optimization
         * @param transferType Requireds futher reasearch for optimization
         */
        /**
         * Contrutor prepairs the PalletModel for use
         * @param pal Pallet reference.
         */
        public PalletModel(RGB[] pal) {
            super(8);
            this.pal = java.util.Arrays.copyOf(pal, pal.length);
            pal = null;
        }

        /**
         * return the Red components at pallet index
         * @param pixel index of color
         * @return the required component
         */
        @Override
        public final int getRed(int pixel) {
            return pal[pixel].r;
        }

        /**
         * return the Green components at pallet index
         * @param pixel index of color
         * @return the required component
         */
        @Override
        public final int getGreen(int pixel) {
            return pal[pixel].g;
        }

        /**
         * return the Blue components at pallet index
         * @param pixel index of color
         * @return the required component
         */
        @Override
        public final int getBlue(int pixel) {
            return pal[pixel].b;
        }

        /**
         * return the Alpha components at pallet index
         * @param pixel index of color
         * @return the required component
         */
        @Override
        public final int getAlpha(int pixel) {
            return pal[pixel].a;
        }

        /**
         * return the components at pallet index
         * @param pixel index of color
         * @return the required components as 0xAARRGGBB
         */
        @Override
        public final int getRGB(int pixel) {
            return pal[pixel].value;
        }

        /**
         * return the transluceny mask
         * @see java.awt.Transparency
         * @return opaque
         */
        @Override
        public final int getTransparency() {
            return Transparency.OPAQUE;
        }

        /**
         * Returns the closest color in the existing pallet
         * @param rgb packed color as 0xAARRGGBB
         * @return the index in this pallet of the closest color
         */
        public final int getClosetColor(int rgb) {
            return getClosetColor(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), ((rgb >> 0) & 0xff));
        }

        /**
         * Returns the closest color in the existing pallet
         * @param rgb the RGB triplet to match
         * @return the index in this pallet of the closest color
         */
        public final int getClosetColor(RGB rgb) {
            return getClosetColor(rgb.r, rgb.g, rgb.b);
        }

        /**
         * Returns the closest color in the existing pallet
         * @param r Red component to match
         * @param g Green component to match
         * @param b Blue component to match
         * @return the index in this pallet of the closest color
         */
        public final int getClosetColor(int r, int g, int b) {
            RGB ColorMatch = Colors.BLACK;
            int indexMatch = 0;

            int LeastDistance = 0x7fffffff;

            for (int i = 0; i < pal.length; i++) {
                RGB PaletteColor = pal[i];

                int RedDistance = PaletteColor.r - r;
                int GreenDistance = PaletteColor.g - g;
                int BlueDistance = PaletteColor.b - b;

                int Distance = (RedDistance * RedDistance) +
                        (GreenDistance * GreenDistance) +
                        (BlueDistance * BlueDistance);

                if (Distance < LeastDistance) {
                    indexMatch = i;
                    ColorMatch = PaletteColor;
                    LeastDistance = Distance;

                    if (Distance == 0) {
                        return i;
                    //return PaletteColor;
                    }
                }
            }
            return indexMatch;
        //return ColorMatch;
        }

        /**
         * getSize() simply returns the size of the Pallet
         * @return the pallet size
         */
        public int getSize() {
            return pal.length;
        }

        /**
         * setPallet(int, RGB) sets a color in this pallet at index to a specific color
         * @param index index of coloe
         * @param color color to set
         */
        public void setPallet(int index, RGB color) {
            pal[((index & (lookupPallet.length - 1)) % lookupPallet.length)] = color;
        }
    }
    /**
     * Internal ComlorModel structure
     */
    private PalletModel colormodel;
    /**
     * Reference Object for 256 color pallet, required by Configuration to determin correct graphics enviroment</br> 
     * example </br><pre>                   
     * public Object getVideoMemoryModel(){
     *      return adaptor.MemoryModelPalletized8Bit;
     * }
     * </pre>
     * @see legacy.Configuration
     */
    public static final Object MemoryModelPalletized8Bit = new Object();
    /**
     * Reference Object for 64 color pallet, required by Configuration to determin correct graphics enviroment</br> 
     * example </br><pre>                   
     * public Object getVideoMemoryModel(){
     *      return adaptor.MemoryModelPalletized6Bit;
     * }
     * </pre>
     * @see legacy.Configuration
     */
    public static final Object MemoryModelPalletized6Bit = new Object();
    /**
     * Reference Object for 16 color pallet, required by Configuration to determin correct graphics enviroment</br> 
     * example </br><pre>                   
     * public Object getVideoMemoryModel(){
     *      return adaptor.MemoryModelPalletized4Bit;
     * }
     * </pre>
     * @see legacy.Configuration
     */
    public static final Object MemoryModelPalletized4Bit = new Object();
    /**
     * Reference Object for monochrome color pallet, required by Configuration to determin correct graphics enviroment</br> 
     * example </br><pre>                   
     * public Object getVideoMemoryModel(){
     *      return adaptor.MemoryModelPalletized1Bit;
     * }
     * </pre>
     * @see legacy.Configuration
     */
    public static final Object MemoryModelPalletized1Bit = new Object();
    /**
     * Internal pallet size can only be set once. this ensures that during
     * initialization the internal pallet stucture is unmodifed.
     */
    private static int memoryModelHash = -1;
    /**
     * Internal pallet structure holds pallet data before Initialization.</br>
     */
    protected static RGB[] lookupPallet = null;
    /**
     * reference to the usable java graphics window
     */
    private JFrame videoFrame;
    /**
     * reference to the usable buffer strategy via JFrame</br>
     * <b>Note</b><i> will always have 2 buffers as this is the mwthod empoly by the VideoAdapter layer.</i>
     */
    private BufferStrategy videoFrameBuffer = null;
    /**
     * reference to the usable java graphics device via BufferStrategy
     */
    private Graphics videoFrameGraphics;
    /**
     * the current page as boolean 2 possibliltie quick flip ^=
     */
    private static boolean page = false;
    /**
     * size of page buffer
     */
    protected int bufferPageSize;
    /**
     * buffer
     */
    private int[] bufferPagePixelsFront;
    /**
     * memory image source for pixels
     */
    private MemoryImageSource bufferPageSourceFront;
    /**
     * image for image source
     */
    private Image bufferPageImageFront;
    /**
     * buffer
     */
    private int[] bufferPagePixelsBack;
    /**
     * memory image source for pixels
     */
    private MemoryImageSource bufferPageSourceBack;
    /**
     * image for image source
     */
    private Image bufferPageImageBack;
    /**
     * active buffer to be pushed ont to the source
     */
    protected int[] activeBufferPagePixels;
    /**
     * active source to be rendered
     */
    private MemoryImageSource activeBufferPageSource;
    /**
     * image of source to render
     */
    private Image activeBufferPageImage;

    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * VideoAdapter() is the default constructor and calls register 
     * wich must be called before any other class or method invocation.
     * <b>Note</b><i> this is called within the Hêbê kernel.</i>
     */
    protected VideoAdapter() {
        Register();
    }

    /**
     * VideoAdapter(int width, String height) constucts a video standard with supplied
     * paramiters for later use.
     * @param width scanline lenght
     * @param height width supplied to detrmin ratio
     */
    private VideoAdapter(int width, int height) {
        this.ratio = (float) width / (float) height;
        if (standards.contains(this)) {
            this.standard = standards.get(standards.indexOf(this)).getStandard();
        } else {
            this.standard = "x" + height + " " + getRatioString(this.ratio);
        }

    }

    /**
     * VideoAdapter(int width, int height, String standard) constucts a video standard with supplied
     * paramiters for later use.
     * @param width scanline lenght
     * @param height width supplied to detrmin ratio
     * @param standard known standard name
     */
    protected VideoAdapter(int width, int height, String standard) {
        this.ratio = (float) width / (float) height;
        if (standards.contains(this)) {
            this.standard = standards.get(standards.indexOf(this)).getStandard();
        } else {
            this.standard = standard;
        }
    }

    /**
     * optimizeAdapter(int width, int height, String standard) creates the optimized
     * versions of the abstract methods required for use with Rendition
     * @param width width of the adaptor required
     * @param height height of the adaptrr required
     * @param standard name of the adapter
     * @return the optimized adapter
     */
    @SuppressWarnings("unchecked")
    private static final VideoAdapter optimizeAdapter(int width, int height, String standard) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.makeClass("legacy.VideoAdapterAccelerated", cp.get("legacy.VideoAdapter"));
            cc.setSuperclass(cp.get("legacy.VideoAdapter"));

            CtMethod m = CtNewMethod.make("public final int getWidth() {" +
                    "return " + width + ";" +
                    "}", cc);
            cc.addMethod(m);

            m = CtNewMethod.make("public final int getHeight() {" +
                    "return " + height + ";" +
                    "}", cc);
            cc.addMethod(m);

            m = CtNewMethod.make("public final void putPixel(int x, int y, int color){" +
                    "if(x>=0&&x<" + width + "&&y>=0&&y<" + height + ")" +
                    "this.activeBufferPagePixels[x + " + Accelerators.optimizeScanlineShift(width, "y") + "] = ((color & " + (lookupPallet.length - 1) + ") % " + lookupPallet.length + ");}", cc);
            cc.addMethod(m);

            m = CtNewMethod.make("public final void clearScreen(int color){" +
                    "java.util.Arrays.fill(this.activeBufferPagePixels, ((color & " + (lookupPallet.length - 1) + ") % " + lookupPallet.length + "));}", cc);
            cc.addMethod(m);


            CtClass[] argTypes = {CtClass.intType, CtClass.intType, cp.get("java.lang.String")};
            CtConstructor cs = CtNewConstructor.make(argTypes, null, cc);
            cc.addConstructor(cs);
            cc.writeFile();

            Class c = cc.toClass();
            Class[] intArgsClass = new Class[]{int.class, int.class, String.class};
            Object[] intArgs = new Object[]{width, height, standard};

            Constructor intArgsConstructor = c.getConstructor(intArgsClass);
            return (VideoAdapter) intArgsConstructor.newInstance(intArgs);
        } catch (CannotCompileException CCE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), CCE);
        } catch (NotFoundException NFE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), NFE);
        } catch (NoSuchMethodException NSME) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), NSME);
        } catch (InstantiationException IA) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IA);
        } catch (IllegalAccessException IAE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IAE);
        } catch (InvocationTargetException ITE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), ITE);
        } catch (IOException IOE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IOE);
        }
    }

    /**
     * blankAdapter(int width, int height, String standard) creates the blank
     * versions of the abstract methods required for use with Rendition subsequent call to 
     * optimizeAdapter ensures the correct adaptor and its methods are returned
     * @param width width of the adaptor required
     * @param height height of the adaptrr required
     * @param standard name of the adapter
     * @see #getAdapter(int, int) 
     * @return the optimized adapter
     */
    @SuppressWarnings("unchecked")
    private static final VideoAdapter blankAdapter(int width, int height, String standard) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.makeClass("legacy.VideoAdapter" + width + "x" + height, cp.get("legacy.VideoAdapter"));
            cc.setSuperclass(cp.get("legacy.VideoAdapter"));

            CtMethod m = CtNewMethod.make("public int getWidth() {" +
                    "return " + width + ";" +
                    "}", cc);
            cc.addMethod(m);

            m = CtNewMethod.make("public int getHeight() {" +
                    "return " + height + ";" +
                    "}", cc);
            cc.addMethod(m);

            CtClass[] argTypes = {CtClass.intType, CtClass.intType, cp.get("java.lang.String")};
            CtConstructor cs = CtNewConstructor.make(argTypes, null, cc);
            cc.addConstructor(cs);
            cc.writeFile();

            Class c = cc.toClass();
            Class[] intArgsClass = new Class[]{int.class, int.class, String.class};
            Object[] intArgs = new Object[]{width, height, standard};

            Constructor intArgsConstructor = c.getConstructor(intArgsClass);

            return (VideoAdapter) intArgsConstructor.newInstance(intArgs);
        } catch (CannotCompileException CCE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), CCE);
        } catch (NotFoundException NFE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), NFE);
        } catch (NoSuchMethodException NSME) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), NSME);
        } catch (InstantiationException IA) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IA);
        } catch (IllegalAccessException IAE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IAE);
        } catch (InvocationTargetException ITE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), ITE);
        } catch (IOException IOE) {
            throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION), IOE);
        }
    }

    /**
     * Register() prepairs displays and compaitibiliy statndards.
     */
    private static void Register() {
        if (!registered) {
            registered = true;
            DisplayMode modes[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();
            for (DisplayMode mode : modes) {
                for (VideoAdapter adaptor : standards) {
                    if ((mode.getWidth() == adaptor.getWidth()) &&
                            (mode.getHeight() == adaptor.getHeight()) &&
                            (mode.getBitDepth() == 8) &&
                            (mode.getRefreshRate() == 60)) {
                        compatible.add(adaptor);
                    }
                }
            }
        }
    }

    /**
     * Initialize() instanciates the constructed VideoAdapter and is called <b>exclusivly</b> from getAdapter(int width, int height)<br/>
     * this acts as an extension to construction  where VideoAdapter feilds and methods are prepaired for use.</br>
     * <b>Note</b><i> The actual display buffer has a 32bit depth not 8 as traditional pallets for 8 bit operate
     * on port assignments ranging from 0 to 63, which is far from being native to java and could cause expensive
     * operations to keep the engine as true to classical game programming as possible.
     * @see #getAdapter(int, int) 
     */
    private final void Initialize() {
        // <editor-fold defaultstate="collapsed" desc="Intialize MemoryModel">
        if (lookupPallet.hashCode() == memoryModelHash) {
            colormodel = new PalletModel(lookupPallet);
            lookupPallet = null;
            ResourceManager.setVideoCodecToolkit(this);
        } else {
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_INVALID_DISPAY_ADAPTER)); //Pallet was changed illegaly
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Intialize Display">
        this.videoFrame = new JFrame(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        this.videoFrame.setIgnoreRepaint(true);
        this.videoFrame.setUndecorated(true);
        this.videoFrame.setSize(this.getWidth(), this.getHeight());
        this.videoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.videoFrame.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                Legacy.isRunning = false;
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
        this.videoFrame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F4 && e.isAltDown()) {
                    Legacy.isRunning = false;
                }
            }
        });
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this.videoFrame);
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setDisplayMode(new DisplayMode(this.getWidth(), this.getHeight(), 16, 60));
        } catch (java.lang.IllegalArgumentException IAE) {
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_INVALID_DISPAY_ADAPTER));
        }
        this.videoFrame.createBufferStrategy(1);
        this.videoFrameBuffer = this.videoFrame.getBufferStrategy();
        this.videoFrameGraphics = videoFrameBuffer.getDrawGraphics();
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Initialize Buffers">
        this.bufferPageSize = this.getWidth() * this.getHeight();
        // <editor-fold defaultstate="collapsed" desc="Front Buffer">
        this.bufferPagePixelsFront = new int[this.bufferPageSize];
        java.util.Arrays.fill(this.bufferPagePixelsFront, 0);
        this.bufferPageSourceFront = new MemoryImageSource(this.getWidth(), this.getHeight(), this.colormodel, this.bufferPagePixelsFront, 0, this.getWidth());

        this.bufferPageSourceFront.setAnimated(true);
        this.bufferPageSourceFront.setFullBufferUpdates(true);

        this.bufferPageImageFront = videoFrame.createImage(this.bufferPageSourceFront);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Back buffer">
        this.bufferPagePixelsBack = new int[this.bufferPageSize];
        java.util.Arrays.fill(this.bufferPagePixelsBack, 0);
        this.bufferPageSourceBack = new MemoryImageSource(this.getWidth(), this.getHeight(), this.colormodel, this.bufferPagePixelsBack, 0, this.getWidth());

        this.bufferPageSourceBack.setAnimated(true);
        this.bufferPageSourceBack.setFullBufferUpdates(true);

        this.bufferPageImageBack = this.videoFrame.createImage(this.bufferPageSourceBack);
    // </editor-fold>

    // </editor-fold>

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * getAdapter(int width, int height) caution when using this method.
     * this will return a valid adapter only if the VideoAdapter is not active and
     * the height and width are valid compatible formats.</br>
     * <b>Note</b><i> called exlusivly from the Hêbê kernel to instanciate the VideoAdapter.</i>     
     * @see #getStandards() 
     * @param width requested width
     * @param height requested height
     * @return applicible adapter
     */
    protected static VideoAdapter getAdapter(int width, int height) {
        Register();
        VideoAdapter adapter = null;
        if (!isActive) {
            for (VideoAdapter v : compatible) {
                if (v.getWidth() == width && v.getHeight() == height) {
                    adapter = optimizeAdapter(width, height, v.standard);
                    adapter.Initialize();
                    break;
                }
            }
        } else {
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_INVALID_DISPAY_ADAPTER));
        }
        return adapter;
    }

    /**
     * createMemoryModel prepairs the pallet for use.  Sebsequent Initialize() required.
     * <b>Note</b><i> called exlusivly from the Hêbê kernel to instanciate the VideoAdapters color functions.</i></br>  
     * @param memoryModel refererence to acceptable model 
     */
    protected static RGB[] createMemoryModel(Object memoryModel) {
        RGB[] rgb = null;
        if (memoryModel.equals(MemoryModelPalletized1Bit)) {
            rgb = new RGB[2];
            memoryModelHash = rgb.hashCode();
        } else if (memoryModel.equals(MemoryModelPalletized4Bit)) {
            rgb = new RGB[16];
            memoryModelHash = rgb.hashCode();

        } else if (memoryModel.equals(MemoryModelPalletized6Bit)) {
            rgb = new RGB[64];
            memoryModelHash = rgb.hashCode();

        } else if (memoryModel.equals(MemoryModelPalletized8Bit)) {
            rgb = new RGB[256];
            memoryModelHash = rgb.hashCode();
        }
        lookupPallet = rgb;
        return rgb;
    }

    /**
     * getRenderer() called exclusivly from within the legacy kernal to flip the page
     * and return this Video Interface
     * @return the Video Interface associated with this VideoAdapter
     */
    protected final VideoAdapter getRenderer() {
        if (page ^= true) {
            this.activeBufferPageSource = this.bufferPageSourceFront;
            this.activeBufferPageImage = this.bufferPageImageFront;
            this.activeBufferPagePixels = bufferPagePixelsFront;
            return this;
        }

        this.activeBufferPageSource = this.bufferPageSourceBack;
        this.activeBufferPageImage = this.bufferPageImageBack;
        this.activeBufferPagePixels = bufferPagePixelsBack;
        return this;
    }

    /**
     * render() pushes the current video buffer or page into the video device
     */
    protected final void render() {
        this.videoFrameGraphics = videoFrameBuffer.getDrawGraphics();
        this.activeBufferPageSource.newPixels(0, 0, this.getWidth(), this.getHeight());
        this.videoFrameGraphics.drawImage(activeBufferPageImage, 0, 0, null);
        this.videoFrameGraphics.dispose();
        this.videoFrameBuffer.show();
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * getRatioString(float r) coverts a decimal fraction to a ration string where applicable
     * @param r the decimal; fractio o parse must not be NaN
     * @return the resulting string standard ie [4:3]
     */
    public static String getRatioString(
            float r) {
        for (float i = 1.0f; i <
                100.0f; i++) {
            if (((r * i) % 1) == 0.0) {
                return "[" + ((int) (i * r)) + ":" + (int) i + "]";
            }

        }
        return "NaN";
    }

    /**
     * getRatioFloat(String s) parses a ratio ie [16:9] to it ratio where [16:9] = 16/9=1.77777...8 
     * @param s ratio to parse
     * @return the rusilting fraction or NaN if invalid format
     */
    public static float getRatioFloat(String s) {
        try {
            StringTokenizer t = new StringTokenizer(s.substring(1, s.length() - 1), ":");
            return (float) Integer.parseInt(t.nextToken()) / (float) Integer.parseInt(t.nextToken());
        } catch (Exception e) {
            return Float.NaN;
        }

    }

    /**
     * getWidth() returns the width of the active VideoAdapter
     * @return width of the adapter
     */
    @Accelerated("ByteCode")
    public abstract int getWidth();

    /**
     * getHeight() returns the height of the active VideoAdapter
     * @return height of the adapter
     */
    @Accelerated("ByteCode")
    public abstract int getHeight();

    /**
     * putPixel(int x, int y, int color) places a  pixel on the active video buffer
     * @param x coordinate
     * @param y coordinate
     * @param color as pallet index
     */
    @Accelerated("ByteCode")
    public abstract void putPixel(int x, int y, int color);

    /**
     * clearScreen(int color) clears the active video buffer to a color index
     * @param color as pallet index
     */
    @Accelerated("ByteCode")
    public abstract void clearScreen(int color);

    /**
     * getPalletSize() simply returns the size of the Pallet
     * @return the pallet size
     */
    public final int getPalletSize() {
        return colormodel.getSize();
    }

    /**
     * setPalletIndex(int, RGB) sets a color in this pallet at index to a specific color
     * @param index index of coloe
     * @param color color to set
     */
    public final void setPalletIndex(int index, RGB color) {
        colormodel.setPallet(index, color);
    }

    /**
     * getPalletIndex(int) returns the color currently set for the pallet at index of
     * @param index of color
     * @return the RGB for a pallet index
     */
    public final RGB getPalletIndex(int index) {
        index = (index & (colormodel.getSize() - 1)) % colormodel.getSize();
        return new RGB(RGB.getColor(colormodel.getRed(index), colormodel.getGreen(index), colormodel.getBlue(index), colormodel.getAlpha(index)));
    }

    /**
     * Returns the closest color in the existing pallet
     * @param rgb the RGB triplet to match
     * @return the index in this pallet of the closest color
     */
    public final int getClosetColor(RGB rgb) {
        return colormodel.getClosetColor(rgb.r, rgb.g, rgb.b);
    }

    /**
     * Returns the closest color in the existing pallet
     * @param r Red component to match
     * @param g Green component to match
     * @param b Blue component to match
     * @return the index in this pallet of the closest color
     */
    public final int getClosetColor(int r, int g, int b) {
        return colormodel.getClosetColor(r, g, b);
    }

    /**
     * getRatio() returns the ratio of the resolution eg VGA has an aspect ratio or [4:3] 
     * so 480x [4:3]  would equate to 480*(4/3=1.333)=640
     * @return the ratio
     */
    public float getRatio() {
        return ratio;
    }

    /**
     * getStandard() returns a 
     * @return a name representing this standard ie </br>CGA [8:5] </br>or /<br>
     * x288 [11:9]</br>
     * <b>Note</b><i> this would in reality be 352x288 or CIF [11:9] which is currently unsupported</i>
     */
    public String getStandard() {
        return this.standard;
    }

    /**
     * getStandards() returns a list of DisplayAdapters or video modes compatible with 
     * this machine
     * @return an array of compatible DispayAdapters
     */
    public static VideoAdapter[] getStandards() {
        Register();
        return compatible.toArray(new VideoAdapter[compatible.size()]);
    }

    /**
     * getStandardFor() returns a DisplayAdapters or video mode compatible with 
     * this machine provided it matched width and height parameters
     * @param width parameter to match
     * @param height parameter to match
     * @return a compatible DispayAdapter or null otherwise
     */
    public static VideoAdapter getStandardFor(int width, int height) {
        Register();
        VideoAdapter adapter = null;
        for (VideoAdapter v : compatible) {
            if (v.getWidth() == width && v.getHeight() == height) {
                adapter = v;
                break;

            }


        }
        return adapter;
    }

    /**
     * isActive() returns weather this VideoAdaptor is active and therfore sucessfully
     * prepaired
     * @return state of this adaptor
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Generic fucntion.
     * @see #getStandard() 
     * @return as String representation of this version
     */
    @Override
    public String toString() {
        return getStandard();
    }

    /**
     * close() should be called by legacy to ensure graphics are correctly disposed of
     */
    protected void close() {
        if (this.videoFrame != null) {
            this.videoFrame.dispose();

        }

        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
    }    // </editor-fold>    
}
