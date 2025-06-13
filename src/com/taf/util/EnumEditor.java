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

public class EnumEditor<T extends Enum<T>> extends JComboBox<T> implements TableCellEditor, TreeCellEditor {
	private static final long serialVersionUID = 1L;

	private T defaultValue;
	
	protected EventListenerList listeners = new EventListenerList();
	protected ChangeEvent event = new ChangeEvent(this);

	public EnumEditor(T[] valuesToPresent, T defaultValue) {
		super(valuesToPresent);
		this.defaultValue = defaultValue;
		addActionListener(e -> stopCellEditing());
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
	public Object getCellEditorValue() {
		return getSelectedItem();
	}

	@Override
	public boolean isCellEditable(EventObject eo) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject eo) {
		return true; // sometimes false is nicer
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	@Override
	public void addCellEditorListener(CellEditorListener cel) {
		listeners.add(CellEditorListener.class, cel);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener cel) {
		listeners.remove(CellEditorListener.class, cel);
	}

	protected void fireEditingCanceled() {
		CellEditorListener[] aListeners = listeners.getListeners(CellEditorListener.class);
		// Cell listeners are ordered from last to first!
		for (int j = aListeners.length - 1; j >= 0; j -= 1) {
			aListeners[j].editingCanceled(event);
		}
	}

	protected void fireEditingStopped() {
		CellEditorListener[] aListeners = listeners.getListeners(CellEditorListener.class);
		// Cell listeners are ordered from last to first!
		for (int j = aListeners.length - 1; j >= 0; j -= 1) {
			aListeners[j].editingStopped(event);
		}
	}
}
