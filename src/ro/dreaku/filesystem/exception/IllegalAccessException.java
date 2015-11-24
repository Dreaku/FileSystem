package ro.dreaku.filesystem.exception;

/**
 * Exceptie aruncata cand se incearca scrierea intr-un fisier Read-Only sau cand se incearca citirea
 * dupa EOF.
 */
public class IllegalAccessException extends Exception
{
    private static final long serialVersionUID = 1L;

    public IllegalAccessException()
    {
    }

    public IllegalAccessException(String message)
    {
        super(message);
    }
}
