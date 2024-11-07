package org.example;

import java.util.Scanner;

public class AdministratorActions implements UserActions {
    @Override
    public void performAction(Scanner scanner) throws Exception {
        while (true) {
            System.out.println("1. Блокировать пользователя");
            System.out.println("2. Редактировать заказ");
            System.out.println("0. Выход");

            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            Command command = null;
            Command loggingCommand = null;
            switch (choice) {
                case 1:
                    command = new BlockUserCommand(scanner);
                    loggingCommand = new LoggingCommandDecorator(command);
                    loggingCommand.execute();
                    break;
                case 2:
                    command = new EditOrderCommand(scanner);
                    loggingCommand = new LoggingCommandDecorator(command);
                    loggingCommand.execute();
                    break;
                case 0:
                    System.out.println("Выход из меню администратора.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Некорректный выбор, попробуйте снова.");
                    continue;
            }

            if (command != null) {
                command.execute();
            }
        }
    }
}
