package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.BuyProductRealization;
import shoppingappjavafx.usecase.ShoppingAppModelBuilder;
import shoppingappjavafx.usecaserealization.ShoppingAppBuyProductRealization;

public class JavafxMain extends Application {
	private UseCaseModelBuilder modelBuilder;
	private UseCaseModelRunner modelRunner;
	private Stock stock;
	private JavafxDisplay display;

	@Override
	public void start(Stage primaryStage) throws IOException {
		accessStock();
		createModelBuilderAndRunner();
        createAndShowDisplay(primaryStage);
		createAndRunUseCaseRealization(primaryStage);
	}
	
	private void accessStock() {
		this.stock = new Stock();
	}

	private void createModelBuilderAndRunner() {
		this.modelBuilder = UseCaseModelBuilder.newBuilder();
		this.modelRunner = new UseCaseModelRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new JavafxDisplay(modelRunner, primaryStage);
		primaryStage.setTitle("Shopping Application (JavaFX) - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseRealization(Stage primaryStage) {	
		BuyProductRealization buyProductRealization = new ShoppingAppBuyProductRealization(stock, display);
		UseCaseModel useCaseModel = new ShoppingAppModelBuilder(buyProductRealization).buildWith(modelBuilder);
		modelRunner.run(useCaseModel);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
