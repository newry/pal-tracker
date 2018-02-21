package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements  TimeEntryRepository {
    private Map<Long, TimeEntry> map = new HashMap<>();
    private long id=1l;
    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        map.put(id, timeEntry);
        timeEntry.setId(id);
        id++;
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return this.map.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(this.map.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(map.containsKey(id)) {
            map.put(id, timeEntry);
            timeEntry.setId(id);
            return timeEntry;
        }
        return null;
    }

    @Override
    public TimeEntry delete(long id) {
        return map.remove(id);
    }
}
