package ro.dreaku.filesystem.file;

import java.awt.Point;

import ro.dreaku.filesystem.Console;
import ro.dreaku.filesystem.exception.DiskFullException;
import ro.dreaku.filesystem.exception.IllegalAccessException;

public class File implements FileInterface
{
    Console console;
    Point entry;
    int mode;
    int position;

    File(Console console, Point entry, int mode)
    {
        this.console = console;
        this.entry = entry;
        this.mode = mode;
    }

    @Override
    public int read(byte[] buffer, int readCount) throws IllegalAccessException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int write(byte[] buffer, int writeCount) throws IllegalAccessException, DiskFullException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void seek(int position)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getSize()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMode()
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
