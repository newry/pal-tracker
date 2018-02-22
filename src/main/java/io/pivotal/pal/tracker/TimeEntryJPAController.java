package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/time-entries-jpa")
public class TimeEntryJPAController {
    private TimeEntryJPARepository timeEntryRepository;

    public TimeEntryJPAController(TimeEntryJPARepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        timeEntryRepository.save(timeEntry);
        return ResponseEntity.status(201).body(timeEntry);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = this.timeEntryRepository.findOne(id);
        if (timeEntry != null) {
            return ResponseEntity.ok(timeEntry);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        Iterable<TimeEntry> all = this.timeEntryRepository.findAll();
        List<TimeEntry> list = new ArrayList<>();
        all.forEach(a -> list.add(a));
        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry update = this.timeEntryRepository.findOne(id);
        if (update != null) {
            update.setDate(timeEntry.getDate());
            update.setHours(timeEntry.getHours());
            update.setProjectId(timeEntry.getProjectId());
            update.setUserId(timeEntry.getUserId());
            timeEntryRepository.save(update);
            return ResponseEntity.ok(update);
        }
        return ResponseEntity.status(404).body(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        TimeEntry update = this.timeEntryRepository.findOne(id);
        if (update != null) {
            this.timeEntryRepository.delete(update);
        }
        return ResponseEntity.status(204).body(null);
    }

}
