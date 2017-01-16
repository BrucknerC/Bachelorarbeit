package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: Documentation
public class BodyConainer implements Iterable<Body> {

    private ArrayList<Body> bodies;
    private ArrayList<Vector3D> forceList;

    private static BodyConainer instance = new BodyConainer();

    public static BodyConainer getInstance() {
        return instance;
    }

    private BodyConainer() {
        bodies = new ArrayList<>();
        forceList = new ArrayList<>();
    }

    @Override
    public Iterator<Body> iterator() {
        return bodies.iterator();
    }

    public void add(Body body, int index) {
        bodies.add(index, body);
        forceList.add(index, new Vector3D());
    }

    public int size() {
        return bodies.size();
    }

    public Body get(int index) {
        return bodies.get(index);
    }

    public Vector3D getForce(int index) {
        return forceList.get(index);
    }
    public void setForce(Vector3D vector, int index) {
        forceList.set(index, vector);
    }
}
