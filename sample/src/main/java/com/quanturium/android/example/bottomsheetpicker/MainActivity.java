package com.quanturium.android.example.bottomsheetpicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.quanturium.android.library.bottomsheetpicker.BottomSheetPickerFragment;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomSheetPickerFragment.BottomSheetPickerListener {

	private final static int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;

	private BottomSheetPickerFragment bottomSheetPickerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			final BottomSheetPickerFragment fragment = (BottomSheetPickerFragment) getSupportFragmentManager().findFragmentByTag("picker");
			if (fragment != null) {
				fragment.setListener(this);
			}
		}

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(v -> {

			// Here, thisActivity is the current activity
			if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

				// Should we show an explanation?
				if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					Toast.makeText(MainActivity.this, "Permission required to access files", Toast.LENGTH_SHORT).show();
				} else {
					// No explanation needed, we can request the permission.
					ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
				}
			} else {
				showBottomSheetPicker();
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_EXTERNAL_STORAGE:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					showBottomSheetPicker();
				} else {
					Toast.makeText(MainActivity.this, "Permission required to access files", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void showBottomSheetPicker() {
		bottomSheetPickerFragment = new BottomSheetPickerFragment.Builder()
				.setSelectionMode(BottomSheetPickerFragment.SelectionMode.IMAGES_AND_VIDEOS)
				.build();

		bottomSheetPickerFragment.setListener(this);
		bottomSheetPickerFragment.show(getSupportFragmentManager(), "picker");
	}

	@Override
	public void onFileLoad(ImageView imageView, Uri uri) {
		Glide.with(this)
				.load(uri)
				.placeholder(R.drawable.thumbnail_loading)
				.centerCrop()
				.crossFade()
				.into(imageView);
	}

	@Override
	public void onFilesSelected(List<Uri> uriList) {
		if (bottomSheetPickerFragment != null) {
			bottomSheetPickerFragment.dismiss();
			Toast.makeText(getApplicationContext(), "# selected files: " + uriList.size(), Toast.LENGTH_LONG).show();
		}
	}
}
