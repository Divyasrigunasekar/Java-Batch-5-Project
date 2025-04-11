package model;
import java.sql.Date;
public class Project {
    private int id;
    private String projectName;
    private String description;
    private Date startDate;
    private String status;

    public Project() {} 
    public Project(int id, String projectName, String description, Date startDate, String status) 
    {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.status = status;
    }
    
    public int getId() 
    { 
    	return id; 
    }
    public void setId(int id)
    { 
    	this.id = id; 
    }
    public String getProjectName() 
    { 
    	return projectName; 
    }
    public void setProjectName(String projectName)
    { 
    	this.projectName = projectName; 
    }
    public String getDescription()
    { 
    	return description;
    }
    public void setDescription(String description)
    { 
    	this.description = description;
    }
    public Date getStartDate() 
    { 
    	return startDate;
    }
    public void setStartDate(Date startDate)
    { 
    	this.startDate = startDate;
    }
    public String getStatus()
    {
    	return status;
    }
    public void setStatus(String status) 
    { 
    	this.status = status; 
    }
}