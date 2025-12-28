package bala.signoff.entity;

// import java.util.ArrayList;
import java.util.List;
// import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionRole
{
	@Nullable private List<String> members;
	@Nullable private List<String> groups;

	public boolean contains(User user)
	{
		if (members.contains(user.getAccount())) return false;

		// to-do: check group

		return false;
	}
}
