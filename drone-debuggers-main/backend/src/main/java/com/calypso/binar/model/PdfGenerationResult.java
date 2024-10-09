package com.calypso.binar.model;

public class PdfGenerationResult {
    private byte[] pdfData;
    private String fileName;

    public PdfGenerationResult(byte[] pdfData, String fileName) {
        this.pdfData = pdfData;
        this.fileName = fileName;
    }

    public byte[] getPdfData() {
        return pdfData;
    }

    public void setPdfData(byte[] pdfData) {
        this.pdfData = pdfData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
