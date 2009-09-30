package org.sgodden.query;

import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Transforms result sets to XML.
 * 
 * @author sgodden
 * 
 */
public class ResultSetFormatter {

    private ResultSetCellRenderer renderer;

    public ResultSetFormatter() {
        renderer = new DefaultResultSetCellRenderer();
    }

    /**
     * Sets the cell renderer to be used to render particular cell values.
     * 
     * @param renderer
     *            the renderer to use.
     */
    public void setCellRenderer(ResultSetCellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Adds the results of a query to a root xml element.
     * 
     * @param rs
     *            the result set to execute
     * @param root
     *            the root element to add new elements to
     */
    private void toXml(ResultSet rs, Element root) {

        Query q = rs.getQuery();
        for (int row = 0; row < rs.getRowCount(); row++) {
            ResultSetRow rsrow = rs.getRow(row);
            Element rowE = new Element(getLastPart(q.getObjectClassName()));
            root.addContent(rowE);
            for (int col = 0; col < rsrow.getColumns().length; col++) {
                ResultSetColumn column = rsrow.getColumns()[col];
                QueryColumn qc = rs.getQuery().getColumns().get(col);
                Element colE = new Element(legaliseColumnName(qc
                        .getAttributePath()));
                Object value = column.getValue();
                if (value != null) {
                    colE.setText(renderer.renderString(rs, value, col, row));
                }
                rowE.addContent(colE);
            }
        }
    }

    /**
     * Transforms the passed result sets to XML.
     * 
     * @param out
     *            the output stream to which to write the XML.
     * @param pretty
     *            whether to prettify the returned XML.
     * 
     * @param resultSets
     *            the result sets to transform.
     */
    public void toXml(OutputStream out, boolean pretty, ResultSet... resultSets) {
        Element root = new Element("ResultSet");
        Document doc = new Document(root);

        for (ResultSet resultSet : resultSets) {
            toXml(resultSet, root);
        }

        XMLOutputter xmlout = new XMLOutputter();
        if (pretty) {
            xmlout.setFormat(Format.getPrettyFormat());
        }
        try {
            xmlout.output(doc, out);
        } catch (Exception e) {
            throw new RuntimeException("Error generating XML", e);
        }
    }

    private String legaliseColumnName(String columnName) {
        return columnName.replaceAll(" ", "");
    }

    private String getLastPart(String s) {
        String ret = s;
        if (ret.indexOf('.') != -1) {
            ret = ret.substring(ret.lastIndexOf('.') + 1, ret.length());
        }
        return ret;
    }

}
