package com.tim.FileSplitter.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.stream.Collectors;
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

	public static void splitFile(RandomAccessFile raf, int numOfSplits) throws IOException {
		long fileLength = raf.length();
		long bytesPerSplit = fileLength / numOfSplits;
		long remainingBytes = fileLength % numOfSplits;

		int maxReadBufferSize = 8 * 1024;
		BufferedOutputStream bw = null;

		System.out.println("Expected bytes per split: " + bytesPerSplit);
		System.out.println("Expected bytes for leftover file: " + remainingBytes);

		for (int destIndex = 1; destIndex <= numOfSplits; destIndex++) {
			String filename = outputDestination + sourceFilename + "-split_" + destIndex + ".txt";
			System.out.println("Creating new file: " + filename);
			bw = new BufferedOutputStream(new FileOutputStream(filename));
			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWrite(raf, bw, maxReadBufferSize);
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
			bw = new BufferedOutputStream(new FileOutputStream(
					outputDestination + sourceFilename + "-split_" + (numOfSplits + 1) + ".txt"));
			System.out.println("Creating new file: " + outputDestination + sourceFilename + "-split_"
					+ (numOfSplits + 1) + ".txt");
			readWrite(raf, bw, remainingBytes);
			bw.close();

		}
		bw.close();
		raf.close();

	}

	public static void splitFileForZip(RandomAccessFile raf, int numOfSplits) throws IOException {
		long fileLength = raf.length();
		long bytesPerSplit = fileLength / numOfSplits;
		long remainingBytes = fileLength % numOfSplits;

		int maxReadBufferSize = 8 * 1024;
		ZipOutputStream out = null;

		System.out.println("Expected bytes per split: " + bytesPerSplit);
		System.out.println("Expected bytes for leftover file: " + remainingBytes);

		File zip = new File(outputDestination + sourceFilename + "-split.zip");
		out = new ZipOutputStream(new FileOutputStream(zip));

		for (int destIndex = 1; destIndex <= numOfSplits; destIndex++) {
			String filename = "";

			filename = sourceFilename + "-split_" + destIndex + ".txt";
			System.out.println("Creating new file for zip: " + filename);
			ZipEntry e = new ZipEntry(filename);
			out.putNextEntry(e);

			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWriteToZip(raf, out, maxReadBufferSize);
				}
				if (numRemainingRead > 0) {
					readWriteToZip(raf, out, numRemainingRead);
				}
			} else {
				readWriteToZip(raf, out, bytesPerSplit);

			}
			out.closeEntry();
		}

		if (remainingBytes > 0) {
			ZipEntry e = new ZipEntry(sourceFilename + "-split_" + (numOfSplits + 1) + ".txt");
			System.out.println("Creating new file for zip: " + e.getName());
			out.putNextEntry(e);
			readWriteToZip(raf, out, remainingBytes);
			out.closeEntry();
			out.close();
		}
		raf.close();
	}

	public static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
		byte[] buff = new byte[(int) numBytes];
		int val = raf.read(buff);
		buff = Base64.getEncoder().encode(buff);
		if (val != -1) {
			bw.write(buff);
		}
	}

	public static void readWriteToZip(RandomAccessFile raf, ZipOutputStream bw, long numBytes) throws IOException {
		byte[] buff = new byte[(int) numBytes];
		int val = raf.read(buff);
		buff = Base64.getEncoder().encode(buff);
		if (val != -1) {
			bw.write(buff);
		}
	}

	public static void reconstructOriginalFile() throws IOException {
		File splitDir = new File(outputDestination);
		File newFile = new File(outputDestination + sourceFilename + "-reconstructed" + fileExtension);

		FileInputStream fin = null;
		BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(newFile));

		int chunkSize = ((8 * 1024)/3) * 4 + 4;
		List<File> fileList = Arrays.asList(splitDir.listFiles()).stream().filter(f -> f.getName().contains("-split_"))
				.collect(Collectors.toList());
		for (File fileEntry : fileList) {
			System.out.println("Reading file: " + fileEntry.getName());
			System.out.println("Size of file: " + fileEntry.length());
			long numReads = fileEntry.length() / Long.valueOf(chunkSize);
			long remainingBytes = fileEntry.length() % Long.valueOf(chunkSize);
			fin = new FileInputStream(fileEntry);
			long bytesRead = 0L;
			if (fileEntry.length() > Long.valueOf(chunkSize)) {
				for (long i = 0L; i < numReads; i++) {
					byte[] bytes = new byte[chunkSize];
					fin.read(bytes);
					bytes = Base64.getDecoder().decode(bytes);
					bw.write(bytes);
					bytesRead = bytesRead + chunkSize;
				}
			}
			if (remainingBytes > 0L) {
				byte[] bytes = new byte[(int) remainingBytes];
				fin.read(bytes);
				bytes = Base64.getDecoder().decode(bytes);
				bw.write(bytes);
				bytesRead = bytesRead + remainingBytes;

			}
			fin.close();
			System.out.println("Bytes read: " + bytesRead);
		}
		bw.close();
	}

	public static void saveOrignalFileToSplitDestination(String sourcePath) throws IOException {
		File originalFile = new File(sourcePath);
		System.out.println("Copy of Original file length: " + originalFile.length());
		BufferedOutputStream bw = new BufferedOutputStream(
				new FileOutputStream(outputDestination + sourceFilename + "-original" + fileExtension));

		Files.copy(originalFile.toPath(), bw);
		bw.close();

	}

}
