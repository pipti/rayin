package com.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.extend.FSCacheEx;
import com.openhtmltopdf.extend.FSCacheValue;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.impl.FSDefaultCacheStore;
import com.openhtmltopdf.latexsupport.LaTeXDOMMutator;
import com.openhtmltopdf.mathmlsupport.MathMLDrawer;
import com.openhtmltopdf.objects.zxing.ZXingObjectDrawer;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.render.DefaultObjectDrawerFactory;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.openhtmltopdf.util.XRLog;
import com.rayin.tools.utils.ResourceUtil;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import sun.net.www.protocol.file.FileURLConnection;

import java.awt.*;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * OpenhttptopdfRenderBuilder对象工厂
 * 实例化OpenhttptopdfRenderBuilder对象，添加字体
 *
 * @author Jonah wz
 * @date 2019-08-10
 * @version 1.0
 * @since 1.8
 */
public class OpenhttptopdfRendererObjectFactory implements PooledObjectFactory<OpenhttptopdfRenderBuilder> {
    private static Logger logger = LoggerFactory.getLogger(OpenhttptopdfRendererObjectFactory.class);

    private  HashMap<String,File> fontFileCache = new HashMap<String,File>();
    private  static HashMap<String, FSSupplier<InputStream>> fontFSSupplierCache = new HashMap<String,FSSupplier<InputStream>>();
    private  static HashMap<String, byte[]> fontFileCacheIsb = new HashMap<String,byte[]>();
    private static LinkedHashSet<String> fontNames = new LinkedHashSet<String>();

    private static OpenhttptopdfRendererObjectFactory factory = new OpenhttptopdfRendererObjectFactory();
    private static GenericObjectPool<OpenhttptopdfRenderBuilder> objectPool;

    /**
     * 最小线程数
     */
    private static int MinIdle = 5;

    /**
     * 最大空闲数
     */
    private static int MaxIdle = 8;

    /**
     * 最大线程数
     */
    private static int MaxTotal = 10;
    static{
        synchronized(OpenhttptopdfRendererObjectFactory.class) {
            try {
                factory.FontCache();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }

            //设置对象池的相关参数
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            //最大空闲数
            poolConfig.setMaxIdle(MaxIdle);
            //最大线程数
            poolConfig.setMaxTotal(MaxTotal);
            //最小线程数
            poolConfig.setMinIdle(MinIdle);
            //新建一个对象池,传入对象工厂和配置
            objectPool = new GenericObjectPool<OpenhttptopdfRenderBuilder>(factory, poolConfig);

        }

    }
//
//    private String readFontFamilyName(File font) throws IOException, FontFormatException {
//        Font f = Font.createFont(Font.TRUETYPE_FONT, font);
//        logger.info("FamilyName:" + f.getFamily());
//        logger.info("FontName:" + f.getFontName());
//        logger.info("Name:" + f.getName());
//        logger.info("PSName:" + f.getPSName());
//        return f.getFamily();
//    }

    private String readFontPSName(File font) throws IOException, FontFormatException {
        Font f = Font.createFont(Font.TRUETYPE_FONT, font);
        return f.getPSName();
    }

    private String readFontName(File font) throws IOException, FontFormatException {
        Font f = Font.createFont(Font.TRUETYPE_FONT, font);
        return f.getFontName();
    }

    public static void init() throws Exception {
        //加载字体
        OpenhttptopdfRenderBuilder openhttptopdfRenderBuilder = OpenhttptopdfRendererObjectFactory.getPdfRendererBuilderInstance();
        OpenhttptopdfRendererObjectFactory.returnPdfBoxRenderer(openhttptopdfRenderBuilder);
    };

    /**
     * 初始化-线程池参数
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @throws Exception
     */
    public static void init(int minIdle,int maxIdle,int maxTotal) throws Exception {
        MinIdle = minIdle;
        MaxIdle = maxIdle;
        MaxTotal = maxTotal;
        init();
    }

    /**
     * 获取OpenhttptopdfRenderBuilder实例
     *
     * @return OpenhttptopdfRenderBuilder
     */
    public static OpenhttptopdfRenderBuilder getPdfRendererBuilderInstance() throws Exception {
        return objectPool.borrowObject();
    }

    public static HashMap<String, File> getFontFileCache() throws Exception {
        return factory.fontFileCache;
    }

    public static HashMap<String, FSSupplier<InputStream>> getFSSupplierCacheCache() throws Exception {
        return fontFSSupplierCache;
    }

