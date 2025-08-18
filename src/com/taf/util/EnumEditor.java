/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.util;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/**
 * The EnumEditor is a {@link JTable} cell editor that is used to display a
 * {@link JComboBox} containing enum values inside a cell.
 * 
 * @author Adrien Jakubiak
 * @param <T> the enumeration class
 */
public class EnumEditor<T extends Enum<T>> extends JComboBox<T> implements TableCellEditor, TreeCellEditor {

	private static final long serialVersionUID = 1L;

	/** The default value to select when initializing the cell editor. */
	private T defaultValue;

	/** The listeners. */
	protected EventListenerList listeners = new EventListenerList();

	/** The event. */
	protected ChangeEvent event = new ChangeEvent(this);

	/**
	 * Instantiates a new enum editor with a set of values and a default value.
	 *
	 * @param valuesToPresent the values to present
	 * @param defaultValue    the default value
	 */
	public EnumEditor(T[] valuesToPresent, T defaultValue) {
		super(valuesToPresent);
		this.defaultValue = defaultValue;
		addActionListener(e -> stopCellEditing());
	}

	@Override
	public void addCellEditorListener(CellEditorListener cel) {
		listeners.add(CellEditorListener.class, cel);
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	@Override
	public Object getCellEditorValue() {
		return getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null) {
			value = defaultValue;
		}

		setSelectedItem(value);
		return this;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		if (value == null) {
			value = defaultValue;
		}

		setSelectedItem(value);
		return this;
	}

	@Override
	public boolean isCellEditable(EventObject eo) {
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener cel) {
		listeners.remove(CellEditorListener.class, cel);
	}

	@Override
	public boolean shouldSelectCell(EventObject eo) {
		return true; // sometimes false is nicer
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	/**
	 * Fire the editing canceled event for all the cell editor listeners.
	 */
	protected void fireEditingCanceled() {
		CellEditorListener[] aListeners = listeners.getListeners(CellEditorListener.class);
		// Cell listeners are ordered from last to first!
		for (int j = aListeners.length - 1; j >= 0; j -= 1) {
			aListeners[j].editingCanceled(event);
		}
	}

	/**
	 * Fire the editing stopped event for all the cell editor listeners.
	 */
	protected void fireEditingStopped() {
		CellEditorListener[] aListeners = listeners.getListeners(CellEditorListener.class);
		// Cell listeners are ordered from last to first!
		for (int j = aListeners.length - 1; j >= 0; j -= 1) {
			aListeners[j].editingStopped(event);
		}
	}
}
