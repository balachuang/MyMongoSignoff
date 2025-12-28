package bala.signoff.entity;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectIssuetypeWorkflow
{
	@NonNull private List<ProjectIssuetypeWorkflowStatus> status = new ArrayList<>();
	@NonNull private List<ProjectIssuetypeWorkflowAction> actions = new ArrayList<>();

	static public ProjectIssuetypeWorkflow createDefault()
	{
		ProjectIssuetypeWorkflowStatus statusStart = new ProjectIssuetypeWorkflowStatus("START", "Start");
		ProjectIssuetypeWorkflowStatus statusEnd = new ProjectIssuetypeWorkflowStatus("END", "End");
		ArrayList<ProjectIssuetypeWorkflowStatus> defaultStatus = new ArrayList<>();
		defaultStatus.add(statusStart);
		defaultStatus.add(statusEnd);

		ProjectIssuetypeWorkflowAction action = new ProjectIssuetypeWorkflowAction("Approve", "START", "END");
		ArrayList<ProjectIssuetypeWorkflowAction> defaultAction = new ArrayList<>();
		defaultAction.add(action);

		ProjectIssuetypeWorkflow defaultWorkflow = new ProjectIssuetypeWorkflow();
		defaultWorkflow.setStatus(defaultStatus);
		defaultWorkflow.setActions(defaultAction);
		return defaultWorkflow;
	}
}
