package server.commands;


import utility.Answer;

public class PrintFieldDescendingStartDate extends UnaryAction{

    @Override
    public Answer execute() {
        return notifyAboutResult(manager.printFieldDescendingStartDate());
    }
}

