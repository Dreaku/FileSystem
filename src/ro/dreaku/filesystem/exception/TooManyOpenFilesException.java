package ro.dreaku.filesystem.exception;

/**
 * A fost depasit numarul maxim admis de fisiere deschise.
 */
public class TooManyOpenFilesException extends Exception
{
    private static final long serialVersionUID = 1L;

    public TooManyOpenFilesException()
    {
    }

    public TooManyOpenFilesException(String message)
    {
        super(message);
    }
}
