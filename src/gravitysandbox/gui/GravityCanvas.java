package gravitysandbox.gui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyConainer;
import gravitysandbox.physics.Physics;
import gravitysandbox.util.BigDecimalMath;
import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

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
        glu.gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);

        gl.glClearColor(0, 0, 0, 1);

        gl.glColor3f(1, 1, 1);

        for (Body body : bodyContainer) {
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
                for (Vector3D point : body.getPreviousLocations()) {
                    gl.glVertex3d(
                            point.getX().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                            point.getY().divide(Physics.AU, 50, HALF_UP).doubleValue(),
                            point.getZ().divide(Physics.AU, 50, HALF_UP).doubleValue()
                    );
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
}