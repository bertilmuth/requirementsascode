package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.usecase.ShoppingExampleUseCaseModel;

public class ShoppingApplicationMain extends Application {
	private Stock stock;
	private Stage primaryStage;
	private UseCaseRunner useCaseModelRunner;
	private ShoppingApplicationDisplay display;

	@Override
	public void start(Stage primaryStage) throws IOException {
		accessStock();
		createUseCaseModelRunner();
        createAndShowDisplay(primaryStage);
		createAndRunUseCaseModel(primaryStage);
	}
	
	private void accessStock() {
		this.stock = new Stock();
	}

	private void createUseCaseModelRunner() {
		this.useCaseModelRunner = new UseCaseRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new ShoppingApplicationDisplay(useCaseModelRunner, primaryStage);
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Shopping Example JavaFX - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseModel(Stage primaryStage) {				
		new ShoppingExampleUseCaseModel(useCaseModelRunner.getUseCaseModel(), stock, display);
		useCaseModelRunner.run();
	}
	
	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
