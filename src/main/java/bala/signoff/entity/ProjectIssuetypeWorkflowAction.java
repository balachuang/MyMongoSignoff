package bala.signoff.entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ProjectIssuetypeWorkflowAction
{
	@NonNull private String name;
	@NonNull private String from;
	@NonNull private String to;
}
