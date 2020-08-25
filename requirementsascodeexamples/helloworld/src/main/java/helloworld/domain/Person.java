package helloworld.domain;

public class Person {
  private final int MIN_AGE = 5;
  private final int MAX_AGE = 130;

  private String name;
  private int age;

  public boolean ageIsOutOfBounds() {
    return age < MIN_AGE || age > MAX_AGE;
  }

  public boolean ageIsOk() {
    return !ageIsOutOfBounds();
  }

  public void saveName(String name) {
    this.name = name;
  }

  public void saveAge(int age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}
