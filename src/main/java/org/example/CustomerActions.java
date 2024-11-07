package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Scanner;

public class CustomerActions implements UserActions {
    @Override
    public void performAction(Scanner scanner) throws Exception {
        Document doc = XMLUtils.loadXML(Main.XML_FILE);
        NodeList dishes = doc.getElementsByTagName("dish");

        while (true) {
            System.out.println("1. Создать новый заказ");
            System.out.println("2. Просмотреть свои заказы");
            System.out.println("0. Выход");

            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createOrder(scanner, doc, dishes);
                case 2 -> viewOrders();
                case 0 -> {
                    System.out.println("Выход из меню...");
                    System.exit(0);
                }
                default -> System.out.println("Некорректный выбор, попробуйте снова.");
            }
        }
    }

    public static void createOrder(Scanner scanner, Document doc, NodeList dishes) throws Exception {
        System.out.println("Доступные блюда:");
        for (int i = 0; i < dishes.getLength(); i++) {
            Element dish = (Element) dishes.item(i);
            String dishName = dish.getAttribute("name");
            String dishPrice = dish.getAttribute("price");

            if (!dishName.isEmpty() && !dishPrice.isEmpty()) {
                System.out.println("ID: " + dish.getAttribute("id") + ", Название: " + dishName + ", Цена: " + dishPrice);
            }
        }

        String orderId = String.valueOf(Main.getNextOrderId());
        Element newOrder = doc.createElement("order");
        newOrder.setAttribute("id", orderId);
        newOrder.setAttribute("customerId", Main.currentAccount.getId());
        newOrder.setAttribute("kitchenWorkerId", "");
        newOrder.setAttribute("courierId", "");
        newOrder.setAttribute("isPrepared", "false");
        newOrder.setAttribute("isReadyForDelivery", "false");
        newOrder.setAttribute("isDeliveried", "false");

        boolean hasDish = false;
        while (true) {
            System.out.print("Введите ID блюда для добавления в заказ (или '0' для завершения): ");
            String dishId = scanner.nextLine();
            if (dishId.equals("0")) break;

            boolean dishExists = false;
            for (int i = 0; i < dishes.getLength(); i++) {
                Element dish = (Element) dishes.item(i);
                if (dish.getAttribute("id").equals(dishId)) {
                    Element orderDish = doc.createElement("dish");
                    orderDish.setAttribute("id", dishId);
                    newOrder.appendChild(orderDish);
                    hasDish = true;
                    dishExists = true;
                    break;
                }
            }

            if (!dishExists) {
                System.out.println("Блюдо с таким ID не найдено. Попробуйте снова.");
            }
        }

        if (hasDish) {
            doc.getDocumentElement().getElementsByTagName("orders").item(0).appendChild(newOrder);
            XMLUtils.saveXML(doc, Main.XML_FILE);
            System.out.println("Заказ создан с ID: " + orderId);
        } else {
            System.out.println("Заказ не создан: не было выбрано ни одного блюда.");
        }
    }

    public static void viewOrders() throws Exception {
        Document doc = XMLUtils.loadXML(Main.XML_FILE);
        NodeList orders = doc.getElementsByTagName("order");

        System.out.println("Ваши заказы:");
        boolean hasOrders = false;
        for (int i = 0; i < orders.getLength(); i++) {
            Element order = (Element) orders.item(i);
            if (order.getAttribute("customerId").equals(Main.currentAccount.getId())) {
                hasOrders = true;
                System.out.println("ID заказа: " + order.getAttribute("id"));
                System.out.println("Доставлен: " + order.getAttribute("isDeliveried"));
                System.out.println("Готов к доставке: " + order.getAttribute("isReadyForDelivery"));
                System.out.println("Приготовлен: " + order.getAttribute("isPrepared"));
                System.out.println("Блюда в заказе:");

                NodeList orderDishes = order.getElementsByTagName("dish");
                for (int j = 0; j < orderDishes.getLength(); j++) {
                    Element dish = (Element) orderDishes.item(j);
                    System.out.println(" - Блюдо ID: " + dish.getAttribute("id"));
                }
                System.out.println();
            }
        }
        if (!hasOrders) {
            System.out.println("У вас нет заказов.");
        }
    }
}
