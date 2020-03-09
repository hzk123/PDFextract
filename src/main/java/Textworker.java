import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Textworker extends PDFTextStripper {
    public  ArrayList<Text> _tmp;

    public Textworker() throws IOException {
        _tmp = new ArrayList<Text>();
    }

    public ArrayList<Text> getArray(){
        return _tmp;
    }
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
