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
package legacy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import legacy.video.RGB;

/**
 * Helpers.class defines methods and classes for reuse.
 * @author Benjamin Tarrant
 */
public class Helpers {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    // <editor-fold defaultstate="collapsed" desc="Common">\
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Internal">

    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * used for casting arrays of type
     */
    private static Hashtable<String, Arrays<?>> arrayHelpers = new Hashtable<String, Arrays<?>>();

    /**
     * getRGBArray() gets a casted Array&lt;?&gt;type for use/reuse to prevent extensive object construction
     * @return the casted Arrays&lt;RGB&gt;
     */
    @SuppressWarnings("unchecked")
    public static Arrays<RGB> getRGBArray() {
        if (!arrayHelpers.containsKey("RGB")) {
            arrayHelpers.put("RGB", new Arrays<RGB>());
        }
        return (Arrays<RGB>) arrayHelpers.get("RGB");
    }

    /**
     * getIntegerArray() gets a casted Array&lt;?&gt;type for use/reuse to prevent extensive object construction
     * @return the casted Arrays&lt;Integer&gt;
     */
    @SuppressWarnings("unchecked")
    public static Arrays<Integer> getIntegerArray() {
        if (!arrayHelpers.containsKey("INTEGER")) {
            arrayHelpers.put("INTEGER", new Arrays<Integer>());
        }
        return (Arrays<Integer>) arrayHelpers.get("INTEGER");
    }

    /**
     * getCallerMethod() used by methods which restrice access or require knowlage of the caller of itsef.
     * @param depth how far back to look into stack
     * @return the caller of the method define as class and method name where &lt;init&gt; may apply
     */
    public static String getCallerMethod(int depth) {
        try {
            StackTraceElement[] stem = Thread.currentThread().getStackTrace();
            return stem[2 + depth].getClassName() + "." + stem[2 + depth].getMethodName();
        } catch (IndexOutOfBoundsException IOOBE) {
            return null;
        }
    }

    /**
     * getCallerClass() used by methods which restrice access or require knowlage of the caller of itsef.
     * @param depth how far back to look into stack
     * @return the caller of the method define as class
     */
    public static String getCallerClass(int depth) {
        try {
            StackTraceElement[] stem = Thread.currentThread().getStackTrace();
            return stem[2 + depth].getClassName();
        } catch (IndexOutOfBoundsException IOOBE) {
            return null;
        }
    }

    /**
     * c(Class,Class) determins it the first class is a sebling of the second.
     * @param sibling as .class ie classExtends(for.class,bar.class) Not class.getClass()
     * @param parent as .class ie classExtends(for.class,bar.class) Not class.getClass()
     * @return true if extension false otherwise
     */
    public static boolean classExtends(Class sibling, Class parent) {
        try {
            if (sibling.isPrimitive()) {
                return sibling.getCanonicalName().equals(parent.getCanonicalName());
            } else {
                if (parent.getCanonicalName().equals(Object.class.getCanonicalName())) {
                    return true;
                } else {
                    String parentName = parent.getCanonicalName();
                    String siblingName = sibling.getCanonicalName();
                    while (!siblingName.equals(Object.class.getCanonicalName())) {
                        if (siblingName.equals(parentName)) {
                            return true;
                        }
                        siblingName = (sibling = sibling.getSuperclass()).getCanonicalName();
                    }
                }
            }
        } catch (NullPointerException NPE) {
        }
        return false;
    }

    /**
     * c(Class,Class) determins it the first class is a sebling of the second.
     * @param siblingCanonicalName as canonical name for
     * @param parentCanonicalName as canonical name for
     * @return true if extension false otherwise
     */
    public static boolean classExtends(String siblingCanonicalName, String parentCanonicalName) {
        try {
            Class sibling = Class.forName(siblingCanonicalName);
            Class parent = Class.forName(siblingCanonicalName);
            if (sibling.isPrimitive()) {
                return sibling.getCanonicalName().equals(parent.getCanonicalName());
            } else {
                if (parent.getCanonicalName().equals(Object.class.getCanonicalName())) {
                    return true;
                } else {
                    String parentName = parent.getCanonicalName();
                    String siblingName = sibling.getCanonicalName();
                    while (!siblingName.equals(Object.class.getCanonicalName())) {
                        if (siblingName.equals(parentName)) {
                            return true;
                        }
                        siblingName = (sibling = sibling.getSuperclass()).getCanonicalName();
                    }
                }
            }
        } catch (NullPointerException NPE) {
        } catch (ClassNotFoundException CNFE) {
        }
        return false;
    }

