package com.quanturium.android.library.multi_select;

public interface MultiSelectComponent {

	/**
	 * Select or unselect an element at position
	 *
	 * @param position position of the item
	 * @param isSelected whether it is selected or not
	 */
	void setSelected(int position, boolean isSelected);

	/**
	 * Set the MultiSelect logic in a selectable state
	 *
	 * @param isSelectable whether it is selectable or not
	 */
	void setSelectable(boolean isSelectable);

	/**
	 * Returns true if the item at position is selectable. This allow excluding some items from being selectable
	 *
	 * @param position position of the item
	 * @return true if the item is selectable
	 */
	boolean isItemSelectable(int position);

	/**
	 * Set a callback on the class implementing this interface in order to be able to communicate back with the Manager
	 *
	 * @param multiSelectCallback The MultiSelectCallback instance
	 */
	void setCallback(MultiSelectManager.MultiSelectCallback multiSelectCallback);

}
