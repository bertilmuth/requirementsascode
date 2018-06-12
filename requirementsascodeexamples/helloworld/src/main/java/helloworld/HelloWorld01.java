package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

public class HelloWorld01 {

    public Model buildWith(ModelBuilder modelBuilder) {
	Model model = modelBuilder.useCase("Get greeted")
		.basicFlow()
			.step("S1").system(this::greetsUser)
		.build();
	return model;
    }

    private void greetsUser() {
	System.out.println("Hello, User.");
    }

    public static void main(String[] args) {
	HelloWorld01 example = new HelloWorld01();
	example.start();
    }

    private void start() {
	Model model = buildWith(Model.builder());
	new ModelRunner().run(model);
    }
}