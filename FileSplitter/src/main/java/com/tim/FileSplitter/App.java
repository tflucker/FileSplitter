package com.tim.FileSplitter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

import org.apache.commons.lang3.ArrayUtils;

import com.tim.FileSplitter.util.FileSplitterUtil;

public class App {

	/***
	 * 
	 * IDEA: Take any file (Ex. email attachment that is too large
	 * 
	 * + split file into manageable chunks ---------DONE + compile in zip file ----
	 * DONE + Cleanup and remove old / unused methods + Recombine file from chunks,
	 * verify no data loss ------ DONE + Handle other file types besides text ??? +
	 * Improve logging message (custom exceptions?)
	 * 
	 * + Integrate into email client?
	 * 
	 * 6. Encrypt data for security (Base64)
	 * 
	 * TODO: 1. send file attached to email
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

	public static void main(String[] args) throws Exception {
		FileSplitterModel splitter = new FileSplitterModel();
		if (!ArrayUtils.isEmpty(args)) {
			// if string args specified use those values
			splitter = new FileSplitterModel(args[0], args[1], Integer.valueOf(args[2]), Boolean.valueOf(args[3]),
					Boolean.valueOf(args[4]), Boolean.valueOf(args[5]));
		} else {
			// if no values specified use hard coded object
			ClassLoader cl = App.class.getClassLoader();
			String sourceFilePath = Paths.get(cl.getResource("file/sampleImage.jpg").toURI()).toString();
			splitter = new FileSplitterModel(sourceFilePath, "C:\\Users\\Tim Flucker\\Desktop\\FileSplitter\\", 7,
					false, true, true);
		}
		execute(splitter);
	}

	public static void execute(FileSplitterModel splitter) throws IOException {
		File file = new File(splitter.getFileLocation());
		System.out.println("File length: " + file.length());
		RandomAccessFile raf = new RandomAccessFile(file, "r");

		System.out.println("Parsing file information ...");
		FileSplitterUtil.setConstants(splitter.getFileLocation(), splitter.getOutputDirectory());

		if (splitter.isSaveOriginalFile()) {
			System.out.println("Saving original file ...");
			FileSplitterUtil.saveOrignalFileToSplitDestination(splitter.getFileLocation());
		}

		System.out.println("Splitting files ...");
		if (splitter.isSaveToZip()) {
			FileSplitterUtil.splitFileForZip(raf, splitter.getNumSplits());
		} else {
			FileSplitterUtil.splitFile(raf, splitter.getNumSplits());
		}

		if (splitter.isReconstructFile()) {
			System.out.println("Reconstructing file ...");
			FileSplitterUtil.reconstructOriginalFile();
		}

		System.out.println("--- Process complete ---");

	}

}
