package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Venue;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    
    private final VenueRepository venueRepository;
    
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }
    
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
    }
    
    @Transactional
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }
    
    @Transactional
    public Venue updateVenue(Long id, Venue venueDetails) {
        Venue venue = getVenueById(id);
        venue.setName(venueDetails.getName());
        venue.setAddress(venueDetails.getAddress());
        venue.setCity(venueDetails.getCity());
        venue.setCapacity(venueDetails.getCapacity());
        return venueRepository.save(venue);
    }
    
    @Transactional
    public void deleteVenue(Long id) {
        Venue venue = getVenueById(id);
        venueRepository.delete(venue);
    }
}
