package shoppingappjavafx.gui.controller;

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
import shoppingappjavafx.domain.Product;
import shoppingappjavafx.domain.Products;
import shoppingappjavafx.domain.PurchaseOrder;
import shoppingappjavafx.usecase.event.AddProductToCart;
import shoppingappjavafx.usecase.event.CheckoutPurchase;

public class DisplayProductsController extends AbstractController{
    @FXML
    private Label shoppingCartItemCountLabel;

    @FXML
    private Button checkoutButton;
    
    @FXML
    private ListView<Product> productsListView;
        
    @FXML
    void onCheckout(ActionEvent event) {
    	CheckoutPurchase checkoutPurchase =  new CheckoutPurchase();
    	useCaseModelRunner().reactTo(checkoutPurchase);
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
			AddProductToCart addProductToCart = new AddProductToCart(product);
			useCaseModelRunner().reactTo(addProductToCart);
			productsListView.refresh();
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
                enableOrDisableBuyButton();
                enableOrDisableCheckoutButton();
            }
        }
        
    	private void enableOrDisableBuyButton() {
    		buyButton.setDisable(whenNoMoreProductsCanBeBought());
    	}
    	private boolean whenNoMoreProductsCanBeBought() {
    		return !useCaseModelRunner().canReactTo(AddProductToCart.class);
    	}
    }

	public void displayProducts(Products products) {
		productsListView.setCellFactory(listView -> new ProductListItem());
		productsListView.setItems(products.get());
	}

	public void displayShoppingCartSize(PurchaseOrder purchaseOrder) {
		shoppingCartItemCountLabel.textProperty().bind(convert(size(purchaseOrder.findProducts())));
	}
	
	private void enableOrDisableCheckoutButton() {
		checkoutButton.setDisable(whenCheckoutIsNotPossible());
	}
	private boolean whenCheckoutIsNotPossible() {
		return !useCaseModelRunner().canReactTo(CheckoutPurchase.class);
	}
}