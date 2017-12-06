package apt.exception;


public class GenericException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public GenericException(String message) {
	super(message);
    }
    
    public GenericException(Exception e) {
	super(e);
    }
    
    public GenericException(Exception e, String message) {
	super(e);
    }
    
    public GenericException() {
	super();
    }


}
