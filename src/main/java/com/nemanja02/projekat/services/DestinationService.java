package com.nemanja02.projekat.services;

import com.nemanja02.projekat.entities.Destination;
import com.nemanja02.projekat.repositories.destination.DestinationRepository;
import javax.inject.Inject;

import java.util.List;

public class DestinationService {
    public DestinationService() {
        System.out.println(this);
    }

    @Inject
    private DestinationRepository destinationRepository;

    public Destination addDestination(Destination destination) {
        return this.destinationRepository.addDestination(destination);
    }
    public Destination getDestination(Integer id) {
        return this.destinationRepository.getDestination(id);
    }
    public Destination editDestination(Destination destination) {
        return this.destinationRepository.editDestination(destination);
    }
    public boolean deleteDestination(Integer id) {
        return this.destinationRepository.deleteDestination(id);
    }

    public List<Destination> getDestinations(Integer page) {
        return this.destinationRepository.getDestinations(page);
    }

    public int getDestinationsCount() {
        return this.destinationRepository.getDestinationsCount();
    }
}
