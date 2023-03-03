package restaurant.repositories;

import restaurant.entities.healthyFoods.Food;
import restaurant.repositories.interfaces.HealthFoodRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HealthFoodRepositoryImpl implements HealthFoodRepository<Food> {

    private Collection<Food> foods;

    public HealthFoodRepositoryImpl() {
        this.foods = new ArrayList<>();
    }

    @Override
    public Food foodByName(String name) {
        for (Food food : foods) {
            if (food.getName().equals(name)) {
                return food;
            }
        }
        return null;
    }

    @Override
    public Collection<Food> getAllEntities() {
        return Collections.unmodifiableCollection(foods);
    }

    @Override
    public void add(Food entity) {
        foods.add(entity);
    }
}
