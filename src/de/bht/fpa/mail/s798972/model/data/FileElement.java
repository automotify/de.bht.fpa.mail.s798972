
package de.bht.fpa.mail.s798972.model.data;

import java.io.File;

/*
 * This is the leaf part of a composite pattern.
 *
 * @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class FileElement extends Component {

    public FileElement(File path) {
        super(path);
    }

    @Override
    public boolean isExpandable() {
        return false;
    }
    
}
