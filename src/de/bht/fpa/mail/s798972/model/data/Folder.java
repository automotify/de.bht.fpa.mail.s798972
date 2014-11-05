
package de.bht.fpa.mail.s798972.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Folder extends Component {

    private final ArrayList<Component> content;
    private final boolean expandable;
    
    public Folder(File path, boolean expandable) {
        super(path);
        content = new ArrayList<>();
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
