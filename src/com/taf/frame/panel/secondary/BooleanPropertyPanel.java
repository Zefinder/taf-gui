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
package com.taf.frame.panel.secondary;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.taf.annotation.FactoryObject;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.FieldType;
import com.taf.util.Consts;

/**
 * The BooleanPropertyPanel is a secondary property panel used for
 * {@link BooleanType}. It allows the user to set weights for the true and false
 * values.
 * 
 * @see EntitySecondaryPropertyPanel
 *
 * @author Adrien Jakubiak
 */
@FactoryObject(types = FieldType.class, generate = true)
public class BooleanPropertyPanel extends EntitySecondaryPropertyPanel {

	private static final long serialVersionUID = -9212722796480771296L;

	/** The false weight label text. */
	private static final String FALSE_WEIGHT_LABEL_TEXT = "False weight";

	/** The true weight label text. */
	private static final String TRUE_WEIGHT_LABEL_TEXT = "True weight";

	/** The false weight label. */
	private JLabel falseWeightLabel;

	/** The false weight field. */
	private JFormattedTextField falseWeightField;

	/** The true weight label. */
	private JLabel trueWeightLabel;

	/** The true weight field. */
	private JFormattedTextField trueWeightField;

	/**
	 * Instantiates a new boolean property panel.
	 *
	 * @param type the type
	 */
	public BooleanPropertyPanel(BooleanType type) {
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP);
		c.weightx = 0;
		c.weighty = 0;
		falseWeightLabel = new JLabel(FALSE_WEIGHT_LABEL_TEXT);
		addComponent(falseWeightLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		falseWeightField = new JFormattedTextField(type.getFalseWeight());
		falseWeightField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		falseWeightField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editTrueWeight(((Number) falseWeightField.getValue()).intValue()));
		addComponent(falseWeightField, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		trueWeightLabel = new JLabel(TRUE_WEIGHT_LABEL_TEXT);
		addComponent(trueWeightLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(Consts.SMALL_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		trueWeightField = new JFormattedTextField(type.getTrueWeight());
		trueWeightField.setColumns(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		trueWeightField.addPropertyChangeListener(Consts.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
				evt -> type.editTrueWeight(((Number) trueWeightField.getValue()).intValue()));
		addComponent(trueWeightField, c);

		// Add an empty panel to force everything to the top
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.weighty = 1;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 2;
		addComponent(new JPanel(), c);
	}

}
