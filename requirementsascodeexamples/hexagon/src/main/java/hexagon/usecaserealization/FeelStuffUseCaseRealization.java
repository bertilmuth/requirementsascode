package hexagon.usecaserealization;

import hexagon.usecase.AsksForPoem;

public class FeelStuffUseCaseRealization{
  private RepositoryPort repositoryPort;
  private WriterPort writerPort;

  public FeelStuffUseCaseRealization(WriterPort writerPort, RepositoryPort repositoryPort) {
    this.writerPort = writerPort;
    this.repositoryPort = repositoryPort;
  }
  
  public void writesSadPoem(AsksForPoem ask){
    String poem = repositoryPort.getSadPoem();
    writerPort.write(poem); 
  }
  
  public void writesHappyPoem(AsksForPoem ask){
    String poem = repositoryPort.getHappyPoem();
    writerPort.write(poem); 
  }
  
  public void writesFunnyPoem(AsksForPoem ask){
    String poem = repositoryPort.getFunnyPoem();
    writerPort.write(poem); 
  }
}