package org.example;

import org.w3c.dom.*;

import java.util.Scanner;

public class CourierActions implements UserActions {
    @Override
    public void performAction(Scanner scanner) throws Exception {
        while (true) {
            System.out.println("Введите '1' для выбора заказа на доставку или '0' для завершения работы.");
            String choice = scanner.next();

            switch (choice) {
                case "0":
                    System.out.println("Завершение работы курьера!");
                    System.exit(0);
                    break;

                case "1":
                    Document doc = XMLUtils.loadXML(Main.XML_FILE);
                    NodeList orders = doc.getElementsByTagName("order");

                    for (int i = 0; i < orders.getLength(); i++) {
                        Element order = (Element) orders.item(i);
                        if (Boolean.parseBoolean(order.getAttribute("isReadyForDelivery")) && !Boolean.parseBoolean(order.getAttribute("isDeliveried"))) {
                            System.out.println("ID заказа: " + order.getAttribute("id"));
                        }
                    }

                    System.out.print("Введите ID заказа для доставки: ");
                    String orderId = scanner.next();
                    for (int i = 0; i < orders.getLength(); i++) {
                        Element order = (Element) orders.item(i);
                        if (order.getAttribute("id").equals(orderId)) {
                            order.setAttribute("isDeliveried", "true");
                            order.setAttribute("courierId", Main.currentAccount.getId()); // Установка ID курьера
                            XMLUtils.saveXML(doc, Main.XML_FILE);
                            System.out.println("Заказ " + orderId + " доставлен.");
                        }
                    }
                    break;

                default:
                    System.out.println("Некорректный выбор. Пожалуйста, попробуйте снова.");
                    break;
            }
        }
    }
}
