package server.commands;

import utility.Answer;

/**
 * Класс события вывода коллекции
 */
public class Show extends UnaryAction {

    @Override
    public Answer execute() {
        return notifyAboutResult(manager.show());
    }
}