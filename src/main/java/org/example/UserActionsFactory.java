package org.example;

public class UserActionsFactory {
    public static UserActions getUserActions(String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return new AdministratorActions();
            case "courier":
                return new CourierActions();
            case "customer":
                return new CustomerActions();
            case "kitchen_worker":
                return new KitchenWorkerActions();
            default:
                throw new IllegalArgumentException("Неверная роль пользователя: " + role);
        }
    }
}
