package de.bht.fpa.mail.s798972.model.applicationLogic;

/*
 * This is the interface for classes that manage
 * folders.
 */
import de.bht.fpa.mail.s798972.model.data.Folder;

public interface EmailManagerIF {

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    public Folder getTopFodler();

    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    public void loadContent(Folder f);

    /**
     * Loads all relevant emails in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the emails of the corresponding directory
     * should be loaded
     */
    public void loadEmails(Folder f);
    
    /**
     * Print folder content
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    public void printFolderContent(Folder f);
}
