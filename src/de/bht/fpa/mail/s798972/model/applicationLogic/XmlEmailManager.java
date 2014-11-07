package de.bht.fpa.mail.s798972.model.applicationLogic;

import de.bht.fpa.mail.s798972.model.data.Email;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.util.List;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

public class XmlEmailManager implements EmailManagerIF {

    //top Folder of the managed hierarchy
    private final Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public XmlEmailManager(File file) {
        topFolder = new Folder(file, true);
    }

    @Override
    public Folder getTopFodler() {
        return topFolder;
    }

    /**
     * Loads all relevant content in the directory path of a folder object into
     * the folder.
     *
     * @param folder the folder into which the content of the corresponding
     * directory should be loaded
     */
    @Override
    public void loadContent(Folder folder) {
        if (hasSubFolders(new File(folder.getPath()))) {
            folder.getComponents().removeAll(folder.getComponents());
            for (final File path : new File(folder.getPath()).listFiles()) {
                if (path.isDirectory()) {
                    final Folder subFolder = new Folder(path, hasSubFolders(path));
                    folder.addComponent(subFolder);
                }
            }
        }
    }

    private Boolean hasSubFolders(File path) {
        try {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(path.getName());
            System.out.println("Permission error by reading length of: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void loadEmails(Folder folder) {
        for (File f : new File(folder.getPath()).listFiles()) {
            if (f.isFile() && f.getName().endsWith(".xml")) {
                try {
                    folder.addEmail(JAXB.unmarshal(f, Email.class));
                } catch (DataBindingException e) {
                    System.out.println("XML is not conform, error see below: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void printFolderContent(Folder folder) {
        if (folder.getEmails().isEmpty()) {
            loadEmails(folder);
        }

        List<Email> list = folder.getEmails();

        //full path of directory
        System.out.println("Selected directory: " + folder.getPath());
        //number of emails
        System.out.println("Number of emails: " + folder.getEmails().size());

        //list emails
        for (Email email : list) {
            System.out.printf("[Email: sender=%s received=%s subject=%s] %n", email.getSender(), email.getReceived(), email.getSubject());
        }
        System.out.println("End of check for emails.................");
    }
}
