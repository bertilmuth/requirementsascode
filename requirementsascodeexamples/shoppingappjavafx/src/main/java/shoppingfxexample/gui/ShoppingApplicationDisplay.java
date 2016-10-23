package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.gui.controller.AbstractUseCaseRunnerController;
import shoppingfxexample.gui.controller.DisplayPurchaseOrderSummaryController;
import shoppingfxexample.gui.controller.DisplayStockedProductsAndPurchaseOrderController;
import shoppingfxexample.gui.controller.EnterShippingInformationController;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;

public class ShoppingApplicationDisplay {
	private static final String RELATIVE_FXML_PACKAGE_NAME = "fxml";
	private UseCaseRunner useCaseRunner;
	private Stage primaryStage;
	private VBox vBox;
	private AbstractUseCaseRunnerController controller;
	
	public ShoppingApplicationDisplay(UseCaseRunner useCaseRunner, Stage primaryStage) {
		this.useCaseRunner = useCaseRunner;
		this.primaryStage = primaryStage;
}
	
	public void displayStockedProductsAndPurchaseOrder(DisplayStockedProductsAndPurchaseOrderEvent displayStockedProductsAndPurchaseOrder){
		loadAndDisplay("DisplayStockedProductsAndPurchaseOrder.fxml");
		DisplayStockedProductsAndPurchaseOrderController displayStockedProductsController = (DisplayStockedProductsAndPurchaseOrderController)controller;
		displayStockedProductsController.displayStockedProductsAndPurchaseOrder(displayStockedProductsAndPurchaseOrder);
	}
	
	public void enterShippingInformation(CheckoutPurchaseEvent checkoutPurchase){
		loadAndDisplay("EnterShippingInformation.fxml");
		EnterShippingInformationController enterShippingInformationController = (EnterShippingInformationController)controller;
		enterShippingInformationController.enterShippingInformation(checkoutPurchase);
	}
	
	public void displayPurchaseOrderSummary(PurchaseOrder checkoutPurchase){
		loadAndDisplay("DisplayPurchaseOrderSummary.fxml");
		DisplayPurchaseOrderSummaryController displayPurchaseOrderSummaryController = (DisplayPurchaseOrderSummaryController)controller;
		displayPurchaseOrderSummaryController.displayPurchaseOrderSummary(checkoutPurchase);
	}
		
	private void loadAndDisplay(String fxmlFileName){
		loadFXML(fxmlFileName);
		Scene productsScene = new Scene(vBox);
		primaryStage.setScene(productsScene);
	}
	
	private void loadFXML(String fxmlFileName) {		
		try {
			 FXMLLoader loader = new FXMLLoader(getClass().getResource(RELATIVE_FXML_PACKAGE_NAME + "/" + fxmlFileName));
			
			vBox = loader.load(); 
			controller = (AbstractUseCaseRunnerController)loader.getController();
			controller.setUseCaseRunner(useCaseRunner);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
}
