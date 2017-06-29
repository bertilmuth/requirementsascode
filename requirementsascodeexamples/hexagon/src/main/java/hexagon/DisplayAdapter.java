package hexagon;

public class DisplayAdapter implements DisplayPort{
  public DisplayAdapter() {}
  
  @Override
  public void displaySadPoem(AskForPoem ask){
    System.out.println("The saddest poem ever written."); 
  }
  
  @Override
  public void displayHappyPoem(AskForPoem ask){
    System.out.println("The happiest poem ever written."); 
  }
  
  @Override
  public void displayFunnyPoem(AskForPoem ask){
    System.out.println("The funniest poem ever written."); 
  }
}