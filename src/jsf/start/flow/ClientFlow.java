package jsf.start.flow;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.*;

public class ClientFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        System.out.println("============================= Flow builder is working");
        String flowId = "ClientFlow";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml")
                   .markAsStartNode();
        flowBuilder.returnNode("expireSession").fromOutcome("/expired");
        return flowBuilder.getFlow();
    }
}
