package gr.hua.dit.officehours.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Ticket entity.
 */
@Entity
@Table(
    name = "ticket",
    indexes = {
        @Index(name = "idx_ticket_status", columnList = "status"),
        @Index(name = "idx_ticket_student", columnList = "student_id"),
        @Index(name = "idx_ticket_teacher", columnList = "teacher_id"),
        @Index(name = "idx_ticket_queued_at", columnList = "queued_at"),
    }
)
public final class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_student"))
    private Person student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_teacher"))
    private Person teacher;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private TicketStatus status;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "subject", length = 255)
    private String subject;

    @NotNull
    @NotBlank
    @Size(max = 1000)
    @Column(name = "student_content", length = 1000)
    private String studentContent;

    // It can be null.
    @Size(max = 1000)
    @Column(name = "teacher_content", length = 1000)
    private String teacherContent;

    @CreationTimestamp
    @Column(name = "queued_at", nullable = false, updatable = false)
    private Instant queuedAt;

    @Column(name = "in_progress_at")
    private Instant inProgressAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    public Ticket() {
    }

    public Ticket(Long id,
                  Person student,
                  Person teacher,
                  TicketStatus status,
                  String subject,
                  String studentContent,
                  String teacherContent,
                  Instant queuedAt,
                  Instant inProgressAt,
                  Instant completedAt) {
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.status = status;
        this.subject = subject;
        this.studentContent = studentContent;
        this.teacherContent = teacherContent;
        this.queuedAt = queuedAt;
        this.inProgressAt = inProgressAt;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentContent() {
        return studentContent;
    }

    public void setStudentContent(String studentContent) {
        this.studentContent = studentContent;
    }

    public String getTeacherContent() {
        return teacherContent;
    }

    public void setTeacherContent(String teacherContent) {
        this.teacherContent = teacherContent;
    }

    public Instant getQueuedAt() {
        return queuedAt;
    }

    public void setQueuedAt(Instant queuedAt) {
        this.queuedAt = queuedAt;
    }

    public Instant getInProgressAt() {
        return inProgressAt;
    }

    public void setInProgressAt(Instant inProgressAt) {
        this.inProgressAt = inProgressAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ticket{");
        sb.append("id=").append(id);
        sb.append(", student=").append(student);
        sb.append(", teacher=").append(teacher);
        sb.append(", status=").append(status);
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", studentContent='").append(studentContent).append('\'');
        sb.append(", teacherContent='").append(teacherContent).append('\'');
        sb.append(", queuedAt=").append(queuedAt);
        sb.append(", inProgressAt=").append(inProgressAt);
        sb.append(", completedAt=").append(completedAt);
        sb.append('}');
        return sb.toString();
    }
}
