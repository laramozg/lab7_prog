package server.commands;

import utility.Answer;

/**
 * Класс события вывода всех команд
 */
public class Help extends UnaryAction {


    @Override
    public Answer execute() {
        return notifyAboutResult(manager.help());
    }
}