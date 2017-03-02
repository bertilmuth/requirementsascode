package shoppingfxexample.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.gui.controller.AbstractController;
import shoppingfxexample.gui.controller.DisplayPaymentDetailsFormController;
import shoppingfxexample.gui.controller.DisplayProductsController;
import shoppingfxexample.gui.controller.DisplayPurchaseOrderSummaryController;
import shoppingfxexample.gui.controller.DisplayShippingInformationFormController;
import shoppingfxexample.usecase.event.Products;

public class ShoppingApplicationDisplay {
	private static final String RELATIVE_FXML_PACKAGE_NAME = "fxml";
	private UseCaseRunner useCaseRunner;
	private Stage primaryStage;
	private VBox vBox;
	private AbstractController controller;
	
	public ShoppingApplicationDisplay(UseCaseRunner useCaseRunner, Stage primaryStage) {
		this.useCaseRunner = useCaseRunner;
		this.primaryStage = primaryStage;
}
	
	public void displayProductsAndShoppingCartSize(Products products, PurchaseOrder purchaseOrder){
		loadAndDisplay("DisplayProducts.fxml");
		DisplayProductsController displayProductsController = (DisplayProductsController)controller;
		displayProductsController.displayProducts(products);
		displayProductsController.displayShoppingCartSize(purchaseOrder);
	}
	
	public void displayShippingInformationForm(){
		loadAndDisplay("DisplayShippingInformationForm.fxml");
		DisplayShippingInformationFormController displayShippingInformationFormController = (DisplayShippingInformationFormController)controller;
		displayShippingInformationFormController.displayShippingInformationForm();
	}
	
	public void displayPaymentDetailsForm(){
		loadAndDisplay("DisplayPaymentDetailsForm.fxml");
		DisplayPaymentDetailsFormController displayPaymentDetailsFormController = (DisplayPaymentDetailsFormController)controller;
		displayPaymentDetailsFormController.displayPaymentDetails();
	}
	
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder){
		loadAndDisplay("DisplayPurchaseOrderSummary.fxml");
		DisplayPurchaseOrderSummaryController displayPurchaseOrderSummaryController = (DisplayPurchaseOrderSummaryController)controller;
		displayPurchaseOrderSummaryController.displayPurchaseOrderSummary(purchaseOrder);
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
			controller = (AbstractController)loader.getController();
			controller.setUseCaseRunner(useCaseRunner);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
}
