package com.cs203.locus.service;

import com.cs203.locus.models.participant.Participant;
import com.cs203.locus.models.participant.ParticipantDTO;
import com.cs203.locus.repository.ParticipantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


    public Participant findById(Integer id){
        if (participantRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Participant with ID: " + id);
        }
        return participantRepository.findById(id).get();
    }

    public Participant createParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public Participant updateParticipant(Integer id, ParticipantDTO participantDTO){
        if (participantRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Participant with ID: " + id);
        }
        Participant newParticipant = participantRepository.findById(id).get();
        newParticipant.setVaxAwsUrl(participantDTO.getVaxAwsUrl());
        newParticipant.setVaxStatus(participantDTO.getVaxStatus());
        return participantRepository.save(newParticipant);
    }

    // need to add more methods?
    @Transactional
    public Participant deleteParticipant(Integer id) {
        if (participantRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No Participant with ID: " + id);
        }

        Participant current = participantRepository.findById(id).get();
        participantRepository.delete(current);
        return current;
    }

    // TODO: get all events that a participant is participating in

}