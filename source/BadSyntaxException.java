package source;

public class BadSyntaxException extends Exception {
  public BadSyntaxException() {
      super();
  }
  
  public BadSyntaxException(String message) {
      super(message);
  }
}