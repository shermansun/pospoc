package com.printer;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;

/**
 * Image to be printed.
 * @author nMoncho
 */
public class TicketPrinterJobImage extends TicketPrinterJobLine {
    
    public static final String IMAGEN_TAG_NAME = "Imagen";
    
    public static final String ATTR_URL = "url";
    
    private String url;
    private Image image;
    
    public TicketPrinterJobImage(String url) {
        this.url = url;
    }
    
    public TicketPrinterJobImage(String url, LineAlignEnum align) {
        this.url = url;
        setAlign(align);
    }
    /**
     * Loads image to this object (Lazy loading of image).
     * @return Image object.
     * @throws MalformedURLException
     * @throws IOException 
     */
    public Image loadImage() throws MalformedURLException, IOException {
        if (image == null) {
            image = ImageIO.read(new URL(url));
        }
        return image;
    }
    
    public Element toXML(Element element) {
        element.setAttribute(ATTR_URL, url);
        element.setAttribute(ATTR_ALIGN, align != null ? align.toString() : LineAlignEnum.LEFT.toString());

        return element;
    }

    @Override
    public String getTagName() {
        return IMAGEN_TAG_NAME;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.url != null ? this.url.hashCode() : 0);
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
        final TicketPrinterJobImage other = (TicketPrinterJobImage) obj;
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TicketPrinterJobImage{" + "url=" + url + '}';
    }
    
}
