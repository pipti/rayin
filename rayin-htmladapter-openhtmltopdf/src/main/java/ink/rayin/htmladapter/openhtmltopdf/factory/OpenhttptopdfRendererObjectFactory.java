/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.extend.FSCacheEx;
import com.openhtmltopdf.extend.FSCacheValue;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.impl.FSDefaultCacheStore;
import com.openhtmltopdf.latexsupport.LaTeXDOMMutator;
import com.openhtmltopdf.mathmlsupport.MathMLDrawer;
import com.openhtmltopdf.objects.jfreechart.JFreeChartBarDiagramObjectDrawer;
import com.openhtmltopdf.objects.jfreechart.JFreeChartPieDiagramObjectDrawer;
import com.openhtmltopdf.objects.zxing.ZXingObjectDrawer;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.render.DefaultObjectDrawerFactory;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.openhtmltopdf.util.XRLog;
import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.awt.*;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * OpenhttptopdfRenderBuilder对象工厂
 * 实例化OpenhttptopdfRenderBuilder对象，添加字体
 *
 * @author Janah Wang / 王柱 2019-08-10
 *
 * @version 1.0
 * @since 1.8
 */
public class OpenhttptopdfRendererObjectFactory implements PooledObjectFactory<OpenhttptopdfRenderBuilder> {
    private static Logger logger = LoggerFactory.getLogger(OpenhttptopdfRendererObjectFactory.class);

    private  HashMap<String,File> fontFileCache = new HashMap<>();
    private  static HashMap<String, FSSupplier<InputStream>> fontFSSupplierCache = new HashMap<String,FSSupplier<InputStream>>();
    private  static HashMap<String, byte[]> fontFileCacheIsb = new HashMap<>();
    private static LinkedHashSet<String> fontNames = new LinkedHashSet<>();

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

    private static String cFontPathDirectory;
    /**
     * 连接空闲的最小时间，达到此值后空闲链接将会被移除
     */
    private static long SoftMinEvictableIdleTimeMillis = 30000L;

    @SneakyThrows
    private String readFontPSName(File font) {
        Font f = Font.createFont(Font.TRUETYPE_FONT, font);
        return f.getPSName();
    }
    @SneakyThrows
    private String readFontName(File font) {
        Font f = Font.createFont(Font.TRUETYPE_FONT, font);
        return f.getFontName();
    }

    public static void init() {
        synchronized(OpenhttptopdfRendererObjectFactory.class) {

            factory.FontCache();

            //设置对象池的相关参数
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            //最大空闲数
            poolConfig.setMaxIdle(MaxIdle);
            //最大线程数
            poolConfig.setMaxTotal(MaxTotal);
            //最小线程数
            poolConfig.setMinIdle(MinIdle);

            poolConfig.setSoftMinEvictableIdleTimeMillis(SoftMinEvictableIdleTimeMillis);

            logger.debug("pool param:");
            logger.debug("MaxIdle:" + MaxIdle);
            logger.debug("MaxTotal:" + MaxTotal);
            logger.debug("MinIdle:" + MinIdle);
            //新建一个对象池,传入对象工厂和配置
            objectPool = new GenericObjectPool<OpenhttptopdfRenderBuilder>(factory, poolConfig);

        }

    }

    public static void init(String customizeFontPathDirectory) {
        cFontPathDirectory = customizeFontPathDirectory;
        init();
    }

//    /**
//     * 初始化-线程池参数
//     * @param minIdle 最小线程
//     * @param maxIdle 最大空闲
//     * @param maxTotal 最大线程总数
//     * @throws Exception
//     */
//    public static void init(int minIdle,int maxIdle,int maxTotal) throws Exception {
//        MinIdle = minIdle;
//        MaxIdle = maxIdle;
//        MaxTotal = maxTotal;
//        init();
//    }

