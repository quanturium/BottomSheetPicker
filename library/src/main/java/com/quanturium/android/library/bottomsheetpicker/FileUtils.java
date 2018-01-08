package com.quanturium.android.library.bottomsheetpicker;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;

	public static Uri createImageUri(Context context) throws IOException {
		Uri result = null;
		File file = null;
		try {
			file = createMediaFile(context, 1);
		} catch (IOException e) {
			return null;
		}

		if (file != null) {
			result = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), file);
		}
		return result;
	}

	public static Uri createVideoUri(Context context) throws IOException {
		Uri result = null;
		File file = null;
		try {
			file = createMediaFile(context, 2);
		} catch (IOException e) {
			return null;
		}

		if (file != null) {
			result = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), file);
		}
		return result;
	}

	private static File createMediaFile(Context context, int type) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

		File file = null;
		if (type == MEDIA_TYPE_IMAGE) {
			String imageFileName = "JPEG_" + timeStamp + "_";
			file = File.createTempFile(
					imageFileName,
					".jpg",
					storageDir
			);
		} else if (type == MEDIA_TYPE_VIDEO) {
			String imageFileName = "MP4_" + timeStamp + "_";
			file = File.createTempFile(
					imageFileName,
					".mp4",
					storageDir      
			);
		}

		return file;
	}

}