    /**
     * getExtension(String ext) returns in general the extension of a file or a sub string
     * of the last index of "." in a string to the end of that string
     * @param ext string expected to contain an extension
     * @return the extension
     */
    public static String getExtension(String ext) {
        int i = ext.lastIndexOf('.');

        if (i > 0 && i < ext.length() - 1) {
            ext = ext.substring(i + 1).toLowerCase();
        }
        if (ext == null) {
            return "";
        }
        return ext;
    }

    /**
     * getInputStreamProtocol(String) return a readable stream from a string location.</br>
     * Allowed Protocols</br>
     * jar:// ie jar://c:/path/my.jar/mypackage/myfile.txt <b>Warning<b> will not allow recusive jar files and oly allows acces to local storage devices</br>
     * zip:// indenticle to above </br>
     * file:// ie file://c:/myfile.txt where myfile.txt is located on a logical(local) device</br>
     * http:// as per URL </br>
     * socket://address/port ie socket://127.0.0.1/256 where a socket will stream the data from a port 256 from this machine</br>
     * </br>
     * failing this getInputStreamProtocol will attemt to load the location as from a url or file.
     * @param location we are looking at
     * @return and imput stream for the location or null otherwise
     */
    public static InputStream getInputStreamProtocol(String location) {
        location = location.toLowerCase();
        if (location.startsWith("jar://")) {  //load file from jar
            location = location.substring("jar://".length());
            try {
                JarFile jar = new JarFile(new File(location.substring(0, location.indexOf(".jar")) + ".jar"), false, JarFile.OPEN_READ);
                JarEntry entry = jar.getJarEntry(location.substring(location.indexOf(".jar/") + 5));
                return jar.getInputStream(entry);

            } catch (Exception ex) {
                return null;
            }

        } else if (location.startsWith("zip://")) {  //load file from jar
            location = location.substring("zip://".length());
            try {
                ZipFile zip = new JarFile(new File(location.substring(0, location.indexOf(".zip")) + ".zip"), false, ZipFile.OPEN_READ);
                ZipEntry entry = zip.getEntry(location.substring(location.indexOf(".zip/") + 5));
                return zip.getInputStream(entry);
            } catch (Exception ex) {
                return null;
            }
        } else if (location.startsWith("file://")) { //load from file
            try {
                return new BufferedInputStream(new FileInputStream(location.substring("file://".length())));
            } catch (IOException IOE) {
                return null;
            }

        } else if (location.startsWith("http://")) { //load from url
            try {
                return new URL(location).openStream();
            } catch (IOException IOE) {
                return null;
            }
        } else if (location.startsWith("socket://")) { //load from socket socket://address/port
            try {
                location = location.substring("socket://".length());
                Socket s = new Socket(location.substring(0, location.indexOf("/")), Integer.parseInt(location.substring(location.indexOf("/") + 1)));
                return s.getInputStream();
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                return new BufferedInputStream(new FileInputStream(location));
            } catch (IOException IOE) {
            }
        }
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
    }

    /**
     * pack packs 4 byte values ranging from 0 to 255 into a single integer
     * @param x byte mask 0xFF000000
     * @param y byte mask 0x00FF0000
     * @param z byte mask 0x0000FF00
     * @param u byte mask 0x000000FF
     * @return result as 0xXXYYZZUU
     */
    public static final int pack(int x, int y, int z, int u) {
        return ((x & 255) << 24) | ((y & 255) << 16) | ((z & 255) << 8) | u;
    }

    /**
     * unpack extracts four bytes from an integer and return the values hi to low bytes
     * @param value to unpack
     * @return the result array
     */
    public static final int[] unpack(int value) {
        return new int[]{(value >> 24) & 0xff, (value >> 16) & 0xff, (value >> 8) & 0xff, (value >> 0) & 0xff};
    }
    // </editor-fold>  
    // <editor-fold defaultstate="collapsed" desc="Instantiation  ">
    public static void main(String[] args) {
        String str = "jar://c:/path/file.jar/resource/file.image";
        str = str.substring("jar://".length());
        System.out.println(str.substring(0, str.indexOf(".jar")) + ".jar");
        System.out.println(str.substring(str.indexOf(".jar/") + 5));
    }
    // </editor-fold>    
}
