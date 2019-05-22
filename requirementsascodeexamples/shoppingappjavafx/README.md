# Shopping Application (JavaFX)
Small, simplistic application that demonstrates a "shopping-like" use case:
* The customer can buy up to 10 items
* The customer then enters shipping information
* The customer enters payment information (well, not really)
* The system shows the customer a summary of the entered data, and the customer
finishes and return to the main page.

Run the application via the Gradle run task. 

The main class of this application is ```shoppingapp.javafx.JavafxMain```.

This application illustrates how a use case can be represented with 
requirementsascode in the ```shoppingapp.boundar.UseCaseModel``` class.

This is also an example of how requirementsascode separates the frontend
(JavaFX user interface in the ```shoppingapp.javafx``` package)
from the backend domain model (in the ```shoppingapp.boundary.internal.domain``` package).

The frontend sends events to the model runner. The runner, knowing which events to
react to under which conditions because it is configured by the model, forwards 
each event to a single method in the backend. 
All communication from frontend to backend is channeled through the model runner.

Note that if you want to use the shoppingappjavafx example in Eclipse, you should use
the e(fx)clipse plugin.

