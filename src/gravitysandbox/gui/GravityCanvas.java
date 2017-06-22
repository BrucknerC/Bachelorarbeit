package gravitysandbox.gui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;
import gravitysandbox.physics.Physics;
import gravitysandbox.util.Vector3D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static java.lang.Math.pow;
import static java.math.RoundingMode.HALF_UP;

/**
 * @version 0.6
 * @since 0.1
 */
public class GravityCanvas extends GLCanvas implements GLEventListener {

    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private BodyContainer bodyContainer;
    private BigDecimal simSpeed;
    private double bodySize;
    private float mouseSpeed;
    private Point savedMouseLocation;
    private float cameraPosition[] = {0, 0, 10};
    private float lookAtPosition[] = {0, 0, 0};
    private float upVector[] = {0, 1, 0};
    private int mouseButton;
    private boolean animationRunning;

    public GravityCanvas() {
        super();
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        //GL-Objekt holen
        gl = glDrawable.getGL().getGL2();
        //Buffer leeren
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //Modellmatrix initialisieren
        gl.glLoadIdentity();
        //Kamera positionieren
        glu.gluLookAt(cameraPosition[0], cameraPosition[1], cameraPosition[2],
                lookAtPosition[0], lookAtPosition[1], lookAtPosition[2],
                upVector[0], upVector[1], upVector[2]);

        gl.glClearColor(0, 0, 0, 1);

        int lightCount = 0;

        for (Body body : bodyContainer) {

            gl.glColor3f(1, 1, 1);

            gl.glPushMatrix();

            gl.glTranslated(
                    body.getPosition().getX().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                    body.getPosition().getY().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                    body.getPosition().getZ().divide(Physics.AU, 50, HALF_UP).doubleValue()
            );

            if (body.isStar() && lightCount < GL2.GL_MAX_LIGHTS) {
                float light_pos[] = {
                        body.getPosition().getX().divide(Physics.AU, 50, HALF_UP).floatValue(),
                        body.getPosition().getY().divide(Physics.AU, 50, HALF_UP).floatValue(),
                        body.getPosition().getZ().divide(Physics.AU, 50, HALF_UP).floatValue(),
                        1};
                float light_color[] = {1, 1, 1, 1};
                float material_emmision[] = {1, 1, 1, 1};

                gl.glEnable(GL2.GL_LIGHT1 + lightCount);

                gl.glLightfv(GL2.GL_LIGHT1 + lightCount, GL2.GL_POSITION, light_pos, 0);
                gl.glLightfv(GL2.GL_LIGHT1 + lightCount, GL2.GL_AMBIENT, light_color, 0);
                gl.glLightfv(GL2.GL_LIGHT1 + lightCount, GL2.GL_DIFFUSE, light_color, 0);
                gl.glLightfv(GL2.GL_LIGHT1 + lightCount, GL2.GL_SPECULAR, light_color, 0);

                lightCount++;

                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material_emmision, 0);
            } else {
                float material_emmision[] = {0, 0, 0, 1};
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material_emmision, 0);
            }

            glut.glutSolidSphere(Math.log10(Math.log10(body.getMass().doubleValue()) * body.getMass().doubleValue()) * bodySize, 50, 50);

            gl.glPopMatrix();

