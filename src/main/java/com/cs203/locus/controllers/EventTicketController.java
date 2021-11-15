package com.cs203.locus.controllers;

import com.cs203.locus.models.event.Event;
import com.cs203.locus.models.event.EventTicket;
import com.cs203.locus.models.event.EventTicketDTO;
import com.cs203.locus.models.participant.Participant;
import com.cs203.locus.service.EventService;
import com.cs203.locus.service.EventTicketService;
import com.cs203.locus.service.ParticipantService;
import com.cs203.locus.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cs203.locus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ticket")
public class EventTicketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventTicketController.class);

    @Autowired
    private EventTicketService eventTicketService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailUtil emailUtil;


    @GetMapping(value = "/list/{id}")
    public @ResponseBody
    ResponseEntity<?> getAllEventTicketsByEventID(@PathVariable Integer id) {
        Iterable<EventTicket> temp = eventTicketService.findEventTicketByEventId(id);
        ArrayList<EventTicketDTO> result = new ArrayList<>();
        for (EventTicket eventTicket : temp) {
            EventTicketDTO toRet = new EventTicketDTO();
            toRet.setIsVaccinated(eventTicket.getParticipant().getVaxStatus());
            toRet.setId(eventTicket.getId());
            toRet.setParticipantName(eventTicket.getParticipant().getUser().getName());
            toRet.setParticipantId(eventTicket.getParticipant().getId());
            toRet.setOrganiserId(eventTicket.getEvent().getOrganiser().getId());
            toRet.setOrganiserName(eventTicket.getEvent().getOrganiser().getUser().getName());
            toRet.setEventName(eventTicket.getEvent().getName());
            toRet.setEventId(eventTicket.getEvent().getId());
            toRet.setStartDateTime(eventTicket.getEvent().getStartDateTime());
            toRet.setEndDateTime(eventTicket.getEvent().getEndDateTime());
            toRet.setEventAddress(eventTicket.getEvent().getAddress());
            result.add(toRet);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<EventTicketDTO> findById(@PathVariable Integer id) {
        EventTicket result = eventTicketService.findById(id);

        if (result == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No EventTicket with ID: " + id);
        }

        EventTicketDTO toRet = new EventTicketDTO();
        toRet.setId(result.getId());
        toRet.setParticipantName(result.getParticipant().getUser().getName());
        toRet.setParticipantId(result.getParticipant().getId());
        toRet.setIsVaccinated(result.getParticipant().getVaxStatus());
        toRet.setOrganiserName(result.getEvent().getOrganiser().getUser().getName());
        toRet.setOrganiserId(result.getEvent().getOrganiser().getId());
        toRet.setEventName(result.getEvent().getName());
        toRet.setEventId(result.getEvent().getId());
        toRet.setStartDateTime(result.getEvent().getStartDateTime());
        toRet.setEndDateTime(result.getEvent().getEndDateTime());
        toRet.setEventAddress(result.getEvent().getAddress());

        return ResponseEntity.ok(toRet);
    }

    // TODO: ensure participant can only access his own list of EventTickets
    @GetMapping(value = "/listParticipantTickets/{id}")
    public @ResponseBody
    ResponseEntity<ArrayList<EventTicketDTO>> getParticipantTickets(@PathVariable Integer id) {
        if (participantService.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Participant with ID: " + id);
        }

        Iterable<EventTicket> temp = eventTicketService.findEventTicketByParticipant(id);
        ArrayList<EventTicketDTO> result = new ArrayList<>();
        for (EventTicket eventTicket : temp) {
            EventTicketDTO toRet = new EventTicketDTO();
            toRet.setId(eventTicket.getId());
            toRet.setIsVaccinated(eventTicket.getParticipant().getVaxStatus());
            toRet.setParticipantName(eventTicket.getParticipant().getUser().getName());
            toRet.setParticipantId(eventTicket.getParticipant().getId());
            toRet.setOrganiserName(eventTicket.getEvent().getOrganiser().getUser().getName());
            toRet.setOrganiserId(eventTicket.getEvent().getOrganiser().getId());
            toRet.setEventName(eventTicket.getEvent().getName());
            toRet.setEventId(eventTicket.getEvent().getId());
            toRet.setStartDateTime(eventTicket.getEvent().getStartDateTime());
            toRet.setEndDateTime(eventTicket.getEvent().getEndDateTime());
            toRet.setEventAddress(eventTicket.getEvent().getAddress());
            result.add(toRet);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/hasParticipatedEvent/{participantId}/{eventId}")
    ResponseEntity<Boolean> getParticpationStatus(@PathVariable Integer participantId, @PathVariable Integer eventId){
        return ResponseEntity.ok(eventTicketService.existingTicket(participantId,eventId));
    }

    // Identify event ticket exists using eventId and userId
    @GetMapping(value = "/listParticipantTickets/{id}/{eventId}")
    public @ResponseBody
    ResponseEntity<ArrayList<EventTicketDTO>> getParticipantTickets(@PathVariable Integer id, @PathVariable Integer eventId) {
        if (eventTicketService.findSpecificTicket(id, eventId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Ticket with Id: " + id + " and eventId: " + eventId);
        }

        Iterable<EventTicket> temp = eventTicketService.findSpecificTicket(id, eventId);
        ArrayList<EventTicketDTO> result = new ArrayList<>();
        for (EventTicket eventTicket : temp) {
            EventTicketDTO toRet = new EventTicketDTO();
            toRet.setId(eventTicket.getId());
            toRet.setIsVaccinated(eventTicket.getParticipant().getVaxStatus());
            toRet.setParticipantName(eventTicket.getParticipant().getUser().getName());
            toRet.setParticipantId(eventTicket.getParticipant().getId());
            toRet.setOrganiserName(eventTicket.getEvent().getOrganiser().getUser().getName());
            toRet.setOrganiserId(eventTicket.getEvent().getOrganiser().getId());
            toRet.setEventName(eventTicket.getEvent().getName());
            toRet.setEventId(eventTicket.getEvent().getId());
            toRet.setStartDateTime(eventTicket.getEvent().getStartDateTime());
            toRet.setEndDateTime(eventTicket.getEvent().getEndDateTime());
            toRet.setEventAddress(eventTicket.getEvent().getAddress());
            result.add(toRet);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/new")
    public @ResponseBody
    ResponseEntity<EventTicketDTO> addTicket(@RequestParam Integer participantId,
                                             @RequestParam(required = false) Integer eventId,
                                             @RequestParam(required = false) String inviteCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.findByUsername(auth.getName()).getId().equals(participantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Event event;
        if (eventId == null) {
            event = eventService.findByInviteCode(inviteCode);
        } else if (inviteCode == null) {
            event = eventService.findById(eventId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (event == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid event");
        }

        Participant participant = participantService.findById(participantId);
        if (participant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Participant with ID: " + eventId);
        }
        if (eventTicketService.existingTicket(participantId, eventId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Participant has already joined event.");
        }
        EventTicket ticket = new EventTicket();
        ticket.setEvent(event);
        ticket.setParticipant(participant);

        EventTicket created = eventTicketService.addTicket(ticket);
        EventTicketDTO toRet = new EventTicketDTO();
        toRet.setId(created.getId());
        toRet.setIsVaccinated(created.getParticipant().getVaxStatus());
        toRet.setParticipantName(created.getParticipant().getUser().getName());
        toRet.setParticipantId(created.getParticipant().getId());
        toRet.setOrganiserName(created.getEvent().getOrganiser().getUser().getName());
        toRet.setOrganiserId(created.getEvent().getOrganiser().getId());
        toRet.setEventName(created.getEvent().getName());
        toRet.setEventId(created.getEvent().getId());
        toRet.setStartDateTime(created.getEvent().getStartDateTime());
        toRet.setEndDateTime(created.getEvent().getEndDateTime());
        toRet.setEventAddress(created.getEvent().getAddress());


        // Send the Email
        Map<String, Object> formModel = new HashMap<>();
        formModel.put("recipientEmailAddress", created.getParticipant().getUser().getEmail());
        formModel.put("userName", created.getParticipant().getUser().getName());
        formModel.put("eventName", created.getEvent().getName());
        formModel.put("eventId", created.getEvent().getId());

        // Send an Email to the organiser to let them know they have successfully created the event
        try {
            emailUtil.sendEventSignUpEmail(formModel);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
      
        return ResponseEntity.ok(toRet);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<EventTicketDTO> deleteWithId(@PathVariable Integer id) {
        EventTicket toDel = eventTicketService.findById(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer authParticipantId = userService.findByUsername(username).getId();
        if (toDel == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No EventTicket with ID: " + id);
        }

        if (!toDel.getParticipant().getId().equals(authParticipantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        EventTicket result = eventTicketService.deleteById(id);
        EventTicketDTO toRet = new EventTicketDTO();
        toRet.setId(result.getId());
        toRet.setIsVaccinated(result.getParticipant().getVaxStatus());
        toRet.setParticipantName(result.getParticipant().getUser().getName());
        toRet.setParticipantId(result.getParticipant().getId());
        toRet.setOrganiserName(result.getEvent().getOrganiser().getUser().getName());
        toRet.setOrganiserId(result.getEvent().getOrganiser().getId());
        toRet.setEventName(result.getEvent().getName());
        toRet.setEventId(result.getEvent().getId());
        toRet.setStartDateTime(result.getEvent().getStartDateTime());
        toRet.setEndDateTime(result.getEvent().getEndDateTime());
        toRet.setEventAddress(result.getEvent().getAddress());


        return ResponseEntity.ok(toRet);
    }

}
