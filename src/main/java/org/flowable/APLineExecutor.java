package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APLineExecutor implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String lineString = (String) execution.getVariable("lines");
		try {
			parseJson(lineString, execution);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

	public static void parseJson(String jsonString, DelegateExecution execution) throws JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();
		JsonNode j = obj.readTree(jsonString);
		JsonNode line = j.get("line");
		String businessunit = line.get("businessunit").asText();
		String budescription = line.get("budescription").asText();
		String amount = line.get("amount").asText();
		String assignee = line.get("assignee")!=null ? line.get("assignee").asText() : null;
		String approvalLevelTill  = line.get("approvalLevelTill")!=null ? line.get("approvalLevelTill").asText() : null;
		
		execution.setVariable("businessunit", businessunit);
		execution.setVariable("budescription", budescription);
		execution.setVariable("amount", amount);
		execution.setVariable("assignee", assignee);
		execution.setVariable("approvalLevelTill", approvalLevelTill);
	}
}
