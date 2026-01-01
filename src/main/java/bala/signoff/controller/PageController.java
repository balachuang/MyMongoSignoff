package bala.signoff.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bala.signoff.entity.Issue;
import bala.signoff.entity.IssueCustfield;
import bala.signoff.entity.IssueCustfieldDisplay;
import bala.signoff.entity.Project;
import bala.signoff.entity.ProjectIssuetype;
import bala.signoff.entity.ProjectIssuetypeCustfield;
import bala.signoff.entity.User;
import bala.signoff.repository.IssueRepo;
import bala.signoff.repository.ProjectRepo;
import bala.signoff.service.PermissionService;
import lombok.extern.slf4j.Slf4j;


// http://localhost:9090/MySignoff/
@Slf4j
@Controller
@RequestMapping("/MySignoff")
public class PageController
{
	@Autowired
    private IssueRepo issuesDao;

	@Autowired
    private ProjectRepo projectDao;


	@GetMapping({"/", "/home", "/index"})
	public String home(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
	{
		User user = PermissionService.getCurrentLoginUser();

		// filter projects by current login user
		List<Project> projectCandidates = projectDao.findAll();
		ArrayList<Project> projects = new ArrayList<>();
		for (Project proj : projectCandidates) if (user.canBeViewerOf(proj)) projects.add(proj);
		model.addAttribute("projects", projects);

		// refer to: /resources/templates/home.html
		return "home";
	}

	@GetMapping("/project/{key}")
	public String project(@PathVariable String key, Model model)
	{
		User user = PermissionService.getCurrentLoginUser();

		Project project = projectDao.findProjectByKey(key).orElse(null);
		if (!user.canBeViewerOf(project)) project = null;

		String projKey = (project == null) ? null : project.getKey();
		List<Issue> issues = issuesDao.findIssuesByProject(projKey).orElse(null);

		model.addAttribute("project", project);
		model.addAttribute("issues", issues);
		return "project";
	}

	@GetMapping("/issue/{key}")
	public String issue(@PathVariable String key, Model model)
	{
		User user = PermissionService.getCurrentLoginUser();

		// get issue
		Issue issue = issuesDao.findIssueByKey(key).orElse(null);
		if (issue == null) return "error";

		// get project
		Project project = projectDao.findProjectByKey(issue.getProject()).orElse(null);
		if (!user.canBeViewerOf(project)) return "error";

		// generate cust. field display by name/value
		ProjectIssuetype thisIssueType = project.getIssueTypeByKey(issue.getIssuetype());
		if (thisIssueType == null) {
			logger.error("ERROR: Issue Type not in Project !!!");
			return "error";
		}

		ArrayList<IssueCustfieldDisplay> custFielValues = new ArrayList<>();
		for(IssueCustfield cust : issue.getCustomfields())
		{
			ProjectIssuetypeCustfield cfInfo = thisIssueType.getCustFieldByKey(cust.getKey());
			if (cfInfo == null) {
				logger.error("ERROR: Custom Field not in Issue Type !!!");
				return "error";
			}
			custFielValues.add(new IssueCustfieldDisplay(cfInfo.getName(), cust.getValue()));
		}

		model.addAttribute("project", project);
		model.addAttribute("issue", issue);
		model.addAttribute("cfvalues", custFielValues);
		return "issue";
	}

	// create a new project
	@PostMapping("/project/new")
	public String project(@PathVariable String key, @PathVariable String name, @PathVariable String creator, Model model)
	{
		Project defaultProj = Project.createDefault(key, name, creator);
		projectDao.save(defaultProj);

		return project(key, model);
	}
}
