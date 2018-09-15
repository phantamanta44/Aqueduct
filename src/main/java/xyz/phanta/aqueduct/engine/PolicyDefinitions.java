package xyz.phanta.aqueduct.engine;

import xyz.phanta.aqueduct.graph.edge.EdgeMode;
import xyz.phanta.aqueduct.graph.node.NodeAttribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class PolicyDefinitions implements IPolicies {

    private Set<NodeAttribute> defaultNodeAttribs = EnumSet.noneOf(NodeAttribute.class);
    private EdgeMode defaultEdgeMode = EdgeMode.TRANSFER_ALL;

    @Override
    public IPolicies withDefaultNodeAttributes(Set<NodeAttribute> attribs) {
        this.defaultNodeAttribs = Collections.unmodifiableSet(attribs);
        return this;
    }

    @Override
    public IPolicies withDefaultEdgeMode(EdgeMode mode) {
        this.defaultEdgeMode = mode;
        return this;
    }

    public Set<NodeAttribute> getDefaultNodeAttribs() {
        return defaultNodeAttribs;
    }

    public EdgeMode getDefaultEdgeMode() {
        return defaultEdgeMode;
    }

}
