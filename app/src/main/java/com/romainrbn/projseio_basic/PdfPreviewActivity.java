package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPreviewActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;

    String patientName;
    String doctorEmail;
    int score;

    int pageWidth = 612;
    int pageHeight = 792;

    Bitmap bmp, scaledBitmap;

    /*public PdfPreviewActivity(String patientName, String doctorEmail, int score) {
        this.patientName = patientName;
        this.doctorEmail = doctorEmail;
        this.score = score;
    }

    public PdfPreviewActivity() {

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        setTitle(R.string.pdfPreviewTitle);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_header);
        int bmpWidth = (int) Math.round((pageWidth * 0.4) / 2.0);
        int bmpHeight = (int) Math.round((pageHeight * 0.33) / 2.4);
        scaledBitmap = Bitmap.createScaledBitmap(bmp, bmpWidth, bmpHeight, false);

        this.patientName = getIntent().getStringExtra("patientName");
        this.doctorEmail = getIntent().getStringExtra("doctorEmail");
        this.score = getIntent().getIntExtra("score", 0);

        pdfView = findViewById(R.id.pdfView);

        createPDF();

        pdfView.fromFile(file).load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.close_quiz_btn) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPDF() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = myPdfDocument.startPage(pageInfo);

        String q1 = getIntent().getStringExtra("q1");
        String q2 = getIntent().getStringExtra("q2");
        String q3 = getIntent().getStringExtra("q3");
        String q4 = getIntent().getStringExtra("q3");
        String q5 = getIntent().getStringExtra("q5");

        String i1 = getIntent().getStringExtra("i1");
        String i2 = getIntent().getStringExtra("i2");
        String i3 = getIntent().getStringExtra("i3");
        String i4 = getIntent().getStringExtra("i4");
        String i5 = getIntent().getStringExtra("i5");

        Date date = new Date();
        String dateString = DateFormat.format("dd/MM/yyyy", date).toString();


        Canvas canvas = page.getCanvas();
        canvas.setDensity(500);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(getString(R.string.pdfTitle), 270, 20, myPaint);
        myPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawBitmap(scaledBitmap, 270, 40, myPaint);
        canvas.drawText(getString(R.string.pdfPatientName, patientName), 40, 200, myPaint);
        canvas.drawText(getString(R.string.pdfDoctorName, doctorEmail), 40, 230, myPaint);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(getString(R.string.pdfScore, score), 40, 260, myPaint);
        myPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText("Date : " + dateString, 40, 290, myPaint);

        canvas.drawText("Question 1 : " + i1 + " : " + q1, 40, 310, myPaint);
        canvas.drawText("Question 2 : " + i2 + " : " + q2, 40, 330, myPaint);
        canvas.drawText("Question 3 : " + i3 + " : " + q3, 40, 350, myPaint);
        canvas.drawText("Question 4 : " + i4 + " : " + q4, 40, 370, myPaint);
        canvas.drawText("Question 5 : " + i5 + " : " + q5, 40, 390, myPaint);

        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        myPaint.setColor(Color.GRAY);
        canvas.drawText(getString(R.string.pdfDisclaimer), 40, 750, myPaint);

        myPdfDocument.finishPage(page);

        // On enlève tous les espaces du nom entré par l'utilisateur pour l'utiliser dans un nom de fichier
        String patientNameWithoutSpaces = patientName.replaceAll("\\W+","");

        file = new File(this.getExternalFilesDir("/"), "ResultatTest_" + patientNameWithoutSpaces + ".pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();
    }
}