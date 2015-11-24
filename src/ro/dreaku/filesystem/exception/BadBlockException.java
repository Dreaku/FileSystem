package ro.dreaku.filesystem.exception;

/**
 * Exception thrown when the buffer does not have the size
 * {@link ro.dreaku.filesystem.disk.Disk#BLOCK_SIZE BLOCK_SIZE} or the block number is illegal.
 */
public class BadBlockException extends Exception
{
    private static final long serialVersionUID = 1L;

    public BadBlockException()
    {
    }

    public BadBlockException(String message)
    {
        super(message);
    }
}
