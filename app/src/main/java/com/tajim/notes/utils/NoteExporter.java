package com.tajim.notes.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class NoteExporter {

    private final Activity activity;
    private String content;
    private String fileType;
    private String fileName;
    private static final int EXPORT_REQUEST_CODE = 1001;

    public NoteExporter(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param content  The string content to save
     * @param fileType "txt", "docx", or "pdf"
     * @param fileName The base name of file without extension
     */
    public void export(String content, String fileType, String fileName) {
        this.content = content;
        this.fileType = fileType;
        this.fileName = fileName;

        String mimeType = "text/plain";
        String suggestedName = fileName + ".txt";

        switch (fileType) {
            case "docx":
                mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                suggestedName = fileName + ".docx";
                break;
            case "pdf":
                mimeType = "application/pdf";
                suggestedName = fileName + ".pdf";
                break;
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, suggestedName);
        activity.startActivityForResult(intent, EXPORT_REQUEST_CODE);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EXPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try (OutputStream outputStream = activity.getContentResolver().openOutputStream(uri)) {
                    if (outputStream == null) {
                        Toast.makeText(activity, "Cannot open output stream", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d("NoteExporter", "Writing content: " + content);
                    if (fileType.equals("txt")) {
                        writeTxt(outputStream);
                    } else if (fileType.equals("docx")) {
                        writeDocx(outputStream);
                    } else if (fileType.equals("pdf")) {
                        writePdf(outputStream);
                    }
                    outputStream.flush();
                    Toast.makeText(activity, fileType.toUpperCase() + " saved successfully.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Failed to save file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void writeTxt(OutputStream out) throws Exception {
        out.write(content.getBytes(StandardCharsets.UTF_8));
    }

    private void writeDocx(OutputStream out) throws Exception {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText(content);
        document.write(out);
        document.close();
    }

    private void writePdf(OutputStream out) throws Exception {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);
        int x = 40;
        int y = 50;
        int lineHeight = (int)(paint.descent() - paint.ascent()) + 10;

        for (String line : content.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += lineHeight;
        }

        pdfDocument.finishPage(page);
        pdfDocument.writeTo(out);
        pdfDocument.close();
    }
}

