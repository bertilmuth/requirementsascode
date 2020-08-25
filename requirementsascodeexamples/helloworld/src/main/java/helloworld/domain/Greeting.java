package helloworld.domain;

class Greeting {
  public static String forUserWithName(String userName) {
    return "Hello, " + userName + ".";
  }

  public static String forUserWithAge(int age) {
    return "You are " + age + " years old.";
  }
}