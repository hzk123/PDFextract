package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
 
public class GetPos extends PDFTextStripper {
 
	private String key;
	private PDDocument document;
	private List<float[]> list = new ArrayList<float[]>();
	private List<float[]> pagelist = new ArrayList<float[]>();
 
	public GetPos(String _key, PDDocument d) throws IOException {
		super();
		super.setSortByPosition(true);
		this.document = d;
		this.key = _key;
	}
 
	public String getKey() {
		return key;
	}
 
	public void setKey(String key) {
		this.key = key;
	}
 
	public PDDocument getDocument() {
		return document;
	}
 
	public void setSrc(PDDocument d) {
		this.document = d;
	}
 
	public List<float[]> getPosition() throws IOException {
		try {
			int pages = document.getNumberOfPages();
			
			for (int i = 1; i <= pages; i++) {
				pagelist.clear();
				super.setSortByPosition(true);
				super.setStartPage(i);
				super.setEndPage(i);
				System.out.println( i + " : ");
				Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
				super.writeText(document, dummy);
				for (float[] li : pagelist) {
					li[2] = i;
				}
				list.addAll(pagelist);
			}
			return list;
 
		} finally {
			if (document != null) {
				document.close();
			}
		}
 
	}
 
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
		for (int i = 0; i < textPositions.size(); i++) {
 
			String str = textPositions.get(i).getUnicode();
			if (str.equals(key.charAt(0) + "")) {
				for (int j = 1; j < key.length() && i + j < textPositions.size() ; j++) 
					str += textPositions.get(i + j).getUnicode();
				if (str.equals(key)) {
					
					//System.out.println( str + " " + key);
					float[] idx = new float[3];
					idx[0] = textPositions.get(i).getXDirAdj();
					idx[1] = textPositions.get(i).getYDirAdj();
				//	idx[3] = textPositions.get(i).getUnicode();
					
					
					//System.out.println(idx[0] + " " + idx[1]);
					int st , ed;
					//while ( st > 0 && textPositions.get(st).getYDirAdj() == idx[1] ) st--;
					//while ( ed < textPositions.size() && textPositions.get(ed).getYDirAdj() == idx[1]) ed++;
					for ( st = i ; st > 0 && textPositions.get(st).getYDirAdj() == textPositions.get(i).getYDirAdj() ; st--) ;
					for ( ed = i ; ed < textPositions.size() - 1 && textPositions.get(ed).getYDirAdj() == textPositions.get(i).getYDirAdj() ; ed++) ;
					for (int k = st ; k <= ed  ; k++)
					System.out.print(textPositions.get(k).getUnicode());
					System.out.println("");
				
				pagelist.add(idx);
				}
			}
 
		}
	}
}
