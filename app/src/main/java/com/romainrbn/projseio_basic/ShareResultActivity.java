package com.romainrbn.projseio_basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission;
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShareResultActivity extends AppCompatActivity {

    Switch showPasswordSwitch;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button sendPDFButton;

    String patientName;
    String patientFirstName;
    String patientBirthYear;
    String doctorEmail;
    int score;

    File file;
    File protectedFile;

    int pageWidth = 612;
    int pageHeight = 792;

    Bitmap bmp, scaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_result);
        setTitle(getString(R.string.exportPDFPageTitle));

        showPasswordSwitch = findViewById(R.id.showPasswordSwitch);
        passwordEditText = findViewById(R.id.documentPasswordET);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordET);
        sendPDFButton = findViewById(R.id.exportPDFButton);

        this.patientName = getIntent().getStringExtra("patientName");
        this.patientBirthYear = getIntent().getStringExtra("patientBirthYear");
        this.patientFirstName = getIntent().getStringExtra("patientFirstName");
        this.doctorEmail = getIntent().getStringExtra("doctorEmail");
        this.score = getIntent().getIntExtra("score", 0);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_header);
        int bmpWidth = (int) Math.round((pageWidth * 0.4) / 2.0);
        int bmpHeight = (int) Math.round((pageHeight * 0.33) / 2.4);
        scaledBitmap = Bitmap.createScaledBitmap(bmp, bmpWidth, bmpHeight, false);

        sendPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });

        showPasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    passwordEditText.setInputType(129);
                    confirmPasswordEditText.setInputType(129);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
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

    private String generateUserPassword() {
        String lowercaseFirstNameWithoutSpaces = patientFirstName.toLowerCase().replaceAll("\\s+","");
        String lowerCaseFirstLetterName = Character.toString(patientName.toLowerCase().replaceAll("\\s+", "").charAt(0));
        String noSpacesBirthDate = patientBirthYear.replaceAll("\\s+", "");

        return lowercaseFirstNameWithoutSpaces + lowerCaseFirstLetterName + noSpacesBirthDate;
    }

    private void createPDF() {
        if(passwordEditText.getText().toString().length() == 0 ||
                confirmPasswordEditText.getText().toString().length() == 0 ||
                !passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.alertErrorTitle)
                    .setMessage(R.string.alertMessageNotSamePasswords)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            PdfDocument myPdfDocument = new PdfDocument();
            Paint myPaint = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = myPdfDocument.startPage(pageInfo);
            Date date = new Date();
            String dateString = DateFormat.format("dd/MM/yyyy", date).toString();

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
            String patientNameWithoutSpaces = patientName.replaceAll("\\W+", "");

            SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.FRANCE);
            Date now = new Date();


            file = new File(this.getExternalFilesDir("/"), "TestResult_" + patientNameWithoutSpaces + ".pdf");
            protectedFile = new File(this.getExternalFilesDir("/"), "TestResult_" + patientNameWithoutSpaces + "_" + formatter.format(now) + ".pdf");

            try {
                myPdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            myPdfDocument.close();

            try {
                PDDocument pdd = PDDocument.load(file);
                AccessPermission ap = new AccessPermission();
                StandardProtectionPolicy stpp = new StandardProtectionPolicy(passwordEditText.getText().toString(), generateUserPassword(), ap);
                stpp.setEncryptionKeyLength(128);
                stpp.setPermissions(ap);
                pdd.protect(stpp);
                pdd.save(protectedFile);
                pdd.close();

                sendDocument();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDocument() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {doctorEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.exportPDFEmailSubject, patientName));

        if(!protectedFile.exists()) {
            return;
        }

        Uri fileUri = FileProvider.getUriForFile(ShareResultActivity.this, "com.romainrbn.projseio_basic.provider", protectedFile);
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.exportPDFChooserTitle)));
    }
}