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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.util.ArrayList;

public class PDFTextExtract {

	//private static ArrayList<Text> _tmp = new ArrayList<Text>();
	private  static  ArrayList<Text> _tmp;

	public String PDFFilePath;
	public String OutputFilepath;
	public String Json_OutputFilepath;
	public boolean getjson;
	/**
	 * Instantiate a new PDFTextStripper object.
	 *
	 * @throws IOException If there is an error loading the properties.
	 */	
	public PDFTextExtract() throws IOException {
	}

	public PDFTextExtract(String PDffilepath, String OutputFilepath, boolean getjson) throws IOException {
		this.PDFFilePath = PDffilepath;
		this.OutputFilepath = OutputFilepath;
		System.out.println(OutputFilepath);
		this.Json_OutputFilepath = OutputFilepath.replace(".txt",".json");
		this.getjson = getjson;
		 _tmp = new ArrayList<Text>();
	}

	/**
	 * This will parse the documents data.
	 * 
	 * @throws IOException If there is an error parsing the document.
	 */
	public void process() throws IOException {
		PDDocument document = null;
		ArrayList<Textline> lines = new ArrayList<Textline>();
		try {
			// Target PDF file.
			document = PDDocument.load(new File(this.PDFFilePath));
			// Extract Text from PDF ordered by page number.
			for (int i = 1; i <= document.getNumberOfPages(); i++) {
				//System.out.println("processing page " + i + "...");
				Textworker stripper = new Textworker();
				// Tell PDFBox to sort the text positions.
				stripper.setSortByPosition(true);
				// Extract only one page.
				stripper.setStartPage(i);
				stripper.setEndPage(i);
				// Convert class `textPositions` into `Text`.
				// Save conversion result in `_tmp`.
				Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
				stripper.writeText(document, dummy);

				_tmp = stripper.getArray();
				// Skip if nothing converted.
				if (_tmp.isEmpty())
					continue;
				double prey = _tmp.get(0).Y;
				// Concate result into string.
				String line = "";
				for (Text now : _tmp) {
					if ( Math.abs(now.Y - prey) > 1.) {
						lines.add(new Textline(line, i , prey));
						//System.out.println(line);
						line = "";
					}
					line += now.unicode;
					prey = now.Y;
				}
				//System.out.println(line);
				lines.add(new Textline(line, i , prey));
			}
			// Write result to file.
			String res = "";
			JSONArray Jarray = new JSONArray();

			for (Textline g:lines) {
				String now_str = g.text.strip();
				if (now_str.isEmpty() == true)
					continue;;
				res += now_str + "\n";
				JSONObject Jobj = new JSONObject();
				Jobj.put("Text" , now_str);
				Jobj.put("Page" , g.page);
				Jobj.put("Y" , g.Y);
				Jarray.add(Jobj);
			}

			File output_file = new File(OutputFilepath);
			FileOutputStream fos = new FileOutputStream(output_file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter writer = new BufferedWriter(osw);
			writer.write(res);
			writer.close();

			if (getjson == true){
				File Joutput_file = new File(Json_OutputFilepath);
				FileOutputStream Jfos = new FileOutputStream(Joutput_file);
				OutputStreamWriter Josw = new OutputStreamWriter(Jfos, "UTF-8");
				BufferedWriter Jwriter = new BufferedWriter(Josw);
				Jwriter.write(Jarray.toJSONString());
				Jwriter.close();
			}
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}
}
