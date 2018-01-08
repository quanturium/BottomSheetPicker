package com.quanturium.android.library.bottomsheetpicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quanturium.android.library.common.util.ViewUtils;
import com.quanturium.android.library.multi_select.MultiSelectComponent;
import com.quanturium.android.library.multi_select.MultiSelectManager;
import com.quanturium.android.library.multi_select.MultiSelectWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetPickerRecyclerAdapter extends CursorRecyclerAdapter<BottomSheetPickerRecyclerAdapter.ViewHolder> implements MultiSelectComponent {
	private final LayoutInflater layoutInflater;
	private final OnRecyclerViewEventListener listener;
	private final boolean isBrowseMoreEnabled;
	private final int maxItems;
	private MultiSelectManager.MultiSelectCallback multiSelectCallback;
	private int selectionViewPadding = ViewUtils.dpToPx(2);

	public BottomSheetPickerRecyclerAdapter(Context context, OnRecyclerViewEventListener listener, boolean browseMoreEnabled, int maxItems) {
		super(null);

		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
		this.isBrowseMoreEnabled = browseMoreEnabled;
		this.maxItems = maxItems;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = layoutInflater.inflate(R.layout.bottomsheetpicker_tile, parent, false);
		MultiSelectWrapper multiSelectWrapper = MultiSelectWrapper.wrap(v);
		multiSelectWrapper.setDrawableOffset(selectionViewPadding,selectionViewPadding);
		return new ViewHolder(multiSelectWrapper);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
		if (!payloads.isEmpty()) {
			if (payloads.get(0) instanceof Integer) {
				holder.multiSelectWrapper.setSelectable(multiSelectCallback.isSelectable(), true);
				holder.multiSelectWrapper.setSelected(multiSelectCallback.isSelected(position), true);
				return;
			}
		}

		super.onBindViewHolder(holder, position, payloads);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
		long id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
		int type = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE));
		Uri fileUri = getUriFromCursor(cursor);

		if (fileUri != null) {
			if (listener != null)
				listener.onTileLoad(holder.thumbnail, fileUri);
			holder.videoIcon.setVisibility(type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ? View.VISIBLE : View.GONE);

			holder.multiSelectWrapper.setSelectable(multiSelectCallback.isSelectable(), false);
			holder.multiSelectWrapper.setSelected(multiSelectCallback.isSelected(cursor.getPosition()), false);
		} else {
			Log.e("BSPRecyclerAdapter", "fileUri = null");
		}
	}

	@Override
	public int getItemCount() {
		int cursorCount = super.getItemCount();
		if (cursorCount >= maxItems) {
			return isBrowseMoreEnabled ? maxItems + 1 : maxItems;
		} else {
			return cursorCount;
		}
	}

	public static Uri getUriFromCursor(Cursor cursor) {
		String fileLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
		return Uri.fromFile(new File(fileLocation));
	}

	public List<Uri> getItems(List<Integer> positionList) {
		List<Uri> itemList = new ArrayList<>();
		int savedPosition = getCursor().getPosition();
		for (Integer p : positionList) {
			getCursor().moveToPosition(p);
			itemList.add(getUriFromCursor(getCursor()));
		}
		getCursor().moveToPosition(savedPosition);
		return itemList;
	}

	private void onTileLongClick(ViewHolder viewHolder) {
		multiSelectCallback.onSelection(viewHolder.getAdapterPosition());
	}

	private void onTileClick(ViewHolder viewHolder) {
		if (!multiSelectCallback.isSelectable() || !multiSelectCallback.onSelection(viewHolder.getAdapterPosition())) {
			if (listener != null) {
				int savedPosition = getCursor().getPosition();
				getCursor().moveToPosition(viewHolder.getAdapterPosition());
				listener.onTileSelected(getUriFromCursor(getCursor()));
				getCursor().moveToPosition(savedPosition);
			}
		}
	}

	@Override
	public void setSelected(int position, boolean isSelected) {
		notifyItemChanged(position, position);
	}

	@Override
	public void setSelectable(boolean isSelectable) {
		notifyItemRangeChanged(0, getItemCount(), -1);
	}

	@Override
	public boolean isItemSelectable(int position) {
		return true;
	}

	@Override
	public void setCallback(MultiSelectManager.MultiSelectCallback multiSelectCallback) {
		this.multiSelectCallback = multiSelectCallback;
	}

	interface OnRecyclerViewEventListener {
		void onTileLoad(ImageView imageView, Uri uri);

		void onTileSelected(Uri uri);
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

		MultiSelectWrapper multiSelectWrapper;
		ImageView thumbnail;
		ImageView videoIcon;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);
			view.setOnLongClickListener(this);

			multiSelectWrapper = (MultiSelectWrapper) view;
			thumbnail = (ImageView) view.findViewById(R.id.bottomsheetpicker_thumbnail);
			videoIcon = (ImageView) view.findViewById(R.id.bottomsheetpicker_video_icon);
		}

		@Override
		public void onClick(View v) {
			onTileClick(this);
		}

		@Override
		public boolean onLongClick(View v) {
			onTileLongClick(this);
			return true;
		}
	}
}