    /**
     * 归还openhtpdfRenderObject对象
     */
    public static void returnPdfBoxRenderer(OpenhttptopdfRenderBuilder openhtpdfRenderObject) throws Exception {
        if(openhtpdfRenderObject != null && openhtpdfRenderObject.isActive() == true) {
            objectPool.returnObject(openhtpdfRenderObject);
        }
    }

    /**
     * 字体缓存
     * @throws IOException
     * @throws FontFormatException
     */
    private void FontCache() throws IOException, FontFormatException {
        File fontsLocalDir = null;
        Resource jarFontsResource = ResourceUtil.getResource("fonts");
        URL jarFontsUrl = null;
        if(jarFontsResource != null){
            jarFontsUrl = jarFontsResource.getURL();
        }
        Resource localFontsResource = ResourceUtil.getResource("fonts");
        URL localFontsUrl = null;
        if(localFontsResource != null){
            localFontsUrl = localFontsResource.getURL();
        }

        URLConnection urlConn = null;
        if(localFontsUrl != null){
            urlConn = localFontsUrl.openConnection();
        }else{
            logger.warn("本地目录「localfonts」 没有发现字体！");
        }

        if(urlConn != null && (urlConn instanceof FileURLConnection)) {
            fontsLocalDir = new File(ResourceUtil.getResource("fonts").getURI());

            if (fontsLocalDir != null && fontsLocalDir.isDirectory()) {
                List<File> flist = new ArrayList<File>();
                dirAllFontFiles(fontsLocalDir,flist);

                for (int i = 0; i < flist.size(); i++) {
                    File f = flist.get(i);
                    if (f == null || f.isDirectory()) {
                        break;
                    }
                    logger.debug("add local font =>" + f.getAbsolutePath());

                    //将字体写入内存
                    byte[] fontByte = ResourceUtil.getResourceAsByte(f.getAbsolutePath()).toByteArray();

                    final String  fontFileName = f.getName().substring(0,f.getName().indexOf("."));
                    fontFileCacheIsb.put(fontFileName,fontByte);
                    fontNames.add(fontFileName);
                    fontFSSupplierCache.put(fontFileName,new FSSupplier<InputStream>() {
                        @Override
                        public InputStream supply() {
                            logger.debug("fontFileName Requesting font");
                            return new ByteArrayInputStream(fontFileCacheIsb.get(fontFileName));
                        }
                    });

                }
            }
        }
        urlConn = null;
        if(jarFontsUrl != null){
            urlConn = jarFontsUrl.openConnection();
        }else{
            logger.warn("jar包 「rayin-font」 没有发现字体！");
        }
        if(urlConn != null && (urlConn instanceof FileURLConnection)) {
            fontsLocalDir = new File(ResourceUtil.getResource("fonts").getURI());

            if (fontsLocalDir != null && fontsLocalDir.isDirectory()) {
                List<File> flist = new ArrayList<File>();
                dirAllFontFiles(fontsLocalDir,flist);
                //fontResolver.addFontDirectory(ResourceLoader.getResource("fonts").getPath(),true);
                for (int i = 0; i < flist.size(); i++) {
                    File f = flist.get(i);
                    if (f == null || f.isDirectory()) {
                        break;
                    }
                    logger.debug("add local font =>" + f.getAbsolutePath());

                    //将字体写入内存
                    byte[] fontByte = ResourceUtil.getResourceAsByte(f.getAbsolutePath()).toByteArray();

                    final String  fontFileName = f.getName().substring(0,f.getName().indexOf("."));;
                    fontFileCacheIsb.put(fontFileName,fontByte);
                    fontNames.add(fontFileName);
                    fontFSSupplierCache.put(fontFileName,new FSSupplier<InputStream>() {
                        @Override
                        public InputStream supply() {
                            logger.debug("fontFileName Requesting font");
                            return new ByteArrayInputStream(fontFileCacheIsb.get(fontFileName));
                        }
                    });
                }
            }
        }

        JarURLConnection jarURLConnection = null;

        if(urlConn != null && (urlConn instanceof JarURLConnection)){
            try {
                jarURLConnection = (JarURLConnection) urlConn;
                if(jarURLConnection == null){
                    logger.warn("没有发现「rayin-font」 字体jar！");
                    return;
                    // return fontResolver;
                }
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();
                JarEntry jarEntry ;
                String fontTmpPath;
                String jarFileName;
                //File jarfile;
                while (jarEntrys.hasMoreElements()) {
                    jarEntry = jarEntrys.nextElement();
                    jarFileName = jarEntry.getName();

                    if(jarEntry.isDirectory() || ".afm;.pfm;.ttf;.otf;.ttc".indexOf(jarFileName.substring(jarFileName.lastIndexOf(".")).toLowerCase()) < 0){
                    }else{
                        File jarfile = inputStreamToFile(jarFile.getInputStream(jarEntry),jarFileName.substring(jarFileName.lastIndexOf("/"),jarFileName.lastIndexOf(".")),
                                jarFileName.substring(jarFileName.lastIndexOf(".")) );
                        fontTmpPath = jarfile.getAbsolutePath();
                        logger.debug("jar font save to tmpdir => " + fontTmpPath);

                        //将字体写入内存
                        byte[] fontByte = ResourceUtil.getResourceAsByte(jarfile.getAbsolutePath()).toByteArray();

                        final String  fontName = readFontName(jarfile);
                        fontFileCacheIsb.put(fontName,fontByte);
                        fontNames.add(fontName);
                        fontFSSupplierCache.put(fontName,new FSSupplier<InputStream>() {
                            @Override
                            public InputStream supply() {
                                logger.debug("readFontName Requesting font");
                                return new ByteArrayInputStream(fontFileCacheIsb.get(fontName));
                            }
                        });

                        final String  fontPSName = readFontPSName(jarfile);
                        fontFileCacheIsb.put(fontPSName,fontByte);
                        fontNames.add(fontPSName.replace(" ",""));
                        fontFSSupplierCache.put(fontPSName,new FSSupplier<InputStream>() {
                            @Override
                            public InputStream supply() {
                                logger.debug("readFontPSName Requesting font");
                                return new ByteArrayInputStream(fontFileCacheIsb.get(fontPSName));
                            }
                        });

                        //TODO 临时文件也有可能升级，最好不要使用FILE读取
                        jarfile.delete();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        logger.info("添加的字体信息：");

        logger.info("ThreadId:" + Thread.currentThread().getId() + ",添加的字体信息：");
        fontNames.forEach((v)->{
            logger.info(v);
        });

    }

    public static HashMap<String, byte[]> getFontCacheInfo(){
        return fontFileCacheIsb;
    }

    public static void appendFontIntoCache(HashMap<String, byte[]> fonts){

        fonts.forEach((key,value)->{
            fontFSSupplierCache.put(key,new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    return new ByteArrayInputStream(value);
                }
            });
        });

    }
    public static File inputStreamToFile(InputStream inputStream,String prefix,String suffix) throws IOException{
        File tmp = File.createTempFile(prefix, suffix);
        OutputStream os = new FileOutputStream(tmp);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        return tmp;
    }

    public static void dirAllFontFiles(File file,List<File> flist){
        if(file != null && file.isDirectory()){
            File[] files = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".ttf") || lower.endsWith(".ttc") || lower.endsWith(".otf");
                }});

