package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;
import utility.element.Worker;
import utility.exceptions.IncorrectArgumentException;

public class Update extends BinaryAction {

    @Override
    public Answer execute() {
        int id = getId();
        checkIdExist(id);
        Worker element = getElement();
        CollectionDatabase.getInstance().checkElementAccess(id, ActionInvoker.getInstance().getCurrentUser());
        if (element == null) {
            return needElement();
        }
        CollectionDatabase.getInstance().updateElement(id, element);
        manager.update(id, element);
        return notifyAboutResult("Done");
    }

    private void checkIdExist(int id) {
        if (manager.getKeyById(id) == null) {
            throw new IncorrectArgumentException("Такого id не существует!");
        }
    }

    private int getId() {
        return Integer.parseInt(getParameter());
    }
}
