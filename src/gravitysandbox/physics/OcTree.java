package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The OcTree used by the Barnes-Hut-Algorithm.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class OcTree {

    /**
     * The Theta used by the Barnes-Hut-Algorithm
     */
    private final BigDecimal THETA = BigDecimal.ONE;

    /**
     * Constant used for scaling {@link Vector3D} objects.
     */
    private final BigDecimal BD0_5 = new BigDecimal("0.5");

    /**
     * The root node of the tree.
     */
    private OcTreeNode rootNode;

    /**
     * Generate the OcTree within the given boundary.
     *
     * @param frontLowerLeft The first corner point to specify the boundary.
     * @param backUpperRight The second corner point to specify the boundary.
     */
    public void buildTree(Vector3D frontLowerLeft, Vector3D backUpperRight) {
        //if (rootNode != null)
        //    resetTree();
        rootNode = new OcTreeNode(frontLowerLeft, backUpperRight);

        for (Body body : BodyContainer.getInstance()) {
            // Check if the body lies within the boundary.
            if (body.getPosition().getX().compareTo(frontLowerLeft.getX()) >= 0
                    && body.getPosition().getY().compareTo(frontLowerLeft.getY()) >= 0
                    && body.getPosition().getZ().compareTo(frontLowerLeft.getZ()) >= 0
                    && body.getPosition().getX().compareTo(backUpperRight.getX()) <= 0
                    && body.getPosition().getY().compareTo(backUpperRight.getY()) <= 0
                    && body.getPosition().getZ().compareTo(backUpperRight.getZ()) <= 0)
                rootNode.insert(body);
        }
    }

    /**
     * Empties the tree.
     */
    private void resetTree() {
        rootNode.deleteNode();
    }

    /**
     * Calculates the mass distribution in the tree.
     */
    public void calculateMassDistribution() {
        rootNode.calculateMassDistribution();
    }

    /**
     * Calculates the overall gravity on a given {@link Body}.
     *
     * @param targetBody The body on which the force will be acting.
     * @return The force on targetBody.
     */
    public Vector3D calculateGravitationalForce(Body targetBody) {
        return rootNode.calculateGravitationalForce(targetBody);
    }

    /**
     * A node within the {@link OcTree}
     *
     * @author Christoph Bruckner
     * @version 1.0
     * @since 1.0
     */
    class OcTreeNode {

        /**
         * The possible sub nodes of this node.
         */
        private OcTreeNode[] octants;

        /**
         * The sum of masses from all bodies inside this node and all his sub nodes.
         */
        private BigDecimal mass;

        /**
         * Coordinates of the center of mass.
         */
        private Vector3D centerOfMass;

        /**
         * The coordinates of the center point.
         */
        private Vector3D center;

        /**
         * The number of bodies within this node and all his sub nodes.
         */
        private int numberOfBodies;

        /**
         * If numberOfBodies is equal to 1 the reference of the {@link Body} will be stored here.
         */
        private Body existingParticle;

        /**
         * The first corner point to specify the boundary.
         */
        private Vector3D frontLowerLeft;

        /**
         * The second corner point to specify the boundary.
         */
        private Vector3D backUpperRight;

        /**
         * Creates a new Node for the given boundary.
         *
         * @param frontLowerLeft The first corner point to specify the boundary.
         * @param backUpperRight The second corner point to specify the boundary.
         */
        OcTreeNode(Vector3D frontLowerLeft, Vector3D backUpperRight) {
            octants = new OcTreeNode[8];
            numberOfBodies = 0;

            mass = BigDecimal.ZERO;
            centerOfMass = new Vector3D();

            this.frontLowerLeft = frontLowerLeft;
            this.backUpperRight = backUpperRight;
            center = frontLowerLeft.add(backUpperRight.subtract(frontLowerLeft).scale(BD0_5));
        }

        /**
         * Insert the given {@link Body} to this node. Split node if necessary until there is only one body per node.
         *
         * @param newBody The {@link Body} to be inserted.
         */
        void insert(Body newBody) {
            Octant oct;
            int index;
            if (numberOfBodies > 1) {
                oct = getOctant(newBody);
                index = oct.ordinal();

                if (octants[index] == null)
                    octants[index] = createSubnode(oct);

                octants[index].insert(newBody);
            } else if (numberOfBodies == 1) {
                oct = getOctant(existingParticle);
                index = oct.ordinal();

                if (octants[index] == null)
                    octants[index] = createSubnode(oct);
                octants[index].insert(existingParticle);

                oct = getOctant(newBody);
                index = oct.ordinal();

                if (octants[index] == null)
                    octants[index] = createSubnode(oct);
                octants[index].insert(newBody);

                existingParticle = null;
            } else {
                existingParticle = newBody;
            }
            numberOfBodies++;
        }

        /**
         * Create a OcTreeNode based on the current boundaries and the specified octant.
         *
         * @param oct The octant.
         * @return new OcTreeNode with the calculated boundaries based on the octant.
         */
        private OcTreeNode createSubnode(Octant oct) {
            OcTreeNode returnNode = null;
            switch (oct) {
                case LSW:
                    returnNode = new OcTreeNode(frontLowerLeft, center);
                    break;
                case LNW:
                    returnNode = new OcTreeNode(
                            new Vector3D(frontLowerLeft.getX(), center.getY(), frontLowerLeft.getZ()),
                            new Vector3D(center.getX(), backUpperRight.getY(), center.getZ()));
                    break;
                case LNE:
                    returnNode = new OcTreeNode(
                            new Vector3D(center.getX(), center.getY(), frontLowerLeft.getZ()),
                            new Vector3D(backUpperRight.getX(), backUpperRight.getY(), center.getZ()));
                    break;
                case LSE:
                    returnNode = new OcTreeNode(
                            new Vector3D(center.getX(), frontLowerLeft.getY(), frontLowerLeft.getZ()),
                            new Vector3D(backUpperRight.getX(), center.getY(), center.getZ()));
                    break;
                case USW:
                    returnNode = new OcTreeNode(
                            new Vector3D(frontLowerLeft.getX(), frontLowerLeft.getY(), center.getZ()),
                            new Vector3D(center.getX(), center.getY(), backUpperRight.getZ()));
                    break;
                case UNW:
                    returnNode = new OcTreeNode(
                            new Vector3D(frontLowerLeft.getX(), center.getY(), center.getZ()),
                            new Vector3D(center.getX(), backUpperRight.getY(), backUpperRight.getZ()));
                    break;
                case UNE:
                    returnNode = new OcTreeNode(center, backUpperRight);
                    break;
                case USE:
                    returnNode = new OcTreeNode(
                            new Vector3D(center.getX(), frontLowerLeft.getY(), center.getZ()),
                            new Vector3D(backUpperRight.getX(), center.getY(), backUpperRight.getZ()));
                    break;
            }
            return returnNode;
        }

        /**
         * Calculates the mass distribution of this node.
         */
        void calculateMassDistribution() {
            if (numberOfBodies == 1) {
                centerOfMass = existingParticle.getPosition();
                mass = existingParticle.getMass();
            } else {
                for (OcTreeNode oct : octants) {
                    if (oct != null) {
                        oct.calculateMassDistribution();
                        mass = mass.add(oct.mass);
                        centerOfMass = centerOfMass.add(oct.centerOfMass.scale(oct.mass));
                    }
                }
                centerOfMass = centerOfMass.scale(BigDecimal.ONE.divide(mass, 50, RoundingMode.HALF_UP));
            }
        }

        /**
         * Calculate the gravitational force acting from this node on the target body.
         *
         * @param targetBody The targeted {@link Body}.
         * @return The gravity on targetBody.
         */
        Vector3D calculateGravitationalForce(Body targetBody) {
            Vector3D force = new Vector3D();


            if (numberOfBodies == 1) {
                force = Physics.calculateGravitationalForce(targetBody, existingParticle);
            } else {
                BigDecimal r = centerOfMass.subtract(targetBody.getPosition()).length();
                BigDecimal d = frontLowerLeft.subtract(backUpperRight).length();

                if (r.compareTo(BigDecimal.ZERO) != 0 && d.divide(r, 50, RoundingMode.HALF_UP).compareTo(THETA) < 0) {
                    Body tmp = new Body("", centerOfMass, null, mass, false);
                    force = Physics.calculateGravitationalForce(targetBody, tmp);
                } else {
                    for (OcTreeNode oct : octants) {
                        if (oct != null) {
                            force = force.add(oct.calculateGravitationalForce(targetBody));
                        }
                    }
                }
            }
            return force;
        }

        /**
         * Calculate in which octant the given {@link Body} lies.
         *
         * @param body The {@link Body} for which the octant will be determined.
         * @return The octant of body.
         */
        Octant getOctant(Body body) {
            Vector3D position = body.getPosition();
            if (position.getZ().compareTo(center.getZ()) <= 0) {
                if (position.getX().compareTo(center.getX()) <= 0 && position.getY().compareTo(center.getY()) <= 0) {
                    return Octant.LSW;
                } else if (position.getX().compareTo(center.getX()) <= 0 && position.getY().compareTo(center.getY()) >= 0) {
                    return Octant.LNW;
                } else if (position.getX().compareTo(center.getX()) >= 0 && position.getY().compareTo(center.getY()) >= 0) {
                    return Octant.LNE;
                } else if (position.getX().compareTo(center.getX()) >= 0 && position.getY().compareTo(center.getY()) <= 0) {
                    return Octant.LSE;
                } else {
                    throw new IllegalArgumentException("Can't calculate octant.");
                }
            } else {
                if (position.getX().compareTo(center.getX()) <= 0 && position.getY().compareTo(center.getY()) <= 0) {
                    return Octant.USW;
                } else if (position.getX().compareTo(center.getX()) <= 0 && position.getY().compareTo(center.getY()) >= 0) {
                    return Octant.UNW;
                } else if (position.getX().compareTo(center.getX()) >= 0 && position.getY().compareTo(center.getY()) >= 0) {
                    return Octant.UNE;
                } else if (position.getX().compareTo(center.getX()) >= 0 && position.getY().compareTo(center.getY()) <= 0) {
                    return Octant.USE;
                } else {
                    throw new IllegalArgumentException("Can't calculate octant.");
                }
            }
        }

        /**
         * Frees all the references of this node and all his sub nodes.
         */
        void deleteNode() {
            for (OcTreeNode oct : octants) {
                if (oct != null)
                    oct.deleteNode();
                octants = null;
                existingParticle = null;
                center = centerOfMass = frontLowerLeft = backUpperRight = null;
                mass = null;
            }
        }

    }

    /**
     * @author Christoph Bruckner
     * @version 1.0
     * @since 1.0
     */
    enum Octant {
        UNW, UNE, USE, USW, LNW, LNE, LSE, LSW
    }

}
