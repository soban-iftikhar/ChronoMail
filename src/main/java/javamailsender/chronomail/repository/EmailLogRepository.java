package javamailsender.chronomail.repository;

import javamailsender.chronomail.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findByRecipientEmail(String recipientEmail);
    List<EmailLog> findByStatusAndSentTimeBetween(String status, LocalDateTime start, LocalDateTime end);
    List<EmailLog> findByStatus(String status);
}
