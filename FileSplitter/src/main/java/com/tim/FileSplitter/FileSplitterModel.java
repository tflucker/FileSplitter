package com.tim.FileSplitter;

public class FileSplitterModel {

	private String fileLocation;

	private String outputDirectory;

	private int numSplits;

	private boolean saveToZip;

	private boolean saveOriginalFile;

	private boolean reconstructFile;

	public FileSplitterModel() {

	}

	public FileSplitterModel(String fileLocation, String outputDirectory, int numSplits, boolean saveToZip,
			boolean saveOriginalFile, boolean reconstructFile) {
		this.fileLocation = fileLocation;
		this.outputDirectory = outputDirectory;
		this.numSplits = numSplits;
		this.saveToZip = saveToZip;
		this.saveOriginalFile = saveOriginalFile;
		this.reconstructFile = reconstructFile;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public int getNumSplits() {
		return numSplits;
	}

	public void setNumSplits(int numSplits) {
		this.numSplits = numSplits;
	}

	public boolean isSaveToZip() {
		return saveToZip;
	}

	public void setSaveToZip(boolean saveToZip) {
		this.saveToZip = saveToZip;
	}

	public boolean isSaveOriginalFile() {
		return saveOriginalFile;
	}

	public void setSaveOriginalFile(boolean saveOriginalFile) {
		this.saveOriginalFile = saveOriginalFile;
	}

	public boolean isReconstructFile() {
		return reconstructFile;
	}

	public void setReconstructFile(boolean reconstructFile) {
		this.reconstructFile = reconstructFile;
	}

}
