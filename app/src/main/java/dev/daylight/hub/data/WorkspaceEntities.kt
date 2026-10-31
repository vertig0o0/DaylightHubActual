package dev.daylight.hub.data

import androidx.room.*

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val pinned: Boolean = false,
    val archived: Boolean = false,
    val sortOrder: Long = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val revision: Long = 0
)

@Entity(tableName = "task_dependencies", primaryKeys = ["taskId", "prerequisiteId"],
    indices = [Index("prerequisiteId")], foreignKeys = [
        ForeignKey(entity = PlannerItem::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = PlannerItem::class, parentColumns = ["id"], childColumns = ["prerequisiteId"], onDelete = ForeignKey.RESTRICT)
    ])
data class TaskDependency(val taskId: Long, val prerequisiteId: Long)

@Entity(tableName = "labels", indices = [Index(value = ["name"], unique = true)])
data class Label(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)

@Entity(tableName = "project_templates")
data class ProjectTemplate(@PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String, val payload: String, val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "conversations", indices = [Index("projectId")])
data class Conversation(@PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "New conversation", val projectId: Long? = null,
    val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "conversation_messages", indices = [Index("conversationId")], foreignKeys = [
    ForeignKey(entity = Conversation::class, parentColumns = ["id"], childColumns = ["conversationId"], onDelete = ForeignKey.CASCADE)
])
data class ConversationMessage(@PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long, val user: Boolean, val text: String,
    val createdAt: Long = System.currentTimeMillis())
