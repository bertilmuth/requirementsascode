package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.ShoppingExampleUseCaseRealization;

public class ShoppingApplicationMain extends Application {
	private Stock stock;
	private UseCaseRunner useCaseRunner;
	private ShoppingApplicationDisplay display;

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
		this.display = new ShoppingApplicationDisplay(useCaseRunner, primaryStage);
		primaryStage.setTitle("Shopping Example JavaFX - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseRealization(Stage primaryStage) {		
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		new ShoppingExampleUseCaseRealization(stock, display).create(useCaseModel);
		useCaseRunner.run();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
