package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.BuyProductRealization;
import shoppingappjavafx.usecase.UseCaseModelCreator;
import shoppingappjavafx.usecaserealization.ShoppingAppBuyProductRealization;

public class JavafxMain extends Application {
	private Stock stock;
	private UseCaseRunner useCaseRunner;
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
		this.useCaseRunner = new UseCaseRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new JavafxDisplay(useCaseRunner, primaryStage);
		primaryStage.setTitle("Shopping Application (JavaFX) - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseRealization(Stage primaryStage) {		
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		BuyProductRealization buyProductRealization = new ShoppingAppBuyProductRealization(stock, display);
		new UseCaseModelCreator(buyProductRealization).create(useCaseModel);
		useCaseRunner.run();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
