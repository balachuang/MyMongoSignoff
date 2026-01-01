package bala.signoff.entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class IssueCustfieldDisplay
{
	@NonNull private String name;
	@NonNull private String value;
}
