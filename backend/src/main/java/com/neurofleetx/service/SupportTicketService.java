package com.neurofleetx.service;

import com.neurofleetx.model.SupportTicket;
import com.neurofleetx.model.User;
import com.neurofleetx.repository.SupportTicketRepository;
import com.neurofleetx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportTicketService {
    @Autowired
    private SupportTicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SupportTicket> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }

    public SupportTicket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<SupportTicket> getTicketsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<SupportTicket> getTicketsByStatus(SupportTicket.TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    public List<SupportTicket> getTicketsByPriority(SupportTicket.TicketPriority priority) {
        return ticketRepository.findByPriorityOrderByCreatedAtDesc(priority);
    }

    public SupportTicket createTicket(String username, SupportTicket ticket) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ticket.setUser(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(SupportTicket.TicketStatus.OPEN);
        
        return ticketRepository.save(ticket);
    }

    public SupportTicket updateTicket(Long id, SupportTicket ticketDetails) {
        SupportTicket ticket = getTicketById(id);
        
        if (ticketDetails.getSubject() != null) {
            ticket.setSubject(ticketDetails.getSubject());
        }
        if (ticketDetails.getDescription() != null) {
            ticket.setDescription(ticketDetails.getDescription());
        }
        if (ticketDetails.getCategory() != null) {
            ticket.setCategory(ticketDetails.getCategory());
        }
        if (ticketDetails.getPriority() != null) {
            ticket.setPriority(ticketDetails.getPriority());
        }
        if (ticketDetails.getStatus() != null) {
            ticket.setStatus(ticketDetails.getStatus());
            if (ticketDetails.getStatus() == SupportTicket.TicketStatus.RESOLVED ||
                ticketDetails.getStatus() == SupportTicket.TicketStatus.CLOSED) {
                ticket.setResolvedAt(LocalDateTime.now());
            }
        }
        if (ticketDetails.getResolution() != null) {
            ticket.setResolution(ticketDetails.getResolution());
        }
        
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public SupportTicket assignTicket(Long ticketId, String assigneeUsername) {
        SupportTicket ticket = getTicketById(ticketId);
        User assignee = userRepository.findByUsername(assigneeUsername)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        
        ticket.setAssignedTo(assignee);
        ticket.setStatus(SupportTicket.TicketStatus.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());
        
        return ticketRepository.save(ticket);
    }

    public SupportTicket resolveTicket(Long ticketId, String resolution) {
        SupportTicket ticket = getTicketById(ticketId);
        
        ticket.setResolution(resolution);
        ticket.setStatus(SupportTicket.TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
