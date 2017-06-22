package gravitysandbox.physics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Container class for {@link Body} objects using the singleton pattern.
 * @version 1.2
 * @since 0.1
 */
public class BodyContainer implements Iterable<Body> {

    /**
     * The {@link ArrayList} containing the {@link Body} objects.
     */
    private ArrayList<Body> bodies;

    /**
     * The instance property for the singleton pattern.
     */
    private static BodyContainer instance = new BodyContainer();

    /**
     * Returns the only instance of this class according to the singleton pattern.
     * @return The instance reference.
     */
    public static BodyContainer getInstance() {
        return instance;
    }

    /**
     * Private constructor for generating the instance of this class.
     */
    private BodyContainer() {
        bodies = new ArrayList<>();
    }

    /**
     * Returns the {@link Iterator} for the {@link Body} objects.
     * @return The {@link Iterator}.
     */
    @Override
    public Iterator<Body> iterator() {
        return bodies.iterator();
    }

    /**
     * Adds a new {@link Body} at the given index.
     * @param body The {@link Body} object to be added.
     * @param index The index at which the object will be added.
     */
    public void add(Body body, int index) {
        bodies.add(index, body);
    }

    /**
     * Add a new {@link Body} at the end.
     * @param body The {@link Body} object to be added.
     */
    public void add(Body body) {
        bodies.add(bodies.size(), body);
    }

    /**
     * Returns the current number of {@link Body} objects.
     * @return The current number of objects.
     */
    public int size() {
        return bodies.size();
    }

    /**
     * Retrieves the object at the given index.
     * @param index The index.
     * @return The object.
     */
    public Body get(int index) {
        return bodies.get(index);
    }

    /**
     * Deletes every element in this container.
     */
    public void clear() {bodies.clear();}

    /**
     * Removes the element at the given index.
     * @param index The index.
     */
    public void remove(int index){
        bodies.remove(index);
    }

    /**
     * Removes the element identified by its reference.
     * @param body The element to be removed.
     */
    public void remove(Body body) {
        bodies.remove(body);
    }

}
