package org.example;

public abstract class CommandDecorator implements Command {
    protected Command command;

    public CommandDecorator(Command command) {
        this.command = command;
    }

    @Override
    public void execute() throws Exception {
        command.execute();
    }
}

