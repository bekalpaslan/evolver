package com.evolver.context;

import java.util.List;

/**
 * A section of context organized by type
 */
public class ContextSection {
    private final ContextType type;
    private final String content;
    private final List<ContextFragment> fragments;

    public ContextSection(ContextType type, String content, List<ContextFragment> fragments) {
        this.type = type;
        this.content = content;
        this.fragments = fragments;
    }

    public ContextType getType() { return type; }
    public String getContent() { return content; }
    public List<ContextFragment> getFragments() { return fragments; }
}
