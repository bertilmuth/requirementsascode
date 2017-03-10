package shoppingappjavafx.gui;

import java.io.IOException;

import org.requirementsascode.UseCaseRunner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.domain.ShippingInformation;
import shoppingappjavafx.gui.controller.AbstractController;
import shoppingappjavafx.gui.controller.DisplayPaymentDetailsFormController;
import shoppingappjavafx.gui.controller.DisplayProductsController;
import shoppingappjavafx.gui.controller.DisplayPurchaseOrderSummaryController;
import shoppingappjavafx.gui.controller.DisplayShippingInformationFormController;
import shoppingappjavafx.usecase.event.Products;
import shoppingappjavafx.usecaserealization.interfaces.Display;

public class ShoppingApplicationDisplay implements Display{
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
	
	public void displayShippingInformationForm(ShippingInformation shippingInformation){
		loadAndDisplay("DisplayShippingInformationForm.fxml");
		DisplayShippingInformationFormController displayShippingInformationFormController = (DisplayShippingInformationFormController)controller;
		displayShippingInformationFormController.displayShippingInformationForm(shippingInformation);
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
