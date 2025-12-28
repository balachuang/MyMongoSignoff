package bala.signoff.entity;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@ToString
@NoArgsConstructor
@AllArgsConstructor
// @Document(collection = "Issues")
public class User
{
	@NonNull private String account;
	@NonNull private String name;
	@Nullable private UserProfile profile;

	public boolean canBeAdminOf(Project project)
	{
		ProjectPermission perm = project.getPermissions();
		if (perm.getAdmin().contains(this)) return true;
		return false;
	}

	public boolean canBeCreatorOf(Project project)
	{
		ProjectPermission perm = project.getPermissions();
		if (perm.getAdmin().getMembers().indexOf(account) >= 0) return true;
		if ((perm.getCreator() != null) && perm.getCreator().contains(this)) return true;
		return false;
	}

	public boolean canBeEditorOf(Project project)
	{
		ProjectPermission perm = project.getPermissions();
		if (perm.getAdmin().getMembers().indexOf(account) >= 0) return true;
		if ((perm.getCreator() != null) && perm.getCreator().contains(this)) return true;
		if ((perm.getEditor() != null) && perm.getEditor().contains(this)) return true;
		return false;
	}

	public boolean canBeViewerOf(Project project)
	{
		ProjectPermission perm = project.getPermissions();
		if (perm.getAdmin().getMembers().indexOf(account) >= 0) return true;
		if ((perm.getCreator() != null) && perm.getCreator().contains(this)) return true;
		if ((perm.getEditor() != null) && perm.getEditor().contains(this)) return true;
		if ((perm.getViewer() != null) && perm.getViewer().contains(this)) return true;
		return false;
	}
}
