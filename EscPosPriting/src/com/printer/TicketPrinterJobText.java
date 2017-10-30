package com.printer;

import org.w3c.dom.Element;
/**
 * Text line.
 * @author nMoncho
 */
public class TicketPrinterJobText extends TicketPrinterJobLine {
    
    public static final String LINEA_TAG_NAME = "Linea";
    
    public static final String ATTR_TEXTO = "texto";
    public static final String ATTR_FUENTE = "fuente";
    public static final String ATTR_SIZE = "size";
    public static final String ATTR_BOLD = "bold";
    public static final String ATTR_ITALIC = "italic";
    
    public static final String TRUE_VALUE = "Y";
    public static final String FALSE_VALUE = "N";
    
    private String text;
    private String font;
    private int size;
    private boolean bold;
    private boolean italic;

    public TicketPrinterJobText(String text, String font, int size
            , boolean bold, boolean italic, LineAlignEnum align) {
        this.text = text;
        this.font = font;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.align = align;
    }

    public TicketPrinterJobText(String text) {
        this(text, "Arial", 12, false, false, LineAlignEnum.LEFT);
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Element toXML(Element element) {
        element.setAttribute(ATTR_TEXTO, text);
        element.setAttribute(ATTR_FUENTE, font);
        element.setAttribute(ATTR_SIZE, Integer.toString(size));
        element.setAttribute(ATTR_BOLD, bold ? TRUE_VALUE : FALSE_VALUE);
        element.setAttribute(ATTR_ITALIC, italic ? TRUE_VALUE : FALSE_VALUE);
        element.setAttribute(ATTR_ALIGN, align != null ? align.toString() : LineAlignEnum.LEFT.toString());

        return element;
    }

    @Override
    public String getTagName() {
        return LINEA_TAG_NAME;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.text != null ? this.text.hashCode() : 0);
        hash = 29 * hash + (this.font != null ? this.font.hashCode() : 0);
        hash = 29 * hash + this.size;
        hash = 29 * hash + (this.bold ? 1 : 0);
        hash = 29 * hash + (this.italic ? 1 : 0);
        hash = 29 * hash + (this.align != null ? this.align.hashCode() : 0);
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
        final TicketPrinterJobText other = (TicketPrinterJobText) obj;
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        if ((this.font == null) ? (other.font != null) : !this.font.equals(other.font)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (this.bold != other.bold) {
            return false;
        }
        if (this.italic != other.italic) {
            return false;
        }
        if (this.align != other.align) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (bold) {
            sb.append("[b]");
        }
        if (italic) {
            sb.append("[i]");
        }
        sb.append(text);
        if (bold) {
            sb.append("[/b]");
        }
        if (italic) {
            sb.append("[/i]");
        }
        
        return sb.toString();
    }
}
