package ro.dreaku.filesystem.exception;

/**
 * Este lansata cand se incearca denumirea unui fisier cu un nume care deja a fost alocat (in
 * directorul respectiv)
 */
public class FileAlreadyExistsException extends Exception
{
    private static final long serialVersionUID = 1L;

    public FileAlreadyExistsException()
    {
    }

    public FileAlreadyExistsException(String message)
    {
        super(message);
    }
}
