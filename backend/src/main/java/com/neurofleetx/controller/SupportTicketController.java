package com.neurofleetx.controller;

import com.neurofleetx.model.SupportTicket;
import com.neurofleetx.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportTicketController {
    @Autowired
    private SupportTicketService ticketService;

    @GetMapping("/admin/support/tickets")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/manager/support/tickets")
    public ResponseEntity<List<SupportTicket>> getManagerTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/customer/support/tickets")
    public ResponseEntity<List<SupportTicket>> getCustomerTickets(@RequestParam String username) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(username));
    }

    @GetMapping("/support/tickets/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/support/tickets/status/{status}")
    public ResponseEntity<List<SupportTicket>> getTicketsByStatus(@PathVariable SupportTicket.TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping("/support/tickets/priority/{priority}")
    public ResponseEntity<List<SupportTicket>> getTicketsByPriority(@PathVariable SupportTicket.TicketPriority priority) {
        return ResponseEntity.ok(ticketService.getTicketsByPriority(priority));
    }

    @PostMapping("/customer/support/tickets")
    public ResponseEntity<SupportTicket> createTicket(@RequestParam String username, @RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(username, ticket));
    }

    @PutMapping("/support/tickets/{id}")
    public ResponseEntity<SupportTicket> updateTicket(@PathVariable Long id, @RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticket));
    }

    @PutMapping("/manager/support/tickets/{id}/assign")
    public ResponseEntity<SupportTicket> assignTicket(@PathVariable Long id, @RequestParam String assigneeUsername) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeUsername));
    }

    @PutMapping("/manager/support/tickets/{id}/resolve")
    public ResponseEntity<SupportTicket> resolveTicket(@PathVariable Long id, @RequestParam String resolution) {
        return ResponseEntity.ok(ticketService.resolveTicket(id, resolution));
    }

    @DeleteMapping("/support/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }
}
