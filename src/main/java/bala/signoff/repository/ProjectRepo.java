package bala.signoff.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import bala.signoff.entity.Project;


public interface ProjectRepo extends MongoRepository<Project, String>
{
	Optional<Project> findProjectByKey(String key);
}
