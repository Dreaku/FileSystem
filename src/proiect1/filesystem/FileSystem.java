package proiect1.filesystem;

public interface FileSystem {
	/** Numarul maxim de fisiere deschise simultan. */
	public static final int MAX_OPEN = 16;

	/**
	 * Creaza, deschide si cauta octetul 0 intr-un nou fisier.
	 * 
	 * @param name
	 *            reprezinta numele fisierului creat.
	 * @returneaza o referinta la fisierul creat.
	 * @lanseaza PhileAlreadyExistsException daca numele dat este deja folosit
	 *           de un alt fisier sau director.
	 * @lanseaza DiskFullException daca nu mai este spatiu pe disk (mai precis,
	 *           nu mai sunt intrari disponibile).
	 * @lanseaza exceptia TooManyOpenPhilesException daca a fost depasit numarul
	 *           maxim admis de fisiere deschise.
	 */
	public FileInterface create(String name) throws FileAlreadyExistsException,
			TooManyOpenFilesException, DiskFullException;

	/**
	 * Sterge un fisier existent.
	 * 
	 * @param name
	 *            reprezinta numele fisierului ce urmeaza a fi sters.
	 * @lanseaza exceptia PhileNotFound daca fisierul nu poate fi sters intrucat
	 *           nu exista.
	 */
	public void delete(String name) throws FileNotFoundException;

	/**
	 * Deschide un fisier existent si cauta octetul 0. Metoda nu are nici-un
	 * efect daca fisierul este deja deschis.
	 * 
	 * @param name
	 *            reprezinta numele fisierului
	 * @param mode
	 *            reprezinta modul in care fisierul va fi deschis (0 pentru
	 *            Read-Only, 1 pentru Read-Write).
	 * @return un fisier deschis cu position = 0.
	 * @lanseaza exceptia PhileNotFound cand fisierul nu poate fi deschis
	 *           intrucat nu exista.
	 * @lanseaza DiskFullException daca nu mai este spatiu pe disk.
	 * @lanseaza TooManyOpenPhilesException cand este depasit numarul maxim
	 *           admis de fisiere simultan deschise (MAX_OPEN).
	 */
	public FileInterface open(String name, int mode)
			throws FileNotFoundException, TooManyOpenFilesException,
			DiskFullException;

	/**
	 * Inchide un fisier dat.
	 * 
	 * @param name
	 *            reprezinta numele fisierului ce urmeaza a fi inchis.
	 * @lanseaza exceptia PhileNotOpenException la incercarea de a inchide un
	 *           fisier ce nu a fost deschis
	 */
	public void close(FileInterface phile) throws FileNotOpenException;

	/**
	 * Sterge datele discului. Creaza un nou sistem de fisiere pe disc.
	 * 
	 * @param disk
	 *            reprezinta discul ce va fi formatat.
	 */
	public void format(DiskInterface disk);

	/**
	 * Incarca un sistem de fisiere dintr-un disk dat
	 * 
	 * @param disk
	 *            reprezinta discul ce urmeaza a fi incarcat
	 */
	public void startup(DiskInterface disk);

	/**
	 * Inchide sistemul de fisiere - inchide toate fisierele si elibereaza toate
	 * zonele de memorie alocate.
	 */
	public void shutdown();
}
