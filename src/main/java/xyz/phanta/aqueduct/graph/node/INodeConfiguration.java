package xyz.phanta.aqueduct.graph.node;

import xyz.phanta.aqueduct.graph.builder.IConnectable;

import java.util.Set;

public interface INodeConfiguration extends IConnectable {

    INodeConfiguration withAttribs(Set<NodeAttribute> attribs);

}
