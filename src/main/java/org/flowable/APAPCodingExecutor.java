package org.flowable;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class APAPCodingExecutor implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		List<Object> lineList = new ArrayList<Object>();
		generateJson(lineList,execution);
		execution.setVariable("lines", lineList);
		execution.setVariable("noOfLines", "2");
	}
	
	public static List<Object> generateJson(List<Object> lines, DelegateExecution execution) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ObjectNode rootNode1 = mapper.createObjectNode();
		String jsonString = null;

		ObjectNode childNode1 = mapper.createObjectNode();
		childNode1.put("businessunit", (String) execution.getVariable("line1-businessunit"));
		childNode1.put("budescription",(String) execution.getVariable("line1-budescription"));
		childNode1.put("amount", (Double) execution.getVariable("line1-amount"));
		childNode1.put("approvalLevelTill","2");
		childNode1.put("approvedTill", "0");
		childNode1.put("line1level1assignee", "piqnic");
		childNode1.put("line1level1approved", "No");
		childNode1.put("line1level2assignee", "piqnic");
		childNode1.put("line1level2approved", "No");
		childNode1.put("line1level3assignee", "piqnic");
		childNode1.put("line1level3approved", "No");
		childNode1.put("line1level4assignee", "piqnic");
		childNode1.put("line1level4approved", "No");

		rootNode.set("line1", childNode1);
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
			lines.add(jsonString);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		ObjectNode childNode2 = mapper.createObjectNode();
		childNode2.put("businessunit", (String) execution.getVariable("line2-businessunit"));
		childNode2.put("budescription",(String) execution.getVariable("line2-budescription"));
		childNode2.put("amount", (Double) execution.getVariable("line2-amount"));
		childNode2.put("assignee","piqnic1");
		childNode2.put("approvalLevelTill","4");
		childNode2.put("approvedTill", "0");
		childNode2.put("line2level1assignee", "piqnic1");
		childNode2.put("line2level1approved", "No");
		childNode2.put("line2level2assignee", "piqni1");
		childNode2.put("line2level2approved", "No");
		childNode2.put("line2level3assignee", "piqnic1");
		childNode2.put("line2level3approved", "No");
		childNode2.put("line2level4assignee", "piqnic1");
		childNode2.put("line2level4approved", "No");
		
		rootNode1.set("line2", childNode2);
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode1);
			lines.add(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return lines;
	}

}
