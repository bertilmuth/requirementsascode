#Shopping Application Example (JavaFX)
Small, simplistic application that demonstrates a "shopping-like" use case:
* The customer can buy up to 10 items
* The customer then enters shipping information
* The system shows the customer a summary of the entered data, and the customer
finishes and return to the main page.

This application illustrates how the use case narrative can be represented with 
requirementsascode in the ```shoppingfxexample.usecase``` package.

This is also an example of how requirementsascode separates the frontend
(JavaFX user interface in the ```shoppingfxexample.gui``` package)
from the backend domain model (in the ```shoppingfxexample.domain``` package).

The frontend sends events to the use case runner. The runner, knowing which events to
react to under which conditions because it is configured by the use case model, forwards 
each event to a single method in the backend. 
All communication from frontend to backend is channeled through the use case runner.
