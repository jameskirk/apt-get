package emerge.exception;

public class NoSuchPackageException extends GenericException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoSuchPackageException() {
	super();
    }

    public NoSuchPackageException(Exception e, String message) {
	super(e, message);
    }

    public NoSuchPackageException(Exception e) {
	super(e);
    }

    public NoSuchPackageException(String message) {
	super(message);
    }
    
    

}
