package bala.signoff.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@Document(collection = "Issues")
public class Issue
{
	// @Id private String id;  //這個id物件是對映每個document在被新增時都會有的_id

	@NonNull private String key;
	@NonNull private String project;
	@NonNull private String issuetype;
	@NonNull private String title;
	@NonNull private String creator;
	@NonNull private LocalDateTime createdate;
	@NonNull private LocalDateTime updatedate;
	@NonNull private String assignee;
	@NonNull private String status;

	@Nullable private List<IssueCustfield> customfields;
}
