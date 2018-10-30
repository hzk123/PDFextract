/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test;

import java.awt.Toolkit;
import java.awt.image.RescaleOp;
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
import org.apache.pdfbox.pdfparser.FDFParser;

import javax.security.auth.x500.X500Principal;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;
import javax.xml.stream.events.EndDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.giaybac.traprange.PDFTableExtractor;
import com.giaybac.traprange.entity.Table;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.CycleDetectingLockFactory.WithExplicitOrdering;

public class getConsole extends PDFTextStripper
{
    /**
     * Instantiate a new PDFTextStripper object.
     *
     * @throws IOException If there is an error loading the properties.
     */

	static int cnt = 0;
	static List<List<Text>> texts = new ArrayList< List<Text>>() ;
	static List<Text> tmp = new ArrayList<Text>();
	static List<Text> row = new ArrayList<Text>();
	static int Now_page = 0;
    public getConsole() throws IOException
    {
    }
    /**
     * This will print the documents data.
     * @throws IOException If there is an error parsing the document.
     */
    
    static String PDFFilePath = "C:\\22.pdf";
    static String OutputFilepath = "out";
    public static void main( String[] args ) throws IOException
    { 
        {
            PDDocument document = null; 
            try
            {
                BasicConfigurator.configure();
            	File file = new File(PDFFilePath);
            	texts.clear();
            	document = PDDocument.load( file );
            	String res = "";
            	for (int  i = 0 ; i < document.getNumberOfPages() ; i++)
                {
            		tmp.clear();
            		PDFTextStripper stripper = new getConsole();
            		stripper.setSortByPosition( true );
            		stripper.setStartPage( i );
            		stripper.setEndPage( i );
            		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            		stripper.writeText(document, dummy);  
            		if (tmp.isEmpty()) continue;
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
            	OutputStream os = new FileOutputStream(OutputFilepath);
            	System.out.println( res );
            	byte[] bt = res.getBytes();
            	os.write(bt);
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
            	/* only for debug 
            	 * System.out.println( "String[" + text.getXDirAdj() + "," +
                    text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" +
                    text.getXScale() + " height=" + text.getHeightDir() + " space=" +
                    text.getWidthOfSpace() + " width=" +
                    text.getWidthDirAdj() + "]" + text.getUnicode() ); */
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
            
            if (yDifference < 1. ||
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
