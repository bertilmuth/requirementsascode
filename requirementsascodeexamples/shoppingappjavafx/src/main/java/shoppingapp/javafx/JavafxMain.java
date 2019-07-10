package shoppingapp.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import shoppingapp.boundary.Boundary;
import shoppingapp.boundary.internal.domain.Stock;
import shoppingapp.javafx.driven_adapter.JavafxDisplay;

public class JavafxMain extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		Stock stock = new Stock();
		JavafxDisplay javafxDisplay = new JavafxDisplay(primaryStage);
		Boundary boundary = new Boundary(stock, javafxDisplay);
		javafxDisplay.setBoundary(boundary);

		primaryStage.setTitle("Shopping Application (JavaFX) - Requirements as Code");
		primaryStage.show();
		boundary.run();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
