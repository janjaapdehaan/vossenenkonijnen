import java.util.*;

public interface Actor {
	public abstract void act(List<Actor> newActor);
	public abstract boolean isActive();
}
