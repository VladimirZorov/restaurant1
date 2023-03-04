package restaurant.entities.tables;

import restaurant.entities.drinks.interfaces.Beverages;
import restaurant.entities.healthyFoods.interfaces.HealthyFood;
import restaurant.entities.tables.interfaces.Table;

import java.util.ArrayList;
import java.util.Collection;

import static restaurant.common.ExceptionMessages.INVALID_NUMBER_OF_PEOPLE;
import static restaurant.common.ExceptionMessages.INVALID_TABLE_SIZE;

public abstract class BaseTable implements Table {

    private Collection<HealthyFood> healthyFood;
    private Collection<Beverages> beverages;
    private int number;
    private int size;
    private int numberOfPeople;
    private double pricePerPerson;
    private boolean isReservedTable;
    private double allPeople;

    public BaseTable(int number, int size, double pricePerPerson) {
        this.healthyFood = new ArrayList<>();
        this.beverages = new ArrayList<>();
        this.number = number;
        setSize(size);
        this.numberOfPeople = numberOfPeople;
        this.pricePerPerson = pricePerPerson;
        this.isReservedTable = false;
        this.allPeople = allPeople;
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(INVALID_TABLE_SIZE);
        }
        this.size = size;
    }


    @Override
    public int getTableNumber() {
        return this.number;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int numberOfPeople() {
        return this.numberOfPeople;
    }

    @Override
    public double pricePerPerson() {
        return this.pricePerPerson;
    }

    @Override
    public boolean isReservedTable() {
        return false;
    }

    @Override
    public double allPeople() {
        return 0;
    }

    @Override
    public void reserve(int numberOfPeople) {
        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException(INVALID_NUMBER_OF_PEOPLE);
        }
        isReservedTable = true;
        this.numberOfPeople = numberOfPeople;
    }

    @Override
    public void orderHealthy(HealthyFood food) {
        healthyFood.add(food);
    }

    @Override
    public void orderBeverages(Beverages beverages) {
        this.beverages.add(beverages);
    }

    @Override
    public double bill() {
        double totalBill = 0;
        for (HealthyFood food : healthyFood) {
           totalBill+= food.getPrice();
        }
        for (Beverages beverages1 : beverages) {
            totalBill += beverages1.getPrice();
        }
        totalBill += numberOfPeople * pricePerPerson;

        return totalBill;
    }

    @Override
    public void clear() {

    }

    @Override
    public String tableInformation() {
        return String.format("Table - %d%n", number +
                "Size - %d%n", size +
                "Type - %s%n", getClass().getSimpleName() +
                "All price - %.2f", pricePerPerson);
    }
}
