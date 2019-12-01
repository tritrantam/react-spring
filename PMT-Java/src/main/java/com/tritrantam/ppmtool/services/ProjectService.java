package com.tritrantam.ppmtool.services;

import com.tritrantam.ppmtool.domain.Project;
import com.tritrantam.ppmtool.exceptions.ProjectIdException;
import com.tritrantam.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier().toUpperCase()+"' already exists");
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
