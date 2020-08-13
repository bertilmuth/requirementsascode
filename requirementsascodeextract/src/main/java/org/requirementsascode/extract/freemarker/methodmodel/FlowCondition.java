package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getFlowFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Flow;
import org.requirementsascode.flowposition.After;
import org.requirementsascode.flowposition.AfterSingleStep;
import org.requirementsascode.flowposition.FlowPosition;
import org.requirementsascode.flowposition.InsteadOf;

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
    String sep = predicate.isEmpty() ? "" : PREDICATE_POSTFIX;
    String capitalizedPredicateWithColon = StringUtils.capitalize(predicate) + sep;
    return capitalizedPredicateWithColon;
  }

  private String getFlowPosition(Flow flow) {
    FlowPosition flowPosition = flow.getFlowPosition();
    
    String flowPositionWords = "";
    if(flowPosition instanceof InsteadOf) {
      InsteadOf insteadOf = (InsteadOf)flowPosition;
      String stepName = insteadOf.getStepName();
      flowPositionWords = flowPositionToWords(flowPosition, stepName);
    } else if(flowPosition instanceof After) {
      After after = (After)flowPosition;
      
      String afterStepNames = 
        after.getAfterForEachSingleStep().stream()
          .map(AfterSingleStep::getStepName)
          .filter(name -> name != null)
          .collect(Collectors.joining(","));
      
      flowPositionWords = afterStepNames.isEmpty()? "" : "After " + afterStepNames;
    }
    

    return flowPositionWords;
  }

  private String flowPositionToWords(FlowPosition flowPosition, String stepName) {
    String result = "";
    if (flowPosition != null) {
      boolean isNonDefaultFlowPosition = isNonDefaultFlowCondition(flowPosition, stepName);
      if (isNonDefaultFlowPosition) {
        String flowPositionWords = getLowerCaseWordsOfClassName(flowPosition.getClass());
        String flowPositionWithStepName = flowPositionWords + " " + stepName;
        result = flowPositionWithStepName.trim();
      }
    }
    return result;
  }

  boolean isNonDefaultFlowCondition(FlowPosition flowPosition, String stepName) {
    return !After.class.equals(flowPosition.getClass()) || stepName != null;
  }

  private String getFlowPredicateSeparator(Flow flow, String sep) {
    String flowPosition = getFlowPosition(flow);
    String result = "";
    if (!flowPosition.isEmpty() && !getCondition(flow).isEmpty()) {
      result = sep;
    }
    return result;
  }

  private String getCondition(Flow flow) {
    String conditionWords = flow.getCondition().map(
      condition -> (WHEN + getLowerCaseWordsOfClassName(condition.getClass()))).orElse("");
    return conditionWords;
  }
}
