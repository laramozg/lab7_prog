package server.commands;

import utility.Answer;

/**
 * Класс события вывода информации о коллекции
 */
public class Info extends UnaryAction {

    @Override
    public Answer execute() {
        return notifyAboutResult(manager.info());
    }
}