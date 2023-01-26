package org.flowable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;

public class APApprovalExecutor implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String linesString = execution.getVariable("lines").toString();
		String approvalLevel = (String) execution.getVariable("approvallevel");
		String noOfLines = (String) execution.getVariable("noOfLines");
		if(approvalLevel == null) {
			approvalLevel = "1";
			execution.setVariable("approvallevel", "1");
			try {
				setLineAssigneeAndExecution(linesString, execution, Integer.parseInt(noOfLines), Integer.parseInt(approvalLevel));
				boolean isCurrentLevelApproved = executeApproval(linesString, execution , Integer.parseInt(noOfLines), Integer.parseInt(approvalLevel));
				if(isCurrentLevelApproved) {
					execution.setVariable("approvallevel", String.valueOf(Integer.parseInt(approvalLevel) + 1));
				}
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			List<Object> lineList = new ArrayList<Object>();
			generateJson(lineList,execution);
			execution.setVariable("lines", lineList);
			linesString = execution.getVariable("lines").toString();
			try {
				boolean isCurrentLevelApproved = executeApproval(linesString, execution , Integer.parseInt(noOfLines), Integer.parseInt(approvalLevel));
				if(isCurrentLevelApproved && Integer.parseInt(approvalLevel)<4) {
					execution.setVariable("approvallevel", String.valueOf(Integer.parseInt(approvalLevel) + 1));
					setCurrentLevelAssignee(linesString, execution, Integer.parseInt(noOfLines), (Integer.parseInt(approvalLevel)+1));
					setLineAssigneeAndExecution(linesString, execution, Integer.parseInt(noOfLines), (Integer.parseInt(approvalLevel)+1));
				}else if(isCurrentLevelApproved && Integer.parseInt(approvalLevel)==4) {
					execution.setVariable("approvallevel", "8");
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean executeApproval(String lineString, DelegateExecution execution,int noOfLines , int approvalLevel) throws JsonProcessingException {
		Boolean isCurrentLevelApproved = false;
		String currentAssignee = null;
		for(int i=0;i<noOfLines;i++) {
			JsonNode linesTree = parseLineJson(lineString,i);
			JsonNode line = linesTree.get("line"+ (i+1));
			String isLineApprovedAtCurrentLevel = line.get("line"+ (i+1) +"level" + approvalLevel +"approved").asText();
			if(!isLineApprovedAtCurrentLevel.equals("Yes")) {
				currentAssignee = line.get("line"+ (i+1) +"level" + approvalLevel +"assignee").asText();
				execution.setVariable("approvalassignee", currentAssignee);
				isCurrentLevelApproved = false;
				break;
			}else if((i+1) == noOfLines && isLineApprovedAtCurrentLevel.equals("Yes")) {
				isCurrentLevelApproved = true;
			}
		}
		return isCurrentLevelApproved;
	}
	
	public static void setCurrentLevelAssignee(String lineString, DelegateExecution execution,int noOfLines , int approvalLevel) throws JsonProcessingException {
		String currentAssignee = null;
		for(int i=0;i<noOfLines;i++) {
			JsonNode linesTree = parseLineJson(lineString,i);
			JsonNode line = linesTree.get("line"+ (i+1));
			String isLineApprovedAtCurrentLevel = line.get("line"+ (i+1) +"level" + approvalLevel +"approved").asText();
			if(!isLineApprovedAtCurrentLevel.equals("Yes")) {
				currentAssignee = line.get("line"+ (i+1) +"level" + approvalLevel +"assignee").asText();
				execution.setVariable("approvalassignee", currentAssignee);
				break;
			}
		}
	}
	
	public static void setLineAssigneeAndExecution(String lineString, DelegateExecution execution,int noOfLines , int approvalLevel) throws JsonProcessingException {
		for(int i=0;i<noOfLines;i++) {
			JsonNode linesTree = parseLineJson(lineString,i);
			JsonNode line = linesTree.get("line"+ (i+1));
			String lineApprovalString = line.get("line"+ (i+1) +"level" + approvalLevel +"approved").asText();
			execution.setVariable("line"+ (i+1) +"level" + approvalLevel +"approved", lineApprovalString);
			String lineAssignee = line.get("line"+ (i+1) +"level" + approvalLevel +"assignee").asText();
			execution.setVariable("line"+ (i+1) +"level" + approvalLevel +"assignee", lineAssignee);
		}
	}

	public static JsonNode parseLineJson(String lineString,int index) throws JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();
		JsonNode linesTree = obj.readTree(lineString);
		return linesTree.get(index);
	}
	
	public static List<Object> generateJson(List<Object> lines, DelegateExecution execution) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ObjectNode rootNode1 = mapper.createObjectNode();
		String jsonString = null;

		ObjectNode childNode1 = mapper.createObjectNode();
		childNode1.put("businessunit", (String) execution.getVariable("line1-businessunit"));
		childNode1.put("budescription",(String) execution.getVariable("line1-budescription"));
		childNode1.put("amount", (Integer) execution.getVariable("line1-amount"));
		childNode1.put("approvalLevelTill","2");
		childNode1.put("approvedTill", "0");
		childNode1.put("line1level1assignee", "piqnic");
		childNode1.put("line1level1approved", (String) execution.getVariable("line1level1approved"));
		childNode1.put("line1level2assignee", "piqnic");
		childNode1.put("line1level2approved", (String) execution.getVariable("line1level2approved"));
		childNode1.put("line1level3assignee", "piqnic");
		childNode1.put("line1level3approved", (String) execution.getVariable("line1level3approved"));
		childNode1.put("line1level4assignee", "piqnic");
		childNode1.put("line1level4approved", (String) execution.getVariable("line1level4approved"));

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
		childNode2.put("amount", (Integer) execution.getVariable("line2-amount"));
		childNode2.put("assignee","piqnic1");
		childNode2.put("approvalLevelTill","4");
		childNode2.put("approvedTill", "0");
		childNode2.put("line2level1assignee", "piqnic1");
		childNode2.put("line2level1approved", (String) execution.getVariable("line2level1approved"));
		childNode2.put("line2level2assignee", "piqni1");
		childNode2.put("line2level2approved", (String) execution.getVariable("line2level2approved"));
		childNode2.put("line2level3assignee", "piqnic1");
		childNode2.put("line2level3approved", (String) execution.getVariable("line2level3approved"));
		childNode2.put("line2level4assignee", "piqnic1");
		childNode2.put("line2level4approved", (String) execution.getVariable("line2level4approved"));
		
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
