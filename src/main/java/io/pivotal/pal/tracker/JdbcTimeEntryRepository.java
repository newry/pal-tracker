package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    private RowMapper<TimeEntry> rowMapper = new RowMapper<TimeEntry>() {

        @Override
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeEntry timeEntry = new TimeEntry(rs.getLong("id"), rs.getLong("project_id"), rs.getLong("user_id"), rs.getDate("date").toLocalDate(), rs.getInt("hours"));
            return timeEntry;
        }
    };

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        final KeyHolder holder = new GeneratedKeyHolder();
        final PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement("insert into time_entries (project_id, user_id,date,hours) values (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, timeEntry.getProjectId());
                ps.setLong(2, timeEntry.getUserId());
                ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                ps.setInt(4, timeEntry.getHours());

                return ps;
            }
        };
        jdbcTemplate.update(psc, holder);
        timeEntry.setId(holder.getKey().longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        List<TimeEntry> timeEntryList = jdbcTemplate.query("select * from time_entries where id =?", new Object[]{id}, rowMapper);
        if (!CollectionUtils.isEmpty(timeEntryList)) {
            return timeEntryList.get(0);
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntryList = jdbcTemplate.query("select * from time_entries", rowMapper);
        return timeEntryList;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry timeEntryFound = this.find(id);
        if (timeEntryFound != null) {
            timeEntry.setId(id);
            jdbcTemplate.update("update time_entries set project_id=?, user_id=?, date=?, hours=? where id =?",
                    timeEntry.getProjectId(), timeEntry.getUserId(), Date.valueOf(timeEntry.getDate()), timeEntry.getHours(), id);
            return timeEntry;
        }
        return null;
    }

    @Override
    public TimeEntry delete(long id) {
        TimeEntry timeEntry = this.find(id);
        jdbcTemplate.update("delete from time_entries where id =?", id);
        if (timeEntry != null) {
            return timeEntry;
        }
        return null;
    }
}
