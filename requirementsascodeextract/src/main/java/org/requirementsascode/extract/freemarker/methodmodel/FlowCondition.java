package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getFlowFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Flow;
import org.requirementsascode.Step;
import org.requirementsascode.flowposition.After;
import org.requirementsascode.flowposition.FlowPosition;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FlowCondition implements TemplateMethodModelEx {
    private static final String WHEN = "when ";
    private static final String PREDICATE_SEPARATOR = ", ";
    private static final String PREDICATE_POSTFIX = ": ";

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
	if (arguments.size() != 1) {
	    throw new TemplateModelException("Wrong number of arguments. Must be 1.");
	}

	Flow flow = getFlowFromFreemarker(arguments.get(0));

	String flowPredicate = getFlowPredicate(flow);

	return new SimpleScalar(flowPredicate);
    }

    private String getFlowPredicate(Flow flow) {
	String predicate = getFlowPosition(flow) + getFlowPredicateSeparator(flow, PREDICATE_SEPARATOR)
		+ getCondition(flow);
	String sep = "".equals(predicate) ? "" : PREDICATE_POSTFIX;
	String capitalizedPredicateWithColon = StringUtils.capitalize(predicate) + sep;
	return capitalizedPredicateWithColon;
    }

    private String getFlowPosition(Flow flow) {
	FlowPosition flowPosition = flow.getFlowPosition();
	String result = "";

	if (flowPosition != null) {
	    Step step = flowPosition.getStep();
	    boolean isNonDefaultFlowPosition = isNonDefaultFlowCondition(flowPosition, step);
	    if (isNonDefaultFlowPosition) {
		String stepName = step != null ? step.getName() : "";
		String flowPositionWords = getLowerCaseWordsOfClassName(flowPosition.getClass());
		String flowPositionWithStepName = flowPositionWords + " " + stepName;
		result = flowPositionWithStepName.trim();
	    }
	}
	return result;
    }

    boolean isNonDefaultFlowCondition(FlowPosition flowPosition, Step step) {
	return !After.class.equals(flowPosition.getClass()) || step != null;
    }

    private String getFlowPredicateSeparator(Flow flow, String sep) {
	String flowPosition = getFlowPosition(flow);
	String result = "";
	if (flowPosition != "" && getCondition(flow) != "") {
	    result = sep;
	}
	return result;
    }

    private String getCondition(Flow flow) {
	String conditionWords = flow.getCondition()
		.map(condition -> (WHEN + getLowerCaseWordsOfClassName(condition.getClass()))).orElse("");
	return conditionWords;
    }
}
