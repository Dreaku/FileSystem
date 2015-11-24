package ro.dreaku.filesystem.filesystem;

import ro.dreaku.filesystem.exception.DiskFullException;
import ro.dreaku.filesystem.exception.IllegalAccessException;

public interface FileSystemInterface
{
    /** Fisier deschis in modul READ-ONLY */
    public static final int READ_ONLY = 0;
    /** Fisier deschis in modul READ/WRITE */
    public static final int READ_WRITE = 1;

    /**
     * Citeste si returneaza readCount octeti din fisier, incepand cu pozitia curenta (data de
     * pointerul position). Pointerul position este setat pe pozitia fiecarui octet citit. Daca se
     * incearca citirea dupa EOF, metoda trebuie sa arunce exceptia IllegalAccessException.
     *
     * @param buffer
     *            este un buffer in care se plaseaza rezultatul
     * @param readCount
     *            reprezinta nr de octeti ce trebuie cititi, incepand cu pozitia curenta
     * @metoda returneaza numarul de octeti cititi.
     */
    public int read(byte buffer[], int readCount) throws IllegalAccessException;

    /**
     * Scrie writeCount octeti incepand cu pozitia curenta in fisier (data de pointerul position).
     * Pointerul position este setat pe pozitia fiecarui octet scris. Daca se incearca scrierea dupa
     * EOF, sau intr-un fisier Read-Only, metoda trebuie sa arunce exceptia IllegalAccessException.
     * Daca in timpul scrierii este depasit spatiul pe disk, trebuie lansata exceptia
     * DiskFullException.
     *
     * @param buffer
     *            reprezinta bufferul de date ce urmeaza a fi scris in fisier
     * @param writeCount
     *            reprezinta nr de octeti ce urmeaza a fi scrisi, incepand cu pozitia curenta
     * @returneaza numarul actual de octeti scrisi
     */
    public int write(byte buffer[], int writeCount) throws IllegalAccessException, DiskFullException;

    /**
     * Seteaza pozitia curenta in fisier (pointerul position). Daca argumentul este negativ,
     * position se seteaza pe 0. Daca argumentul depaseste EOF, position se seteaza la capatul
     * fisierului.
     *
     * @param position
     *            reprezinta noua valoare a pointerului position
     */
    public void seek(int position);

    /**
     * @returneaza lungimea curenta a fisierului [in octeti]
     */
    public int getSize();

    /**
     * @returneaza modul in care a fost deschis fisierul
     */
    public int getMode();
}
