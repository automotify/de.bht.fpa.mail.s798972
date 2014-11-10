
package de.bht.fpa.mail.s798972.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the leaf node part of a composite pattern.
 *
 * @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class Folder extends Component {

    private ArrayList<Component> content;
    private boolean expandable;
    
    public Folder(File path, boolean expandable) {
        super(path);
        content = new ArrayList<Component>();
	this.expandable = expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }
    
}
