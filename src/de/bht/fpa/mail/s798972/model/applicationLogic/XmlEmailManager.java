package de.bht.fpa.mail.s798972.model.applicationLogic;

import de.bht.fpa.mail.s798972.model.data.Email;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.util.List;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

/*
 * This is the interface for classes that manage
 * email functions.
 */
public class XmlEmailManager implements EmailManagerIF {

    /**
     * Loads all relevant email in the directory path of a folder into the
     * folder.
     *
     * @param folder the folder which should be checked for email
     * @return changed folder with all email added
     */
    @Override
    public Folder loadEmails(Folder folder) {
        File path = new File(folder.getPath());

        /* Loop through all files of directory and search for emails */
        for (File f : path.listFiles()) {
            /* handle emails, all of theme ends with .xml */
            if (f.isFile() && f.getName().endsWith(".xml")) {
                try {
                    folder.addEmail(JAXB.unmarshal(f, Email.class)); /* read XML object and add email object to folder */

                } catch (DataBindingException e) {
                    System.out.println("XML is not conform, error see below: " + e.getMessage());
                }
            }
        }
        return folder; /* return folder filled with found emails */

    }

    /**
     * Print folder content with all email
     *
     * @param folder the folder to get all of theme email component
     */
    @Override
    public void printFolderContent(Folder folder) {
        /* get all email contents in folder */
        List<Email> list = folder.getEmails();

        /* Print all emails */
        System.out.println("Selected directory: " + folder.getPath()); /* full path of directory */

        System.out.println("Number of emails: " + folder.getEmails().size()); /* number of emails */

        /* print all email elements */
        for (Email email : list) {
            System.out.printf("[Email: sender=%s received=%s subject=%s] %n", email.getSender(), email.getReceived(), email.getSubject());
        }
        System.out.println("End of check for emails.................");
    }

}
