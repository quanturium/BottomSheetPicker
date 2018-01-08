package com.quanturium.android.library.multi_select;

import android.os.Bundle;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of a MultiSelectSelector
 */
public class MultiSelectBaseSelector implements MultiSelectSelector {

	private static final String SELECTION_POSITIONS = "position";
	private SparseBooleanArray mSelections = new SparseBooleanArray();
	private boolean mIsSelectable;

	public MultiSelectBaseSelector() {
	}

	public boolean isSelectable() {
		return mIsSelectable;
	}

	public void setSelectable(boolean isSelectable) {
		mIsSelectable = isSelectable;
	}

	public void setSelected(int position, boolean isSelected) {
		if (!isSelected)
			mSelections.delete(position);
		else
			mSelections.put(position, true);
	}

	public boolean isSelected(int position) {
		return mSelections.get(position);
	}

	public void clearSelectedPositions() {
		mSelections.clear();

	}

	public List<Integer> getSelectedPositions() {
		List<Integer> positions = new ArrayList<>();

		for (int i = 0; i < mSelections.size(); i++) {
			if (mSelections.valueAt(i)) {
				positions.add(mSelections.keyAt(i));
			}
		}

		return positions;
	}

	public int getSelectedCount() {
		return mSelections.size();
	}

	public Bundle saveMultiSelectStates() {
		Bundle information = new Bundle();
		information.putIntegerArrayList(SELECTION_POSITIONS, (ArrayList<Integer>) getSelectedPositions());
		return information;
	}

	public void restoreMultiSelectStates(Bundle savedStates) {
		List<Integer> selectedPositions = savedStates.getIntegerArrayList(SELECTION_POSITIONS);
		if (selectedPositions == null || selectedPositions.size() == 0) return;
		mSelections.clear();
		for (Integer selectedPosition : selectedPositions) {
			mSelections.put(selectedPosition, true);
		}
		mIsSelectable = true;
	}
}