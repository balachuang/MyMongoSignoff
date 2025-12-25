package bala.signoff.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import bala.signoff.entity.Issue;


public interface IssueRepo extends MongoRepository<Issue, String>
{
	Optional<Issue> findIssueByKey(String key);
}
