package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getFlowFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Flow;
import org.requirementsascode.flowposition.Anytime;
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
	String predicate = getFlowPosition(flow) + getFlowPredicateSeparator(flow, PREDICATE_SEPARATOR) + getWhen(flow);
	String sep = "".equals(predicate) ? "" : PREDICATE_POSTFIX;
	String capitalizedPredicateWithColon = StringUtils.capitalize(predicate) + sep;
	return capitalizedPredicateWithColon;
    }

    private String getFlowPosition(Flow flow) {
	FlowPosition flowPosition = flow.getFlowPosition();
	String result = "";
	
	if(flowPosition != null) {
		String flowPositionWords = getLowerCaseWordsOfClassName(flowPosition.getClass());
		String stepName = flowPosition.getStepName();
		boolean isNonDefaultFlowPosition = Anytime.class.equals(flowPosition.getClass()) || !"".equals(stepName);
		String flowPositionWithStepName = isNonDefaultFlowPosition? flowPositionWords + " " + stepName : "";
		result = flowPositionWithStepName.trim();
	}
	return result;
    }

    private String getFlowPredicateSeparator(Flow flow, String sep) {
	String flowPosition = getFlowPosition(flow);
	String result = "";
	if (flowPosition != "" && getWhen(flow) != "") {
	    result = sep;
	}
	return result;
    }

    private String getWhen(Flow flow) {
	String whenWords = "";
	if (flow.getWhen().isPresent()) {
	    Predicate<?> when = flow.getWhen().get();
	    whenWords = WHEN + getLowerCaseWordsOfClassName(when.getClass());
	}
	return whenWords;
    }
}
