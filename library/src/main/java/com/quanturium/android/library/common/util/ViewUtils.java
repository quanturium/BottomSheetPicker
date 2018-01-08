/*
 *  IBM Confidential
 *  OCO Source Materials
 *  5725S57
 *  Copyright IBM Corp. 2016
 *  The source code for this program is not published or otherwise
 *  divested of it trade secrets, irrespective of what has been
 *  deposited with the U.S. Copyright Office.
 */

package com.quanturium.android.library.common.util;

import android.content.res.Resources;

public class ViewUtils {

	public static float pxToDp(float px) {
		float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
		return px / (densityDpi / 160f);
	}

	public static int dpToPx(int dp) {
		float density = Resources.getSystem().getDisplayMetrics().density;
		return Math.round(dp * density);
	}
}