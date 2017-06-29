package hexagon;

public class RepositoryAdapter implements RepositoryPort {
  @Override
  public String getSadPoem() {
    return "That is one sad poem.";
  }

  @Override
  public String getHappyPoem() {
    return "That is one happy poem.";
  }
  
  @Override
  public String getFunnyPoem() {
    return "That is one funny poem.";
  }
}
