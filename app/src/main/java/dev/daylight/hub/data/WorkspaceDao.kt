package dev.daylight.hub.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM projects ORDER BY archived, pinned DESC, sortOrder, name")
    fun projects(): Flow<List<Project>>
    @Query("SELECT * FROM projects WHERE id=:id") suspend fun project(id: Long): Project?
    @Insert suspend fun insertProject(project: Project): Long
    @Update suspend fun updateProject(project: Project)
    @Query("DELETE FROM projects WHERE id=:id") suspend fun deleteProject(id: Long)
    @Query("UPDATE planner_items SET projectId=NULL, revision=revision+1 WHERE projectId=:id") suspend fun unassignTasks(id: Long)
    @Query("UPDATE notes SET projectId=NULL, revision=revision+1 WHERE projectId=:id") suspend fun unassignNotes(id: Long)
    @Query("UPDATE conversations SET projectId=NULL WHERE projectId=:id") suspend fun unassignConversations(id: Long)
    @Query("SELECT * FROM task_dependencies") fun dependencies(): Flow<List<TaskDependency>>
    @Query("SELECT * FROM task_dependencies") suspend fun allDependencies(): List<TaskDependency>
    @Query("DELETE FROM task_dependencies WHERE taskId=:id") suspend fun clearDependencies(id: Long)
    @Insert suspend fun insertDependencies(edges: List<TaskDependency>)
    @Query("SELECT * FROM planner_items WHERE deletedAt IS NULL AND isDone=0 AND manualBlocker='' AND NOT EXISTS (SELECT 1 FROM task_dependencies d LEFT JOIN planner_items p ON p.id=d.prerequisiteId WHERE d.taskId=planner_items.id AND (p.id IS NULL OR p.deletedAt IS NOT NULL OR p.isDone=0)) ORDER BY priority DESC, dueAt IS NULL, dueAt, sortOrder, id")
    fun readyTasks(): Flow<List<PlannerItem>>
    @Query("SELECT * FROM labels ORDER BY name") fun labels(): Flow<List<Label>>
    @Query("SELECT * FROM labels WHERE id=:id") suspend fun label(id: Long): Label?
    @Insert suspend fun insertLabel(label: Label): Long
    @Query("SELECT * FROM project_templates ORDER BY name") fun templates(): Flow<List<ProjectTemplate>>
    @Query("SELECT * FROM project_templates WHERE id=:id") suspend fun template(id: Long): ProjectTemplate?
    @Insert suspend fun insertTemplate(template: ProjectTemplate): Long
    @Query("SELECT COUNT(*) FROM planner_items WHERE projectId=:projectId AND deletedAt IS NULL") suspend fun templateTaskCount(projectId:Long):Int
    @Query("SELECT COALESCE(SUM(LENGTH(title)+LENGTH(details)+LENGTH(manualBlocker)),0) FROM planner_items WHERE projectId=:projectId AND deletedAt IS NULL") suspend fun templateTaskCharacters(projectId:Long):Long
    @Query("SELECT COALESCE(SUM(LENGTH(title)+LENGTH(body)+LENGTH(document)+LENGTH(tags)+LENGTH(notebook)),0) FROM notes WHERE projectId=:projectId AND deletedAt IS NULL") suspend fun templateNoteCharacters(projectId:Long):Long
    @Query("SELECT * FROM planner_items WHERE projectId=:projectId AND deletedAt IS NULL ORDER BY sortOrder,id LIMIT 1001") suspend fun templateTasks(projectId:Long):List<PlannerItem>
    @Query("SELECT * FROM notes WHERE projectId=:projectId AND deletedAt IS NULL ORDER BY id LIMIT 201") suspend fun templateNotes(projectId:Long):List<Note>
    @Query("SELECT d.* FROM task_dependencies d JOIN planner_items p ON p.id=d.taskId WHERE p.projectId=:projectId AND p.deletedAt IS NULL LIMIT 5001") suspend fun templateEdges(projectId:Long):List<TaskDependency>
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC") fun conversations(): Flow<List<Conversation>>
    @Query("SELECT * FROM conversations WHERE id=:id") suspend fun conversation(id: Long): Conversation?
    @Insert suspend fun insertConversation(conversation: Conversation): Long
    @Update suspend fun updateConversation(conversation: Conversation)
    @Query("SELECT * FROM conversation_messages WHERE conversationId=:id ORDER BY id") suspend fun messages(id: Long): List<ConversationMessage>
    @Query("SELECT * FROM conversation_messages WHERE conversationId=:id ORDER BY id DESC LIMIT :limit") suspend fun recentMessages(id:Long,limit:Int):List<ConversationMessage>
    @Query("SELECT * FROM conversation_messages WHERE conversationId=:id AND id<:before ORDER BY id DESC LIMIT :limit") suspend fun messagesBefore(id:Long,before:Long,limit:Int):List<ConversationMessage>
    @Insert suspend fun insertMessage(message: ConversationMessage): Long
}
