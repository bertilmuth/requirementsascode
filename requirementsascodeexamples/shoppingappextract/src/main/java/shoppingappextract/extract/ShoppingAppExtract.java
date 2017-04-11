package shoppingappextract.extract;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.io.File;
import java.io.FileWriter;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.extract.freemarker.FreeMarkerEngine;

import shoppingappjavafx.usecase.ShoppingAppModel;
import shoppingappjavafx.usecaserealization.BuyProductRealization;

public class ShoppingAppExtract {
  public static void main(String[] args) throws Exception {
    UseCaseModel useCaseModel = buildUseCaseModel();

    FreeMarkerEngine engine = new FreeMarkerEngine();
    engine.put("useCaseModel", useCaseModel);

    File templateFile = new File("src/test/resources/htmlExample.ftlh");
    File outputFile = File.createTempFile("shoppingappextract_", ".html");

    engine.process(templateFile, new FileWriter(outputFile));
    System.out.println("Wrote file to: " + outputFile);
  }

  private static UseCaseModel buildUseCaseModel() {
    BuyProductRealization buyProductRealization = new BuyProductRealization(null, null);
    UseCaseModel useCaseModel = new ShoppingAppModel(buyProductRealization).buildWith(newBuilder());
    return useCaseModel;
  }
}
