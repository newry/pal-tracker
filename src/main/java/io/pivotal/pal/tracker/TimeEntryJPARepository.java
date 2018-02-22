package io.pivotal.pal.tracker;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TimeEntryJPARepository extends CrudRepository<TimeEntry ,Long>{
}
