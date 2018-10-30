import java.io.IOException;

public class Main {
	public static void main(String args[]) throws IOException {
		// Change this line to your input PDF file path.
		String pdffilepath = "/your/path/to/pdf/file.pdf";
        // Change this line to your output txt file path.
		String outputpath = "/your/path/to/txt/file.txt";
		PDFTextExtract example = new PDFTextExtract(pdffilepath, outputpath);
		example.process();
	}
}
