/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path
 */
package de.bht.fpa.mail.s798972.model.applicationLogic;

import de.bht.fpa.mail.s798972.model.data.FileElement;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;

public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    private final Folder topFolder;

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
        if (hasSubFiles(new File(f.getPath()))) {
            f.getComponents().removeAll(f.getComponents());
            for (final File path : new File(f.getPath()).listFiles()) {
                if (path.getName().startsWith(".")) {
                    continue; // keine versteckten Files anzeigen
                }
                if (path.isDirectory()) {
                    final Folder subFolder = new Folder(path, hasSubFiles(path));
                    f.addComponent(subFolder);
                } else {
                    final FileElement subFile = new FileElement(path);
                    f.addComponent(subFile);
                }
            }
        }
    }
    
    private Boolean hasSubFiles(File path) {
        try {
            return path.list().length > 0;
        } catch (Exception e) {
            System.out.println(path.getName());
            System.out.println("Error by reading length of: " + e.getMessage());
        }
        return false;
    }
}
