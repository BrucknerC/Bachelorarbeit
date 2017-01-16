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
import gravitysandbox.util.Matrix2D;
import gravitysandbox.util.Vector3D;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class GravityCanvas extends GLCanvas implements GLEventListener {

    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private BodyConainer bodyConainer;
    private Matrix2D<Vector3D> bodyMatrix;
    private BigDecimal simSpeed;

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
        glu.gluLookAt(0, 0, 2, 0, 0, 0, 0, 1, 0);

        gl.glClearColor(0,0,0,1);

        gl.glColor3f(1,1,1);

        for (Body body : bodyConainer) {
            gl.glPushMatrix();

            gl.glTranslated(body.getPosition().getX().divide(Physics.AU, HALF_UP).doubleValue(),
                    body.getPosition().getY().divide(Physics.AU, HALF_UP).doubleValue(),
                    body.getPosition().getZ().divide(Physics.AU, HALF_UP).doubleValue());

            /*
            System.out.println(body.getName());
            System.out.println(body.getPosition().getX().divide(Physics.AU, HALF_UP).doubleValue());
            System.out.println(body.getPosition().getY().divide(Physics.AU, HALF_UP).doubleValue());
            System.out.println(body.getPosition().getZ().divide(Physics.AU, HALF_UP).doubleValue());
            System.out.println(body.getMass().movePointRight(body.getMass().scale()-5).doubleValue());
            */

            glut.glutSolidSphere(body.getMass().movePointRight(body.getMass().scale()-5).doubleValue(),50,50);

            gl.glPopMatrix();
            /*
            gl.glBegin(GL2.GL_LINES);
            for(Vector3D point : body.getPreviousLocations()) {
                gl.glVertex3d(point.getX().doubleValue(), point.getY().doubleValue(), point.getZ().doubleValue());
            }
            gl.glEnd();*/
        }

        if (getAnimator().isStarted())
            animate();

        gl.glFlush();

    }

    @Override
    public void init(GLAutoDrawable gLDrawable) {
        glu = new GLU();
        glut = new GLUT();
        bodyConainer = BodyConainer.getInstance();
        simSpeed = new BigDecimal("0.1");
        bodyMatrix = new Matrix2D<>();
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
        Body body1;
        Body body2;
        /*if (bodyConainer.size() == 2) {

        } else {

        }*/

        for(int i = 0; i < bodyConainer.size(); i++) {
            for(int j = i+1; j<bodyConainer.size(); j++) {
                body1 = bodyConainer.get(i);
                body2 = bodyConainer.get(j);
                Vector3D vForce = Physics.calculateGravity(body1, body2);
                bodyConainer.setForce(
                        bodyConainer.getForce(i)
                                .add(vForce)
                                .scale(body1.getMass().pow(-1)), i);
                body1.setVelocity(body1.getVelocity().add(bodyConainer.getForce(i).scale(simSpeed)));
                body1.setPosition(body1.getPosition().add(body1.getVelocity().scale(simSpeed)));

                bodyConainer.setForce(
                        bodyConainer.getForce(j)
                                .add(vForce)
                                .scale(body2.getMass().pow(-1)), j);
                body2.setVelocity(body2.getVelocity().add(bodyConainer.getForce(j).scale(simSpeed)));
                body2.setPosition(body2.getPosition().add(body2.getVelocity().scale(simSpeed)));
            }
        }



    }
}