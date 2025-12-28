package bala.signoff.entity;

// import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

// import lombok.AllArgsConstructor;
import lombok.Data;
// import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
// @NoArgsConstructor
// @Document(collection = "Issues")
public class UserProfile
{
	@Nullable private boolean isSystemDarkMode;
}
