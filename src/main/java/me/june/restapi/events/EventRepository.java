package me.june.restapi.events;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-06
 * Time: 21:56
 **/
public interface EventRepository extends JpaRepository<Event, Long> {
}
