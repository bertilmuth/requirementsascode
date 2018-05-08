package creditcard_eventsourcing.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.extract.freemarker.FreeMarkerEngine;

import creditcard_eventsourcing.model.CreditCard;
import creditcard_eventsourcing.model.CreditCardModelRunner;

public class CreditCardExtract {
    public static void main(String[] args) throws Exception {
	new CreditCardExtract().start();
    }

    private void start() throws Exception {
	FreeMarkerEngine engine = new FreeMarkerEngine("");
	File outputFile = outputFile();
	engine.extract(model(), templateFileName(), new FileWriter(outputFile));

	System.out.println("Wrote file to: " + outputFile);
    }

    private Model model() {
	CreditCard creditCard = new CreditCard(UUID.randomUUID());
	Model model = new CreditCardModelRunner(creditCard, new ModelRunner()).model();
	return model;
    }

    private String templateFileName() {
	return "htmlExample.ftlh";
    }

    private File outputFile() throws IOException {
	File outputFile = File.createTempFile("creditcard_eventsourcing_", ".html");
	return outputFile;
    }
}
