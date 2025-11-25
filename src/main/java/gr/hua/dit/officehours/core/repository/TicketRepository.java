package gr.hua.dit.officehours.core.repository;

import gr.hua.dit.officehours.core.model.Ticket;

import gr.hua.dit.officehours.core.model.TicketStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

/**
 * Repository for {@link Ticket} entity.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByStudentId(long studentId);

    List<Ticket> findAllByTeacherId(long teacherId);

    List<Ticket> findByStatusAndQueuedAtBefore(final TicketStatus status, Instant before);

    boolean existsByStudentIdAndTeacherIdAndStatusIn(final long studentId, final long teacherId, final Collection<TicketStatus> statuses);

    long countByStudentIdAndStatusIn(final long studentId, final Collection<TicketStatus> statuses);
}
