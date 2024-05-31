package com.nemanja02.projekat.repositories.destination;

import com.nemanja02.projekat.entities.Destination;

import java.util.List;

public interface DestinationRepository {
    public Destination addDestination(Destination destination);
    public Destination getDestination(Integer id);
    public Destination editDestination(Destination destination);
    public boolean deleteDestination(Integer id);

    public List<Destination> getDestinations(Integer page);
    public int getDestinationsCount();
}