    /**
     * 初始化-线程池参数
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @param customizeFontPathDirectory 自定义字体目录，可空
     */
    public static void init(int minIdle,int maxIdle,int maxTotal, String customizeFontPathDirectory) {
        cFontPathDirectory = customizeFontPathDirectory;

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
    @SneakyThrows
    public static OpenhttptopdfRenderBuilder getPdfRendererBuilderInstance() {
        logger.debug("pollActiveNum:" + objectPool.getNumActive());
        return objectPool.borrowObject();
    }

    public static HashMap<String, File> getFontFileCache() throws Exception {
        return factory.fontFileCache;
    }

    public static HashMap<String, FSSupplier<InputStream>> getFSSupplierCache() {
        return fontFSSupplierCache;
    }

    /**
     * 归还openhtpdfRenderObject对象
     * @param openhtpdfRenderObject  openhtpdfRenderObject
     */
    public static void returnPdfBoxRenderer(OpenhttptopdfRenderBuilder openhtpdfRenderObject) {
        if(openhtpdfRenderObject != null && openhtpdfRenderObject.isActive() == true) {
            objectPool.returnObject(openhtpdfRenderObject);
        }
    }

    /**
     * 字体缓存
     */
    @SneakyThrows
    private void FontCache(){
        File fontsLocalDir = null;
        Resource fontsResource = ResourceUtil.getResource("fonts");

        File fontsFile = null;
        URL fontsUrl = null;
        try{
            if(fontsResource.getURL().toString().lastIndexOf(".jar") > 0){
                fontsUrl = fontsResource.getURL();
            }else{
                fontsFile = fontsResource.getFile();
            }
        }catch (FileNotFoundException e){
            logger.warn("No extend fonts and no load extend fonts resources!");
        }

        if(fontsFile != null) {
            fontsLocalDir = fontsFile;

            if (fontsLocalDir != null && fontsLocalDir.isDirectory()) {
                List<File> flist = new ArrayList<File>();
                dirAllFontFiles(fontsLocalDir,flist);

                for (int i = 0; i < flist.size(); i++) {
                    File f = flist.get(i);
                    if (f == null || f.isDirectory()) {
                        break;
                    }
                    logger.debug("add extend local font =>" + f.getAbsolutePath());

                    //将字体写入内存
                    byte[] fontByte = ResourceUtil.getResourceAsByte(f.getAbsolutePath()).toByteArray();

                    final String  fontFileName = f.getName().substring(0,f.getName().indexOf("."));
                    fontFileCacheIsb.put(fontFileName,fontByte);
                    fontNames.add(fontFileName);
                    fontFSSupplierCache.put(fontFileName,new FSSupplier<InputStream>() {
                        @Override
                        public InputStream supply() {
                            //logger.debug("font file name Requesting font：" + fontFileName);
                            return new ByteArrayInputStream(fontFileCacheIsb.get(fontFileName));
                        }
                    });

                }
            }
        }

        JarURLConnection jarURLConnection = null;

        if(fontsUrl != null){
            try {
                jarURLConnection = (JarURLConnection) fontsUrl.openConnection();
                if(jarURLConnection == null){
                    logger.warn("No extend fonts and no load extend fonts resources!");
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

                    if(jarEntry.isDirectory() || ".ttf".indexOf(jarFileName.substring(jarFileName.lastIndexOf(".")).toLowerCase()) < 0){
                    }else{
                        File jarfile = inputStreamToFile(jarFile.getInputStream(jarEntry),jarFileName.substring(jarFileName.lastIndexOf("/"),jarFileName.lastIndexOf(".")),
                                jarFileName.substring(jarFileName.lastIndexOf(".")) );
                        fontTmpPath = jarfile.getAbsolutePath();
                        logger.debug("extend jar font save to tmpdir => " + fontTmpPath);

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
                                logger.debug("read font PSName Requesting font");
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
        //自定义的字体路径
        if(StringUtil.isNotBlank(cFontPathDirectory)){
            Resource defaultFontResource = ResourceUtil.getResource(cFontPathDirectory);
            File cFontsFile = defaultFontResource.getFile();
            //fontsLocalDir = cFontsFile;

            if (cFontsFile.isDirectory()) {
                List<File> flist = new ArrayList<File>();
                dirAllFontFiles(cFontsFile,flist);

                for (int i = 0; i < flist.size(); i++) {
                    File f = flist.get(i);
                    if (f == null || f.isDirectory()) {
                        break;
                    }
                    logger.debug("add extend local font =>" + f.getAbsolutePath());

                    //将字体写入内存
                    byte[] fontByte = ResourceUtil.getResourceAsByte(f.getAbsolutePath()).toByteArray();

                    final String  fontFileName = f.getName().substring(0,f.getName().indexOf("."));
                    fontFileCacheIsb.put(fontFileName,fontByte);
                    fontNames.add(fontFileName);
                    fontFSSupplierCache.put(fontFileName,new FSSupplier<InputStream>() {
                        @Override
                        public InputStream supply() {
                            //logger.debug("font file name Requesting font");
                            return new ByteArrayInputStream(fontFileCacheIsb.get(fontFileName));
                        }
                    });

                }
            }
        }

        //base包默认字体
        Resource defaultFontResource = ResourceUtil.getResource("rayin_default_fonts");
        URL defaultFontUrl = defaultFontResource.getURL();
        if(defaultFontUrl.getPath().indexOf(".jar!") > 0){
            try {
                jarURLConnection = (JarURLConnection) defaultFontUrl.openConnection();
                if(jarURLConnection == null){
                    logger.warn("No default fonts and no load default fonts resources!");
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
                        logger.debug("default font save to tmpdir => " + fontTmpPath);

                        //将字体写入内存
                        byte[] fontByte = ResourceUtil.getResourceAsByte(jarfile.getAbsolutePath()).toByteArray();

                        final String  fontName = readFontName(jarfile);
                        fontFileCacheIsb.put(fontName,fontByte);
                        fontNames.add(fontName);
                        fontFSSupplierCache.put(fontName,new FSSupplier<InputStream>() {
                            @Override
                            public InputStream supply() {
                                logger.debug("read font name Requesting font");
                                return new ByteArrayInputStream(fontFileCacheIsb.get(fontName));
                            }
                        });

                        final String  fontPSName = readFontPSName(jarfile);
                        fontFileCacheIsb.put(fontPSName,fontByte);
                        fontNames.add(fontPSName.replace(" ",""));
                        fontFSSupplierCache.put(fontPSName,new FSSupplier<InputStream>() {
                            @Override
                            public InputStream supply() {
                                logger.debug("read font PSName Requesting font");
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
        }else{
            File cFontsFile = defaultFontResource.getFile();
            //fontsLocalDir = cFontsFile;

            if (cFontsFile.isDirectory()) {
                List<File> flist = new ArrayList<File>();
                dirAllFontFiles(cFontsFile,flist);

                for (int i = 0; i < flist.size(); i++) {
                    File f = flist.get(i);
                    if (f == null || f.isDirectory()) {
                        break;
                    }
                    logger.debug("add default local font =>" + f.getAbsolutePath());

                    //将字体写入内存
                    byte[] fontByte = ResourceUtil.getResourceAsByte(f.getAbsolutePath()).toByteArray();

                    final String  fontFileName = f.getName().substring(0,f.getName().indexOf("."));
                    fontFileCacheIsb.put(fontFileName,fontByte);
                    fontNames.add(fontFileName);
                    fontFSSupplierCache.put(fontFileName,new FSSupplier<InputStream>() {
                        @Override
                        public InputStream supply() {
                            //logger.debug("font file name Requesting font");
                            return new ByteArrayInputStream(fontFileCacheIsb.get(fontFileName));
                        }
                    });

                }
            }
        }

        if(fontNames.size() != 0){
            logger.info("added fonts info：");

            logger.info("ThreadId:" + Thread.currentThread().getId() + ",added fonts info：");
            fontNames.forEach((v)->{
                logger.info(v);
            });
        }


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

    @SneakyThrows
    public static File inputStreamToFile(InputStream inputStream,String prefix,String suffix){
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
                    return lower.endsWith(".ttf");
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
        String i = UUID.randomUUID().toString();
        logger.debug("make OpenhttptopdfRender object：" + i);

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
            builder.useFont(value,key, 500, BaseRendererBuilder.FontStyle.NORMAL, true);
        });
//        List<String> validFileExtensions = new ArrayList();
//        validFileExtensions.add("ttc");
//        validFileExtensions.add("ttf");
//        AutoFont.toBuilder(builder,AutoFont.findFontsInDirectory(Paths.get(ResourceUtil.getResource("fonts").getURI()) , validFileExtensions, true,true));

        builder.useFastMode();
        XRLog.setLoggingEnabled(false);
        DefaultObjectDrawerFactory factory = new DefaultObjectDrawerFactory();
        factory.registerDrawer("image/barcode", new ZXingObjectDrawer());
        factory.registerDrawer("jfreechart/pie", new JFreeChartPieDiagramObjectDrawer());
        factory.registerDrawer("jfreechart/bar", new JFreeChartBarDiagramObjectDrawer());
        factory.registerDrawer("fontwatermark", new WatermarkDrawer(fontFSSupplierCache));
        builder.useObjectDrawerFactory(factory);
        openhttptopdfRenderBuilder.setPdfRendererBuilder(builder);

        return new DefaultPooledObject(openhttptopdfRenderBuilder);
    }

    @Override
    public void destroyObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) {
        //logger.debug("destroyObject" );
        pooledObject.getObject().setActive(false);
    }

    @Override
    public boolean validateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) {
        //logger.debug("validateObject" );
        return pooledObject.getObject().isActive();

    }

    @Override
    public void activateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject) {
        //logger.debug("activateObject");
        pooledObject.getObject().setActive(true);
    }

    @Override
    public void passivateObject(PooledObject<OpenhttptopdfRenderBuilder> pooledObject){
        //logger.debug("passivateObject");
        //pooledObject.getObject().setActive(false);
    }

    public static LinkedHashSet<String>  getFontNames(){
        return fontNames;
    }
}
