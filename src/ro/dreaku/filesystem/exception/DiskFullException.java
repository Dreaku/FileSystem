package ro.dreaku.filesystem.exception;

/**
 * Exception thrown when there is no more space on the disk or there are no more entries available.
 */
public class DiskFullException extends Exception
{
    private static final long serialVersionUID = 1L;

    public DiskFullException()
    {
    }

    public DiskFullException(String message)
    {
        super(message);
    }
}
