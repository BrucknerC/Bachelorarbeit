package gravitysandbox.physics;

import java.math.BigDecimal;

import gravitysandbox.util.Vector3D;
// TODO: Documentation
public class Body {

    private Vector3D position;

    private Vector3D velocity;

    private BigDecimal mass;

    public Body(Vector3D position, Vector3D velocity, BigDecimal mass) {
        setPosition(position);
        setPosition(velocity);
        setMass(mass);
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    public BigDecimal getMass() {
        return mass;
    }

    public void setMass(BigDecimal mass) {
        this.mass = mass;
    }

}