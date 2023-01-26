package org.flowable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class APBuAndCodingExecutor implements JavaDelegate {

	Random random = new Random();

	public void execute(DelegateExecution execution) {

		Integer randomInt = this.random.nextInt(1000);
		List<String> lineList = new ArrayList<String>();
		
		if(randomInt%2==0) {
			execution.setVariable("isCodingManual", false);
			generateJson(lineList);
			execution.setVariable("noOfLines", "2");
			for(int i = 0; i<lineList.size(); i++) {
				try {
					setExecutionVariables(lineList.get(i), execution, i);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			execution.setVariable("lines", lineList);
		}else {
			execution.setVariable("isCodingManual", true);
		}
		
	}
	
	public static List<String> generateJson(List<String> lines) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ObjectNode rootNode1 = mapper.createObjectNode();
		String jsonString = null;

		ObjectNode childNode1 = mapper.createObjectNode();
		childNode1.put("businessunit", "00215000");
		childNode1.put("budescription", "W&H Financial Shared Services");
		childNode1.put("amount", "10.00");
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
		childNode2.put("businessunit", "00215005");
		childNode2.put("budescription", "GM Financial Shared Services");
		childNode2.put("amount", "15.00");
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
	
	public static void setExecutionVariables(String jsonString, DelegateExecution execution , int index) throws JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();
		JsonNode j = obj.readTree(jsonString);
		if(index==0) {
			JsonNode line = j.get("line1");
			String businessunit = line.get("businessunit").asText();
			String budescription = line.get("budescription").asText();
			String amount = line.get("amount").asText();
			execution.setVariable("line1-businessunit", businessunit);
			execution.setVariable("line1-budescription", budescription);
			execution.setVariable("line1-amount", amount);
		}else {
			JsonNode line = j.get("line2");
			String businessunit = line.get("businessunit").asText();
			String budescription = line.get("budescription").asText();
			String amount = line.get("amount").asText();
			execution.setVariable("line2-businessunit", businessunit);
			execution.setVariable("line2-budescription", budescription);
			execution.setVariable("line2-amount", amount);
		}
		
	}
}
