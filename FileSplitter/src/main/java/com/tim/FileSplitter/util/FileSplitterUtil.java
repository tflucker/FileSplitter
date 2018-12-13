package com.tim.FileSplitter.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

public class FileSplitterUtil {

	// filename of of source file, no extension
	public static String sourceFilename = "";

	// file extension of source file, used for each separate file
	public static String fileExtension = "";

	// directory all file pieces are saved in
	public static String outputDestination = "";

	public static void setConstants(String filePath, String destination) {
		outputDestination = destination;
		sourceFilename = StringUtils.substring(filePath, filePath.lastIndexOf('\\') + 1, filePath.lastIndexOf('.'));
		fileExtension = StringUtils.substring(filePath, filePath.lastIndexOf('.'));

	}

	public static void splitFile(RandomAccessFile raf, int numOfSplits, boolean saveToZip) throws IOException {
		long fileLength = raf.length();
		long bytesPerSplit = fileLength / numOfSplits;
		long remainingBytes = fileLength % numOfSplits;

		int maxReadBufferSize = 8 * 1024;

		BufferedOutputStream bw = null;
		ZipOutputStream out = null;

		if (saveToZip) {
			File zip = new File(outputDestination + sourceFilename + "-split.zip");
			out = new ZipOutputStream(new FileOutputStream(zip));
		}

		for (int destIndex = 1; destIndex <= numOfSplits; destIndex++) {
			String filename = "";
			if (saveToZip) {
				filename = sourceFilename + "-split_" + destIndex + fileExtension;
				System.out.println("Creating new file for zip: " + filename);
				ZipEntry e = new ZipEntry(filename);
				out.putNextEntry(e);
			} else {
				filename = outputDestination + sourceFilename + "-split_" + destIndex + fileExtension;
				System.out.println("Creating new file: " + filename);
				bw = new BufferedOutputStream(new FileOutputStream(filename));
			}

			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					if (saveToZip) {
						readWriteToZip(raf, out, numRemainingRead);
					} else {
						readWrite(raf, bw, numRemainingRead);
					}
				}
				if (numRemainingRead > 0) {
					if (saveToZip) {
						readWriteToZip(raf, out, numRemainingRead);
					} else {
						readWrite(raf, bw, numRemainingRead);
					}
				}
			} else {
				if (saveToZip) {
					readWriteToZip(raf, out, bytesPerSplit);
				} else {
					readWrite(raf, bw, bytesPerSplit);
				}
			}
			if (saveToZip) {
				out.closeEntry();
			}
		}

		if (remainingBytes > 0) {
			System.out.println("Creating file to handle leftovers...");
			if (saveToZip) {
				ZipEntry e = new ZipEntry(sourceFilename + "-split_" + (numOfSplits + 1) + fileExtension);
				out.putNextEntry(e);
				readWriteToZip(raf, out, remainingBytes);
				out.closeEntry();
				out.close();
			} else {
				bw = new BufferedOutputStream(new FileOutputStream(
						outputDestination + sourceFilename + "-split_" + (numOfSplits + 1) + fileExtension));
				readWrite(raf, bw, remainingBytes);
				bw.close();
			}
		}
		raf.close();

	}

	public static void splitFileBackup(RandomAccessFile raf, int numOfSplits, boolean saveToZip) throws IOException {

		long fileLength = raf.length();
		long bytesPerSplit = fileLength / numOfSplits;
		long remainingBytes = fileLength % numOfSplits;

		int maxReadBufferSize = 8 * 1024;

		for (int destIndex = 1; destIndex <= numOfSplits; destIndex++) {
			String filename = outputDestination + "-split_" + destIndex + fileExtension;
			System.out.println("Creating new file: " + filename);
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filename));
			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWrite(raf, bw, numRemainingRead);
				}
				if (numRemainingRead > 0) {
					readWrite(raf, bw, numRemainingRead);
				}
			} else {
				readWrite(raf, bw, bytesPerSplit);
			}
			bw.close();
		}

		if (remainingBytes > 0) {
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split." + (numOfSplits + 1)));
			readWrite(raf, bw, remainingBytes);
			bw.close();
		}
		raf.close();
	}

	public static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
		byte[] buff = new byte[(int) numBytes];
		int val = raf.read(buff);
		if (val != -1) {
			bw.write(buff);
		}
	}

	public static void readWriteToZip(RandomAccessFile raf, ZipOutputStream bw, long numBytes) throws IOException {
		byte[] buff = new byte[(int) numBytes];
		int val = raf.read(buff);
		if (val != -1) {
			bw.write(buff);
		}
	}

	public static String createDestinationFilename(String path, String destination) {
		String filename = StringUtils.substring(path, path.lastIndexOf('\\') + 1);
		filename = StringUtils.substring(filename, 0, filename.lastIndexOf('.'));
		return destination + filename;
	}

	public static String getFileExtension(String filename) {
		return StringUtils.substring(filename, filename.lastIndexOf('.'));
	}

}
