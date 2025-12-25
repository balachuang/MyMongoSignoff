package bala.signoff.entity;

import java.util.List;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ProjectIssuetypeWorkflow
{
	@NonNull private List<ProjectIssuetypeWorkflowStatus> status;
	@NonNull private List<ProjectIssuetypeWorkflowAction> actions;
}
