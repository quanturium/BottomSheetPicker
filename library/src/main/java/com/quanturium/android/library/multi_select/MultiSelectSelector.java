package com.quanturium.android.library.multi_select;

import android.os.Bundle;

import java.util.List;

public interface MultiSelectSelector {

	/**
	 * Checks whether the multiSelect is in a selectable state
	 *
	 * @return true if yes, false otherwise
	 */
	boolean isSelectable();

	/**
	 * Change the state of the multiSelect.
	 *
	 * @param isSelectable true to activate it, false otherwise
	 */
	void setSelectable(boolean isSelectable);

	/**
	 * Select or unselect an item on the multiselect
	 *
	 * @param position position of the item
	 * @param isSelected whether it is selected or not
	 */
	void setSelected(int position, boolean isSelected);

	/**
	 * Check if an item is selected or not
	 *
	 * @param position position of the item
	 * @return true if yes, false otherwise
	 */
	boolean isSelected(int position);

	/**
	 * Clear the list of selected items
	 */
	void clearSelectedPositions();

	/**
	 * Get the list of positions of selected items
	 *
	 * @return the list of position of selected items
	 */
	List<Integer> getSelectedPositions();

	/**
	 * Returns the number of selected items
	 *
	 * @return the count of selected items
	 */
	int getSelectedCount();

	/**
	 * Save the state of multi select into a bundle
	 *
	 * @return a bundle with states
	 */
	Bundle saveMultiSelectStates();

	/**
	 * Restore the state passed as a bundle
	 *
	 * @param savedStates The savedStates bundle
	 */
	void restoreMultiSelectStates(Bundle savedStates);
}
