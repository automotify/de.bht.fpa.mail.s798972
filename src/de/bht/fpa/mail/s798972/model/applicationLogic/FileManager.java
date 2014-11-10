package de.bht.fpa.mail.s798972.model.applicationLogic;

import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;

/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path
 */
public class FileManager implements FolderManagerIF {

    private final Folder topFolder; /* top Folder of the managed hierarchy */


    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public FileManager(File file) {
        topFolder = new Folder(file, true);
    }

    /**
     * Get root folder and return it
     *
     * @return Folder root item
     */
    @Override
    public Folder getTopFodler() {
        return topFolder;
    }

    /**
     * Loads all relevant content in the directory path of a folder object into
     * the folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadContent(Folder f) {
        File path = new File(f.getPath());

        /* check first if directory has sub files */
        if (hasSubFiles(path)) {
            f.getComponents().removeAll(f.getComponents()); /* remove old Components */

            /* Search for all directories in Folder f */
            for (File file : path.listFiles()) {
                if (file.getName().startsWith(".")) {
                    continue; /* ignore all hidden files */

                }
                /* add all directories to folder */
                if (file.isDirectory()) {
                    Folder subFolder = new Folder(file, hasSubFiles(file));
                    f.addComponent(subFolder);
                }
            }
        }
    }

    /**
     * check if File path is empty or not
     *
     * @param path File object to check
     * @return if directory have sub files or is empty
     */
    private Boolean hasSubFiles(File path) {
        /* search for directories in directory path */
        for (File f : path.listFiles()) {
            if (f.isDirectory()) {
                return true; /* return true if there is a sub directory */

            }
        }
        return false; /* otherwise return false */

    }
}
