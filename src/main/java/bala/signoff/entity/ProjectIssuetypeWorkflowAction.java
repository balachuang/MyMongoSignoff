package bala.signoff.entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectIssuetypeWorkflowAction
{
	@NonNull private String name = "";
	@NonNull private String from = "";
	@NonNull private String to = "";

	static public ProjectIssuetypeWorkflowAction createDefault()
	{
		ProjectIssuetypeWorkflowAction defaultAction = new ProjectIssuetypeWorkflowAction();
		defaultAction.setFrom("Start");
		defaultAction.setTo("Close");
		defaultAction.setName("Approve");
		return defaultAction;
	}
}
