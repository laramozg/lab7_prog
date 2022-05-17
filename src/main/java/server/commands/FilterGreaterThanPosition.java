package server.commands;

import utility.Answer;
import utility.element.Position;
/**
 * Выводит элементы, значение поля position которого больше заданного
 **/
public class FilterGreaterThanPosition extends BinaryAction{

    @Override
    public Answer execute(){
        Position position = Position.valueOf(getParameter());
        return notifyAboutResult(manager.filterGreaterThanPosition(position));
    }
}
