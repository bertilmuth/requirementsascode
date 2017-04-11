package shoppingappextract.extract;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.extract.freemarker.FreeMarkerEngine;

import shoppingappjavafx.usecase.ShoppingAppModel;
import shoppingappjavafx.usecaserealization.BuyProductRealization;

public class ShoppingAppExtract {
  public static void main(String[] args) throws Exception {
    new ShoppingAppExtract().start();
  }

  private void start() throws Exception {
    UseCaseModel useCaseModel = buildUseCaseModel();

    FreeMarkerEngine engine = new FreeMarkerEngine();
    File outputFile = outputFile();
    engine.extract(useCaseModel, templateFile(), new FileWriter(outputFile));
    
    System.out.println("Wrote file to: " + outputFile);
  }

  private UseCaseModel buildUseCaseModel() {
    BuyProductRealization buyProductRealization = new BuyProductRealization(null, null);
    UseCaseModel useCaseModel = new ShoppingAppModel(buyProductRealization).buildWith(newBuilder());
    return useCaseModel;
  }

  private File templateFile() {
    File templateFile = new File("src/test/resources/htmlExample.ftlh");
    return templateFile;
  }

  private File outputFile() throws IOException {
    File outputFile = File.createTempFile("shoppingappextract_", ".html");
    return outputFile;
  }
}
