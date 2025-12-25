package bala.signoff.entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ProjectIssuetypeWorkflowStatus
{
	@NonNull private String key;
	@NonNull private String name;
}
