package xyz.phanta.aqueduct.engine;

import xyz.phanta.aqueduct.graph.edge.EdgeMode;
import xyz.phanta.aqueduct.graph.node.NodeAttribute;

import java.util.Set;

public interface IPolicies {

    IPolicies withDefaultNodeAttributes(Set<NodeAttribute> attribs);

    IPolicies withDefaultEdgeMode(EdgeMode mode);

}
