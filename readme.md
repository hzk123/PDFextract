Pdf Extract
use PDFBOX

Ignore Main.class and GetKeyPosstion.class , these are for testing and PDF Chart extracting.

You can create a new Main.class , use this for example

PDFextract ex = new PDFextract(pdffilepath,outputpath);

ex.extract_json();

// ex.extract_txt();

// ex.extract_console(); 

the usage is just like its funtion name
