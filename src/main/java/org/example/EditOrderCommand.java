package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Scanner;

public class EditOrderCommand implements Command {
    private final Scanner scanner;

    public EditOrderCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() throws Exception {
        Document doc = XMLUtils.loadXML(Main.XML_FILE);
        NodeList orders = doc.getElementsByTagName("order");

        System.out.print("Введите ID заказа для редактирования: ");
        String orderId = scanner.nextLine();

        boolean orderFound = false;
        for (int i = 0; i < orders.getLength(); i++) {
            Element order = (Element) orders.item(i);
            if (order.getAttribute("id").equals(orderId)) {
                orderFound = true;
                System.out.println("Редактирование заказа " + orderId + ". Текущие атрибуты:");
                System.out.println("Готов: " + order.getAttribute("isPrepared"));
                System.out.println("Готов к доставке: " + order.getAttribute("isReadyForDelivery"));
                System.out.println("Доставлен: " + order.getAttribute("isDeliveried"));

                System.out.print("Введите новый статус (isPrepared, isReadyForDelivery, isDeliveried): ");
                String attributeToEdit = scanner.nextLine();
                System.out.print("Введите новое значение (true/false): ");
                String newValue = scanner.nextLine();

                order.setAttribute(attributeToEdit, newValue);
                XMLUtils.saveXML(doc, Main.XML_FILE);
                System.out.println("Заказ " + orderId + " обновлен.");
                break;
            }
        }

        if (!orderFound) {
            System.out.println("Заказ с ID " + orderId + " не найден.");
        }
    }
}