            for(File f:files){
                if(f.isFile()){
                    flist.add(f);
                }else{
                    dirAllFontFiles(f,flist);
                }
            }

        }else{
            flist.add(file);
        }
    }


    @Override
    public PooledObject<OpenhttptopdfRenderBuilder> makeObject() {
        FSCacheEx<String, FSCacheValue> fsCacheEx = new FSDefaultCacheStore();
        OpenhttptopdfRenderBuilder openhttptopdfRenderBuilder = new OpenhttptopdfRenderBuilder();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
        builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        builder.useSVGDrawer(new BatikSVGDrawer());
        builder.useMathMLDrawer(new MathMLDrawer());
        builder.addDOMMutator(LaTeXDOMMutator.INSTANCE);
        builder.defaultTextDirection(BaseRendererBuilder.TextDirection.LTR);

        builder.useCacheStore(PdfRendererBuilder.CacheStore.PDF_FONT_METRICS, fsCacheEx);
            fontFSSupplierCache.forEach((key,value)->{
            builder.useFont(value,key, 400, BaseRendererBuilder.FontStyle.NORMAL, true);
        });

        builder.useFastMode();
        XRLog.setLoggingEnabled(false);
        DefaultObjectDrawerFactory factory = new DefaultObjectDrawerFactory();
        factory.registerDrawer("image/barcode", new ZXingObjectDrawer());
        builder.useObjectDrawerFactory(factory);



        openhttptopdfRenderBuilder.setPdfRendererBuilder(builder);

        return new DefaultPooledObject(openhttptopdfRenderBuilder);
    }

    @Override
    public void destroyObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) throws Exception {
        pooledObject.getObject().setActive(false);
    }

    @Override
    public boolean validateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) {
        return pooledObject.getObject().isActive();

    }

    @Override
    public void activateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) throws Exception {
        pooledObject.getObject().setActive(true);
    }

    @Override
    public void passivateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) throws Exception {
        pooledObject.getObject().setActive(false);
    }

    public static LinkedHashSet<String>  getFontNames(){
        return fontNames;
    }
}
