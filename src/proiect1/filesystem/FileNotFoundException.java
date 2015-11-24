package proiect1.filesystem;

/**
 * Exceptie aruncata la incercarea de a opera asupra unui fisier care nu exista.
 */
public class FileNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileNotFoundException() {
	}

	public FileNotFoundException(String message) {
		super(message);
	}
}
