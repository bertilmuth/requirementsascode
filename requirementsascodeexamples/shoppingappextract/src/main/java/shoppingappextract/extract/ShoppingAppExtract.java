package shoppingappextract.extract;

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

    FreeMarkerEngine engine = new FreeMarkerEngine("shoppingappextract/extract");
    File outputFile = outputFile();
    engine.extract(useCaseModel, templateFileName(), new FileWriter(outputFile));
    
    System.out.println("Wrote file to: " + outputFile);
  }

  private UseCaseModel buildUseCaseModel() {
    BuyProductRealization buyProductRealization = new BuyProductRealization(null, null);
    UseCaseModel useCaseModel = new ShoppingAppModel(buyProductRealization).buildWith(UseCaseModel.builder());
    return useCaseModel;
  }

  private String templateFileName() {
    return "htmlExample.ftlh";
  }

  private File outputFile() throws IOException {
    File outputFile = File.createTempFile("shoppingappextract_", ".html");
    return outputFile;
  }
}
