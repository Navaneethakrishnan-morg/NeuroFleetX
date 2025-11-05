package com.neurofleetx.repository;

import com.neurofleetx.model.SupportTicket;
import com.neurofleetx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
    List<SupportTicket> findByStatus(SupportTicket.TicketStatus status);
    List<SupportTicket> findByAssignedToOrderByCreatedAtDesc(User assignedTo);
    List<SupportTicket> findByPriorityOrderByCreatedAtDesc(SupportTicket.TicketPriority priority);
    List<SupportTicket> findByCategoryOrderByCreatedAtDesc(SupportTicket.TicketCategory category);
    List<SupportTicket> findAllByOrderByCreatedAtDesc();
}
