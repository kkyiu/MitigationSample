package com.veracode.sample.apiwrapper.models;

import org.w3c.dom.Node;

/**
 * POJO of a flaw 
 *
 * ID is the only attribute for now. Additional attributes can be added in the future.
 */
public class Flaw {
    private final String ISSUE_ID = "issueid";

    // The flaw ID
    private final String ID;

    public Flaw(Node node) {
        if (null == node) {
            throw new IllegalArgumentException("Invalid XML node.");
        }
        ID = node.getAttributes().getNamedItem(ISSUE_ID).getNodeValue();
    }

    public String getID() {
        return ID;
    }
}
