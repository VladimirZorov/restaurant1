package restaurant.core;

import restaurant.core.interfaces.Controller;
import restaurant.entities.drinks.Fresh;
import restaurant.entities.drinks.Smoothie;
import restaurant.entities.healthyFoods.Salad;
import restaurant.entities.healthyFoods.VeganBiscuits;
import restaurant.entities.healthyFoods.interfaces.HealthyFood;
import restaurant.entities.drinks.interfaces.Beverages;
import restaurant.entities.tables.InGarden;
import restaurant.entities.tables.Indoors;
import restaurant.entities.tables.interfaces.Table;
import restaurant.repositories.BeverageRepositoryImpl;
import restaurant.repositories.HealthFoodRepositoryImpl;
import restaurant.repositories.TableRepositoryImpl;
import restaurant.repositories.interfaces.*;

import static restaurant.common.ExceptionMessages.*;
import static restaurant.common.OutputMessages.*;

public class ControllerImpl implements Controller {

    private HealthFoodRepository<HealthyFood> healthFoodRepository;
    private BeverageRepository<Beverages> beverageRepository;
    private TableRepository<Table> tableRepository;


    public ControllerImpl(HealthFoodRepository<HealthyFood> healthFoodRepository, BeverageRepository<Beverages> beverageRepository, TableRepository<Table> tableRepository) {
        this.healthFoodRepository = new HealthFoodRepositoryImpl();
        this.beverageRepository = new BeverageRepositoryImpl();
        this.tableRepository = new TableRepositoryImpl();
    }

    @Override
    public String addHealthyFood(String type, double price, String name) {
        HealthyFood food = null;
        if (type.equals("Salad")) {
            food = new Salad(name, price);
        } else if (type.equals("VeganBiscuits")) {
            food = new VeganBiscuits(name, price);
        }
        for (HealthyFood food1 : healthFoodRepository.getAllEntities()) {
            if (food1.equals(food)) {
                throw new IllegalArgumentException(String.format(FOOD_EXIST, name));
            }
        }
        healthFoodRepository.add(food);
        return String.format(FOOD_ADDED, name);
    }

    @Override
    public String addBeverage(String type, int counter, String brand, String name) {
        Beverages beverage = null;
        if (type.equals("Smoothie")) {
            beverage = new Smoothie(name, counter, brand);
        } else if (type.equals("Fresh")) {
            beverage = new Fresh(name, counter, brand);
        }

        for (Beverages beverage1 : beverageRepository.getAllEntities()) {
            if (beverage1.equals(beverage)) {
                throw new IllegalArgumentException(String.format(BEVERAGE_EXIST, name));
            }
        }

        beverageRepository.add(beverage);
        return String.format(BEVERAGE_ADDED, type, brand);
    }

    @Override
    public String addTable(String type, int tableNumber, int capacity) {
        Table table = null;
        if (type.equals("InGarden")) {
            table = new InGarden(tableNumber, capacity);
        } else if (type.equals("Indoors")) {
            table = new Indoors(tableNumber, capacity);
        }

        for (Table table1 : tableRepository.getAllEntities()) {
            if (table1.equals(table)) {
                throw new IllegalArgumentException(String.format(TABLE_EXIST, tableNumber));
            }
        }
        tableRepository.add(table);
        return String.format(TABLE_ADDED, tableNumber);
    }

    @Override
    public String reserve(int numberOfPeople) {

        for (Table table : tableRepository.getAllEntities()) {
            if (table.isReservedTable() == false && table.getSize() >= numberOfPeople) {
                table.reserve(numberOfPeople);
                return String.format(TABLE_RESERVED, table.getTableNumber(), numberOfPeople);
            }
        }

        return String.format(RESERVATION_NOT_POSSIBLE, numberOfPeople);
    }

    @Override
    public String orderHealthyFood(int tableNumber, String healthyFoodName) {
        Table tableForOrderFood = tableRepository.getAllEntities().stream()
                .filter(table -> table.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        HealthyFood foodToAdd = healthFoodRepository.getAllEntities().stream()
                .filter(healthyFood -> healthyFood.getName().equals(healthyFoodName))
                .findFirst().orElse(null);

        if (foodToAdd == null) {
            return String.format(NONE_EXISTENT_FOOD, healthyFoodName);
        } else if (tableForOrderFood == null) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        }

        tableForOrderFood.orderHealthy(foodToAdd);
        return String.format(FOOD_ORDER_SUCCESSFUL, healthyFoodName, tableNumber);
    }

    @Override
    public String orderBeverage(int tableNumber, String name, String brand) {
        Table tableForAddBeverage = tableRepository.getAllEntities().stream()
                .filter(table -> table.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        Beverages beverageToAdd = beverageRepository.getAllEntities().stream()
                .filter(beverages -> beverages.getName().equals(name))
                .filter(beverages -> beverages.getBrand().equals(brand))
                .findFirst().orElse(null);

        if (tableForAddBeverage == null) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        } else if (beverageToAdd == null) {
            return String.format(NON_EXISTENT_DRINK, name, brand);
        }

        tableForAddBeverage.orderBeverages(beverageToAdd);
        return String.format(BEVERAGE_ADDED, name, tableNumber);
    }

    @Override
    public double closedBill(int tableNumber) {
        Table tableToBill = tableRepository.getAllEntities().stream()
                .filter(table -> table.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        if (tableToBill != null) {
            return tableToBill.bill();
        }
        //TODO:
        return null;
    }


    @Override
    public String totalMoney() {
        //TODO:
        return null;
    }
}
