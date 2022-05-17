package server.commands;

import server.CollectionManager;
import utility.Answer;
import utility.AnswerType;
import utility.element.Worker;

/**
 * Общий класс для всех событий,
 * запускаемых командами
 */
public abstract class Action {
    protected CollectionManager manager;
    /**
     *Содержит количество слов в команде
     */
    protected int numOfWords;

    public Answer needElement(){
        return new Answer(AnswerType.NEED_ELEMENT,"");
    }

    public Answer notifyAboutResult(String message){
        return new Answer(AnswerType.RETURN_ACTION,message);
    }

    public Worker getElement(){
        return ActionInvoker.getInstance().getCursorCommand().getElement();
    }

    public String getParameter(){
        return ActionInvoker.getInstance().getCursorCommand().getParameter();
    }

    public abstract Answer execute();

    public void setManager(CollectionManager manager) {
        this.manager = manager;
    }


    public int getNumOfWords() {
        return numOfWords;
    }

}