package bala.signoff.entity;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ProjectIssuetype
{
	@NonNull  private String key;
	@NonNull  private String name;

	@NonNull private ProjectIssuetypeWorkflow workflow;
	@NonNull private List<ProjectIssuetypeCustfield> customfields;

	static public ProjectIssuetype createDefault()
	{
		ProjectIssuetypeWorkflow defaultWorkflow = ProjectIssuetypeWorkflow.createDefault();

		ProjectIssuetypeCustfield defaultCustfield = ProjectIssuetypeCustfield.createDefault();
		ArrayList<ProjectIssuetypeCustfield> defaultCustfields = new ArrayList<>();
		defaultCustfields.add(defaultCustfield);

		ProjectIssuetype defaultIssueType = new ProjectIssuetype("ISSUETYPE", "Issue Type", defaultWorkflow, defaultCustfields);
		return defaultIssueType;
	}

	public ProjectIssuetypeCustfield getCustFieldByKey(String key)
	{
		for (ProjectIssuetypeCustfield custField : this.customfields) {
			if (custField.getKey().equals(key)) return custField;
		}

		return null;
	}
}
