package com.rayin.htmladapter.openhtmltopdf.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.ghost4j.converter.ConverterException;
import org.ghost4j.converter.PDFConverter;
import org.ghost4j.converter.PSConverter;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.document.PSDocument;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSizeName;
import java.io.*;
import java.util.Arrays;

public class PDFPrintService {
    public static void pdf2ps(String pdfPath, String psPath) throws PrintException, IOException {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, psMimeType);
        if (factories.length == 0) {
            System.err.println("No suitable factories");
            System.exit(0);
        }
        FileOutputStream outStream = new FileOutputStream(psPath);
        StreamPrintService printService = factories[0].getPrintService(outStream);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(MediaSizeName.);
        //aset.add(MediaTray.MANUAL);
        //aset.add(MediaSizeName.ISO_A4);
        PDDocument doc = PDDocument.load(new File(pdfPath));

        SimpleDoc pdfDoc = new SimpleDoc(new PDFPrintable(doc, Scaling.ACTUAL_SIZE, false), flavor, null);

        DocPrintJob newJob = printService.createPrintJob();
        newJob.print(pdfDoc, aset);

        outStream.close();
    }

    public static ByteArrayOutputStream pdf2ps(InputStream fos) throws PrintException, IOException {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, psMimeType);
        if (factories.length == 0) {
            System.err.println("No suitable factories");
            System.exit(0);
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        StreamPrintService printService = factories[0].getPrintService(outStream);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(MediaSizeName.);
        //aset.add(MediaTray.MANUAL);
        PDDocument doc = PDDocument.load(fos);

        SimpleDoc pdfDoc = new SimpleDoc(new PDFPrintable(doc, Scaling.ACTUAL_SIZE, false), flavor, null);

        DocPrintJob newJob = printService.createPrintJob();
        newJob.print(pdfDoc, aset);

        return outStream;
    }


    public static void file2print(String filePath) {
        /* Construct the print request specification.
         * The print data is Postscript which will be
         * supplied as a stream.  The media size
         * required is A4, and 2 copies are to be printed
         */
        DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
        PrintRequestAttributeSet aset =
                new HashPrintRequestAttributeSet();
        aset.add(MediaSizeName.ISO_A4);
        //aset.add(new Copies(2));
        //不支持双面打印的打印机无法找到
        // aset.add(Sides.TWO_SIDED_LONG_EDGE);
        // aset.add(Finishings.STAPLE);
        // aset.add(new PageRanges(2));

        /* locate a print service that can handle it */
        PrintService[] pservices =
                PrintServiceLookup.lookupPrintServices(flavor, aset);
        Arrays.asList(pservices).forEach(item -> System.out.println("selected printer " + item.getName()));



        if (pservices.length > 0) {
            //System.out.println("selected printer " + pservices[0].getName());
            Media med[] = (Media[])pservices[0].getSupportedAttributeValues(Media.class, null, null);
            for (int k=0; k<med.length; k++) {
                System.out.println("Name : " + med[k].getClass() + " - Value : " + med[k].getValue() +
                        " - Name : " + med[k].getName() );
            }

            /* create a print job for the chosen service */
            DocPrintJob pj = pservices[0].createPrintJob();

            try {
                /*
                 * Create a Doc object to hold the print data.
                 * Since the data is postscript located in a disk file,
                 * an input stream needs to be obtained
                 * BasicDoc is a useful implementation that will if requested
                 * close the stream when printing is completed.
                 */
                FileInputStream fis = new FileInputStream(filePath);
                Doc doc = new SimpleDoc(fis, flavor, null);

                /* print the doc as specified */
                pj.print(doc, aset);

                /*
                 * Do not explicitly call System.exit() when print returns.
                 * Printing can be asynchronous so may be executing in a
                 * separate thread.
                 * If you want to explicitly exit the VM, use a print job
                 * listener to be notified when it is safe to do so.
                 */

            } catch (IOException ie) {
                System.err.println(ie);
            } catch (PrintException e) {
                System.err.println(e);
            }
        }
    }


