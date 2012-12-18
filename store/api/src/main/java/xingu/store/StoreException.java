package xingu.store;

public class StoreException
    extends RuntimeException
{

	public StoreException() {
		super();
	}

	public StoreException(String message, Throwable e) {
		super(message, e);
	}

	public StoreException(String message) {
		super(message);
	}

	public StoreException(Throwable e) {
		super(e);
	}

	

}
