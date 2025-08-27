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
package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.taf.annotation.EventMethod;
import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.entity.EntityDeletedEvent;
import com.taf.event.entity.EntityNameChangedEvent;
import com.taf.event.entity.EntitySelectedEvent;
import com.taf.event.entity.ParameterTypeChangedEvent;
import com.taf.event.entity.NodeTypeChangedEvent;
import com.taf.event.entity.creation.ConstraintCreatedEvent;
import com.taf.event.entity.creation.NodeCreatedEvent;
import com.taf.event.entity.creation.ParameterCreatedEvent;
import com.taf.event.entity.creation.TypeCreatedEvent;
import com.taf.frame.popup.TreeEntityPopupMenu;
import com.taf.logic.Entity;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;
import com.taf.manager.EventManager;
import com.taf.manager.TypeManager;
import com.taf.util.Consts;
import com.taf.util.EntityNode;
import com.taf.util.TafTree;

/**
 * <p>
 * The FieldTreePanel is used in the {@link TafPanel} and manages the entities
 * that were added in the project.
 * </p>
 * 
 * <p>
 * When an entity is selected in the tree, the {@link PropertyPanel} will be
 * notified to display the entity informations. If the entity is modified in the
 * {@link PropertyPanel}, the changes will be reflected in the tree.
 * </p>
 * 
 * <p>
 * When an entity is deleted, its node is deleted, the {@link PropertyPanel} is
 * notified as well as the {@link TypeManager}.
 * </p>
 * 
 * @see JPanel
 * @see TafPanel
 * @see PropertyPanel
 *
 * @author Adrien Jakubiak
 */
public class FieldTreePanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8299875121910645683L;

	/** The tree with all project entities. */
	private TafTree tree;

	/** The tree model. */
	private DefaultTreeModel treeModel;

	/** The cached node. */
	private DefaultMutableTreeNode cachedNode;

	/**
	 * Instantiates a new field tree panel.
	 *
	 * @param root the root
	 */
	public FieldTreePanel(Root root) {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP,
				Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP));
		EventManager.getInstance().registerEventListener(this);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		
		tree = new TafTree(root, false);
		treeModel = (DefaultTreeModel) tree.getModel();
		tree.addTreeSelectionListener(e -> {
			cachedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (cachedNode == null) {
				return;
			}

			EntityNode nodeInfo = (EntityNode) cachedNode.getUserObject();
			if (nodeInfo.isRoot()) {
				System.out.println(nodeInfo.getEntity().toString());
			}

			Event event = new EntitySelectedEvent(nodeInfo.getEntity());
			EventManager.getInstance().fireEvent(event);
		});
		MouseAdapter rightClickListener = new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();

				int selRow = tree.getRowForLocation(x, y);

				if (selRow != -1) {
					tree.setSelectionRow(selRow);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectRow(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int selRow = selectRow(e);

				if (selRow != -1) {
					int x = e.getX();
					int y = e.getY();

					EntityNode nodeObject = (EntityNode) cachedNode.getUserObject();
					Entity entity = nodeObject.getEntity();

					// Show popup if not root
					if (!(entity instanceof Root)) {
						JPopupMenu menu = new TreeEntityPopupMenu(entity);
						menu.show(tree, x, y);
					}
				}
			}

			private int selectRow(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int x = e.getX();
					int y = e.getY();

					int selRow = tree.getRowForLocation(x, y);

					if (selRow != -1) {
						tree.setSelectionRow(selRow);
						return selRow;
					}
				}

				return -1;
			}
		};
		tree.addMouseListener(rightClickListener);
		tree.addMouseMotionListener(rightClickListener);

		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, c);
	}

	/**
	 * Handler for {@link ConstraintCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onConstraintCreated(ConstraintCreatedEvent event) {
		addEntity(event.getConstraint());
	}

	/**
	 * Handler for {@link EntityDeletedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityDeleted(EntityDeletedEvent event) {
		removeEntity(event.getEntity());
	}

	/**
	 * Handler for {@link EntityNameChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onEntityNameChanged(EntityNameChangedEvent event) {
		DefaultMutableTreeNode treeNode = tree.getNode(event.getEntity());
		EntityNode nodeObject = (EntityNode) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(cachedNode);
	}

	/**
	 * Handler for {@link ParameterTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onFieldTypeChanged(ParameterTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = tree.getNode(event.getParameter());
		EntityNode nodeObject = (EntityNode) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	/**
	 * Handler for {@link NodeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeCreated(NodeCreatedEvent event) {
		addEntity(event.getNode());
	}

	/**
	 * Handler for {@link NodeTypeChangedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onNodeTypeChanged(NodeTypeChangedEvent event) {
		DefaultMutableTreeNode treeNode = tree.getNode(event.getNode());
		EntityNode nodeObject = (EntityNode) treeNode.getUserObject();
		nodeObject.refresh();
		treeModel.nodeChanged(treeNode);
	}

	/**
	 * Handler for {@link ParameterCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onParameterCreated(ParameterCreatedEvent event) {
		addEntity(event.getParameter());
	}

	/**
	 * Handler for {@link TypeCreatedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onTypeCreated(TypeCreatedEvent event) {
		addEntity(event.getType());
	}

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	/**
	 * Adds an entity to the tree.
	 *
	 * @param entity the entity
	 */
	private void addEntity(Entity entity) {
		// We assume that the cached node will be the parent
		if (cachedNode != null) {
			EntityNode parentObject = (EntityNode) cachedNode.getUserObject();
			Entity parent = parentObject.getEntity();
			if (parent instanceof Type) {
				addEntity((Type) parent, entity);
			}
		}
	}

	/**
	 * Adds an entity to the tree to its parent.
	 *
	 * @param parent the parent
	 * @param entity the entity
	 */
	private void addEntity(Type parent, Entity entity) {
		// Add entity to parent type
		parent.addEntity(entity);

		// Update tree
		tree.putNode(cachedNode, entity);
	}

	/**
	 * Removes an entity of the tree.
	 *
	 * @param entity the entity
	 */
	private void removeEntity(Entity entity) {
		DefaultMutableTreeNode treeNodeToRemove = tree.getNode(entity);
		treeModel.removeNodeFromParent(treeNodeToRemove);
		treeModel.nodeChanged(treeNodeToRemove.getParent());

		// If the cached node is the node to remove, then reset the cached node
		if (treeNodeToRemove.equals(cachedNode)) {
			cachedNode = null;
		}

		// Remove from map
		tree.removeNode(entity);
	}
}
