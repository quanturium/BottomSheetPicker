package com.quanturium.android.library.bottomsheetpicker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.List;

public class IntentUtils {

	public static void sendFileBrowserIntent(Fragment fragment, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static Uri sendCameraImageIntent(Fragment fragment, int requestCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
			try {
				Uri fileUri = FileUtils.createImageUri(fragment.getContext());

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
					Context context = fragment.getContext();
					List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
					for (ResolveInfo resolveInfo : resInfoList) {
						String packageName = resolveInfo.activityInfo.packageName;
						context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
					}
				}

				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				fragment.startActivityForResult(takePictureIntent, requestCode);
				return fileUri;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Display error
		}

		return null;
	}

	public static Uri sendCameraVideoIntent(Fragment fragment, int requestCode) {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
			try {
				Uri fileUri = FileUtils.createVideoUri(fragment.getContext());

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
					Context context = fragment.getContext();
					List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(takeVideoIntent, PackageManager.MATCH_DEFAULT_ONLY);
					for (ResolveInfo resolveInfo : resInfoList) {
						String packageName = resolveInfo.activityInfo.packageName;
						context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
					}
				}

				takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				fragment.startActivityForResult(takeVideoIntent, requestCode);
				return fileUri;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Display error
		}
		return null;

	}

}
