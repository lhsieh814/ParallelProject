package com.example.matrixmanipulation;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private static int RESULT_LOAD_IMAGE = 1;
	int width, size;
	String sizeS;
	Button buttonSerial, buttonParallel;
	ImageView imageView;
	EditText editSize;
	Bitmap bitmap;
	TextView computationTime;
	CheckBox[] arrayCheckboxes = new CheckBox[5];
	int[] checkboxesID = { R.id.flipH, R.id.flipV, R.id.rotateCW,
			R.id.rotateCCW, R.id.sort };
	int[][] images = null;

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
		editSize = (EditText) findViewById(R.id.size);

		for (int i = 0; i < arrayCheckboxes.length; i++) {
			arrayCheckboxes[i] = (CheckBox) findViewById(checkboxesID[i]);
		}

		// Action listener
		editSize.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editSize.setText("");
			}
			
		});
		
		buttonSerial.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sizeS = editSize.getText().toString();
				try {
					size = Integer.parseInt(sizeS);
				} catch (NumberFormatException e) {
					size = 2000;
					editSize.setText("2000");
				}

				if (images == null) {
					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size,
							size, true);
					images = Utilities.convertMapToArray(scaled);
				} else {
					bitmap = Utilities.convertArrayToMap(images);
					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size,
							size, true);
					images = Utilities.convertMapToArray(scaled);
				}

				long start = System.currentTimeMillis();
				int[][] array = Utilities.dothings(images,
						arrayCheckboxes[0].isChecked(),
						arrayCheckboxes[1].isChecked(),
						arrayCheckboxes[2].isChecked(),
						arrayCheckboxes[3].isChecked(),
						arrayCheckboxes[4].isChecked(), true);
				long end = System.currentTimeMillis();
				Bitmap finalMap = Bitmap.createScaledBitmap(
						Utilities.convertArrayToMap(array), width, width, true);
				imageView.setImageBitmap(finalMap);
				// update original image
				images = array;
				computationTime.setText("Serial Execution Time is: "
						+ (end - start) + "ms.");
			}

		});

		buttonParallel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sizeS = editSize.getText().toString();
				try {
					size = Integer.parseInt(sizeS);
				} catch (NumberFormatException e) {
					size = 2000;
					editSize.setText("2000");
				}
				if (images == null) {
					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size,
							size, true);
					images = Utilities.convertMapToArray(scaled);
				} else {
					bitmap = Utilities.convertArrayToMap(images);
					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size,
							size, true);
					images = Utilities.convertMapToArray(scaled);
				}
				long start = System.currentTimeMillis();
				int[][] array = Utilities.dothings(images,
						arrayCheckboxes[0].isChecked(),
						arrayCheckboxes[1].isChecked(),
						arrayCheckboxes[2].isChecked(),
						arrayCheckboxes[3].isChecked(),
						arrayCheckboxes[4].isChecked(), false);
				long end = System.currentTimeMillis();
				Bitmap finalMap = Bitmap.createScaledBitmap(
						Utilities.convertArrayToMap(array), width, width, true);
				imageView.setImageBitmap(finalMap);
				// update original image
				images = array;
				computationTime.setText("Serial Execution Time is: "
						+ (end - start) + "ms.");
			}
		});

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
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

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
