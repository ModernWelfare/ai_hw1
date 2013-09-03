@SuppressWarnings("serial")
public class Connect4Exception extends Exception {

	@SuppressWarnings("unused")
	private final String errorMsg;

	public Connect4Exception(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
