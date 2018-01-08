package com.quanturium.android.library.multi_select;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.List;

/*
 * UI					MultiSelectManager			Adapter / ViewGroup
 * |							|							|
 * |	MultiSelectSelector		|							|
 * |--------------------------->|							|
 * |							|							|
 * |	MultiSelectListener		|							|
 * |<---------------------------|							|
 * |							|							|
 * |							|	MultiSelectComponent	|
 * |							|-------------------------->|
 * |							|							|
 * |							|	MultiSelectCallback		|
 * |							|<--------------------------|
 * |							|							|
 */
public class MultiSelectManager implements MultiSelectSelector {

	private final MultiSelectSelector multiSelectSelector;
	private MultiSelectComponent multiSelectComponent;
	private MultiSelectListener listener;

	private MultiSelectCallback callback = new MultiSelectCallback() {
		@Override
		public boolean isSelectable() {
			return MultiSelectManager.this.isSelectable();
		}

		@Override
		public boolean isSelected(int position) {
			return MultiSelectManager.this.isSelected(position);
		}

		@Override
		public boolean onSelection(int position) {
			if (multiSelectComponent.isItemSelectable(position)) {
				boolean isCurrentlySelectable = isSelectable();

				multiSelectSelector.setSelected(position, !isSelected(position));
				boolean selectable = getSelectedCount() > 0;
				// If we now have 0 item, disable the selection

				if (isCurrentlySelectable != selectable) {
					multiSelectComponent.setSelectable(selectable);
					multiSelectSelector.setSelectable(selectable);
				}

				multiSelectComponent.setSelected(position, isSelected(position));

				if (listener != null)
					listener.onMultiSelectSelectionChanged(isCurrentlySelectable != selectable, selectable, getSelectedCount());

				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * Create an instance of MultiSelectManager with a default selector
	 */
	public MultiSelectManager() {
		this.multiSelectSelector = new MultiSelectBaseSelector();
	}

	/**
	 * @param multiSelectSelector A custom selector
	 */
	public MultiSelectManager(@NonNull MultiSelectSelector multiSelectSelector) {
		this.multiSelectSelector = multiSelectSelector;
	}

	public void setMultiSelectComponent(MultiSelectComponent multiSelectComponent) {
		this.multiSelectComponent = multiSelectComponent;
		this.multiSelectComponent.setCallback(callback);
	}

	public void setListener(MultiSelectListener multiSelectListener) {
		listener = multiSelectListener;
	}

	@Override
	public boolean isSelectable() {
		return multiSelectSelector.isSelectable();
	}

	@Override
	public void setSelectable(boolean isSelectable) {
		multiSelectSelector.setSelectable(isSelectable);
		multiSelectComponent.setSelectable(isSelectable);
	}

	@Override
	public void setSelected(int position, boolean isSelected) {
		if (multiSelectComponent.isItemSelectable(position)) {
			boolean isCurrentlySelectable = isSelectable();

			multiSelectSelector.setSelected(position, isSelected);
			boolean selectable = getSelectedCount() > 0;

			// If we now have 0 item, disable the selection
			if (isCurrentlySelectable != selectable) {
				multiSelectComponent.setSelectable(selectable);
				multiSelectSelector.setSelectable(selectable);
			}

			multiSelectComponent.setSelected(position, isSelected);

			if (listener != null)
				listener.onMultiSelectSelectionChanged(isCurrentlySelectable != selectable, selectable, getSelectedCount());
		}
	}

	@Override
	public boolean isSelected(int position) {
		return multiSelectSelector.isSelected(position);
	}

	@Override
	public void clearSelectedPositions() {
		boolean isCurrentlySelectable = isSelectable();
		multiSelectSelector.clearSelectedPositions();
		multiSelectSelector.setSelectable(false);
		multiSelectComponent.setSelectable(false);
		if (listener != null)
			listener.onMultiSelectSelectionChanged(isCurrentlySelectable, false, getSelectedCount());
	}

	@Override
	public List<Integer> getSelectedPositions() {
		return multiSelectSelector.getSelectedPositions();
	}

	@Override
	public int getSelectedCount() {
		return multiSelectSelector.getSelectedCount();
	}

	@Override
	public Bundle saveMultiSelectStates() {
		return multiSelectSelector.saveMultiSelectStates();
	}

	@Override
	public void restoreMultiSelectStates(Bundle savedStates) {
		multiSelectSelector.restoreMultiSelectStates(savedStates);
	}

	public interface MultiSelectCallback {

		boolean isSelectable();

		boolean isSelected(int position);

		boolean onSelection(int position);
	}

	public interface MultiSelectListener {
		void onMultiSelectSelectionChanged(boolean stateChanged, boolean isSelectable, int selectedCount);
	}
}
