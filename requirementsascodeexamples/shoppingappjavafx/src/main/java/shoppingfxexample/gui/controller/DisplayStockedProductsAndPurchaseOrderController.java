package shoppingfxexample.gui.controller;

import static javafx.beans.binding.Bindings.convert;
import static javafx.beans.binding.Bindings.size;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import shoppingfxexample.domain.Product;
import shoppingfxexample.domain.PurchaseOrder;
import shoppingfxexample.usecase.event.BuyProductEvent;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrderEvent;

public class DisplayStockedProductsAndPurchaseOrderController extends AbstractUseCaseRunnerController{
    @FXML
    private Label shoppingCartItemCountLabel;

    @FXML
    private Button checkoutButton;
    
    @FXML
    private ListView<Product> productsListView;
    
	private PurchaseOrder purchaseOrder;
    
    @FXML
    void onCheckout(ActionEvent event) {
    	CheckoutPurchaseEvent checkoutPurchase =  new CheckoutPurchaseEvent(purchaseOrder);
    	getUseCaseRunner().reactTo(checkoutPurchase);
    }
	
	private class ProductListItem extends ListCell<Product> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button buyButton = new Button("Buy");
        Product product;

        public ProductListItem() {
            super();
            hbox.getChildren().addAll(label, pane, buyButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
            buyButton.setOnAction(clickEvent ->  buyProduct());
        }

		private void buyProduct() {
			BuyProductEvent buyProduct = new BuyProductEvent(product);
			getUseCaseRunner().reactTo(buyProduct);
		}


        @Override
        protected void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            setText(null); 
            if (empty) {
                product = null;
                setGraphic(null);
            } else {
                product = item;
                label.setText(item!=null ? item.getProductName() : "<null>");
                setGraphic(hbox);
            }
        }
    }

	public void displayStockedProductsAndPurchaseOrder(DisplayStockedProductsAndPurchaseOrderEvent displayStockedProductsAndPurchaseOrder) {
		productsListView.setCellFactory(listView -> new ProductListItem());
		productsListView.setItems(displayStockedProductsAndPurchaseOrder.getProductsInStock());
		purchaseOrder = displayStockedProductsAndPurchaseOrder.getPurchaseOrder();
		shoppingCartItemCountLabel.textProperty().bind(convert(size(purchaseOrder.findProducts())));
	}
}