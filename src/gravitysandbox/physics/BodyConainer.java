package gravitysandbox.physics;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: Documentation
public class BodyConainer implements Iterable<Body> {

    private ArrayList<Body> bodies;

    private static BodyConainer instance = new BodyConainer();

    public static BodyConainer getInstance() {
        return instance;
    }

    private BodyConainer() {
        bodies = new ArrayList<>();
    }

    @Override
    public Iterator<Body> iterator() {
        return bodies.iterator();
    }

    public void add(Body body, int index) {
        bodies.add(index, body);
    }

    public int size() {
        return bodies.size();
    }

    public Body get(int index) {
        return bodies.get(index);
    }

}
