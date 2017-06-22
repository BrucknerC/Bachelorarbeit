package gravitysandbox.util;

import gravitysandbox.physics.Body;
import gravitysandbox.physics.BodyContainer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;


/**
 * This class handles the saving and loading of a body system in XML files.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class XMLEngine {

    /**
     * Saves a system to a XML file.
     * If the file already exists, it will be overwritten.
     *
     * @param path The path of the XML file.
     * @throws IOException If there was a problem saving the file.
     */
    public static void save(String path) throws IOException {
        try (FileWriter fileWriter = new FileWriter(path)) {
            Element rootElem = new Element("system");
            Element tmpElem;
            for (Body body : BodyContainer.getInstance()) {
                Element bodyElem = new Element("body");
                bodyElem.setAttribute("name", body.getName());
                bodyElem.setAttribute("mass", body.getMass().toEngineeringString());
                bodyElem.setAttribute("isStar", body.isStar() ? "true" : "false");

                Element positionElem = new Element("position");

                tmpElem = new Element("x");
                tmpElem.addContent(body.getPosition().getX().toEngineeringString());
                positionElem.addContent(tmpElem);
                tmpElem = new Element("y");
                tmpElem.addContent(body.getPosition().getY().toEngineeringString());
                positionElem.addContent(tmpElem);
                tmpElem = new Element("z");
                tmpElem.addContent(body.getPosition().getZ().toEngineeringString());
                positionElem.addContent(tmpElem);

                bodyElem.addContent(positionElem);

                Element velocityElem = new Element("velocity");

                tmpElem = new Element("x");
                tmpElem.addContent(body.getVelocity().getX().toEngineeringString());
                velocityElem.addContent(tmpElem);
                tmpElem = new Element("y");
                tmpElem.addContent(body.getVelocity().getY().toEngineeringString());
                velocityElem.addContent(tmpElem);
                tmpElem = new Element("z");
                tmpElem.addContent(body.getVelocity().getZ().toEngineeringString());
                velocityElem.addContent(tmpElem);

                bodyElem.addContent(velocityElem);

                rootElem.addContent(bodyElem);
            }

            Document document = new Document(rootElem);

            XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
            xmlOut.output(document, fileWriter);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Clears the BodyContainer and loads a system from a XML file.
     *
     * @param path The path of the XML file.
     * @throws IOException If there was an error reading the file.
     */
    public static void load(String path) throws IOException {
        BodyContainer bodyContainer = BodyContainer.getInstance();
        bodyContainer.clear();

        try {
            Document document = new SAXBuilder().build(path);
            Element elem = document.getRootElement();
            for (Element bodies : elem.getChildren()) {
                Element positionNode = bodies.getChild("position");
                Element velocityNode = bodies.getChild("velocity");

                Vector3D position = new Vector3D(new BigDecimal(positionNode.getChild("x").getText()),
                        new BigDecimal(positionNode.getChild("y").getText()),
                        new BigDecimal(positionNode.getChild("z").getText())
                );
                Vector3D velocity = new Vector3D(new BigDecimal(velocityNode.getChild("x").getText()),
                        new BigDecimal(velocityNode.getChild("y").getText()),
                        new BigDecimal(velocityNode.getChild("z").getText())
                );

                bodyContainer.add(new Body(
                        bodies.getAttributeValue("name"),
                        position,
                        velocity,
                        new BigDecimal(bodies.getAttributeValue("mass")),
                        bodies.getAttributeValue("isStar").equals("true")
                ));
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw e;
        }
    }
}
