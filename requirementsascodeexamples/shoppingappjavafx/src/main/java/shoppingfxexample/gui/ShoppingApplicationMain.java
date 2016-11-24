package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.usecaserealization.ShoppingExampleUseCaseRealization;

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
		ShoppingExampleUseCaseRealization shoppingExampleUseCaseRealization = 
			new ShoppingExampleUseCaseRealization(useCaseRunner.getUseCaseModel(),stock, display);
		useCaseRunner.runAs(shoppingExampleUseCaseRealization.getEndCustomerActor());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
