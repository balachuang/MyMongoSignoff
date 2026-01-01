package bala.signoff.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@ToString
@AllArgsConstructor
@Document(collection = "Projects")
public class Project
{
	// @Id private String id;  //這個id物件是對映每個document在被新增時都會有的_id
	@NonNull private String key;
	@NonNull private String name;
	@NonNull private String creater;
	@NonNull private LocalDateTime createdate;

	@NonNull private List<ProjectIssuetype> issuetypes;
	@NonNull private ProjectPermission permissions;

	static public Project createDefault(String key, String name, String creator)
	{
		ProjectIssuetype defaultIssuetype = ProjectIssuetype.createDefault();
		ArrayList<ProjectIssuetype> defaultIssuetypes = new ArrayList<>();
		defaultIssuetypes.add(defaultIssuetype);

		ProjectPermission defaultPermission = ProjectPermission.createDefault(creator);

		Project defaultProject = new Project(key, name, creator, LocalDateTime.now(), defaultIssuetypes, defaultPermission);
		return defaultProject;
	}

	public ProjectIssuetype getIssueTypeByKey(String key)
	{
		for (ProjectIssuetype issType : this.issuetypes) {
			if (issType.getKey().equals(key)) return issType;
		}

		return null;
	}
}
