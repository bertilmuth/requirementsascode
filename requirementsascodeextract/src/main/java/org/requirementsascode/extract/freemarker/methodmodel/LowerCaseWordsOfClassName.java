package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class LowerCaseWordsOfClassName implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments");
    }
    String camelCaseClassName = ((BeanModel)arguments.get(0)).get("simpleName").toString();
    
    String words = Words.getLowerCaseWordsOfClassName(camelCaseClassName);
    return new SimpleScalar(words);
  }
}
