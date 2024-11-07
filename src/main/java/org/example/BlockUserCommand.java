package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Scanner;

public class BlockUserCommand implements Command {
    private final Scanner scanner;

    public BlockUserCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() throws Exception {
        Document doc = XMLUtils.loadXML(Main.XML_FILE);
        NodeList users = doc.getElementsByTagName("account");

        System.out.print("Введите логин пользователя для блокировки: ");
        String loginToBlock = scanner.nextLine();

        boolean userFound = false;
        for (int i = 0; i < users.getLength(); i++) {
            Element user = (Element) users.item(i);
            String xmlLogin = user.getAttribute("login");

            if (xmlLogin.equals(loginToBlock)) {
                user.setAttribute("isBlocked", "true");
                XMLUtils.saveXML(doc, Main.XML_FILE);
                System.out.println("Пользователь " + loginToBlock + " заблокирован.");
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("Пользователь с логином " + loginToBlock + " не найден.");
        }
    }
}
