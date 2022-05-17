
package server.commands;

import utility.Answer;
import utility.AnswerType;

/**
 * Класс события выхода из программы
 */
public class Exit extends UnaryAction {


    @Override
    public Answer execute() {
        return new Answer(AnswerType.EXIT,"");
    }
}