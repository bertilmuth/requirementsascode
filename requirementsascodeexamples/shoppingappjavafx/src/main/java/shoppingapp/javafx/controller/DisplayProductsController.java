package shoppingapp.javafx.controller;

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
import shoppingapp.boundary.internal.domain.Product;
import shoppingapp.boundary.internal.domain.Products;
import shoppingapp.boundary.internal.domain.PurchaseOrder;
import shoppingapp.command.AddsProductToCart;
import shoppingapp.command.ChecksOutPurchase;

public class DisplayProductsController extends AbstractController {
	@FXML
	private Label shoppingCartItemCountLabel;

	@FXML
	private Button checkoutButton;

	@FXML
	private ListView<Product> productsListView;

	@FXML
	void onCheckout(ActionEvent event) {
		ChecksOutPurchase checkoutPurchase = new ChecksOutPurchase();
		boundary().reactTo(checkoutPurchase);
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
			buyButton.setOnAction(clickEvent -> buyProduct());
		}

		private void buyProduct() {
			AddsProductToCart addProductToCart = new AddsProductToCart(product);
			boundary().reactTo(addProductToCart);
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
				label.setText(item != null ? item.getProductName() : "<null>");
				setGraphic(hbox);
				enableOrDisableBuyButton();
				enableOrDisableCheckoutButton();
			}
		}

		private void enableOrDisableBuyButton() {
			buyButton.setDisable(whenNoMoreProductsCanBeBought());
		}

		private boolean whenNoMoreProductsCanBeBought() {
			return !boundary().canReactTo(AddsProductToCart.class);
		}
	}

	public void displayProducts(Products products, PurchaseOrder purchaseOrder) {
		productsListView.setCellFactory(listView -> new ProductListItem());
		productsListView.setItems(products.get());
		shoppingCartItemCountLabel.textProperty().bind(convert(size(purchaseOrder.findProducts())));
	}

	private void enableOrDisableCheckoutButton() {
		checkoutButton.setDisable(whenCheckoutIsNotPossible());
	}

	private boolean whenCheckoutIsNotPossible() {
		return !boundary().canReactTo(ChecksOutPurchase.class);
	}
}