package gravitysandbox.physics;

import java.math.BigDecimal;
import java.util.ArrayList;

import gravitysandbox.util.Vector3D;

// TODO: Documentation

/**
 * The representation of a physical body with its name, position, velocity and mass.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 0.1
 */
public class Body {

    /**
     * The name of the body.
     */
    private String name;

    /**
     * The current position of the body.
     */
    private Vector3D position;

    /**
     * The current velocity of the body.
     */
    private Vector3D velocity;

    /**
     * A list of previous locations.
     */
    private ArrayList<Vector3D> previousLocations;

    /**
     * The current mass of the body represented as a {@link BigDecimal}.
     */
    private BigDecimal mass;

    /**
     * Creates a new Body object based on given starting position, velocity and mass.
     * @param name The starting name.
     * @param position The starting position.
     * @param velocity The starting velocity.
     * @param mass The starting mass.
     */
    public Body(String name, Vector3D position, Vector3D velocity, BigDecimal mass) {
        setName(name);
        setPosition(position);
        setVelocity(velocity);
        setMass(mass);
        previousLocations = new ArrayList<>();
    }

    /**
     * Getter for position.
     * @return The current position of the body.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     *
     * Setter for position.
     * @param position The new value for the position of the body.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Getter for velocity.
     * @return The current velocity of the body.
     */
    public Vector3D getVelocity() {
        return velocity;
    }

    /**
     * Setter for velocity.
     * @param velocity The new value for the velocity of the body.
     */
    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    /**
     * Getter for mass.
     * @return The current mass of the body.
     */
    public BigDecimal getMass() {
        return mass;
    }

    /**
     * Setter for mass.
     * @param mass The new value for the mass of the body.
     */
    public void setMass(BigDecimal mass) {
        this.mass = mass;
    }

    /**
     * Getter for name.
     * @return The current name of the body.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param name The new value for the name of the body.
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addPreviousLocation(Vector3D location) {

    }

    public ArrayList<Vector3D> getPreviousLocations() {
        return previousLocations;
    }
}