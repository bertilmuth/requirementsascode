package shoppingfxexample.gui.controller;

import static javafx.beans.binding.Bindings.convert;
import static javafx.beans.binding.Bindings.size;

import org.requirementsascode.UseCaseRunner;

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
import shoppingfxexample.usecase.event.BuyProduct;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.DisplayStockedProductsAndPurchaseOrder;

public class DisplayStockedProductsController {
    @FXML
    private Label shoppingCartItemCountLabel;

    @FXML
    private Button checkoutButton;
    
    @FXML
    private ListView<Product> productsListView;
    
	private PurchaseOrder purchaseOrder;
	private UseCaseRunner useCaseModelRun;
    
    @FXML
    void onCheckout(ActionEvent event) {
    	CheckoutPurchase checkoutPurchase =  new CheckoutPurchase(purchaseOrder);
    	useCaseModelRun.reactTo(checkoutPurchase);
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
			BuyProduct buyProduct = new BuyProduct(product);
			useCaseModelRun.reactTo(buyProduct);
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

	public void setUseCaseModelRun(UseCaseRunner useCaseModelRun) {
		this.useCaseModelRun = useCaseModelRun;
	}

	public void displayStockedProductsAndPurchaseOrder(DisplayStockedProductsAndPurchaseOrder stockedProductsAndPurchaseOrder) {
		productsListView.setCellFactory(listView -> new ProductListItem());
		productsListView.setItems(stockedProductsAndPurchaseOrder.getProducts());
		purchaseOrder = stockedProductsAndPurchaseOrder.getPurchaseOrder();
		shoppingCartItemCountLabel.textProperty().bind(convert(size(purchaseOrder.getProducts())));
	}


}