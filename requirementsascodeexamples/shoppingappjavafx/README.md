# Shopping Application (JavaFX)
Small, simplistic application that demonstrates a "shopping-like" use case:
* The customer can buy up to 10 items
* The customer then enters shipping information
* The customer enters payment information (well, not really)
* The system shows the customer a summary of the entered data, and the customer
finishes and return to the main page.

The main class of this application is ```shoppingappjavafx.gui.JavafxMain```.

This application illustrates how the use case narrative can be represented with 
requirementsascode in the ```shoppingappjavafx.usecase``` package.

This is also an example of how requirementsascode separates the frontend
(JavaFX user interface in the ```shoppingappjavafx.gui``` package)
from the backend domain model (in the ```shoppingappjavafx.domain``` package).

The frontend sends events to the use case model runner. The runner, knowing which events to
react to under which conditions because it is configured by the use case model, forwards 
each event to a single method in the backend. 
All communication from frontend to backend is channeled through the use case model runner.

# Getting started
* Download the latest shoppingappjavafx zip file from the [Releases](https://github.com/bertilmuth/requirementsascode/releases) tab.
* Extract it to a local folder, and run the batch file from the bin folder

Note that if you want to use the shoppingappjavafx example in Eclipse, you should use
the e(fx)clipse plugin.
