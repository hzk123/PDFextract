import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.apache.log4j.PropertyConfigurator;
public class Main {

	static int cnt = 0;
	public static void extract(String pdffilepath,String outputpath,boolean getjson) throws IOException{
		try {
			System.out.println(pdffilepath + " is processing");
			PDFTextExtract ex = new PDFTextExtract(pdffilepath, outputpath, getjson);
			ex.process();
			cnt++;
			System.out.println(cnt + " files complete");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void extract_folder(String folderpath , String output_root) throws  IOException{
		try{
			int fileNum = 0, folderNum = 0;
			File file = new File(folderpath);
			Path folder_path = Paths.get(output_root);
			if (file.exists()) {
				LinkedList<File> list = new LinkedList<File>();
				File[] files = file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						list.add(file2);
					} else {

						if (file2.getAbsolutePath().endsWith(".pdf") == true) {
							extract(file2.getAbsolutePath() ,
									folder_path.resolve(file2.getName().replace(".pdf", ".txt")).toString(),false);
						}
					}
				}
				File temp_file;
				while (!list.isEmpty()) {
					temp_file = list.removeFirst();
					files = temp_file.listFiles();
					for (File file2 : files) {
						if (file2.isDirectory()) {
							list.add(file2);
						} else {
							if (file2.getAbsolutePath().endsWith(".pdf") == true) {
								//System.out.println(output_root + file2.getName().replace(".pdf", ".txt"));
								extract(file2.getAbsolutePath() ,
										folder_path.resolve(file2.getName().replace(".pdf", ".txt")).toString(),false);
							}
						}
					}
				}
			} else {
				System.out.println("文件不存在!");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void main(String args[]) throws IOException {
		PropertyConfigurator.configure("src/main/cfg/log4j.properties");
		// Change this line to your input PDF file path.
		String pdffilepath = "F:\\test2\\large-test.pdf";
		// Change this line to your output txt file path.
		String outputpath = "F:\\test2\\test.txt";
		extract(pdffilepath, outputpath,false);
		//extract_folder("F:\\fasttext-win\\test_input\\" , outputpath);
	}
}
