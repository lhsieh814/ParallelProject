package com.example.matrixmanipulation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private int width, size, numThreads;
    private String sizeS;
    private Button buttonSerial, buttonParallel;
    private ImageView imageView;
    private EditText editSize;
    private Bitmap bitmap;
    private TextView computationTime;
    private CheckBox[] arrayCheckboxes = new CheckBox[5];
    private int[] checkboxesID = {R.id.flipH, R.id.flipV, R.id.rotateCW,
            R.id.rotateCCW, R.id.sort};
    private int[][] images = null;
    private Spinner matrixSpinner, threadSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        width = (width * 90) / 100;

        // Initialization
        buttonSerial = (Button) findViewById(R.id.serialButton);
        buttonParallel = (Button) findViewById(R.id.parallelButton);
        imageView = (ImageView) findViewById(R.id.imageView1);
        computationTime = (TextView) findViewById(R.id.computationTime);
        matrixSpinner = (Spinner) findViewById(R.id.matrix_spinner);
        threadSpinner  = (Spinner) findViewById(R.id.thread_spinner);

        for (int i = 0; i < arrayCheckboxes.length; i++) {
            arrayCheckboxes[i] = (CheckBox) findViewById(checkboxesID[i]);
        }

        // Serial Execution
        buttonSerial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickSerialButton();
            }

        });

        // Parallel Execution
        buttonParallel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickParallelButton();
            }
        });

        // Select image
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    public void clickSerialButton() {
        sizeS = String.valueOf(matrixSpinner.getSelectedItem());

        try {
            size = Integer.parseInt(sizeS);
        } catch (NumberFormatException e) {
            size = 2000;
            editSize.setText("2000");
        }

        numThreads = Integer.parseInt(String.valueOf(threadSpinner.getSelectedItem()));

        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size, size, true);
        images = Utilities.convertMapToArray(scaled);

        long start = System.currentTimeMillis();
        int[][] array = Utilities.matrixManipulation(images,
                arrayCheckboxes[0].isChecked(),
                arrayCheckboxes[1].isChecked(),
                arrayCheckboxes[2].isChecked(),
                arrayCheckboxes[3].isChecked(),
                arrayCheckboxes[4].isChecked(), true, numThreads);
        long end = System.currentTimeMillis();
        Bitmap finalMap = Bitmap.createScaledBitmap(
                Utilities.convertArrayToMap(array), width, width, true);
        imageView.setImageBitmap(finalMap);

        // update original image
        images = array;
        bitmap = finalMap;
        computationTime.setText("Serial Execution Time is: " + (end - start) + "ms.");
    }

    public void clickParallelButton() {
        sizeS = String.valueOf(matrixSpinner.getSelectedItem());

        try {
            size = Integer.parseInt(sizeS);
        } catch (NumberFormatException e) {
            size = 2000;
            editSize.setText("2000");
        }

        numThreads = Integer.parseInt(String.valueOf(threadSpinner.getSelectedItem()));

        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size, size, true);
        images = Utilities.convertMapToArray(scaled);

        long start = System.currentTimeMillis();

        int[][] array = Utilities.matrixManipulation(images,
                arrayCheckboxes[0].isChecked(),
                arrayCheckboxes[1].isChecked(),
                arrayCheckboxes[2].isChecked(),
                arrayCheckboxes[3].isChecked(),
                arrayCheckboxes[4].isChecked(), false, numThreads);

        long end = System.currentTimeMillis();
        Bitmap finalMap = Bitmap.createScaledBitmap(
                Utilities.convertArrayToMap(array), width, width, true);
        imageView.setImageBitmap(finalMap);

        // update original image
        images = array;
        bitmap = finalMap;
        computationTime.setText("Parallel Execution Time is: " + (end - start) + "ms.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView = (ImageView) findViewById(R.id.imageView1);
            bitmap = BitmapFactory.decodeFile(picturePath);
            Bitmap finalMap = Bitmap.createScaledBitmap(bitmap, width, width,
                    true);

            imageView.setImageBitmap(finalMap);
        }
    }
}
