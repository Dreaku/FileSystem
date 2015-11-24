package ro.dreaku.filesystem.disk;

import ro.dreaku.filesystem.exception.BadBlockException;

public class Disk implements DiskInterface
{
    /** Dimensiunea blocurilor diskului simulat. */
    private static final int BLOCK_SIZE = 1024;

    public byte[][] disk;
    private final int diskSize;

    public Disk(int diskSize)
    {
        this.diskSize = diskSize;
        disk = new byte[diskSize][];
    }

    @Override
    public void format()
    {
        disk = new byte[diskSize][];
    }

    @Override
    public int getSize()
    {
        return diskSize;
    }

    @Override
    public void writeBlock(byte[] buffer, int blockNum) throws BadBlockException
    {
        if (blockNum < 0 || blockNum >= diskSize)
            throw new BadBlockException("Illegal block number: " + blockNum);
        if (buffer.length != BLOCK_SIZE)
            throw new BadBlockException("Wrong buffer size: " + buffer.length);
        disk[blockNum] = buffer;
    }

    @Override
    public byte[] readBlock(int blockNum) throws BadBlockException
    {
        if (blockNum < 0 || blockNum >= diskSize)
            throw new BadBlockException("Illegal block number: " + blockNum);
        byte[] block = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++)
            block[i] = disk[blockNum][i];
        return block;
    }
}
