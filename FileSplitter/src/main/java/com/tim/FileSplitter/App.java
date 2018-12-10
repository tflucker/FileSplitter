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
	 * 1. data loss when recombining files 
	 * 2. sending email from java? 3. how to
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

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\user.name\\Desktop\\TestFile.txt";
		String destination = "C:\\Users\\user.name\\Desktop\\FileSplitter\\";

		int numOfSplits = 10;
		File file = new File(path);
		RandomAccessFile raf = new RandomAccessFile(file, "r");

		System.out.println("Generating filename convention ...");
		String fileNameAndPath = FileSplitterUtil.createDestinationFilename(path, destination);
		String fileExtension = FileSplitterUtil.getFileExtension(path);

		System.out.println("Splitting files ...");
		FileSplitterUtil.splitFile(raf, numOfSplits, fileNameAndPath, fileExtension);

		System.out.println("--- Process complete ---");

	}
}
