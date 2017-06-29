package hexagon;

public class RepositoryAdapter implements RepositoryPort {
  @Override
  public String getSadPoem() {
    return "The saddest poem ever written.";
  }

  @Override
  public String getHappyPoem() {
    return "The happiest poem ever written.";
  }
  
  @Override
  public String getFunnyPoem() {
    return "The funniest poem ever written.";
  }
}
