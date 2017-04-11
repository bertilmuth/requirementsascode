package org.requirementsascode.extract.freemarker;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.extract.freemarker.methodmodel.AfterFirstWordOfMethod;
import org.requirementsascode.extract.freemarker.methodmodel.FirstWordOfMethod;
import org.requirementsascode.extract.freemarker.methodmodel.WordsOfMethod;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerEngine {
  private Map<String, Object> dataModel;
  private Configuration cfg;

  public FreeMarkerEngine(String basePackagePath) {
    createConfiguration(basePackagePath);
    putFreemarkerMethodsInDataModel();
  }

  private void createConfiguration(String basePackagePath) {
    cfg = new Configuration(Configuration.VERSION_2_3_26);
    cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), basePackagePath);
    cfg.setLogTemplateExceptions(false);
    setDefaultEncoding("UTF-8");
    setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }

  private void putFreemarkerMethodsInDataModel() {
    dataModel = new HashMap<String, Object>();
    put("wordsOf", new WordsOfMethod());
    put("firstWordOf", new FirstWordOfMethod());
    put("afterFirstWordOf", new AfterFirstWordOfMethod());
  }

  public void put(String key, Object value) {
    dataModel.put(key, value);
  }

  public void setDefaultEncoding(String encoding) {
    cfg.setDefaultEncoding(encoding);
  }

  public void setTemplateExceptionHandler(TemplateExceptionHandler handler) {
    cfg.setTemplateExceptionHandler(handler);
  }

  /**
   * 'Extracts' the use cases from the model. This is done by putting the specified use case model
   * in the FreeMarker configuration under the name 'useCaseModel'. Then, the specified template is
   * used to transform the use case model to text and write it using the specified writer.
   *
   * @param useCaseModel the input model, created with requirementsascodecore
   * @param templateFileName name of the template file, relative to the base class path (when constructing the engine)
   * @param outputWriter the writer that writes out the resulting text
   * @throws Exception if anything goes wrong
   */
  public void extract(UseCaseModel useCaseModel, String templateFileName, Writer outputWriter)
      throws Exception {
    put("useCaseModel", useCaseModel);
    Template template = cfg.getTemplate(templateFileName);
    template.process(dataModel, outputWriter);
  }
}
