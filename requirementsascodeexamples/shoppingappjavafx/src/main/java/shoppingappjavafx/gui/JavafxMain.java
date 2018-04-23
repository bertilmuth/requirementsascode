package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecase.ShoppingAppModel;
import shoppingappjavafx.usecaserealization.BuyProductRealization;

public class JavafxMain extends Application {
	private ModelBuilder modelBuilder;
	private ModelRunner modelRunner;
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
		this.modelBuilder = Model.builder();
		this.modelRunner = new ModelRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new JavafxDisplay(modelRunner, primaryStage);
		primaryStage.setTitle("Shopping Application (JavaFX) - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseRealization(Stage primaryStage) {	
		BuyProductRealization buyProductRealization = new BuyProductRealization(stock, display);
		Model model = new ShoppingAppModel(buyProductRealization).buildWith(modelBuilder);
		modelRunner.run(model);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
