package bala.signoff.entity;

import java.util.List;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ProjectPermission
{
	@NonNull  private String role;
	@Nullable private List<String> members;
	@Nullable private List<String> groups;
}
