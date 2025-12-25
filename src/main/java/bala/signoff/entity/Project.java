package bala.signoff.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
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
	@NonNull private List<ProjectPermission> permissions;
}
