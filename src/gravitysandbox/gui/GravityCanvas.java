package gravitysandbox.gui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyConainer;
import gravitysandbox.physics.Physics;
import gravitysandbox.util.BigDecimalMath;
import gravitysandbox.util.Vector3D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static java.lang.Math.pow;

/**
 * @version 0.6
 * @since 0.1
 */
public class GravityCanvas extends GLCanvas implements GLEventListener {

    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private BodyConainer bodyContainer;
    private BigDecimal simSpeed;
    private double zoomLevel;
    private float mouseSpeed;
    private Point savedMouseLocation;
    private float cameraPosition[] = {0,0,10};
    private float lookAtPosition[] = {0,0,0};
    private float upVector[] = {0,1,0};
    private int mouseButton;

    public GravityCanvas() {
        super();
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        //GL-Objekt holen
        gl = glDrawable.getGL().getGL2();
        //Buffer leeren
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        //Modellmatrix initialisieren
        gl.glLoadIdentity();
        //Kamera positionieren
        glu.gluLookAt(cameraPosition[0], cameraPosition[1], cameraPosition[2],
                lookAtPosition[0], lookAtPosition[1], lookAtPosition[2],
                upVector[0], upVector[1], upVector[2]);

        gl.glClearColor(0, 0, 0, 1);

        for (Body body : bodyContainer) {

            gl.glColor3f(1, 1, 1);

            gl.glPushMatrix();

            gl.glTranslated(
                    body.getPosition().getX().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                    body.getPosition().getY().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                    body.getPosition().getZ().divide(Physics.AU, 50, HALF_UP).doubleValue()
            );


            /*System.out.println(body.getName());
            System.out.println(body.getPosition().getX().divide(Physics.AU, body.getPosition().getX().scale() - Physics.AU.scale(), HALF_UP) + ", " +
                    body.getPosition().getY().divide(Physics.AU, body.getPosition().getX().scale() - Physics.AU.scale(), HALF_UP) + ", " +
                    body.getPosition().getZ().divide(Physics.AU, body.getPosition().getX().scale() - Physics.AU.scale(), HALF_UP));
                    */
            //System.out.println(body.getPosition().scale(BigDecimal.ONE.divide(Physics.AU, body.getPosition().getX().scale(), HALF_UP)));
            //System.out.println(body.getMass().movePointRight(body.getMass().scale()-5).doubleValue());


            glut.glutSolidSphere(Math.log10(body.getMass().doubleValue()) * zoomLevel, 50, 50);

            gl.glPopMatrix();

            gl.glBegin(GL2.GL_LINE_STRIP);
            if (body.getPreviousLocations().size()>2) {
                int i = 0;
                float iColor;
                for (Vector3D point : body.getPreviousLocations()) {
                    iColor = ((float)i/(float)body.getPreviousLocations().size());
                    gl.glColor3f(iColor, iColor, iColor);
                    gl.glVertex3d(
                            point.getX().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                            point.getY().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                            point.getZ().divide(Physics.AU, 50, HALF_UP).doubleValue()
                    );
                    i++;
                }
            }
            gl.glEnd();

        }

        if (getAnimator().isStarted())
            animate();

        gl.glFlush();

    }

    @Override
    public void init(GLAutoDrawable gLDrawable) {
        glu = new GLU();
        glut = new GLUT();
        bodyContainer = BodyConainer.getInstance();
        simSpeed = new BigDecimal("86400");
        zoomLevel = 0.00125;
        savedMouseLocation = new Point();
        mouseSpeed = 0.02f;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Transparent 16 x 16 pixel cursor image.
                BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                // Create a new blank cursor.
                Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                        cursorImg, new Point(0, 0), "blank cursor");
                // Set the blank cursor to the JFrame.
                setCursor(blankCursor);

                savedMouseLocation = e.getLocationOnScreen();

                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                    case MouseEvent.BUTTON3:
                        mouseButton = e.getButton();
                        break;
                    default:
                        mouseButton = Integer.MIN_VALUE;
                        break;
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    // Move the cursor back to the starting location.
                    Robot robot = new Robot();
                    robot.mouseMove(savedMouseLocation.x, savedMouseLocation.y);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                mouseButton = Integer.MIN_VALUE;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point deltaMouseLocation = new Point(e.getLocationOnScreen().x-savedMouseLocation.x,
                        e.getLocationOnScreen().y-savedMouseLocation.y);
                float viewVector[] = {
                        lookAtPosition[0]-cameraPosition[0],
                        lookAtPosition[1]-cameraPosition[1],
                        lookAtPosition[2]-cameraPosition[2]
                };
                float resultVector1[] = new float[3];
                float resultVector2[] = new float[3];
                viewVector = VectorUtil.normalizeVec3(viewVector);
                upVector = VectorUtil.normalizeVec3(upVector);
                // Calculate right-vector based on view and up vector
                resultVector1 = VectorUtil.crossVec3(resultVector1, viewVector, upVector);
                resultVector1 = VectorUtil.normalizeVec3(resultVector1);
                switch (mouseButton) {
                    case MouseEvent.BUTTON1:

                        // Calculate translation as vectors
                        // resultVector2 is the translation in right-vector direction
                        resultVector2 = VectorUtil.scaleVec3(resultVector2, resultVector1, -mouseSpeed*deltaMouseLocation.x);
                        // resultVector1 is the translation in up-vector direction
                        resultVector1 = VectorUtil.scaleVec3(resultVector1, upVector, mouseSpeed*deltaMouseLocation.y);

                        // Translate both the cameraPosition as well as the lookAtPosition with the two resultVectors
                        cameraPosition = VectorUtil.addVec3(cameraPosition,
                                cameraPosition,
                                resultVector1
                        );
                        cameraPosition = VectorUtil.addVec3(cameraPosition,
                                cameraPosition,
                                resultVector2
                        );

                        lookAtPosition = VectorUtil.addVec3(lookAtPosition,
                                lookAtPosition,
                                resultVector1
                        );
                        lookAtPosition = VectorUtil.addVec3(lookAtPosition,
                                lookAtPosition,
                                resultVector2
                        );
                        break;
                    case MouseEvent.BUTTON3:
                        // Movement in x direction rotates around up vector

                        //TODO: Calculate Angle and rotate

                        // Movement in y direction rotates around right-vector
                        break;
                }

