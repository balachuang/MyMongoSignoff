package bala.signoff.entity;

import java.util.ArrayList;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermission
{
	@NonNull   private ProjectPermissionRole admin;
	@Nullable  private ProjectPermissionRole creator;
	@Nullable  private ProjectPermissionRole editor;
	@Nullable  private ProjectPermissionRole viewer;


	static public ProjectPermission createDefault(String admin)
	{
		ArrayList<String> members = new ArrayList<>();
		members.add(admin);

		ProjectPermissionRole adminRole = new ProjectPermissionRole();
		adminRole.setMembers(members);

		ProjectPermission defaultPermission = new ProjectPermission();
		defaultPermission.setAdmin(adminRole);
		return defaultPermission;
	}
}
