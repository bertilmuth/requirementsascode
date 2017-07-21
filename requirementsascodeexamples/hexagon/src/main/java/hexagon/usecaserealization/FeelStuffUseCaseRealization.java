package hexagon.usecaserealization;

import hexagon.usecase.AskForPoem;

public class FeelStuffUseCaseRealization{
  private RepositoryPort repositoryPort;
  private WriterPort writerPort;

  public FeelStuffUseCaseRealization(WriterPort writerPort, RepositoryPort repositoryPort) {
    this.writerPort = writerPort;
    this.repositoryPort = repositoryPort;
  }
  
  public void writeSadPoem(AskForPoem ask){
    String poem = repositoryPort.getSadPoem();
    writerPort.write(poem); 
  }
  
  public void writeHappyPoem(AskForPoem ask){
    String poem = repositoryPort.getHappyPoem();
    writerPort.write(poem); 
  }
  
  public void writeFunnyPoem(AskForPoem ask){
    String poem = repositoryPort.getFunnyPoem();
    writerPort.write(poem); 
  }
}