            gl.glBegin(GL2.GL_LINE_STRIP);
            if (body.getPreviousLocations().size() > 2) {
                int i = 0;
                float iColor;
                for (Vector3D point : body.getPreviousLocations()) {
                    iColor = ((float) i / (float) body.getPreviousLocations().size());
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

        if (animationRunning)
            animate();

        gl.glFlush();
    }

    @Override
    public void init(GLAutoDrawable gLDrawable) {
        glu = new GLU();
        glut = new GLUT();
        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_NORMALIZE);
        bodyContainer = BodyContainer.getInstance();
        simSpeed = new BigDecimal("86400");
        bodySize = 0.002;
        savedMouseLocation = new Point();
        mouseSpeed = 0.01f;
        animationRunning = false;

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
                Point deltaMouseLocation = new Point(e.getLocationOnScreen().x - savedMouseLocation.x,
                        e.getLocationOnScreen().y - savedMouseLocation.y);

                float rightVector[] = new float[3];
                float resultVector1[] = new float[3];
                float resultVector2[] = new float[3];

                float viewVector[] = new float[3];
                VectorUtil.subVec3(viewVector, lookAtPosition, cameraPosition);
                float viewLength = VectorUtil.normVec3(viewVector);

                viewVector = VectorUtil.normalizeVec3(viewVector);
                upVector = VectorUtil.normalizeVec3(upVector);

                // Calculate right-vector based on view and up vector
                VectorUtil.crossVec3(rightVector, viewVector, upVector);
                switch (mouseButton) {
                    case MouseEvent.BUTTON1:

                        // Calculate translation as vectors
                        // resultVector2 is the translation in right-vector direction
                        VectorUtil.scaleVec3(resultVector2, rightVector, (-mouseSpeed) * deltaMouseLocation.x);
                        // resultVector1 is the translation in up-vector direction
                        VectorUtil.scaleVec3(resultVector1, upVector, (mouseSpeed) * deltaMouseLocation.y);

                        //Calculate the resulting translation and store it in resultVector2
                        VectorUtil.addVec3(resultVector2, resultVector1, resultVector2);

                        // Translate both the cameraPosition as well as the lookAtPosition with the two resultVectors
                        VectorUtil.addVec3(cameraPosition, cameraPosition, resultVector2);
                        VectorUtil.addVec3(lookAtPosition, lookAtPosition, resultVector2);
                        break;
                    case MouseEvent.BUTTON3:
                        float rotationAngle;
                        float[] rotationVector = new float[3];
                        float[] mouseTranslationVector = new float[3];

                        // Calculate mouseTranslationVector
                        VectorUtil.scaleVec3(mouseTranslationVector, upVector, deltaMouseLocation.y);
                        VectorUtil.scaleVec3(resultVector1, rightVector, -deltaMouseLocation.x);
                        VectorUtil.addVec3(mouseTranslationVector, mouseTranslationVector, resultVector1);

                        // Calculate rotationVector
                        VectorUtil.crossVec3(rotationVector, mouseTranslationVector, viewVector);

                        // Calculate rotationAngle
                        rotationAngle = 0.001f * Math.abs((float) Math.atan(VectorUtil.normVec3(mouseTranslationVector) / VectorUtil.normVec3(viewVector)));

                        // Rotate viewVector with angle rotationAngle around rotationVector
                        viewVector = rotVec(viewVector, rotationVector, cameraPosition, rotationAngle);

                        // Calculate the new upVector
                        VectorUtil.crossVec3(upVector, rightVector, viewVector);
                        VectorUtil.normalizeVec3(upVector);

                        // Calculate new cameraPosition
                        VectorUtil.scaleVec3(viewVector, viewVector, -viewLength);
                        VectorUtil.addVec3(cameraPosition, lookAtPosition, viewVector);
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

        addMouseWheelListener(e -> {
            float mouseWheelSpeed = 0.1f;
            float viewVector[] = new float[3];
            float newViewVector[] = new float[3];
            VectorUtil.subVec3(viewVector, lookAtPosition, cameraPosition);

            VectorUtil.scaleVec3(newViewVector, viewVector, (float)-(1 + mouseWheelSpeed * e.getPreciseWheelRotation()));
            VectorUtil.addVec3(cameraPosition, lookAtPosition, newViewVector);

            VectorUtil.addVec3(lookAtPosition, cameraPosition, viewVector);
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
            glu.gluPerspective(60, 1, 0.000001, 100_000);
        else
            glu.gluPerspective(60, aspect, 0.000001, 100_000);
        //gl.glFrustum(-1, 1, -1, 1, 1, 100);
        //und wieder auf Modellmatrix zuruecksetzen
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void animate() {
        Vector3D gravForce;
        Body body1, body2;

        for (int i = 0; i < bodyContainer.size(); i++) {
            body1 = bodyContainer.get(i);
            for (int j = i + 1; j < bodyContainer.size(); j++) {
                body2 = bodyContainer.get(j);
                gravForce = Physics.calculateGravitationalForce(body1, body2);
                body1.setAcceleration(body1.getAcceleration().add(gravForce.scale(BigDecimal.ONE.divide(body1.getMass(), -gravForce.getBDScale() * body1.getMass().scale(), HALF_UP))));
                body2.setAcceleration(body2.getAcceleration().add(gravForce.scale(BigDecimal.ONE.negate().divide(body2.getMass(), -gravForce.getBDScale() * body2.getMass().scale(), HALF_UP))));
            }
            body1.setVelocity(
                    body1.getVelocity().add(body1.getAcceleration().scale(simSpeed))
            );
        }

        for (Body body : bodyContainer) {
            body.setPosition(
                    body.getPosition().add(
                            body.getVelocity()
                                    .scale(simSpeed)
                    )
            );
            body.setAcceleration(new Vector3D());
        }
    }

    public void resetView() {
        cameraPosition[0] = cameraPosition[1] = 0;
        cameraPosition[2] = 10;

        lookAtPosition[0] = lookAtPosition[1] = lookAtPosition[2] = 0;

        upVector[0] = upVector[2] = 0;
        upVector[1] = 1;
    }

    private float[] rotVec(float[] vector, float[] rotationVector, float[] rotationPoint, float angle) {
        if (vector.length != 3 || rotationVector.length != 3 || rotationPoint.length != 3) {
            throw new IllegalArgumentException("Vectors or points have to have 3 dimensions");
        }

        float cosine = (float) Math.cos(angle), sine = (float) Math.sin(angle);

        // Be sure to use only normalized vectors
        float[] normRotationVector = new float[3],
                resultVector = new float[3];
        normRotationVector = VectorUtil.normalizeVec3(normRotationVector, rotationVector);

        resultVector[0] = (float) (rotationPoint[0] * (pow(normRotationVector[1], 2) + pow(normRotationVector[2], 2))
                - rotationVector[0] * (
                rotationPoint[1] * normRotationVector[1] + rotationPoint[2] * normRotationVector[2]
                        - normRotationVector[0] * vector[0] - normRotationVector[1] * vector[1] - normRotationVector[2] * vector[2]
        )) * (1 - cosine)
                + vector[0] * cosine
                + (-rotationPoint[2] * normRotationVector[1] + rotationPoint[1] * normRotationVector[2]
                - normRotationVector[2] * vector[1] + normRotationVector[1] * vector[2]) * sine;

        resultVector[1] = (float) (rotationPoint[1] * (pow(normRotationVector[0], 2) + pow(normRotationVector[2], 2))
                - rotationVector[1] * (
                rotationPoint[0] * normRotationVector[0] + rotationPoint[2] * normRotationVector[2]
                        - normRotationVector[0] * vector[0] - normRotationVector[1] * vector[1] - normRotationVector[2] * vector[2]
        )) * (1 - cosine)
                + vector[1] * cosine
                + (rotationPoint[2] * normRotationVector[0] - rotationPoint[0] * normRotationVector[2]
                + normRotationVector[2] * vector[0] - normRotationVector[0] * vector[2]) * sine;

        resultVector[2] = (float) (rotationPoint[2] * (pow(normRotationVector[0], 2) + pow(normRotationVector[1], 2))
                - rotationVector[2] * (
                rotationPoint[0] * normRotationVector[0] + rotationPoint[1] * normRotationVector[1]
                        - normRotationVector[0] * vector[0] - normRotationVector[1] * vector[1] - normRotationVector[2] * vector[2]
        )) * (1 - cosine)
                + vector[2] * cosine
                + (-rotationPoint[1] * normRotationVector[0] + rotationPoint[0] * normRotationVector[1]
                - normRotationVector[1] * vector[0] + normRotationVector[0] * vector[1]) * sine;

        return resultVector;
    }

    void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }
}