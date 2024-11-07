package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Scanner;

public class Main {
    public static final String XML_FILE = "src/main/resources/data.xml";
    public static Account currentAccount;
    private static int orderCounter = 1;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите логин: ");
            String login = scanner.next();
            System.out.print("Введите пароль: ");
            String password = scanner.next();

            currentAccount = authenticate(login, password);
            if (currentAccount != null) {
                System.out.println("Добро пожаловать, " + currentAccount.getRole() + "!");
                break;
            } else {
                System.out.println("Неверный логин или пароль. Попробуйте снова.");
            }
        }

        while (true) {
            // применение фабричного метода - порождающий паттерн
            UserActions userActions = UserActionsFactory.getUserActions(currentAccount.getRole());
            userActions.performAction(scanner);
        }
    }

    public static int getNextOrderId() {
        return orderCounter++;
    }

    private static Account authenticate(String login, String password) {
        try {
            Document doc = XMLUtils.loadXML(XML_FILE);
            NodeList users = doc.getElementsByTagName("account");

            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String xmlLogin = user.getAttribute("login");
                String xmlPassword = user.getAttribute("password");
                String role = user.getAttribute("role");
                boolean isBlocked = Boolean.parseBoolean(user.getAttribute("isBlocked"));

                if (xmlLogin.equals(login) && xmlPassword.equals(password)) {
                    if (isBlocked) {
                        System.out.println("Аккаунт заблокирован. Свяжитесь с администратором.");
                        return null;
                    }

                    String id = user.getAttribute("id");
                    return new Account(id, xmlLogin, xmlPassword, role, isBlocked);
                }
            }

            System.out.println("Неверный логин или пароль.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
