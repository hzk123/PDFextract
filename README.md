# PDF Text Extract
This project is build on [Apache PDFBox library](https://github.com/apache/pdfbox). It only extract text from PDF file, using Cartesian coordinate `(X,Y)` to recover text order when doing extraction.

## Download
```git clone https://github.com/hzk123/test```

## Environment
* JavaSE-1.8
* PDFBox-2.0.11
* Eclipse SimRel 2018-09 Edition

## Execute
1. Using Eclipse IDE to import this project.
2. Open file `src/Main.java`.
3. You will see the following code.
4. Change the variables:
    * Set `pdffilepath` to your input PDF file path.
    * Set `outputpath` to your output text file path.
5. Then press build button to get the extration result.

```java
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
```

# PDF 文字擷取器
這個專案使用 [Apache PDFBox library](https://github.com/apache/pdfbox) 作為開發基礎，功能為從PDF檔案中擷取出純文字內容，並利用笛卡兒座標 `(X,Y)` 將文字順序正確還原。

## 下載
```git clone https://github.com/hzk123/test```

## 執行環境
* JavaSE-1.8
* PDFBox-2.0.11
* Eclipse SimRel 2018-09 Edition

## 執行方法
1. 使用 Eclipse IDE 開啟這個專案。
2. 打開 `src/Main.java` 這個檔案。
3. 您會看到以下的程式碼。
4. 更改以下的變數內容:
    * 將 `pdffilepath` 改為您的輸入 PDF 檔路徑。
    * 將 `outputpath` 改為您的輸出文字檔路徑。
5. 按下建置按鈕即可得到文字擷取的結果。

```java
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
```

## License (see also LICENSE.txt)

Collective work: Copyright 2015 The Apache Software Foundation.

Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and