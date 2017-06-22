package gravitysandbox.physics;

import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

/**
 * TODO Documentation & Implementation
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class OcTree {

    private final double THETA = 1.0;
    private final BigDecimal BD0_5 = new BigDecimal("0.5");
    private OcTreeNode rootNode;

    public void buildTree(Vector3D frontLowerLeft, Vector3D backUpperRight) {
        resetTree();
        rootNode = new OcTreeNode(null, frontLowerLeft, backUpperRight);

        for (Body body : BodyContainer.getInstance()) {
            rootNode.insert(body);
        }
    }

    private void resetTree() {
        rootNode.deleteNode();
    }

    public void calculateMassDistribution() {

    }

    public void calculateGravitationalForce() {

    }

    /**
     *
     */
    class OcTreeNode {

        private OcTreeNode parent;
        private OcTreeNode[] octants;
        private BigDecimal mass;
        private Vector3D centerOfMass;
        private Vector3D center;
        private BigDecimal size;
        private int numberOfBodies;
        private Body existingParticle;
        private Vector3D frontLowerLeft;
        private Vector3D backUpperRight;

        public OcTreeNode(OcTreeNode parent, Vector3D frontLowerLeft, Vector3D backUpperRight) {
            this.parent = parent;
            octants = new OcTreeNode[8];
            numberOfBodies = 0;

            this.frontLowerLeft = frontLowerLeft;
            this.backUpperRight = backUpperRight;
            center = frontLowerLeft.add(backUpperRight.subtract(frontLowerLeft).scale(BD0_5));
        }

        public void insert(Body newBody) {
            if (numberOfBodies>1) {
                Octant oct = getOctant(newBody);
                int index = oct.ordinal();

                if (octants[index] == null)
                    octants[index] = createSubnode(oct);

                octants[index].insert(newBody);
            } else if (numberOfBodies == 1) {

            } else {
                existingParticle = newBody;
            }
            numberOfBodies++;
        }

        private OcTreeNode createSubnode(Octant oct) {
            /*OcTreeNode returnNode;
            switch (oct) {
                case LSW:
                    returnNode = new OcTreeNode(this, frontLowerLeft, center);
                    break;
                case LNW:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(frontLowerLeft.getX(), center.getY(), frontLowerLeft.getZ()),
                            new Vector3D(center.getX(), backUpperRight.getY(), center.getZ()));
                    break;
                case LNE:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(center.getX(), center.getY(), frontLowerLeft.getZ()),
                            new Vector3D(backUpperRight.getX(), backUpperRight.getY(), center.getZ()));
                    break;
                case LSE:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(center.getX(), frontLowerLeft.getY(), frontLowerLeft.getZ()),
                            new Vector3D(backUpperRight.getX(), center.getY(), center.getZ()));
                    break;
                case USW:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(frontLowerLeft.getX(), frontLowerLeft.getY(), center.getZ()),
                            new Vector3D(center.getX(), center.getY(), backUpperRight.getZ()));
                    break;
                case UNW:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(frontLowerLeft.getX(), center.getY(), center.getZ()),
                            new Vector3D(center.getX(), backUpperRight.getY(), backUpperRight.getZ()));
                    break;
                case UNE:
                    returnNode = new OcTreeNode(this, center, backUpperRight);
                    break;
                case USE:
                    returnNode = new OcTreeNode(this,
                            new Vector3D(center.getX(), frontLowerLeft.getY(), center.getZ()),
                            new Vector3D(backUpperRight.getX(), center.getY(), backUpperRight.getZ()));
                    break;
            }
            return returnNode;*/
            return null;
        }

        public void calculateMassDistribution() {

        }

        public void calculateGravitationalForce() {

        }

        Octant getOctant(Body body) {
            Vector3D position = body.getPosition();
            if(position.getZ().compareTo(center.getZ())<=0) {
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

        public void deleteNode() {
            for (OcTreeNode oct : octants) {
                if (oct != null)
                    oct.deleteNode();
                octants = null;
                parent = null;
                center = centerOfMass = frontLowerLeft = backUpperRight = null;
            }
        }

    }

    enum Octant {
        UNW, UNE, USE, USW, LNW, LNE, LSE, LSW
    }

}
