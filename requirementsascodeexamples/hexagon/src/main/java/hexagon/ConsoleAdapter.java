package hexagon;

public class ConsoleAdapter implements ConsolePort{
  private RepositoryPort repositoryPort;
  private WriterPort writerPort;

  public ConsoleAdapter(WriterPort writerPort, RepositoryPort repositoryPort) {
    this.writerPort = writerPort;
    this.repositoryPort = repositoryPort;
  }
  
  @Override
  public void writeSadPoem(AskForPoem ask){
    String poem = repositoryPort.getSadPoem();
    writerPort.write(poem); 
  }
  
  @Override
  public void writeHappyPoem(AskForPoem ask){
    String poem = repositoryPort.getHappyPoem();
    writerPort.write(poem); 
  }
  
  @Override
  public void writeFunnyPoem(AskForPoem ask){
    String poem = repositoryPort.getFunnyPoem();
    writerPort.write(poem); 
  }
}