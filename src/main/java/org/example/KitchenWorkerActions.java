package org.example;

import org.w3c.dom.*;

import java.util.Scanner;

public class KitchenWorkerActions implements UserActions {
    @Override
    public void performAction(Scanner scanner) throws Exception {
        while (true) {
            System.out.println("Введите '1' для выбора заказа на приготовление или '0' для завершения работы.");
            String choice = scanner.next();

            switch (choice) {
                case "0":
                    System.out.println("Завершение работы повара.");
                    System.exit(0);
                    break;

                case "1":
                    Document doc = XMLUtils.loadXML(Main.XML_FILE);
                    NodeList orders = doc.getElementsByTagName("order");
                    NodeList dishes = doc.getElementsByTagName("dish");

                    StringBuilder dishNames = new StringBuilder();
                    for (int j = 0; j < dishes.getLength(); j++) {
                        Element dish = (Element) dishes.item(j);
                        String dishId = dish.getAttribute("id");
                        String dishName = dish.getAttribute("name");
                        dishNames.append(dishId).append(": ").append(dishName).append("\n");
                    }

                    for (int i = 0; i < orders.getLength(); i++) {
                        Element order = (Element) orders.item(i);
                        if (!Boolean.parseBoolean(order.getAttribute("isPrepared"))) {
                            String orderId = order.getAttribute("id");
                            System.out.print("ID заказа: " + orderId + "\nБлюда: ");

                            NodeList orderDishes = order.getElementsByTagName("dish");
                            StringBuilder orderDishesList = new StringBuilder();
                            for (int k = 0; k < orderDishes.getLength(); k++) {
                                Element orderDish = (Element) orderDishes.item(k);
                                String dishId = orderDish.getAttribute("id");
                                for (int j = 0; j < dishes.getLength(); j++) {
                                    Element dish = (Element) dishes.item(j);
                                    if (dish.getAttribute("id").equals(dishId)) {
                                        orderDishesList.append(dish.getAttribute("name")).append(", ");
                                        break;
                                    }
                                }
                            }
                            if (orderDishesList.length() > 0) {
                                orderDishesList.setLength(orderDishesList.length() - 2);
                            }
                            System.out.println(orderDishesList.toString());
                        }
                    }

                    System.out.print("Введите ID заказа для приготовления: ");
                    String orderId = scanner.next();

                    for (int i = 0; i < orders.getLength(); i++) {
                        Element order = (Element) orders.item(i);
                        if (order.getAttribute("id").equals(orderId)) {
                            order.setAttribute("isPrepared", "true");
                            order.setAttribute("isReadyForDelivery", "true");
                            order.setAttribute("kitchenWorkerId", Main.currentAccount.getId()); // Установка ID работника кухни
                            XMLUtils.saveXML(doc, Main.XML_FILE);
                            System.out.println("Заказ " + orderId + " приготовлен и готов к доставке.");
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
