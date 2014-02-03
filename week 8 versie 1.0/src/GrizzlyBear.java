import java.util.*;


public class GrizzlyBear extends Animal {
	
	private static final int BREEDING_AGE = 200;
	private static final int MAX_AGE = 300;
	private static final double BREEDING_PROBABILITY = 0.12;
	private static final int MAX_LITTER_SIZE = 2;
	private static final int FOX_FOOD_VALUE = 35;


	public GrizzlyBear(boolean randomAge, Field field, Location location)
	{
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(FOX_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = FOX_FOOD_VALUE;
        }
    }

	@Override
	public void act(List<Actor> newGrizzlyBears) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newGrizzlyBears);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
	
	private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                	fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
        }
        return null;
    }
	
	@Override
	protected int getBreedingAge() {
		// TODO Auto-generated method stub
		return BREEDING_AGE;
	}

	@Override
	protected int getMaxAge() {
		// TODO Auto-generated method stub
		return MAX_AGE;
	}

	@Override
	protected int getMaxLitterSize() {
		// TODO Auto-generated method stub
		return MAX_LITTER_SIZE;
	}

	@Override
	protected double getBreedingProbability() {
		// TODO Auto-generated method stub
		return BREEDING_PROBABILITY;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return isAlive();
	}
	

}
