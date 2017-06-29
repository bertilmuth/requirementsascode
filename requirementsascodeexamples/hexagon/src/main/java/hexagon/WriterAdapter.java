package hexagon;

public class WriterAdapter implements WriterPort {
  @Override
  public void write(String string) {
    System.out.println(string); 
  }
}
