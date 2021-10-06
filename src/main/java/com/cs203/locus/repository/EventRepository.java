package com.cs203.locus.repository;
import java.util.List;
import com.cs203.locus.models.event.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//import javax.transaction.Transactional;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
    Iterable<Event> findByOrganiser_Id(Integer id);
}