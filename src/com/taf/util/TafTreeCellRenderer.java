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

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.taf.logic.field.Type;

/**
 * This class defines a tree cell renderer for {@link TafTree}s. The icon of the
 * tree node depends on the entity: if the entity is an instance of
 * {@link Type}, then the icon will be a little folder. Else it will be a white
 * page.
 * 
 * @see DefaultTreeCellRenderer
 * @see TafTree
 *
 * @author Adrien Jakubiak
 */
public class TafTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 3600625563246633955L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		EntityNode nodeObject = (EntityNode) node.getUserObject();
		Icon icon;
		if (nodeObject.isType()) {
			if (expanded) {
				icon = getDefaultOpenIcon();
			} else {
				icon = getDefaultClosedIcon();
			}
		} else {
			icon = getDefaultLeafIcon();
		}

		Color backgroundColor;
		if (hasFocus) {
			backgroundColor = getBackgroundSelectionColor();
		} else {
			backgroundColor = getBackgroundNonSelectionColor();
		}

		setIcon(icon);
		setBackground(backgroundColor);
		return this;
	}

}
