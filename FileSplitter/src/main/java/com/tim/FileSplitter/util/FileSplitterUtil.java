package com.tim.FileSplitter.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

public class FileSplitterUtil {

	public static void splitFile(RandomAccessFile raf, int numOfSplits, String destination, String fileExtension) throws IOException {

		long fileLength = raf.length();
		long bytesPerSplit = fileLength / numOfSplits;
		long remainingBytes = fileLength % numOfSplits;
		
		int maxReadBufferSize = 8 * 1024;

		for (int destIndex = 1; destIndex <= numOfSplits; destIndex++) {
			String filename = destination + "-split_" + destIndex + fileExtension;
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


	public static String createDestinationFilename(String path, String destination) {
		String filename = StringUtils.substring(path, path.lastIndexOf('\\') + 1);
		filename = StringUtils.substring(filename, 0, filename.lastIndexOf('.'));
		return destination + filename;
	}

	public static String getFileExtension(String filename) {
		return StringUtils.substring(filename, filename.lastIndexOf('.'));
	}



	
}
