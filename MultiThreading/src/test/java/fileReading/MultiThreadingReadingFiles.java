package fileReading;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MultiThreadingReadingFiles {
	ArrayList<Integer> list = new ArrayList<>();
	int sum, total = 0,k=1;

	@Test(dataProvider = "readFilePath")
	public synchronized void readFile(String fileName) {
		sum = 0;
		File file = new File(fileName);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = scan.nextLine();
		//System.out.println(s);
		String[] i = s.split(" ");

		for (String s1 : i) {
			//System.out.println(s1);
			sum = sum + Integer.parseInt(s1);
		}

		System.out.println("Current Thread id: " + Thread.currentThread().getId());
		System.out.println("The sum of the file " + k + " is : " + sum);
		k++;

		total = total + sum;
	}

	@AfterClass
	public void printTotal() {
		System.out.println("The total of the sums of the 3 files is : " + total);
	}

	@DataProvider(name = "readFilePath", parallel = true)
	public Object[][] read() {
		FileInputStream fin = null;
		XSSFWorkbook workbook = null;
		try {
			fin = new FileInputStream(
					"./TestData\\IntegerFilePath.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			workbook = new XSSFWorkbook(fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		int r = sheet.getPhysicalNumberOfRows();
		int c = sheet.getRow(0).getLastCellNum();
		Object[][] data = new Object[r][c];
		DataFormatter df = new DataFormatter();
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				data[i][j] = df.formatCellValue(sheet.getRow(i).getCell(j));
			}
		}
		return data;
	}

}
