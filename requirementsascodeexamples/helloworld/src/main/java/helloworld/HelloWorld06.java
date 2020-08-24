package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Condition;
import org.requirementsascode.Model;

import helloworld.actor.AnonymousUser;
import helloworld.actor.NormalUser;
import helloworld.command.EnterText;

public class HelloWorld06{
	public static final Consumer<EnterText> saveName = HelloWorld06::saveName;
	public static final Consumer<EnterText> saveAge = HelloWorld06::saveAge;
	public static final Runnable greetUserWithName = HelloWorld06::greetUserWithName;
	public static final Runnable greetUserWithAge = HelloWorld06::greetUserWithAge;
	public static final Condition ageIsOk = HelloWorld06::ageIsOk;
	public static final Condition ageIsOutOfBounds = HelloWorld06::ageIsOutOfBounds;

  private static final int MIN_AGE = 5;
  private static final int MAX_AGE = 130;

  private static String firstName;
  private static int age;

	private static void saveName(EnterText enterText) {
		firstName = enterText.text;
	}

	private static void saveAge(EnterText enterText) {
		age = Integer.parseInt(enterText.text);
	}

	private static void greetUserWithName() {
		System.out.println("Hello, " + firstName + ".");
	}

	private static void greetUserWithAge() {
		System.out.println("You are " + age + " years old.");
	}

	private static boolean ageIsOutOfBounds() {
		return age < MIN_AGE || age > MAX_AGE;
	}

	private static boolean ageIsOk() {
		return !ageIsOutOfBounds();
	}

	public static void main(String[] args) {
		HelloWorldActor06 helloWorldActor = new HelloWorldActor06(saveName, saveAge, greetUserWithName, greetUserWithAge, ageIsOk, ageIsOutOfBounds);
    
    NormalUser normalUser = new NormalUser(helloWorldActor);
    AnonymousUser anonymousUser = new AnonymousUser(helloWorldActor);
    helloWorldActor.setNormalUser(normalUser);
    helloWorldActor.setAnonymousUser(anonymousUser);

    normalUser.run();	
  }
}

class HelloWorldActor06 extends AbstractActor{
  private final Class<EnterText> entersName = EnterText.class;
  private final Consumer<EnterText> savesName;
  private final Class<EnterText> entersAge = EnterText.class;
  private final Consumer<EnterText> savesAge;
  private final Runnable greetsUserWithName;
  private final Runnable greetsUserWithAge;
  private final Condition ageIsOk;
  private final Condition ageIsOutOfBounds;
  private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;

  private AbstractActor normalUser;
  private AbstractActor anonymousUser;
  
  public HelloWorldActor06(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUserWithName,
    Runnable greetsUserWithAge, Condition ageIsOk, Condition ageIsOutOfBounds) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUserWithName = greetsUserWithName;
    this.greetsUserWithAge = greetsUserWithAge;
    this.ageIsOk = ageIsOk;
    this.ageIsOutOfBounds = ageIsOutOfBounds;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").as(normalUser).user(entersName).system(savesName)
          .step("S2").as(normalUser, anonymousUser).user(entersAge).system(savesAge)
          .step("S3").as(normalUser).system(greetsUserWithName)
          .step("S4").as(normalUser, anonymousUser).system(greetsUserWithAge)
        .flow("Handle out-of-bounds age").insteadOf("S3").condition(ageIsOutOfBounds)
          .step("S5a_2").continuesAt("S2")
        .flow("Handle non-numerical age").insteadOf("S3")
          .step("S5b_2").on(numberFormatException).continuesAt("S2")
        .flow("Anonymous greeted with age only").insteadOf("S3").condition(ageIsOk)
          .step("S5c_1").as(anonymousUser).continuesAt("S4")
        .flow("Anonymous does not enter name").insteadOf("S1")
          .step("S1a_1").as(anonymousUser).continuesAt("S2")
      .build();
    
    return model;
  }
  
  public void setNormalUser(AbstractActor normalUser) {
    this.normalUser = normalUser;
  }
  
  public void setAnonymousUser(AbstractActor anonymousUser) {
    this.anonymousUser = anonymousUser;
  }
}