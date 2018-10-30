
package test;

import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.math.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.ScratchFile;

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

public class PDFextract extends PDFTextStripper
{
	int cnt = 0;
	List<List<Text>> texts = new ArrayList< List<Text>>() ;
	List<Text> tmp = new ArrayList<Text>();
	int Now_page = 0;
    
	int StartPage = -1 , EndPage = -1;
	
	String filepath;
	String desString;
	OutputStream fout;
	public PDFextract(String filepath , String desString ) throws IOException
	{
		super();
		this.filepath = filepath;
		this.desString = desString;
		
		fout = new FileOutputStream(desString);
	}
	
	void setpage( int st , int ed)
	{
		StartPage = st;
		EndPage = ed;
	}
	
    void extract_txt() throws InvalidPasswordException, IOException
    {
        {
            PDDocument document = null;     
            try
            {
                BasicConfigurator.configure();
            	File file = new File(filepath);      	
            	texts.clear();
            	document = PDDocument.load( file );    
            	
            	if (StartPage == -1 )
            		StartPage = 0;
            	
            	if (EndPage == -1)
            		EndPage = document.getNumberOfPages();
            	
            	for (int  i = StartPage ; i < EndPage ; i++)
                {
            		tmp.clear();
            		PDFTextStripper stripper = new PDFTextStripper();
            		stripper.setSortByPosition( true );
            		stripper.setStartPage( i );
            		stripper.setEndPage( i );
            		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            		stripper.writeText(document, dummy);                
            		texts.add(tmp);
            		double pre = tmp.get(0).Y;
            		String res = null;
            		for (Text now : tmp) res += now.unicode;
            		byte bt[] = res.getBytes();
            		fout.write(bt);
                }
            	
            	document.close();
            	texts.clear();
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
        }
    }
    
    void extract_console() throws InvalidPasswordException, IOException
    {
    	{
            PDDocument document = null;     
            try
            {
                BasicConfigurator.configure();
            	File file = new File(filepath);      	
            	texts.clear();
            	document = PDDocument.load( file );    
            	
            	if (StartPage == -1 )
            		StartPage = 0;
            	
            	if (EndPage == -1)
            		EndPage = document.getNumberOfPages();
            	
            	String res = null;
            	
            	for (int  i = StartPage ; i < EndPage ; i++)
                {
            		tmp.clear();
            		PDFTextStripper stripper = new PDFextract(filepath,desString);
            		stripper.setSortByPosition( true );
            		stripper.setStartPage( i );
            		stripper.setEndPage( i );
            		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            		stripper.writeText(document, dummy);  
            		texts.add(tmp);
            		double pre = tmp.get(0).Y;
            		for (Text now : tmp)	
            			{
            			 	if ( Math.abs(now.Y - pre) > 1.) 
            			 		res += '\n';
            			 	res += now.unicode;
            			 	pre = now.Y;
            			}

                }
            	System.out.println(StartPage+ " " + EndPage);

            	System.out.println(res);
            	document.close();
            	texts.clear();
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
        }
    }
    
    void extract_json() throws InvalidPasswordException, IOException
    {
    	{
            PDDocument document = null;     
            try
            {
                BasicConfigurator.configure();
            	File file = new File(filepath);      	
            	texts.clear();
            	document = PDDocument.load( file );    
            	
            	if (StartPage == -1 )
            		StartPage = 0;
            	
            	if (EndPage == -1)
            		EndPage = document.getNumberOfPages();
            	
            	String json = null;
            	for (int  i = StartPage ; i < EndPage ; i++)
                {
            		tmp.clear();
            		PDFTextStripper stripper = new PDFTextStripper();
            		stripper.setSortByPosition( true );
            		stripper.setStartPage( i );
            		stripper.setEndPage( i );
            		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            		stripper.writeText(document, dummy);                
            		texts.add(tmp);
            		double pre = tmp.get(0).Y;
            		
            		String res = null;
            		for (Text now : tmp) res += now.unicode;
            		
            		json += " \"page" + i + "\":" +  "\""+  res + "\"";
            		if ( i != EndPage - 1) json += ",";
                }
            	
            	System.out.println(json);
            	document.close();
            	texts.clear();
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
        }
    }
  
    
    /**
     * Override the default functionality of PDFTextStripper.
     */
    @Override
    
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException
    {
        for (TextPosition text : textPositions)
        {
            /*System.out.println( "String[" + text.getXDirAdj() + "," +
                    text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" +
                    text.getXScale() + " height=" + text.getHeightDir() + " space=" +
                    text.getWidthOfSpace() + " width=" +
                    text.getWidthDirAdj() + "]" + text.getUnicode() );*/
           {
        	   		tmp.add(new Text(text.getXDirAdj(), text.getYDirAdj(), text.getFontSize(), 
            		text.getXScale(), text.getHeightDir() ,  text.getWidthOfSpace(),
            		text.getWidthDirAdj(),text.getUnicode())); 
           }
        }
    }
    
 
    
    public class TextPositionComparator implements Comparator<TextPosition>
    {
    	@Override
        public int compare(TextPosition pos1, TextPosition pos2)
        {
            int cmp1 = Float.compare(pos1.getDir(), pos2.getDir());
            if (cmp1 != 0)
            {
                return cmp1;
            }
            
            float x1 = pos1.getXDirAdj();
            float x2 = pos2.getXDirAdj();
            
            float pos1YBottom = pos1.getYDirAdj();
            float pos2YBottom = pos2.getYDirAdj();

            float pos1YTop = pos1YBottom - pos1.getHeightDir();
            float pos2YTop = pos2YBottom - pos2.getHeightDir();

            float yDifference = Math.abs(pos1YBottom - pos2YBottom);
            
            if (yDifference < .5 ||
                pos2YBottom >= pos1YTop && pos2YBottom <= pos1YBottom ||
                pos1YBottom >= pos2YTop && pos1YBottom <= pos2YBottom)
            {
                return Float.compare(x1, x2);
            }
            else if (pos1YBottom < pos2YBottom)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }
}
