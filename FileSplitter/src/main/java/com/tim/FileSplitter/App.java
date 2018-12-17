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
	 * + split file into manageable chunks ---------DONE
	 * + compile in zip file ---- DONE
	 * + Cleanup and remove old / unused methods 
	 * + Recombine file from chunks, verify no data loss
	 * + Handle other file types besides text ???
	 * + Improve logging message (custom exceptions?)
	 * 
	 * + Integrate into email client?
	 * 
	 * 6. Encrypt data for security (Base64)
	 * 
	 * TODO:
	 * 1. send file attached to email
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
		System.out.println("File length: " + file.length());
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		
		int numOfSplits = 5;
		boolean saveToZip = false;
		
		System.out.println("Parsing file information ...");
		FileSplitterUtil.setConstants(filePath, destination);

		System.out.println("Saving original file ...");
		FileSplitterUtil.saveOrignalFileToSplitDestination(filePath);
		
		System.out.println("Splitting files ...");
		FileSplitterUtil.splitFile(raf, numOfSplits, saveToZip);

		System.out.println("--- Process complete ---");

	}


}
