package de.bht.fpa.mail.s798972.model.applicationLogic;

import de.bht.fpa.mail.s798972.model.data.Folder;

/*
 * This is the interface for classes that manage
 * email functions.
 */
public interface EmailManagerIF {

    /**
     * Loads all relevant email in the directory path of a folder into the
     * folder.
     *
     * @param f the folder which should be checked for email
     * @return changed folder with all email added
     */
    public Folder loadEmails(Folder f);

    /**
     * Print folder content with all email
     *
     * @param f the folder to get all of theme email component
     */
    public void printFolderContent(Folder f);
}
