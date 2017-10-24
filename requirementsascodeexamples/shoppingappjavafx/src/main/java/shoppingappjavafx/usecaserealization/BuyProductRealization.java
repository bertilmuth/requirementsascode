package shoppingappjavafx.usecaserealization;

import shoppingappjavafx.domain.Stock;
import shoppingappjavafx.usecaserealization.componentinterface.Display;
import shoppingappjavafx.usecaserealization.predicate.AnExceptionOccurs;
import shoppingappjavafx.usecaserealization.predicate.AtLeastOneProductIsInCart;
import shoppingappjavafx.usecaserealization.predicate.LessThan10Products;
import shoppingappjavafx.usecaserealization.systemreaction.AddsProductToPurchaseOrder;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysPaymentDetailsForm;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysProducts;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysPurchaseOrderSummary;
import shoppingappjavafx.usecaserealization.systemreaction.DisplaysShippingInformationForm;
import shoppingappjavafx.usecaserealization.systemreaction.InitiatesShipping;
import shoppingappjavafx.usecaserealization.systemreaction.LogsException;
import shoppingappjavafx.usecaserealization.systemreaction.SavesPaymentDetails;
import shoppingappjavafx.usecaserealization.systemreaction.SavesShippingInformation;
import shoppingappjavafx.usecaserealization.systemreaction.StartsWithEmptyShoppingCart;

public class BuyProductRealization {
  private Stock stock;
  private Display display;
  private RunContext runContext;

  public BuyProductRealization(Stock stock, Display display) {
    this.stock = stock;
    this.display = display;
    this.runContext = new RunContext();
  }

  public StartsWithEmptyShoppingCart startsWithEmptyShoppingCart() {
    return new StartsWithEmptyShoppingCart(runContext);
  }

  public DisplaysProducts displaysProducts() {
    return new DisplaysProducts(runContext, stock, display);
  }

  public AddsProductToPurchaseOrder addsProductToPurchaseOrder() {
    return new AddsProductToPurchaseOrder(runContext);
  }

  public LessThan10Products lessThan10Products() {
    return new LessThan10Products(runContext);
  }

  public DisplaysShippingInformationForm displaysShippingInformationForm() {
    return new DisplaysShippingInformationForm(runContext, display);
  }

  public SavesShippingInformation savesShippingInformation() {
    return new SavesShippingInformation(runContext);
  }

  public DisplaysPaymentDetailsForm displaysPaymentDetailsForm() {
    return new DisplaysPaymentDetailsForm(display);
  }

  public SavesPaymentDetails savesPaymentDetails() {
    return new SavesPaymentDetails(runContext);
  }

  public DisplaysPurchaseOrderSummary displaysPurchaseOrderSummary() {
    return new DisplaysPurchaseOrderSummary(runContext, display);
  }

  public InitiatesShipping initiatesShipping() {
    return new InitiatesShipping();
  }

  public AtLeastOneProductIsInCart atLeastOneProductIsInCart() {
    return new AtLeastOneProductIsInCart(runContext);
  }

  public AnExceptionOccurs anExceptionOccurs() {
    return new AnExceptionOccurs();
  }

  public LogsException logsException() {
    return new LogsException();
  }
}
