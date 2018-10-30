
package test;

import java.awt.Toolkit;
import java.awt.image.RescaleOp;
import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.math.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.ScratchFile;
import org.apache.pdfbox.pdfparser.FDFParser;

import javax.security.auth.x500.X500Principal;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;
import javax.xml.stream.events.EndDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.giaybac.traprange.PDFTableExtractor;
import com.giaybac.traprange.entity.Table;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.util.concurrent.FutureCallback;

public class Main
{
	public static void main(String args[]) throws IOException
    {
		String pdffilepath = "C://22.pdf";
		String outputpath = "out.txt";
		getConsole test = new getConsole(pdffilepath,outputpath);
    }
  
 
}
