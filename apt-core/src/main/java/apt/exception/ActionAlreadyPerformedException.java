package apt.exception;

public class ActionAlreadyPerformedException extends GenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ActionAlreadyPerformedException() {
	super();
    }

    public ActionAlreadyPerformedException(Exception e, String message) {
	super(e, message);
    }

    public ActionAlreadyPerformedException(Exception e) {
	super(e);
    }

    public ActionAlreadyPerformedException(String message) {
	super(message);
    }
    

}
