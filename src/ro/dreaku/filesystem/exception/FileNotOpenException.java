package ro.dreaku.filesystem.exception;

/**
 * Exceptie aruncata la incercarea de a opera asupra unui fisier care nu este deschis.
 */
public class FileNotOpenException extends Exception
{
    private static final long serialVersionUID = 1L;

    public FileNotOpenException()
    {
    }

    public FileNotOpenException(String message)
    {
        super(message);
    }
}
