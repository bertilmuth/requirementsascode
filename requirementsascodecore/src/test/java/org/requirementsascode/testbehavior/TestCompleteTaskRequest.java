package org.requirementsascode.testbehavior;

import java.util.UUID;

public class TestCompleteTaskRequest {
  private final UUID todoListId;
  private final UUID taskId;
  private final String newTaskName;

  public TestCompleteTaskRequest(UUID todoListId, UUID taskId, String newTaskName) {
    this.todoListId = todoListId;
    this.taskId = taskId;
    this.newTaskName = newTaskName;
  }

  public UUID getTodoListId() {
    return todoListId;
  }

  public UUID getTaskId() {
    return taskId;
  }

  public String getNewTaskName() {
    return newTaskName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((newTaskName == null) ? 0 : newTaskName.hashCode());
    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
    result = prime * result + ((todoListId == null) ? 0 : todoListId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestCompleteTaskRequest other = (TestCompleteTaskRequest) obj;
    if (newTaskName == null) {
      if (other.newTaskName != null)
        return false;
    } else if (!newTaskName.equals(other.newTaskName))
      return false;
    if (taskId == null) {
      if (other.taskId != null)
        return false;
    } else if (!taskId.equals(other.taskId))
      return false;
    if (todoListId == null) {
      if (other.todoListId != null)
        return false;
    } else if (!todoListId.equals(other.todoListId))
      return false;
    return true;
  }
}
