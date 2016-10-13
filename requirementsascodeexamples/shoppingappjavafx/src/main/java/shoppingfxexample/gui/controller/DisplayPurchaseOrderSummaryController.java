package shoppingfxexample.gui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;

public class DisplayPurchaseOrderSummaryController {
    @FXML
    private Button finishButton;
    
    @FXML
    private ListView<String> purchaseOrderSummaryListView;
	
    @FXML
    void onFinish(ActionEvent event) {
    }
    
	public void displayPurchaseOrderSummary(PurchaseOrder purchaseOrder) {
		List<String> purchaseOrderSummary = new ArrayList<>();
		purchaseOrderSummary.add("-------------------------------------");
		purchaseOrderSummary.add("Your products:");
		purchaseOrderSummary.add("-------------------------------------");
		List<String> productSummary = purchaseOrder.getProductSummary();
		purchaseOrderSummary.addAll(productSummary);
		
		purchaseOrderSummary.add("-------------------------------------");
		purchaseOrderSummary.add("Your shipping information:");
		purchaseOrderSummary.add("-------------------------------------");
		List<String> shippingInformationSummary = 
			purchaseOrder.getShippingInformation().getSummary();
		purchaseOrderSummary.addAll(shippingInformationSummary);
		purchaseOrderSummary.add("-------------------------------------");
		
		ObservableList<String> observablePurchaseOrderSummary = 
				FXCollections.observableArrayList(purchaseOrderSummary);
		
		purchaseOrderSummaryListView.setItems(observablePurchaseOrderSummary);
	}

	public ObservableList<String> getProductSummary(ObservableList<Product> products) {
		Map<String, Long> productToNumberBoughtMap = products.stream()
			.collect(Collectors.groupingBy(Product::getProductName, Collectors.counting()));
		
		List<String> productSummary = productToNumberBoughtMap.entrySet().stream()
			.map(entry -> entry.getValue() + "x " + entry.getKey())
			.collect(Collectors.toList());
				
		ObservableList<String> observableProductSummary = FXCollections.observableArrayList(productSummary);
		return observableProductSummary;
	}
}
