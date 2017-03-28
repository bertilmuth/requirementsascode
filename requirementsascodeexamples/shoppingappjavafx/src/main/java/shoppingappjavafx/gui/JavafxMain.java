package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.BuyProductRealization;
import shoppingappjavafx.usecase.ShoppingAppModelBuilder;
import shoppingappjavafx.usecaserealization.ShoppingAppBuyProductRealization;

public class JavafxMain extends Application {
	private UseCaseModelBuilder useCaseModelBuilder;
	private UseCaseRunner useCaseRunner;
	private Stock stock;
	private JavafxDisplay display;

	@Override
	public void start(Stage primaryStage) throws IOException {
		accessStock();
		createUseCaseModelRunner();
        createAndShowDisplay(primaryStage);
		createAndRunUseCaseRealization(primaryStage);
	}
	
	private void accessStock() {
		this.stock = new Stock();
	}

	private void createUseCaseModelRunner() {
		this.useCaseModelBuilder = new UseCaseModelBuilder();
		this.useCaseRunner = new UseCaseRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new JavafxDisplay(useCaseRunner, primaryStage);
		primaryStage.setTitle("Shopping Application (JavaFX) - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseRealization(Stage primaryStage) {	
		BuyProductRealization buyProductRealization = new ShoppingAppBuyProductRealization(stock, display);
		UseCaseModel useCaseModel = new ShoppingAppModelBuilder(buyProductRealization).buildWith(useCaseModelBuilder);
		useCaseRunner.run(useCaseModel);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
