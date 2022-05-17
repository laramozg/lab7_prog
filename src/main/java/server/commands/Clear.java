package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;

/**
 * Класс события очистки коллекции
 */
public class Clear extends UnaryAction {

    @Override
    public Answer execute() {
        CollectionDatabase.getInstance().clear(ActionInvoker.getInstance().getCurrentUser());
        manager.clear();
        return notifyAboutResult("Done");
    }
}