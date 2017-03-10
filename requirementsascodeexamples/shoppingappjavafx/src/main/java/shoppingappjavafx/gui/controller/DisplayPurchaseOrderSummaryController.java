package shoppingappjavafx.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecase.event.ConfirmPurchase;

public class DisplayPurchaseOrderSummaryController extends AbstractController{
    @FXML
    private Button finishButton;
    
    @FXML
    private ListView<String> purchaseOrderSummaryListView;
	
    @FXML
    void onFinish(ActionEvent event) {
    	useCaseRunner().reactTo(new ConfirmPurchase());
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
			purchaseOrder.shippingInformation().getSummary();
		purchaseOrderSummary.addAll(shippingInformationSummary);
		purchaseOrderSummary.add("-------------------------------------");
		
		ObservableList<String> observablePurchaseOrderSummary = 
				FXCollections.observableArrayList(purchaseOrderSummary);
		
		purchaseOrderSummaryListView.setItems(observablePurchaseOrderSummary);
	}
}
