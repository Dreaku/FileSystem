package ro.dreaku.filesystem;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import ro.dreaku.filesystem.disk.Disk;
import ro.dreaku.filesystem.disk.DiskInterface;
import ro.dreaku.filesystem.exception.BadBlockException;
import ro.dreaku.filesystem.exception.DiskFullException;
import ro.dreaku.filesystem.exception.FileAlreadyExistsException;
import ro.dreaku.filesystem.exception.FileNotFoundException;
import ro.dreaku.filesystem.exception.FileNotOpenException;
import ro.dreaku.filesystem.exception.TooManyOpenFilesException;
import ro.dreaku.filesystem.file.FileInterface;

public class Console extends Frame implements FileSystem
{
    private static final long serialVersionUID = 1L;

    private static final String WINDOW_TITLE = "FileSystem";
    private static final String CONSOLE_STRING = ">";
    private static final String COPYRIGHT_STRING = "Copyright \u00a9 2015 Dreaku.\nAll rights reserved.";

    static final int ROOT_ENTRY_LENGTH = 32; // [bytes]
    static final byte DESCRIPTOR = (byte) 0; // usual: F6 HEX

    private TextArea textArea;

    private Disk disk;
    private String fileName;

    public static void main(String[] args)
    {
        Console c = new Console();
        // c.newDisk(10);
        c.newDisk(10);
        for (int i = 0; i < 10; i++)
        {
            c.disk.disk[i] = new byte[Disk.BLOCK_SIZE];
        }
        c.disk.disk[0][0] = 97;
        c.disk.disk[2][6] = 98;
        c.disk.disk[3][7] = 99;
        c.shutdown();

        try
        {
            c.load();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (c.disk.disk[0][0] == 97)
        {
            System.out.println("V");
        }
        if (c.disk.disk[2][6] == 98)
        {
            System.out.println("V");
        }
        if (c.disk.disk[3][7] == 99)
        {
            System.out.println("V");
        }
        // c.help();
        c.copyright();
        c.printConsole();

    }

    Console()
    {
        super(WINDOW_TITLE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        textArea = new TextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("Courier New", Font.BOLD, 12));
        add(textArea);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    // Message shown after launching the simulator
    void copyright()
    {
        textArea.append(COPYRIGHT_STRING + "\n\n");
    }

    void printConsole()
    {
        textArea.append(CONSOLE_STRING);
    }

    /*
     * shell este metoda ce asigura interactivitatea programului; trebuie sa implementeze comenzile:
     * new (creaza un nou sistem) load (incarca un sistem existent) format (formateaza diskul) save
     * (salveaza diskul pe HDD) shutdown (inchide sistemul) mkdir (creaza un director) mkfile
     * (creaza si deschide un fisier) copy (copiaza un fisier/director la o cale indicata) delete
     * (sterge un fisier sau director) move (muta un fisier/director la o cale indicata) rename
     * (schimba numele unui fisier/director) cd / chdir (schimba directorul curent) up (un director
     * mai sus) ls (listeaza continutul directorului curent; optiunea -a afiseaza si
     * fisierele/directoarele ascunse, optiunea -l afiseaza in format lung ([atribute nume <DIR>
     * dimensiune data timpul], <DIR> apare numai la directoare, data si timpul se refera la
     * momentul la care a fost creat sau modificat(dupa prima modificare)), -al (format lung in care
     * sunt afisate si fisierele/directoarele ascunse)) attr (afiseaza si permite setarea
     * atributelor fisierelor/directoarelor: Read-Only, Hidden, Encrypted) encrypt (cripteaza un
     * fisier) decrypt (decripteaza un fisier criptat) open (deschide un fisier) edit (editeaza un
     * fisier deschis in modul Read-Write) view (afiseaza continutul unui fisier deschis in modul
     * Read-Only) close x (inchide fisierul cu calea x) close (inchide toate fisierele deschise) df
     * (afiseaza dimensiunea spatiului liber) date (afiseaza data sistemului) time (afiseaza timpul
     * sistemului) info (afiseaza informatii despre disk) help / h (afiseaza lista tuturor
     * comenzilor cu o scurta descriere pt fiecare) label (afiseaza eticheta discului) chlabel
     * (schimba eticheta discului) clean (curata toate datele din blocurile care in FAT sunt
     * indicate ca fiind sterse) exec (lanseaza in executie un fisier de pe HDD) save x (salveaza
     * fisierul cu numele (sau calea) x pe HDD) lf (incarca un fisier de pe HDD in diskul virtual)
     */
    void shell()
    {
    }

    // ====================COMMANDS========================//]

    // Create a new disk and a file system on it
    void newDisk(int size)
    {
        disk = new Disk(size);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            fileName = fileToSave.getAbsolutePath();
        }
    }

    // Load an existing disk from HDD; a FileDialog object must be used
    void load() throws IOException
    {
        FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
        FileInputStream fileInputStream = null;
        fd.setVisible(true);
        fileName = fd.getDirectory() + fd.getFile();
        File file = new File(fileName);
        byte[] byteArray = new byte[(int) file.length()];

        try
        {
            fileInputStream = new FileInputStream(file.getPath());
            fileInputStream.read(byteArray);
            fileInputStream.close();
            int size = byteArray.length / Disk.BLOCK_SIZE;
            disk = new Disk(size);
            for (int i = 0; i < size; i++)
            {
                disk.disk[i] = new byte[Disk.BLOCK_SIZE];
                for (int j = 0; j < Disk.BLOCK_SIZE - 1; j++)
                {
                    disk.disk[i][j] = byteArray[i * Disk.BLOCK_SIZE + j];
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    // Format a disk
    @Override
    public void format(DiskInterface disk)
    {

    }

    // Close file system
    @Override
    public void shutdown()
    {
        try
        {
            if (disk != null)
            {
                FileOutputStream fos = new FileOutputStream(fileName);
                for (int i = 0; i < disk.getSize(); i++)
                {
                    fos.write(disk.disk[i]);
                }
                fos.close();
            }
        } catch (Exception e)
        {

        }

    }

    /*
     * salveaza diskul pe HDD (primii 4 octeti vor contine semnatura de disk (la alegere) (aceasta
     * semnatura trebuie verifica la incarcare), urmatorii 2 octeti vor contine data salvarii,
     * urmatorii 2 octeti vor contine timpul salvarii, urmatorii 4 octeti vor contine numarul de
     * intrari din ROOT folosite, urmatorii 4 octeti vor contine dimensiunea diskului (numarul de
     * blocuri))
     */
    void save()
    {
    }

    // Create and open the file with specified name
    void mkfile(String name)
    {
    }

    // Copy from file path to specified path
    void copy(String frompath, String topath) throws FileNotFoundException
    {
    }

    // Open the file in mode 'mode'
    void openFile(String name, int mode)
    {
    }

    // deschide fereastra editorului pentru fisierul openfilePath
    void edit(String openfilePath) throws FileNotOpenException
    {
    }

    // deschide fereastra editorului pentru fisierul openfilePath, in modul
    // view (poate fi doar citit)
    void view(String openfilePath) throws FileNotOpenException
    {
    }

    // encripteaza/decripteaza fisierul necriptat/criptat cu intrarea in ROOT
    // entry
    boolean encrypt_decrypt(Point entry)
    {
        return false;
    }

    // lanseaza in executie un fisier de pe HDD, selectat prin intermediul unui
    // FileDialog
    void exec()
    {
    }

    // Close the file with specified path
    void close(String path)
    {
    }

    // Close all open file
    void closeAll()
    {
    }

    // sterge un fisier/director cu numele name
    @Override
    public void delete(String name) throws FileNotFoundException
    {
    }

    // sterge continutul tuturor blocurilor din zona de date, care in FAT
    // figureaza ca fiind sterse
    void clean()
    {
    }

    // returneaza marimea spatiului liber de pe disk
    void df()
    {
    }

    // schimba directorul curent
    void chdir(String path)
    {
    }

    // listeaza continutul directorului curent (suporta 3 optiuni: -a, -l, -al)
    void ls(String param)
    {
    }

    // afiseaza/seteaza atributele unui fisier/director
    void attr(String name)
    {
    }

    // afiseaza lista comenzilor
    void help()
    {
        String help = "new <size> <filename>(create a new system)\nload (load an existing system)\n"
                + "format (format the disk)\nsave (save the disk on HDD)\n"
                + "shutdown (close the system)\nmkdir (create a new directory)\n"
                + "mkfile (create and open a file)\ncopy (copy a file/directory"
                + " to a specified path)\ndelete (delete a file or directory)\n"
                + "move (move a file/directory to a specified path)\n"
                + "rename (change the name of a file/directory)\ncd / chdir"
                + " (change current directory)\nup (move to an upper directory)\n"
                + "ls (display the content of current directory; option -a display hidden \n"
                + "files/directories, option -l display in long format ([attribute name <DIR>\n"
                + "dimension date time], <DIR> is only for directories, date and time refers to\n"
                + "the moment when directory was created or modified(after first modification)),\n"
                + "-al (long format, display hidden files/directories))\n"
                + "attr (display and allow to set files/directories attributes:"
                + " Read-Only, Hidden, Ecnrypted)\nencrypt (encrypt a file)\n"
                + "decrypt (decrypt an encrypted file)\nopen(open a file)\n"
                + "edit (edit a file that is opened in Read-Write mode)\n"
                + "view(display the contains of a file opened in Read-Only mode)\n"
                + "close x(close the file with path x)\nclose(close all opened files)\n"
                + "df(display the dimension of free space)\ndate(display system date)\n"
                + "time (display sistem time)\ninfo(display informations about disk)\n"
                + "help / h (display the list of all posible commands with a short description for each command)\n"
                + "label (display disk label)\nchlabel (change disk label)\n"
                + "clean (clean all data from bloks that are deleted in FAT)\n"
                + "exec (lounch in execution a file from HDD)\nsave x (save the file with name or path x on HDD)\n"
                + "lf (load a file from HDD on virtual disk)\n";
        System.out.println(help);
        textArea.append(help);
    }

    /*
     * afiseaza informatii despre disk (Disk label, Bytes per Sector, Sectors per Block [Cluter],
     * Number of Blocks, Fat type, Media Descriptor Byte, FAT Start Block, FAT Blocks Occupied, Root
     * Dir Start Block, Root Dir Blocks Occupied, Data Start Block, Data Blocks Occupied, Max Root
     * Entries Count, Max DISK Entries, DISK Size, Used Bytes Count, Free Bytes Count si Free
     * Entries Count)
     */
    void info()
    {
    }

    // scrie fisierul name de pe DISK pe HDD
    void saveFileOnHDD(String name)
    {
    }

    // incarca un fisier de pe HDD in diskul virtual (foloseste un FileDialog)
    void loadFilefromHDD() throws DiskFullException
    {
    }

    // ================TEST FUNCTIONS===================//

    // returneaza true daca fisierul/directorul cu intrarea entry este Read-Only
    // si false in caz contrar
    boolean isReadOnly(Point entry)
    {
        return false;
    }

    // returneaza true daca fisierul/directorul cu intrarea entry este Hidden si
    // false in caz contrar
    boolean isHidden(Point entry)
    {
        return false;
    }

    // returneaza true daca entry reprezinta un director si false in caz contrar
    boolean isDirectory(Point entry)
    {
        return false;
    }

    // returneaza true daca fisierul cu intrarea entry este criptat si false in
    // caz contrar
    boolean isEncrypt(Point entry)
    {
        return false;
    }

    // returneaza true daca entry reprezinta un fisier/director care a fost
    // sters si false in caz contrar
    boolean isDelete(Point entry)
    {
        return false;
    }

    // returneaza true daca entry reprezinta un fisier/director care a fost
    // modificat si false in caz contrar
    boolean isModified(Point entry)
    {
        return false;
    }

    // returneaza intrarea cu numele name din directorul DirEntry (daca nu
    // exista returneaza null)
    Point exists(Point DirEntry, String name)
    {
        return DirEntry;
    }

    // returneaza intrarea cu numele name din directorul directorul curent (daca
    // nu exista returneaza null)
    Point exists(String path)
    {
        return null;
    }

    // returneaza true daca name reprezinta un director si false in caz contrar
    boolean isDirectory(String name)
    {
        return false;
    }

    // returneaza true daca path reprezinta un fisier deschis si false in caz
    // contrar
    int isOpen(String path)
    {
        return 0;
    }

    // listeaza caile fisierelor deschise
    void listOpenFiles()
    {
    }

    // returneaza true daca buffer reprezinta EOF si false in caz contrar
    // param buffer trebuie sa fie o matrice cu macar 4 elemente. Metoda
    // testeaza daca primele 4 elemente coincid cu -1 si in
    // acest caz returneaza true; in caz contrar returneaza false.
    boolean isEOF(byte[] buffer) throws ArrayIndexOutOfBoundsException
    {
        return false;
    }

    // ========================SET FUNCTIONS===========================//

    // stabileste zonele diskului: BOOT, FAT, ROOT, DATA (la dimensiuni mai mari
    // de 64K, DATA poate fi 95% din DISK)
    void set_FAT_ROOT_Sizes(DiskInterface disk)
    {
    }

    // seteaza directorul curent
    void setDir()
    {
    }

    // seteaza octetul de stare a fisierului/directorului cu intrarea entry la
    // valoarea b
    void setStatus(Point entry, byte b)
    {
    }

    // seteaza numele fisierului/directorului cu intrarea entry la name
    void setName(Point entry, String name)
    {
    }

    // seteaza dimensiunea fisierului cu intrarea entry la size
    void setSize(Point entry, int size)
    {
    }

    // seteaza data fisierului/directorului cu intrarea entry
    void setDate(Point entry)
    {
    }

    // seteaza timpul fisierului/directorului cu intrarea entry
    void setTime(Point entry)
    {
    }

    // seteaza indexul blocului de start al fisierului/directorului cu intrarea
    // entry la index
    void setIndexStartBlock(Point entry, int index)
    {
    }

    // seteaza atributele fisierului/directorului cu intrarea entry
    void setAttributes(Point entry, boolean readonly, boolean hidden, boolean directory, boolean encrypt)
    {
    }

    // seteaza atributele fisierului/directorului cu intrarea entry la b
    void setAttributes(Point entry, int b)
    {
    }

    // seteaza directorul parinte al fisierului/directorului cu intrarea entry
    void setDirectory(Point entry, Point DirEntry)
    {
    }

    // ========================GET FUNCTIONS=====================//

    // returneaza indexul blocului care are intrarea (in FAT) FAT_Entry
    int getBlockIndex(Point FAT_Entry)
    {
        return 0;
    }

    // returneaza intrarea din FAT corespunzatoare indexului indexBlock
    Point getFATEntry(int indexBlock)
    {
        return null;
    }

    // returneaza indexul intrarii ROOT_Entry
    int getIndexROOTEntry(Point ROOT_Entry)
    {
        return 0;
    }

    // returneaza intrarea din ROOT cu indexul index
    Point getROOTEntry(int index)
    {
        return null;
    }

    // returneaza indexul continut intr-o intrare din FAT
    int getFATEntryContent(Point FAT_Entry)
    {
        return 0;
    }

    // returneaza octetul de stare al intrarii entry
    byte getStatus(Point entry)
    {
        return 0;
    }

    // returneaza numele continut in intrarea entry
    String getName(Point entry)
    {
        return null;
    }

    // returneaza intrarea corespunzatoare lui path (in cazul in care nu exista,
    // returneaza null)
    Point getEntry(String path)
    {
        return null;
    }

    // returneaza dimensiunea continuta in intrarea entry
    String getSize(Point entry)
    {
        return null;
    }

    // returneaza data continuta in intrarea entry
    String getDate(Point entry)
    {
        return null;
    }

    // returneaza data din numarul q
    String getDate(int q)
    {
        return null;
    }

    // returneaza timpul continut in intrarea entry
    String getTime(Point entry)
    {
        return null;
    }

    // returneaza timpul din numarul q
    String getTime(int q)
    {
        return null;
    }

    // returneaza indexul blocului de start al intrarii entry
    int getStartBlockIndex(Point entry)
    {
        return 0;
    }

    // returneaza octetul atributelor al intrarii entry
    byte getAttributes(Point entry)
    {
        return 0;
    }

    // returneaza directorul parinte al intrarii entry
    Point getDirectory(Point entry)
    {
        return entry;
    }

    // returneaza calea corespunzatoare intrarii entry
    String getPath(Point entry)
    {
        return null;
    }

    // returneaza calea corespunzatoare lui name din directorul DirEntry
    String getPath(Point DirEntry, String name)
    {
        return name;
    }

    // returneaza sfarsitul(EOF) intrarii entry
    Point getEOF(Point entry)
    {
        return entry;
    }

    // returneza numarul de fisiere deschise
    int getOpenFilesCount()
    {
        return 0;
    }

    // returneaza numarul de intrari libere din ROOT
    int getFreeEntriesCount()
    {
        return 0;
    }

    // returneaza intrarea din ROOT corespunzatoare pozitiei entry, din zona de
    // date
    Point getROOTEntryOfDirEntry(Point entry)
    {
        return entry;
    }

    // returneaza pozitiile intrarilor continute in directorul DirEntry
    Point[] getDirEntries(Point DirEntry)
    {
        return null;
    }

    // returneaza intrarile din ROOT continute in directorul DirEntry
    Point[] getDirROOTEntries(Point DirEntry)
    {
        return null;
    }

    // returneaza intrarile din ROOT continute in directorul curent
    Point[] getDirROOTEntries()
    {
        return null;
    }

    // returneaza numarul de intrari continute in directorul DirEntry
    int getDirEntriesCount(Point DirEntry)
    {
        return 0;
    }

    // returneaza numarul de intrari continute in directorul curent
    int getDirEntriesCount()
    {
        return 0;
    }

    // returneaza intrarile corespunzatoare directoarelor din directorul
    // DirEntry
    Point[] getDirectories(Point DirEntry)
    {
        return null;
    }

    // returneaza intrarile corespunzatoare fisierelor din directorul DirEntry
    Point[] getFiles(Point DirEntry)
    {
        return null;
    }

    // returneaza intrarile corespunzatoare directoarelor din directorul curent
    Point[] getDirectories()
    {
        return null;
    }

    // returneaza intrarile corespunzatoare fisierelor din directorul curent
    Point[] getFiles()
    {
        return null;
    }

    // =======================TOOLS1=========================//

    // folosita in load; face setarile initiale ale unui disk
    @Override
    public void startup(DiskInterface disk)
    {
    }

    // descrisa in interfata FileSystem
    @Override
    public FileInterface create(String name)
            throws FileAlreadyExistsException, TooManyOpenFilesException, DiskFullException
    {
        return null;
    }

    // descrisa in interfata FileSystem
    @Override
    public FileInterface open(String name, int mode)
            throws FileNotFoundException, TooManyOpenFilesException, DiskFullException
    {
        return null;
    }

    // descrisa in interfata FileSystem
    @Override
    public void close(FileInterface file) throws FileNotOpenException
    {
    }

    // cauta si returneaza prima intrare libera din FAT
    Point search_first_empty_FATentry(Point start) throws BadBlockException, DiskFullException
    {
        return start;
    }

    // scrie in intrarea FAT_Entry numarul nextIndexBlock
    void writeFATEntry(Point FAT_Entry, int nextIndexBlock)
    {
    }

    // Scrie in zona de date a directorului DirEntry numarul data, reprezentand
    // indexul unei intrari introduse in acest director;
    // data va fi scrisa pe pozitia entry sau, daca entry==null, va fi scrisa pe
    // prima pozitie libera din DirEntry
    void write_EntryIndex_in_DirectoryData(Point DirEntry, int data, Point entry)
    {
    }

    // adauga un nou block, relativ la fat_entry (folosita cand se scrie in
    // fisiere)
    int addBlock(Point fat_entry)
    {
        return 0;
    }

    // adauga o noua intrare in ROOT cu numele name; tip poate lua valorile
    // "directory" si "file"
    // intrarea va fi in directorul curent si va avea ca intrare FAT, prima
    // intrare FAT libera
    void new_root_entry(String name, String tip)
    {
    }

    // adauga o noua intrare in ROOT cu numele name; tip poate lua valorile
    // "directory" si "file"
    // intrarea va fi in directorul DirEntry si va avea ca intrare FAT, prima
    // intrare FAT libera
    void new_root_entry(Point DirEntry, String name, String tip)
    {
    }

    // adauga o noua intrare in ROOT cu numele name; tip poate lua valorile
    // "directory" si "file"
    // intrarea va fi in directorul DirEntry si va avea ca intrare FAT intrarea
    // FAT_Entry
    void new_root_entry(Point FAT_Entry, Point DirEntry, String name, String tip) throws DiskFullException
    {
    }

    // sterge din ROOT intrarea entry
    void delete_root_entry(Point entry) throws BadBlockException
    {
    }

    // sterge directorul entry cu toate fisierele si subdirectoarele sale
    void deleteDirectory(Point entry)
    {
    }

    // sterge intrarea entry, corespunzatoare unui fisier
    void deleteEntry(Point entry)
    {
    }

    // adauga newentry la coada tree, reprezentand calea spre directorul curent
    void appendTree(Point newentry)
    {
    }

    // sterge ultimul element din coada tree
    void delete_last_TreeEntry()
    {
    }

    // returneaza un nume valid de fisier sau director(alfanumeric, cu lungimea
    // <=14 si care nu a mai fost dat si altui
    // fisier/director din directorul curent)
    String name(String s)
    {
        return s;
    }

    // =======================TOOLS2=========================//

    // testeaza daca sirul s este alfanumeric
    boolean testalfanumerical(String s)
    {
        return false;
    }

    // cripteaza/decripteaza sirul s
    String encrypt_decrypt(String s)
    {
        return s;
    }

    // cripteaza/decripteaza matricea de caractere ac
    byte[] encrypt_decrypt(char[] ac)
    {
        return null;
    }

    // cripteaza/decripteaza matricea de octeti buffer
    byte[] encrypt_decrypt(byte[] buffer)
    {
        return buffer;
    }

    // transforma matricea de caractere chars intr-o matrice de octeti
    byte[] charsToBytes(char[] chars)
    {
        return null;
    }

    // transforma matricea de octeti chars intr-o matrice de octeti bytes,
    // intr-o matrice de caractere
    char[] bytesToChars(byte[] bytes)
    {
        return null;
    }

    // buffer.length=4; transforma buffer intr-un intreg fara semn
    int readUnsignedInt(byte[] buffer) throws ArrayIndexOutOfBoundsException
    {
        return 0;
    }

    // buffer.length=2; transforma buffer intr-un intreg fara semn
    int readUnsignedShort(byte[] buffer) throws ArrayIndexOutOfBoundsException
    {
        return 0;
    }

    // returneaza octetii unui numar intreg de tipul short
    byte[] getBytes(char c)
    {
        return null;
    }

    // returneaza un caracter din doi octeti
    char getChar(byte[] b)
    {
        return 0;
    }

    // returneaza octetii unui numar intreg
    byte[] getBytes(int i)
    {
        return null;
    }

    // scrie numarul i cu virgule (ex. 100,000,000)
    String write_with_commas(int i)
    {
        return null;
    }
}
