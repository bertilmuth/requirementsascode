package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.domain.Stock;
import shoppingfxexample.usecase.ShoppingExampleUseCaseModel;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrder;

public class ShoppingApplication extends Application {
	private Stock stock;
	private Stage primaryStage;
	private UseCaseRunner useCaseModelRun;
	private ShoppingApplicationDisplay display;

	@Override
	public void start(Stage primaryStage) throws IOException {
		accessStock();
		createUseCaseModelRun();
        createAndShowDisplay(primaryStage);
		createAndRunUseCaseModel(primaryStage);
	}
	
	private void accessStock() {
		this.stock = new Stock();
	}

	private void createUseCaseModelRun() {
		this.useCaseModelRun = new UseCaseRunner();
	}

	private void createAndShowDisplay(Stage primaryStage) throws IOException {
		this.display = new ShoppingApplicationDisplay(useCaseModelRun, primaryStage);
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Shopping Example JavaFX - Requirements as Code");
		primaryStage.show();		
	}
	
	public void createAndRunUseCaseModel(Stage primaryStage) {				
		ShoppingExampleUseCaseModel shoppingExampleUseCaseModel 
			= new ShoppingExampleUseCaseModel(useCaseModelRun.getUseCaseModel(), display);
		
		useCaseModelRun.run(shoppingExampleUseCaseModel.getEndCustomerActor());
		displayStockedProductsAndPurchaseOrder(stock.getProducts(), shoppingExampleUseCaseModel.getPurchaseOrder());
	}

	public void displayStockedProductsAndPurchaseOrder(ObservableList<Product> stockedProducts, PurchaseOrder purchaseOrder) {
		DisplayStockedProductsAndPurchaseOrder displayStockedProductsAndPurchaseOrder = 
			new DisplayStockedProductsAndPurchaseOrder(stockedProducts, purchaseOrder);
		useCaseModelRun.reactTo(displayStockedProductsAndPurchaseOrder);
	}
	
	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
