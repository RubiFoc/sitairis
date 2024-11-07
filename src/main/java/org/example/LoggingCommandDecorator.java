package org.example;

public class LoggingCommandDecorator extends CommandDecorator {
    public LoggingCommandDecorator(Command command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Начало выполнения команды: " + command.getClass().getSimpleName());
        command.execute();
        System.out.println("Команда выполнена: " + command.getClass().getSimpleName());
    }
}
