package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;
import utility.element.Worker;

import java.sql.SQLException;

/**
 * Класс события добавления
 */
public class Insert extends BinaryAction {
    @Override
    public Answer execute() {
        Worker element = getElement();
        if (element == null) {
            return needElement();
        }
        try {
            element= CollectionDatabase.getInstance().insertElement(getParameter(),element);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        manager.insert(getParameter(),element);
        return notifyAboutResult("Done");
    }
}