package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseModelRun;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.gui.controller.DisplayPurchaseOrderSummaryController;
import shoppingfxexample.gui.controller.DisplayStockedProductsController;
import shoppingfxexample.gui.controller.EnterShippingInformationController;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrder;

public class ShoppingApplicationDisplay {
	private static final String RELATIVE_FXML_PACKAGE_NAME = "fxml";
	private UseCaseModelRun useCaseModelRun;
	private Stage primaryStage;
	private VBox vBox;
	private Object controller;
	
	public ShoppingApplicationDisplay(UseCaseModelRun useCaseModelRun, Stage primaryStage) {
		this.useCaseModelRun = useCaseModelRun;
		this.primaryStage = primaryStage;
}
	
	public void displayStockedProductsAndPurchaseOrder(DisplayStockedProductsAndPurchaseOrder stockedProductsAndPurchaseOrder){
		loadAndDisplay("DisplayStockedProducts.fxml");
		DisplayStockedProductsController displayStockedProductsController = (DisplayStockedProductsController)controller;
		displayStockedProductsController.setUseCaseModelRun(useCaseModelRun);
		displayStockedProductsController.displayStockedProductsAndPurchaseOrder(stockedProductsAndPurchaseOrder);
	}
	
	public void enterShippingInformation(CheckoutPurchase checkoutPurchase){
		loadAndDisplay("EnterShippingInformation.fxml");
		EnterShippingInformationController enterShippingInformationController = (EnterShippingInformationController)controller;
		enterShippingInformationController.setUseCaseModelRun(useCaseModelRun);
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
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
}
