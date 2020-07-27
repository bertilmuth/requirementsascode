package org.requirementsascode.extract.freemarker;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.requirementsascode.Model;
import org.requirementsascode.extract.freemarker.methodmodel.ActorPartOfStep;
import org.requirementsascode.extract.freemarker.methodmodel.FlowCondition;
import org.requirementsascode.extract.freemarker.methodmodel.ReactWhileOfStep;
import org.requirementsascode.extract.freemarker.methodmodel.SystemPartOfStep;
import org.requirementsascode.extract.freemarker.methodmodel.UserPartOfStep;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerEngine {
  private Map<String, Object> dataModel;
  private Configuration cfg;

  /**
   * Creates an engine for generating documentation, based on a
   * requirementsascodecore UseCaseModel and using the FreeMarker template engine.
   * 
   * @param basePackagePath package path in your classpath, where your FreeMarker
   *                        templates are located.
   */
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
    put("flowCondition", new FlowCondition());
    put("actorPartOfStep", new ActorPartOfStep());
    put("userPartOfStep", new UserPartOfStep());
    put("systemPartOfStep", new SystemPartOfStep());
    put("reactWhileOfStep", new ReactWhileOfStep());
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
   * 'Extracts' the use cases from the model. This is done by putting the
   * specified models in the FreeMarker configuration under the name 'model'.
   * Then, the specified template is used to transform the model to text and write
   * it using the specified writer.
   *
   * @param model            the input model, created with requirementsascodecore
   * @param templateFileName name of the template file, relative to the base class
   *                         path (when constructing the engine)
   * @param outputWriter     the writer that writes out the resulting text
   * @throws Exception if anything goes wrong
   */
  public void extract(Model model, String templateFileName, Writer outputWriter) throws Exception {
    put("model", model);
    Template template = cfg.getTemplate(templateFileName);
    template.process(dataModel, outputWriter);
  }
}
