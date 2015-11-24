package ro.dreaku.filesystem.disk;

import ro.dreaku.filesystem.exception.BadBlockException;

/** Aceasta interfata implementeaza un disk brut. */
public interface DiskInterface
{

    /** Sterge toate datele de pe disk. */
    public void format();

    /**
     * @returneaza numarul total de blocuri de pe disk.
     */
    public int getSize();

    /**
     * Scrie blocul cu numarul dat.
     *
     * @param buffer
     *            reprezinta bufferul din care se scrie blocul
     * @param blockNum
     *            reprezinta numarul blocului ce urmeaza a fi scris
     * @Daca buffer nu are dimensiunea BLOCK_SIZE sau indexul blocului este eronat, metoda trebuie
     *       sa arunce exceptia BadBlockException
     */
    public void writeBlock(byte[] buffer, int blockNum) throws BadBlockException;

    /**
     * Citeste blocul cu numarul dat.
     *
     * @param blockNum
     *            reprezinta numarul blocului ce urmeaza a fi citit
     * @metoda returneaza continutul blocului citit
     * @Daca indexul blocului este eronat, metoda trebuie sa arunce exceptia BadBlockException
     */
    public byte[] readBlock(int blockNum) throws BadBlockException;
}
