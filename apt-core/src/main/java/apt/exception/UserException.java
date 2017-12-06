package apt.exception;

public class UserException extends GenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserException() {
	super();
    }

    public UserException(Exception e, String message) {
	super(e, message);
    }

    public UserException(Exception e) {
	super(e);
    }

    public UserException(String message) {
	super(message);
    }
    

}
