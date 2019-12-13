package com.tritrantam.ppmtool.services;

import com.tritrantam.ppmtool.domain.Backlog;
import com.tritrantam.ppmtool.domain.Project;
import com.tritrantam.ppmtool.exceptions.ProjectIdException;
import com.tritrantam.ppmtool.repository.BacklogRepository;
import com.tritrantam.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try {
            project.setProjectIdentifier(projectIdentifier);

            if (project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }

            if (project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+ projectIdentifier+"' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID '"+ projectId.toUpperCase()+"' does not exist");
        }
        return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = findProjectByIdentifier(projectId);
        if(project == null){
            throw new ProjectIdException("Cannot delete Project with ID '"+ projectId.toUpperCase()+"'.This project does not exist");
        }
        projectRepository.delete(project);
    }
}
