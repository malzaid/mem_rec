package org.grouplens.lenskit.hello;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.sun.org.apache.bcel.internal.generic.SWAP;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class WriteExcel {

  private WritableCellFormat timesBoldUnderline;
  private WritableCellFormat times;
  private String inputFile;
  
public void setOutputFile(String inputFile) {
  this.inputFile = inputFile;
  }

  public void write(long[] userId, long [] time, int dataset, int database, int algorithm) throws IOException, WriteException {
    File file = new File(inputFile);
    WorkbookSettings wbSettings = new WorkbookSettings();

    wbSettings.setLocale(new Locale("en", "EN"));

    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
    workbook.createSheet("Report", 0);
    WritableSheet excelSheet = workbook.getSheet(0);
    createLabel(excelSheet);
    createContent(excelSheet, userId, time, dataset, database, algorithm);

    workbook.write();
    workbook.close();
  }

  private void createLabel(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    // create create a bold font with unterlines
    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
        UnderlineStyle.SINGLE);
    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);
    cv.setAutosize(true);

    // Write a few headers
    addCaption(sheet, 0, 0, "UserID");
    addCaption(sheet, 1, 0, "Run time");
    addCaption(sheet, 2, 0, "Dataset");
    addCaption(sheet, 3, 0, "Database");
    addCaption(sheet, 4, 0, "Algorithm");

  }

   
  private void createContent(WritableSheet sheet,long[] userId, long [] time, int dataset, int database, int algorithm) throws WriteException,
  RowsExceededException {
// Write a few number
for (int i = 0; i < userId.length; i++) {
  // First column
  addNumber(sheet, 0, i+1, userId[i]);
  // Second column
  addNumber(sheet, 1, i+1, time[i]);
}
// Lets calculate the sum of it
StringBuffer buf = new StringBuffer();
buf.append("SUM(A2:A"+userId.length+")");
Formula f = new Formula(0, userId.length, buf.toString());
//sheet.addCell(f);
buf = new StringBuffer();
buf.append("AVERAGE(B2:B"+(userId.length+1)+")");
f = new Formula(1, userId.length+1, buf.toString());
sheet.addCell(f);


if(algorithm==1)
	addLabel(sheet, 4, 1, "Item");
else
	addLabel(sheet, 4, 1, "User");

switch (database) {
case 1:
	addLabel(sheet, 3, 1, "Postgre");
	break;

case 2:
	addLabel(sheet, 3, 1, "MonetDB");
	break;

case 3:
	addLabel(sheet, 3, 1, "VoltDB");
	break;

default:
	break;
}

switch (dataset) {
case 0:
	addLabel(sheet, 2, 1, "100k");
	break;

case 1:
	addLabel(sheet, 2, 1, "1Mil");
	break;
case 2:
	addLabel(sheet, 2, 1, "20mil");
	break;
default:
	break;
}
// now a bit of text
// for (int i = 12; i < 20; i++) {
  // First column
//  addLabel(sheet, 0, i, "Boring text " + i);
  // Second column
 // addLabel(sheet, 1, i, "Another text");
//}
}

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
    Label label;
    label = new Label(column, row, s, timesBoldUnderline);
    sheet.addCell(label);
  }

  private void addNumber(WritableSheet sheet, int column, int row,
      Integer integer) throws WriteException, RowsExceededException {
    Number number;
    number = new Number(column, row, integer, times);
    sheet.addCell(number);
  }
  
  private void addNumber(WritableSheet sheet, int column, int row,
	      long integer) throws WriteException, RowsExceededException {
	    Number number;
	    number = new Number(column, row, integer, times);
	    sheet.addCell(number);
	  }

  private void addLabel(WritableSheet sheet, int column, int row, String s)
      throws WriteException, RowsExceededException {
    Label label;
    label = new Label(column, row, s, times);
    sheet.addCell(label);
  }
}