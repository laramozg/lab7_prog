package server.commands;

import server.database.CollectionDatabase;
import utility.Answer;
import utility.element.Worker;

/**
 * Удалить элементы большие введённого элемента
 */
public class RemoveGreater extends UnaryAction{

    @Override
    public Answer execute() {
        Worker element = getElement();
        if (element == null) {
            return needElement();
        }
        CollectionDatabase.getInstance().removeGreater(element.getOrganization().getEmployeesCount(),ActionInvoker.getInstance().getCurrentUser());
        manager.removeGreater(element);
        return notifyAboutResult("Done");
    }
}