                try {
                    // Move the cursor back to the starting location.
                    Robot robot = new Robot();
                    robot.mouseMove(savedMouseLocation.x, savedMouseLocation.y);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }


    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        //GL-Objekt holen
        gl = gLDrawable.getGL().getGL2();
        //Bildausschnitt im Fenster positionieren
        gl.glViewport(0, 0, width, height);
        //Auf Projektionsmatrix umschalten
        gl.glMatrixMode(GL2.GL_PROJECTION);
        //Projektionsmwtrix initialisieren
        gl.glLoadIdentity();
        float aspect = (float) width / (float) height;
        if (aspect == 0 || Float.isInfinite(aspect))
            glu.gluPerspective(60, 1, 1, 100_000);
        else
            glu.gluPerspective(60, aspect, 1, 100_000);
        //gl.glFrustum(-1, 1, -1, 1, 1, 100);
        //und wieder auf Modellmatrix zuruecksetzen
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void animate() {
        /*if (bodyContainer.size() == 2) {

        } else {

        }*/

        Vector3D gravForce;
        Body body1, body2;

        for (int i = 0; i < bodyContainer.size(); i++) {
            body1 = bodyContainer.get(i);
            for (int j = i+1; j < bodyContainer.size(); j++) {
                body2 = bodyContainer.get(j);
                gravForce = Physics.calculateGravitationalAcceleration(bodyContainer.get(i), bodyContainer.get(j));
                body1.setVelocity(
                        body1.getVelocity().add(
                                gravForce.scale(BigDecimal.ONE.divide(body1.getMass(), -gravForce.getBDScale()*body1.getMass().scale(), HALF_UP))
                                        .scale(simSpeed)
                        )
                );
                body2.setVelocity(
                        body2.getVelocity().add(
                                gravForce.scale(BigDecimal.ONE.negate().divide(body2.getMass(), -gravForce.getBDScale()*body2.getMass().scale(), HALF_UP))
                                        .scale(simSpeed)
                        )
                );
            }
        }

        for (Body body : bodyContainer) {
            body.setPosition(
                    body.getPosition().add(
                            body.getVelocity()
                                    .scale(simSpeed)
                    )
            );

        }
    }

    private float[] rotVec(float[] vector, float[] rotationVector, float[] rotationPoint, float angle) {
        if (vector.length != 3 || rotationVector.length != 3 || rotationPoint.length != 3) {
            throw new IllegalArgumentException("Vectors or points have to have 3 dimensions");
        }

        float cosine = (float)Math.cos(angle), sine = (float)Math.sin(angle);

        // Be sure to use only normalized vectors
        float[] normRotationVector = new float[3],
                resultVector = new float[3];
        normRotationVector = VectorUtil.normalizeVec3(normRotationVector, rotationVector);

        resultVector[0] = (float)(rotationPoint[0]*(pow(normRotationVector[1], 2)+pow(normRotationVector[2], 2))
                - rotationVector[0]*(
                        rotationPoint[1]*normRotationVector[1]+rotationPoint[2]*normRotationVector[2]
                        -normRotationVector[0]*vector[0]-normRotationVector[1]*vector[1]-normRotationVector[2]*vector[2]
                        ))*(1-cosine)
                + vector[0]*cosine
                + (-rotationPoint[2]*normRotationVector[1]+rotationPoint[1]*normRotationVector[2]
                    -normRotationVector[2]*vector[1]+normRotationVector[1]*vector[2])*sine;

        resultVector[1] = (float)(rotationPoint[1]*(pow(normRotationVector[0], 2)+pow(normRotationVector[2], 2))
                - rotationVector[1]*(
                        rotationPoint[0]*normRotationVector[0]+rotationPoint[2]*normRotationVector[2]
                        -normRotationVector[0]*vector[0]-normRotationVector[1]*vector[1]-normRotationVector[2]*vector[2]
                    ))*(1-cosine)
                + vector[1]*cosine
                + (rotationPoint[2]*normRotationVector[0]-rotationPoint[0]*normRotationVector[2]
                    +normRotationVector[2]*vector[0]-normRotationVector[0]*vector[2])*sine;

        resultVector[2] = (float)(rotationPoint[2]*(pow(normRotationVector[0], 2)+pow(normRotationVector[1], 2))
                - rotationVector[2]*(
                        rotationPoint[0]*normRotationVector[0]+rotationPoint[1]*normRotationVector[1]
                        -normRotationVector[0]*vector[0]-normRotationVector[1]*vector[1]-normRotationVector[2]*vector[2]
                    ))*(1-cosine)
                + vector[2]*cosine
                + (-rotationPoint[1]*normRotationVector[0]+rotationPoint[0]*normRotationVector[1]
                    -normRotationVector[1]*vector[0]+normRotationVector[0]*vector[1])*sine;

        return resultVector;
    }

}