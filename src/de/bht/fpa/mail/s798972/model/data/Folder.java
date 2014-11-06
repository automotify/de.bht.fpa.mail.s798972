package de.bht.fpa.mail.s798972.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Folder extends Component {

    private final boolean expandable;
    private final ArrayList<Component> content;
    private final ArrayList<Email> emails;

    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<>();
        emails = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email message) {
        emails.add(message);
    }
 }
