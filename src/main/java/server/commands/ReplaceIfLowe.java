package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;
import utility.element.Worker;
import utility.exceptions.IncorrectArgumentException;

/**
 * Метод заменяет значение по ключу, если новое значение меньше старого
 */
public class ReplaceIfLowe extends BinaryAction{
    @Override
    public Answer execute() {
        checkKeyExist(getParameter());
        Worker element = getElement();
        CollectionDatabase.getInstance().checkElementAccess(getParameter(), ActionInvoker.getInstance().getCurrentUser());
        if (element == null) {
            return needElement();
        }
        CollectionDatabase.getInstance().replaceIfLowe(getParameter(),element);
        manager.replaceIfLowe(getParameter(),element);
        return notifyAboutResult("Done");
    }
    private void checkKeyExist(String key) {
        if (!manager.getCollection().containsKey(key)) {
            throw new IncorrectArgumentException("Такого ключа не существует!");
        }
    }
}
