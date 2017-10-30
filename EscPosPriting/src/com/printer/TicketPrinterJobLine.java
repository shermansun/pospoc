package com.printer;

import java.io.Serializable;
import org.w3c.dom.Element;

/**
 * Abstract class representing a line in a print job.
 * @author nMoncho
 */
public abstract class TicketPrinterJobLine implements Serializable {

    public static final String ATTR_ALIGN = "align";
    
    protected LineAlignEnum align = LineAlignEnum.LEFT;

    public LineAlignEnum getAlign() {
        return align;
    }

    public void setAlign(LineAlignEnum align) {
        this.align = align;
    }

    public abstract Element toXML(Element element);
    
    public abstract String getTagName();
}
