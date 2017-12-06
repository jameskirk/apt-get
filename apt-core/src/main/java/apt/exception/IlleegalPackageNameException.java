package apt.exception;

public class IlleegalPackageNameException extends GenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IlleegalPackageNameException() {
	super();
    }

    public IlleegalPackageNameException(Exception e, String message) {
	super(e, message);
    }

    public IlleegalPackageNameException(Exception e) {
	super(e);
    }

    public IlleegalPackageNameException(String message) {
	super(message);
    }
    
}
