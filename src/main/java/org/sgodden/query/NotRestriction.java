package org.sgodden.query;

/**
 * A restriction that negates the contained restriction
 * @author Lloyd Colling
 *
 */
public class NotRestriction implements Restriction {

    Restriction child;

    public NotRestriction(Restriction child) {
        this.child = child;
    }

    public Restriction getChild() {
        return child;
    }

    public void setChild(Restriction child) {
        this.child = child;
    }
    
    public String toString() {
    	return "not( " + child.toString() + " )";
    }
}