//    public static void pdf2print(String pdfPath){
//        /* Construct the print request specification.
//         * The print data is Postscript which will be
//         * supplied as a stream.  The media size
//         * required is A4, and 2 copies are to be printed
//         */
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
//        PrintRequestAttributeSet aset =
//                new HashPrintRequestAttributeSet();
//        aset.add(MediaSizeName.ISO_A4);
//        //aset.add(new Copies(2));
//        //不支持双面打印的打印机无法找到
//        // aset.add(Sides.TWO_SIDED_LONG_EDGE);
//        aset.add(Finishings.STAPLE);
//        // aset.add(new PageRanges(2));
//
//        /* locate a print service that can handle it */
//        PrintService[] pservices =
//                PrintServiceLookup.lookupPrintServices(flavor, aset);
//        Arrays.asList(pservices).forEach(item->System.out.println("selected printer " + item.getName()));
//
//        if (pservices.length > 0) {
//            //System.out.println("selected printer " + pservices[0].getName());
//
//            /* create a print job for the chosen service */
//            DocPrintJob pj = pservices[0].createPrintJob();
//            try {
//                /*
//                 * Create a Doc object to hold the print data.
//                 * Since the data is postscript located in a disk file,
//                 * an input stream needs to be obtained
//                 * BasicDoc is a useful implementation that will if requested
//                 * close the stream when printing is completed.
//                 */
//                FileInputStream fis = new FileInputStream(psPath);
//                Doc doc = new SimpleDoc(fis, flavor, null);
//
//                /* print the doc as specified */
//                pj.print(doc, aset);
//
//                /*
//                 * Do not explicitly call System.exit() when print returns.
//                 * Printing can be asynchronous so may be executing in a
//                 * separate thread.
//                 * If you want to explicitly exit the VM, use a print job
//                 * listener to be notified when it is safe to do so.
//                 */
//
//            } catch (IOException ie) {
//                System.err.println(ie);
//            } catch (PrintException e) {
//                System.err.println(e);
//            }
//        }
//    }


    public static void pdf2ps2(String pdfPath, String psPath) throws IOException, ConverterException, DocumentException {
        PDFDocument document = new PDFDocument();
        document.load(new File(pdfPath));
        //create OutputStream
        FileOutputStream outStream = new FileOutputStream(new File(psPath));

        PSConverter converter = new PSConverter();
        //set options
        converter.setLanguageLevel(3);
        //convert
        converter.convert(document, outStream);
    }

    public static void ps2pdf(String psPath,String pdfPath) throws IOException, ConverterException, DocumentException {
        //load PostScript document
        PSDocument document = new PSDocument();

        document.load(new File(psPath));
        //create OutputStream
        FileOutputStream fos  =new FileOutputStream(new File(pdfPath));
        //create converter
        PDFConverter converter = new PDFConverter();
        //set options
        converter.setPDFSettings(PDFConverter.OPTION_PDFSETTINGS_PREPRESS);
        //convert
        converter.convert(document,fos);
    }

//    public static void pdf2ps(String pdfPath,String psPath) throws PrintException, IOException {
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
//        String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
//        StreamPrintServiceFactory[] factories =
//                StreamPrintServiceFactory.lookupStreamPrintServiceFactories(
//                        flavor, psMimeType);
//        if (factories.length == 0) {
//            System.err.println("No suitable factories");
//            System.exit(0);
//        }
//        FileInputStream fis = new FileInputStream(pdfPath);
//        FileOutputStream outStream = new FileOutputStream(psPath);
//        StreamPrintService sps = factories[0].getPrintService(outStream);
//
//        /* Create and call a Print Job for the GIF image */
//        DocPrintJob pj = sps.createPrintJob();
//        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//        aset.add(new Copies(2));
//        aset.add(MediaSizeName.ISO_A4);
//        aset.add(Sides.TWO_SIDED_LONG_EDGE);
//        aset.add(Finishings.STAPLE);
//
//        Doc doc = new SimpleDoc(fis, flavor, null);
//
//        pj.print(doc, aset);
//        outStream.close();
//        //return outStream;
//    }

//    public static void main(String args[]) {
//
//        /* Use the pre-defined flavor for a GIF from an InputStream */
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
//
//        /* Specify the type of the output stream */
//        String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
//
//        /* Locate factory which can export a GIF image stream as Postscript */
//        StreamPrintServiceFactory[] factories =
//                StreamPrintServiceFactory.lookupStreamPrintServiceFactories(
//                        flavor, psMimeType);
//        if (factories.length == 0) {
//            System.err.println("No suitable factories");
//            System.exit(0);
//        }
//
//        try {
//            /* Load the file */
//            FileInputStream fis = new FileInputStream("java2dlogo.gif");
//            /* Create a file for the exported postscript */
//            String filename = "newfile.ps";
//            FileOutputStream fos = new FileOutputStream(filename);
//
//            /* Create a Stream printer for Postscript */
//            StreamPrintService sps = factories[0].getPrintService(fos);
//
//            /* Create and call a Print Job for the GIF image */
//            DocPrintJob pj = sps.createPrintJob();
//            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//           // aset.add(new Copies(2));
//            aset.add(MediaSizeName.ISO_A4);
//            aset.add(Sides.TWO_SIDED_LONG_EDGE);
//            aset.add(Finishings.STAPLE);
//
//            Doc doc = new SimpleDoc(fis, flavor, null);
//
//            pj.print(doc, aset);
//            fos.close();
//
//        } catch (PrintException pe) {
//            System.err.println(pe);
//        } catch (IOException ie) {
//            System.err.println(ie);
//        }
//    }


    public  void testConvertWithPS(String pdfPath) throws Exception {

        PSDocument document = new PSDocument();
        document.load(new FileInputStream(pdfPath));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PDFConverter converter = new PDFConverter();
        converter.convert(document, baos);

        //assertTrue(baos.size() > 0);

        baos.close();
    }
}
