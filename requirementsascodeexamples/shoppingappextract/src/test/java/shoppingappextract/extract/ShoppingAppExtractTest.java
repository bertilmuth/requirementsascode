package shoppingappextract.extract;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.io.File;
import java.io.FileWriter;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.extract.freemarker.FreeMarkerEngine;

import shoppingappjavafx.usecase.ShoppingAppModel;
import shoppingappjavafx.usecaserealization.BuyProductRealization;

public class ShoppingAppExtractTest {
  private UseCaseModel useCaseModel;

  @Before
  public void setUp() throws Exception {
    BuyProductRealization shoppingAppUseCaseRealization = new BuyProductRealization(null, null);
    useCaseModel = new ShoppingAppModel(shoppingAppUseCaseRealization).buildWith(newBuilder());
  }

  @Test
  public void writesUseCasesToHtmlFile() throws Exception {
    FreeMarkerEngine engine = new FreeMarkerEngine();
    engine.put("useCaseModel", useCaseModel);
    File templateFile = new File("src/test/resources/htmlExample.ftlh");
    File outputFile = File.createTempFile("shoppingappextract_", ".html");
    engine.process(templateFile, new FileWriter(outputFile));
    System.out.println("Wrote file to: " + outputFile);
  }
}
