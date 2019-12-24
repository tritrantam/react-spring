package com.tritrantam.ppmtool.services;

import com.tritrantam.ppmtool.domain.Backlog;
import com.tritrantam.ppmtool.domain.Project;
import com.tritrantam.ppmtool.domain.User;
import com.tritrantam.ppmtool.exceptions.ProjectIdException;
import com.tritrantam.ppmtool.exceptions.ProjectNotFoundException;
import com.tritrantam.ppmtool.repository.BacklogRepository;
import com.tritrantam.ppmtool.repository.ProjectRepository;
import com.tritrantam.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: "+
                        project.getProjectIdentifier()+ " cannot be updated because it doesn't exist");
            }
        }
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try {

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

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

    public Project findProjectByIdentifier(String projectId, String username){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID '"+ projectId.toUpperCase()+"' does not exist");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project is not in your account");
        }
        return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username){
        projectRepository.delete(findProjectByIdentifier(projectId,username));
    }
}
