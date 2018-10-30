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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PDFTextExtract extends PDFTextStripper {

	//private static ArrayList<Text> _tmp = new ArrayList<Text>();
	private  ArrayList<Text> _tmp;

	public String PDFFilePath;
	public String OutputFilepath;

	/**
	 * Instantiate a new PDFTextStripper object.
	 *
	 * @throws IOException If there is an error loading the properties.
	 */	
	public PDFTextExtract(ArrayList<Text> _tmp) throws IOException {
		 this._tmp = _tmp;
	}

	public PDFTextExtract(String PDffilepath, String OutputFilepath) throws IOException {
		this.PDFFilePath = PDffilepath;
		this.OutputFilepath = OutputFilepath;
		 _tmp = new ArrayList<Text>();
	}

	/**
	 * This will parse the documents data.
	 * 
	 * @throws IOException If there is an error parsing the document.
	 */
	public void process() throws IOException {
		PDDocument document = null;
		String res = "";
		OutputStream os = null;
		try {
			// Target PDF file.
			document = PDDocument.load(new File(this.PDFFilePath));

			// Extract Text from PDF ordered by page number.
			for (int i = 1; i <= document.getNumberOfPages(); i++) {
				System.out.println("processing page " + i + "...");
				_tmp.clear();
				PDFTextExtract stripper = new PDFTextExtract(_tmp);
				// Tell PDFBox to sort the text positions.
				stripper.setSortByPosition(true);

				// Extract only one page.
				stripper.setStartPage(i);
				stripper.setEndPage(i);

				// Convert class `textPositions` into `Text`.
				// Save conversion result in `_tmp`.
				Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
				stripper.writeText(document, dummy);

				// Skip if nothing converted.
				if (_tmp.isEmpty())
					continue;

				// Concate result into string.
				for (Text now : _tmp) {
					res += now.unicode;
				}
			}

			// Write result to file.
			os = new FileOutputStream(OutputFilepath);
			os.write(res.getBytes());
			System.out.println("processing completed.");
		} finally {
			if (document != null) {
				document.close();
			}
			if (os != null) {
				os.close();
			}
		}

	}

	/**
	 * Override the default functionality of PDFTextStripper.
	 */
	@Override

	protected void writeString(String _string, List<TextPosition> textPositions) throws IOException {
		for (TextPosition text : textPositions) {
			{
				_tmp.add(new Text(text.getXDirAdj(), text.getYDirAdj(), text.getFontSize(), text.getXScale(),
						text.getHeightDir(), text.getWidthOfSpace(), text.getWidthDirAdj(), text.getUnicode()));
			}
		}
	}

	public class TextPositionComparator implements Comparator<TextPosition> {
		@Override
		public int compare(TextPosition pos1, TextPosition pos2) {
			int cmp1 = Float.compare(pos1.getDir(), pos2.getDir());
			if (cmp1 != 0) {
				return cmp1;
			}

			float x1 = pos1.getXDirAdj();
			float x2 = pos2.getXDirAdj();

			float pos1YBottom = pos1.getYDirAdj();
			float pos2YBottom = pos2.getYDirAdj();

			float pos1YTop = pos1YBottom - pos1.getHeightDir();
			float pos2YTop = pos2YBottom - pos2.getHeightDir();

			float yDifference = Math.abs(pos1YBottom - pos2YBottom);

			if (yDifference < 1. || pos2YBottom >= pos1YTop && pos2YBottom <= pos1YBottom
					|| pos1YBottom >= pos2YTop && pos1YBottom <= pos2YBottom) {
				return Float.compare(x1, x2);
			} else if (pos1YBottom < pos2YBottom) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
