package com.printer;

import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Print Job class.
 * The rationale behind this class is that the print job consists of a list of lines
 * of text or image, being the job pretty simple.
 * As is, it's not possible to overlap text/images, although printers allow it.
 * @author nMoncho
 */
public class TicketPrinterJob implements Serializable {

    private final static Logger logger = Logger.getLogger(TicketPrinterJob.class);

    private Class strategy;
    private int version = 1;
    private List<TicketPrinterJobLine> lines;

    public TicketPrinterJob() {}

    public TicketPrinterJob(List<TicketPrinterJobLine> lines) {
        this.lines = lines;
    }

    public TicketPrinterJob(String[] lines) {
        this(stringToLines(lines));
    }

    public void setLines(List<TicketPrinterJobLine> lines) {
        this.lines = lines;
    }

    public List<TicketPrinterJobLine> getLines() {
        return lines;
    }

    public Class getStrategy() {
        return strategy;
    }

    public void setStrategy(Class strategy) {
        this.strategy = strategy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Helper method for creating a list of text lines ready to be printed from
     * an array of string, each representing a line.
     * @param lines array of lines to be printed.
     * @return list of text lines.
     */
    public static List<TicketPrinterJobLine> stringToLines(String[] lines) {
        List<TicketPrinterJobLine> linez = new ArrayList<TicketPrinterJobLine>(lines.length);
        for (int i = 0; i < lines.length; i++) {
            linez.add(new TicketPrinterJobText(lines[i]));
        }

        return linez;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ticket:\n");
        for (TicketPrinterJobLine line : lines) {
            sb.append(line.toString()).append("\n");
        }
        
        return sb.toString();
    }

    /**
     * Save print job to a XML document.
     * @return XML document.
     */
    public Document toXML() {
        DOMImplementation impl = DOMImplementationImpl.getDOMImplementation();
        Document doc = impl.createDocument(null, "Impresion", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("version", Integer.toString(version));
        if(strategy != null) {
            root.setAttribute("strategyClass", strategy.getCanonicalName());
        }
        
        for(TicketPrinterJobLine line : lines) {
            root.appendChild(line.toXML(doc.createElement(line.getTagName())));
        }

        return doc;
    }

    /**
     * Save print job to a XML string.
     * @return xml String.
     */
    public String toXMLString() {
        Document doc = toXML();
        String xml = "";
        try {
            TransformerFactory xformFactory = TransformerFactory.newInstance();
            Transformer idTransform = xformFactory.newTransformer();
            Source input = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            Result output = new StreamResult(writer);
            idTransform.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            idTransform.transform(input, output);
            xml = writer.toString();
        } catch (TransformerException ex) {
            logger.error(ex, ex);
        }

        return xml;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.lines != null ? this.lines.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TicketPrinterJob other = (TicketPrinterJob) obj;
        
        if ((other.lines != null && lines == null) || (other.lines == null && lines != null)) {
            return false;
        } else if (other.lines.size() != lines.size()) {
            return false;
        }
        
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).equals(other.lines.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
}
