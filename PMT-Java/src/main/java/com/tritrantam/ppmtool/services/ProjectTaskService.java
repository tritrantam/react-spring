package com.tritrantam.ppmtool.services;

import com.tritrantam.ppmtool.domain.Backlog;
import com.tritrantam.ppmtool.domain.Project;
import com.tritrantam.ppmtool.domain.ProjectTask;
import com.tritrantam.ppmtool.exceptions.ProjectNotFoundException;
import com.tritrantam.ppmtool.repository.BacklogRepository;
import com.tritrantam.ppmtool.repository.ProjectRepository;
import com.tritrantam.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            // PTs to be added to a specific project, project != null, BL exists
            //Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

            // set the BL to PT
            projectTask.setBacklog(backlog);
            // we want our project sequence to be like this: PROJ-1,PROJ-2.....PROJ-100
            Integer backlogSequence = backlog.getPTSequence();

            // Update the BL sequence
            backlogSequence++;

            backlog.setPTSequence(backlogSequence);

            //Add sequence to PT
            projectTask.setProjectSequence(projectIdentifier+"-"+ backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            // initial priority when priority null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }
            // initial status when status null
            if (projectTask.getStatus() == null ||Objects.equals(projectTask.getStatus(), "") ){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);

    }

    public List<ProjectTask> findBacklogById(String backlog_id, String username) {
        projectService.findProjectByIdentifier(backlog_id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence (String backlog_id, String pt_id, String username){
        projectService.findProjectByIdentifier(backlog_id,username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null){
            throw new ProjectNotFoundException("Project Task : '"+pt_id+"' does not exist");
        }
        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task "+ pt_id+" does not exist in project: "+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
