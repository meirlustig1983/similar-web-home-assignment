package similar.web.home.assignment.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import similar.web.home.assignment.dto.DataRecord;
import similar.web.home.assignment.dto.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DbFacade {

    private final Map<String, List<Session>> sessionsBySiteUrl = new HashMap<>();

    private final Map<String, List<Session>> sessionsBySiteUrlAndVisitorId = new HashMap<>();

    private final Map<String, Set<String>> visitorsData = new HashMap<>();

    public void insert(DataRecord dataRecord) {
        saveSiteData(dataRecord);
        saveVisitorData(dataRecord);
    }

    public List<Session> getSessionListBySiteUrl(String siteUrl) {
        return sessionsBySiteUrl.getOrDefault(siteUrl, Collections.emptyList()).stream()
            .collect(Collectors.toList());
    }

    public Set getSitesListByVisitorId(String visitorId) {
        return visitorsData.getOrDefault(visitorId, Collections.emptySet());
    }

    private void saveSiteData(DataRecord dataRecord) {

        if (sessionsBySiteUrl.containsKey(dataRecord.getSiteUrl())) {
            if (sessionsBySiteUrlAndVisitorId.containsKey(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId()))) {
                Optional<Session> session = findSession(dataRecord, sessionsBySiteUrlAndVisitorId.get(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId())));
                if (session.isPresent()) {
                    session.get().setSessionEnd(dataRecord.getTimestamp());
                } else {
                    addNewSessionToVisitor(dataRecord);
                }
            } else {
                addNewVisitor(dataRecord);
            }
        } else {
            addNewSite(dataRecord);
        }
    }

    private Optional<Session> findSession(DataRecord dataRecord, List<Session> sessions) {
        return sessions.stream().filter(session -> checkSession(dataRecord, session)).findFirst();
    }

    private boolean checkSession(DataRecord dataRecord, Session session) {
        String startEpochTime = session.getSessionStart();
        String newEpochTime = dataRecord.getTimestamp();
        String endEpochTime = session.getSessionEnd() == null ? session.getSessionStart() : session.getSessionEnd();

        Date startTime = new Date(Long.parseLong(startEpochTime) * 1000);
        Date endTime = new Date((Long.parseLong(endEpochTime) * 1000) + 30 * 60000);
        Date newTime = new Date(Long.parseLong(newEpochTime) * 1000);

        if (startTime.before(newTime) && newTime.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    private void addNewSessionToVisitor(DataRecord dataRecord) {
        Session session = new Session().setSessionStart(dataRecord.getTimestamp());

        List<Session> sessionsForVisitor = sessionsBySiteUrlAndVisitorId.get(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId()));
        sessionsForVisitor.add(session);
        sessionsBySiteUrlAndVisitorId.put(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId()), sessionsForVisitor);

        List<Session> sessionsForSite = sessionsBySiteUrl.get(dataRecord.getSiteUrl());
        sessionsForSite.add(session);
        sessionsBySiteUrl.put(dataRecord.getSiteUrl(), sessionsForSite);
    }

    private void addNewVisitor(DataRecord dataRecord) {
        Session session = new Session().setSessionStart(dataRecord.getTimestamp());

        ArrayList<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessionsBySiteUrlAndVisitorId.put(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId()), sessions);

        List<Session> sessionsForSite = sessionsBySiteUrl.get(dataRecord.getSiteUrl());
        sessionsForSite.add(session);
        sessionsBySiteUrl.put(dataRecord.getSiteUrl(), sessionsForSite);
    }

    private void addNewSite(DataRecord dataRecord) {
        Session session = new Session().setSessionStart(dataRecord.getTimestamp());

        ArrayList<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessionsBySiteUrlAndVisitorId.put(createKey(dataRecord.getSiteUrl(), dataRecord.getVisitorId()), sessions);

        ArrayList<Session> listOfSessions = new ArrayList<>();
        listOfSessions.add(session);
        sessionsBySiteUrl.put(dataRecord.getSiteUrl(), listOfSessions);
    }

    private String createKey(String siteUrl, String visitorId) {
        return String.format("%s#%s", siteUrl, visitorId);
    }

    private void saveVisitorData(DataRecord dataRecord) {
        try {
            if (visitorsData.containsKey(dataRecord.getVisitorId())) {
                visitorsData.get(dataRecord.getVisitorId()).add(dataRecord.getSiteUrl());
            } else {
                Set<String> sites = new HashSet<>();
                sites.add(dataRecord.getSiteUrl());
                visitorsData.put(dataRecord.getVisitorId(), sites);
            }
        } catch (Exception e) {
            log.info("failed to proccess. dataRecord: {}", dataRecord);
        }
    }
}