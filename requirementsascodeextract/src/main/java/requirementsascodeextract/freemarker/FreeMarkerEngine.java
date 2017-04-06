package requirementsascodeextract.freemarker;

import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import requirementsascodeextract.freemarker.methodmodel.AfterFirstWordOfMethod;
import requirementsascodeextract.freemarker.methodmodel.FirstWordOfMethod;
import requirementsascodeextract.freemarker.methodmodel.WordsOfMethod;

public class FreeMarkerEngine {
  private Map<String, Object> dataModel;
  private Configuration cfg;

  public FreeMarkerEngine() {
    this.dataModel = new HashMap<String, Object>();
    createConfiguration();
    putFreemarkerMethodsInDataModel();
  }
  private void createConfiguration() {
    cfg = new Configuration(Configuration.VERSION_2_3_26);
    cfg.setLogTemplateExceptions(false);
    setDefaultEncoding("UTF-8");
    setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }
  private void putFreemarkerMethodsInDataModel() {
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

  public void process(File templateFile, Writer outputWriter) throws Exception {
    cfg.setDirectoryForTemplateLoading(templateDirectory(templateFile));
    Template template = cfg.getTemplate(templateFileName(templateFile));
    template.process(dataModel, outputWriter);
  }

  private File templateDirectory(File templateFile) {
    return templateFile.getParentFile();
  }

  private String templateFileName(File templateFile) {
    return templateFile.getName();
  }
}
