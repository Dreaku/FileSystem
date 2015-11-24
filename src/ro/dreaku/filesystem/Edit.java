package ro.dreaku.filesystem;

import java.awt.Event;
import java.awt.Frame;
import java.awt.Point;
import java.awt.TextArea;

import ro.dreaku.filesystem.file.File;

//==============================================================================//
//fereastra de editare a fisierelor

class Edit extends Frame
{

    private static final long serialVersionUID = 1L;

    TextArea text;
    Console console;
    Point fileEntry;
    File file;
    String name;
    int index;
    int mode;

    // constructor
    Edit(Console console, int index, int mode)
    {
    }

    // adauga bara cu meniuri (aceasta va contine meniul File cu submeniurile
    // Save si Exit pt modul edit si numai
    // meniul Exit, pt modul View)
    void addMenuBar()
    {
    }

    @Override
    public boolean handleEvent(Event e)
    {
        return false;
    }

    // scrie continutul fisierului in TextArea
    void load()
    {
    }

    // salveaza modificarile facute (sterge intreg continutul, seteaza
    // position=0, scrie noul continut)
    void save()
    {
    }

    // folosita in save; scrie fisierul prin intermediul lui file
    void write()
    {
    }

    // folosita in save; sterge continutul fisierului inaintea salvarii
    void delete(Point EOF)
    {
    }

    // inchide fereastra editorului (fara a se iesi din program)
    void close()
    {
    }
}