package com.tim.FileSplitter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.tim.FileSplitter.util.FileSplitterUtil;

public class App {

	/***
	 * 
	 * IDEA: Take any file (Ex. email attachment that is too large
	 * 
	 * 1. split file into manageable chunks ---------DONE
	 * 
	 * 2. compile in zip file
	 * 
	 * 3. send file attached to email
	 * 
	 * 4. Recombine file from chunks
	 * 
	 * Concerns:
	 * 
	 * 1. data loss when recombining files 2. sending email from java? 3. how to
	 * initiate file recombine
	 * 
	 * STARTING POINT
	 * 
	 * https://stackoverflow.com/questions/19177994/java-read-file-and-split-into-multiple-files
	 * 
	 * @throws IOException
	 * 
	 * 
	 */

	static String filePath = "C:\\Users\\Tim Flucker\\Desktop\\TestFile.txt";
	static String destination = "C:\\Users\\Tim Flucker\\Desktop\\FileSplitter\\";

	public static void main(String[] args) throws IOException {

		File file = new File(filePath);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		
		int numOfSplits = 4;
		boolean saveToZip = true;
		
		System.out.println("Parsing file information ...");
		FileSplitterUtil.setConstants(filePath, destination);

		System.out.println("Splitting files ...");
		FileSplitterUtil.splitFile(raf, numOfSplits, saveToZip);

		System.out.println("--- Process complete ---");

	}


}
