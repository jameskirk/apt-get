package apt.exception;

public class InternalException extends GenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InternalException() {
	super();
    }

    public InternalException(Exception e, String message) {
	super(e, message);
    }

    public InternalException(Exception e) {
	super(e);
    }

    public InternalException(String message) {
	super(message);
    }
    

}
