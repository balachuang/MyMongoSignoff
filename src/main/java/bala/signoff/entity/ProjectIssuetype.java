package bala.signoff.entity;

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
}
