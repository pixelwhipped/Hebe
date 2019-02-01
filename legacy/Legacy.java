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

import java.lang.Thread.UncaughtExceptionHandler;
import legacy.Annotations.Restricted;
import legacy.video.RGB;

/**
 * Legacy.class is the base class and drive any application extending it.
 * This class is abstract and requires you to overide the following methods
 * Also enusre super(args) is called from you constructor.
 * </br>
 * </br>public abstract void render(VideoAdapter v);
 * </br>public abstract boolean updateGame(long gameTime);
 * </br>public abstract Configuration getConfiguration();
 * </br>
 * Effort has been taken to ensure this class is far from bulky and internally
 * well protected.
 * </br>
 * <b>Recommend</b><i> reading the java doc for this class.</i>
 * 
 * @see legacy.Legacy#main(java.lang.String[]) 
 * @author Benjamin Tarrant
 */
public abstract class Legacy {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Versioning">
    /**
     * applicationName refers to the name of this project.
     */
    public static final String applicationName = Common.APPLICATION_NAME;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Common"> 
    /**
     * Internal reference flage for execution.
     * will be true by default or false on request or forced termination
     */
    protected static boolean isRunning; //Needed by videoAdapter on window close event.
    /**
     * Internal reference to the videoAdaptor
     */
    private static VideoAdapter videoAdapter;
   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">
     /**
     * Internal locking object used to prevent execution until a synchronized block in the extended construtor is executed
     */
    private static final Object SYNCHRONOUS_CONSTRUCTION = new Object(); 
    /**
     * Internal reference to the VideoAdaptors configured framerate
     * <b>note</b> frames per nanosecond
     */
    private static int framerate;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Legacy(String[] args) is the primary constructor and must always be called
     * with super(args) in any extended constructor. This constructor prepairs all
     * variables and configuration and executes the kernel.</br>
     * </br>
     * <b>Coding practices</b><i> Constructor invokes Thread.start()<br>
     * Legacy can only be constructed by extending this class and as no other 
     * constructors are defined a call to super(args) is forced unless an innerclass
     * is created as in tests.</br> 
     * <b>Ensure all data is prepaired to prevent concurrent race conditions</b></i>
     * 
     * @param args argument to be parsed on for configurations
     */
    public Legacy(String[] args) {
        // <editor-fold defaultstate="collapsed" desc="Prepair Configuration">
        videoAdapter = null;  //must be ready for classes
        final Configuration configuration = getConfiguration(args); //Called once only
        // </editor-fold>
        if (configuration != null) {
            // <editor-fold defaultstate="collapsed" desc="Start Kernel">   
            // <editor-fold defaultstate="collapsed" desc="Prepair variables and conditions">
            Legacy.framerate = (6000 / configuration.getFrameRate()) * 100000;  //nonosecond resolution
            isRunning = true;
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Prepair Video">                        
            RGB[] rgb = VideoAdapter.createMemoryModel(configuration.getVideoMemoryModel());
            if (rgb != null) {
                configuration.createPallet(rgb);
                videoAdapter = configuration.getVideoAdaptor();
                /**
                 * causes VideoAdaptor to be initialised
                 */
                videoAdapter = VideoAdapter.getAdapter(videoAdapter.getWidth(), videoAdapter.getHeight());    //only statndards allowed
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Create Thread">
                /**
                 * final as absolute. Should never be reconstructed or re-reference
                 */
                final Thread thread = new Thread(new Runnable() {

                    /**
                     * run execute the main loop and handles all system function
                     * absolutly no classes or methods have access to this kernal             
                     */
                    @Override
                    public void run() {
                        // <editor-fold defaultstate="collapsed" desc="Prepair timer">    
                        /**
                         * cycleTimer is the calulated time since last execution of an updateGame call
                         */
                        long cycleTimer = 0;
                        /**
                         * difference to framrate incremnts as per cycleTimer until a render is requested
                         */
                        long difference = 0;
                        /**
                         * cycleTime Holds reference to execution time of updateGame call 
                         */
                        long cycleTime = System.nanoTime();
                        // </editor-fold>
                        //recalulate cycleTimer and updateGame if running
                        while (isRunning && updateGame(cycleTimer = System.nanoTime() - cycleTime)) {
                            cycleTime = System.nanoTime(); //nonosecond resolution
                            difference += cycleTimer;   //increment difference
                            if (difference > framerate) {
                                render(videoAdapter.getRenderer()); //call abstract render method
                                videoAdapter.render();  //push the graphics
                                difference -= framerate;  //minus framerate from difference (accuracy)
                            }
                            Thread.yield(); //ensure thread and system integrity
                        }
                        // <editor-fold defaultstate="collapsed" desc="Shutdown">                
                        videoAdapter.close();   //allow adapter to exit correctly
                        throw new RuntimeException(Integer.toString(Common.NORMAL_TERMINATION)); //exit, execution was successfull
                    // </editor-fold>
                    }
                });
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Prepair Thread">
                thread.setPriority(Thread.MIN_PRIORITY);    //greed never succeeds
                //one more level of safty
                thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        e.printStackTrace();
                        isRunning = false;  //Obsolete
                        videoAdapter.close();   //allow adapter to exit correctly
                        throw new RuntimeException(Integer.toString(Common.ABNORMAL_TERMINATION));

                    }
                });
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Execute Thread">
                //Allow constructor to return to extending class constructor before starting
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.currentThread().sleep(200);
                        } catch (InterruptedException IE) {
                        }
                        synchronized (SYNCHRONOUS_CONSTRUCTION) {
                            thread.start();
                        }
                    }
                }).start();
            // </editor-fold>
            }
        // </editor-fold>
        } else {
            // <editor-fold defaultstate="collapsed" desc="Shutdown">      
            throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_INVALID_CONFIGURATION));
        // </editor-fold>
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * render(Video v) is called when needed as defined by the Conifiguration 
     * suppled by the abstract method Configuration getConfiguration().
     * The need is determined by the recommeded frames per second or framrate
     * defined.
     * @see legacy.Legacy#getConfiguration(java.lang.String[])
     * @see legacy.Configuration
     * @param v Video output class
     */
    public abstract void render(VideoAdapter v);

    /**
     * updateGame(long gameTime) is called to allow the came to update its state
     * this method is called independantly of render(Video v) as Renditions are
     * usually operating on a framerate and are only updated as needed, where 
     * updates can occour more frequently to allow for extra operation to occour.
     * 
     * Recommended use. 
     * <pre>
     * ...
     * long totalTime = 0;
     * boolean updateMode = GAME;   //GAME = true and OTHER = false
     * boolean continue = true;
     * ...
     * public boolean updateGame(long gameTime) {
     *      if (updateMode ^= GAME) {
     *          continue = doGameStuff();
     *      }else{
     *          continue = doOtherStuff();
     *      }
     *      return continue;
     * }
     * </pre>
     * @param gameTime time in Nano Seconds since last call
     * @return true to request to be recalled false otherwize
     */
    public abstract boolean updateGame(long gameTime);

    /**
     * getConfiguration() is called once and is used to set the enviroment for
     * the game values are read once only and will any changes will not be
     * recoginized internaly.
     * 
     * Recommended use.
     * <pre>
     * public Configuration getConfiguration(){
     *      return new Configuration(){
     *          ...
     *      }
     * }
     * </pre>
     * @see legacy.Configuration
     * @param args argumens supplied from the commandline
     * @return Configuration to be used.
     */
    public abstract Configuration getConfiguration(String[] args);

    /**
     * getConstructionLock() is used to prevent race conditions.  </br>
     * <b>Warning</b><i> no method other than the constructor may call this class.</i>
     * The correct and only possible usage is as follows.</br>
     * <pre>
     * class myGame extends legacy{
     *      public myGame(String[] args){
     *          synchronized(getConstructionLock()){    //load resources here that are required before any other overridden method call
     *              ...
     *          }
     *      }
     * </pre>
     * @return construction lock object
     */
        
    @Restricted
    public final Object getConstructionLock() {
        if (Helpers.getCallerMethod(1).replaceAll("\\$.*[1234567890]", "").endsWith("<init>")) {
            try {
                Class t = Class.forName(Helpers.getCallerClass(1));
                while (!t.getName().equals("java.lang.Object")) {
                    if (t.getName().equals("legacy.Legacy")) {
                        return SYNCHRONOUS_CONSTRUCTION;
                    }
                    t = t.getSuperclass();
                }
            } catch (ClassNotFoundException CNFE) {
                CNFE.printStackTrace();
            }
        }
        throw new RuntimeException(Integer.toString(Common.ERROR_FATAL_ILLEGAL_USAGE));
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    /**
     * Application entry point.  This methods main method acts as a test bed
     * for the Hêbê system and creates a Annonomys Legacy class.
     * All extenstion of Legacy should include their own main method.
     * @param args argments to be handled by Legacy(String[] args)
     */
    public static void main(String[] args) {
        args = new String[]{"320", "200"};
        final Legacy test = new Legacy 

               (args) {
            // <editor-fold defaultstate="collapsed" desc="Methods">
            int fps = 0;
            long timer = System.nanoTime();
            int color = 0;

            public void render(VideoAdapter v) {

                fps++;
                v.clearScreen(color);
                /*   for (int zy = 0; zy < v.getHeight(); zy++) {
                for (int z = 0; z < v.getWidth(); z++) {
                
                v.putPixel(z, zy, color);
                }
                }*/

                if (System.nanoTime() - timer > 1000000000) {
                    timer = System.nanoTime();
                    System.out.println(fps);
                    fps = 0;
                    color++;
                }

            }

            public boolean updateGame(long gameTime) {
                return true;
            }

            public Configuration getConfiguration(String[] args) {
                final VideoAdapter adaptor = VideoAdapter.getStandardFor(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                if (adaptor != null) {

                    return new  

                         Configuration () {

                             
                        

                        public  VideoAdapter getVideoAdaptor() {
                            return adaptor;
                        }

                        public   Object 
                             getVideoMemoryModel() {
                            return VideoAdapter.MemoryModelPalletized8Bit;
                        }

                        public int getFrameRate() {
                            return 300;
                        }

                        public void createPallet(RGB[] pal) {
                            for (int i = 0; i < pal.length; i++) {
                                pal[i] = new RGB(i, i, i, 255);
                            }
                        }
                    };
                }
                return null;
            }
            // </editor-fold>    
        };
    }
    // </editor-fold>    
}
