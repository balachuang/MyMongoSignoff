package bala.signoff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bala.signoff.entity.Issue;
import bala.signoff.entity.Project;
import bala.signoff.repository.IssueRepo;
import bala.signoff.repository.ProjectRepo;
import lombok.extern.slf4j.Slf4j;


// http://localhost:9090/RetentionDemo/
@Slf4j
@Controller
@RequestMapping("/RetentionDemo")
public class UrlRouting
{
	@Autowired
    private IssueRepo issuesDao;

	@Autowired
    private ProjectRepo projectDao;


	@GetMapping({"/", "/home", "/index"})
	public String home(
		@RequestParam(name="name", required=false, defaultValue="World") String name, 
		Model model)
	{
		String data = "";

		List<Project> projects = projectDao.findAll();
		data += "Projects: " + projects.size() + "<br>";
		for (Project project : projects) data += "&nbsp;&nbsp;&nbsp;&nbsp; " + project.toString() + "<br>";

		data += "<br><br>";

		List<Issue> issues = issuesDao.findAll();
		data += "Issues: " + issues.size() + "<br>";
		for (Issue issue : issues) data += "&nbsp;&nbsp;&nbsp;&nbsp; " + issue.toString() + "<br>";

		model.addAttribute("dbdata", data);

		// refer to: /resources/templates/home.html
		return "home";
	}
}
