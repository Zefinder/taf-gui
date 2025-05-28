package com.taf;

import com.taf.event.Event;
import com.taf.event.FieldSelectedEvent;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.logic.constraint.parameter.ExpressionConstraintParameter;
import com.taf.logic.constraint.parameter.QuantifiersConstraintParameter;
import com.taf.logic.constraint.parameter.RangeConstraintParameter;
import com.taf.logic.constraint.parameter.TypeConstraintParameter;
import com.taf.logic.constraint.parameter.TypeConstraintParameterEnum;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Root;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;

public class Main {

	public static void main(String[] args) {
		Root root = new Root("test_case");
		Node fieldNode = new Node("field", new AnonymousType());

		StringType st = new StringType();
		st.addValue("cabbage", 5);
		st.addValue("leek", 7);
		Field vegetable = new Parameter("vegetable", st);

		AnonymousType at = new AnonymousType();
		at.addMinMaxInstanceParameter(1, 40);
		Node rowNode = new Node("row", at);
		RealType rt = new RealType();
		rt.addMinParameter(10);
		rt.addMaxParameter(100);
		Field length = new Parameter("length", rt);

		Constraint interval = new Constraint("interval");
		interval.addConstraintParameter(new TypeConstraintParameter(TypeConstraintParameterEnum.FORALL));
		ExpressionConstraintParameter expr = new ExpressionConstraintParameter();
		expr.addExpression("row[i] \\ length INFEQ 1.1 * row[i−1] \\ length");
		expr.addExpression("row[i] \\ length SUPEQ 0.9 * row[i−1] \\ length");
		interval.addConstraintParameter(expr);
		ConstraintParameter q = new QuantifiersConstraintParameter('i');
		interval.addConstraintParameter(q);
		ConstraintParameter r = new RangeConstraintParameter("1", "row.nb_instances-1");
		interval.addConstraintParameter(r);

		Constraint interval2 = new Constraint("interval_2");
		ExpressionConstraintParameter expr2 = new ExpressionConstraintParameter();
		expr2.addExpression("row[0] \\ length INFEQ 1.1 * row[row.nb_instances − 1] \\ length");
		expr2.addExpression("row[0] \\ length SUPEQ 0.9 * row[row.nb_instances − 1] \\ length");
		interval2.addConstraintParameter(expr2);

		Node mission = new Node("mission", new AnonymousType());
		Field isFirstTrackOuter = new Parameter("is_first_track_outer", new BooleanType());
		Constraint firstTrack = new Constraint("first_track");
		ExpressionConstraintParameter expr3 = new ExpressionConstraintParameter();
		expr3.addExpression("IMPLIES(..\\field\\row.nb_instances EQ 1, .\\is_first_track_outer EQ True)");
		firstTrack.addConstraintParameter(expr3);

		root.addField(fieldNode);
		fieldNode.addField(vegetable);
		fieldNode.addField(rowNode);
		rowNode.addField(length);
		rowNode.addConstraint(interval);
		rowNode.addConstraint(interval2);

		root.addField(mission);
		mission.addField(isFirstTrackOuter);
		mission.addConstraint(firstTrack);

//		root.addField(param1);
//		root.addField(node);
		System.out.println(root);
	}

}
