package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.requirementsascode.Condition;
import org.requirementsascode.FlowStep;
import org.requirementsascode.Step;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ReactWhileOfStep implements TemplateMethodModelEx {
    private static final String REACT_WHILE_PREFIX = "As long as ";
    private static final String REACT_WHILE_POSTFIX = ": ";

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
	if (arguments.size() != 1) {
	    throw new TemplateModelException("Wrong number of arguments. Must be 1.");
	}

	Step step = getStep(arguments.get(0));

	String reactWhile = "";
	if (step instanceof FlowStep) {
	    Condition reactWhileCondition = ((FlowStep) step).getReactWhile();
	    if (reactWhileCondition != null) {
		reactWhile = REACT_WHILE_PREFIX + getLowerCaseWordsOfClassName(reactWhileCondition.getClass())
			+ REACT_WHILE_POSTFIX;
	    }
	}

	return new SimpleScalar(reactWhile);
    }

    private Step getStep(Object argument) {
	return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
    }
}
