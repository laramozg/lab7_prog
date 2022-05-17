package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;
import utility.exceptions.IncorrectArgumentException;

public class RemoveKey extends BinaryAction{

    @Override
    public Answer execute() {
        checkKeyExist(getParameter());
        CollectionDatabase.getInstance().checkElementAccess(getParameter(),ActionInvoker.getInstance().getCurrentUser());
        CollectionDatabase.getInstance().removeByKey(getParameter());
        manager.removeKey(getParameter());
        return notifyAboutResult("Done");
    }
    private void checkKeyExist(String key) {
        if (!manager.getCollection().containsKey(key)) {
            throw new IncorrectArgumentException("Такого ключа не существует!");
        }
    }
}
