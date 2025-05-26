package com.taf;

import java.util.Random;

import com.taf.logic.constraint.Constraint;
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
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;

public class Main {

	public static void main(String[] args) {
		IntegerType it = new IntegerType();
		it.addMinParameter(2);
		
		
//		st.addValue("a");
//		st.addValue("b", 5);
		
//		Field param3 = new Parameter("str", st);
		
	//		node.addField(param2);
	//		node.addField(param3);
		
		Root root = new Root("test_case");
		Node fieldNode = new Node("field", new AnonymousType());

		StringType st = new StringType();
		st.addValue("cabbage", 5);
		st.addValue("leek", 7);
		Field vegetable = new Parameter("vegetable", st);
		
		Node rowNode = new Node("row", new AnonymousType());
		RealType rt = new RealType();
		rt.addMinParameter(10);
		rt.addMaxParameter(100);
		Field length = new Parameter("length", rt);
		
		Constraint interval = new Constraint("interval");
		interval.addConstraintParameter(new TypeConstraintParameter(TypeConstraintParameterEnum.FORALL));
		var a = new ExpressionConstraintParameter();
		a.addExpression("row[i] \\ length INFEQ 1.1 * row[i−1] \\ length");
		a.addExpression("row[i] \\ length SUPEQ 0.9 * row[i−1] \\ length");
		interval.addConstraintParameter(a);
		var q = new QuantifiersConstraintParameter('i');
		interval.addConstraintParameter(q);
		var r = new RangeConstraintParameter("1", "row.nb_instances-1");
		interval.addConstraintParameter(r);
		
		fieldNode.addField(vegetable);
		fieldNode.addField(rowNode);
		rowNode.addField(length);
		rowNode.addConstraint(interval);
		root.addField(fieldNode);
		
		
//		root.addField(param1);
//		root.addField(node);
		System.out.println(root);
	}

}
