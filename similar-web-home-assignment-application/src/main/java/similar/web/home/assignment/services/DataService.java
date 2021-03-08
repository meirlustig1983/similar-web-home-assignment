package similar.web.home.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import similar.web.home.assignment.db.DbFacade;
import similar.web.home.assignment.dto.Session;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataService {

    private final DbFacade dbFacade;

    @Autowired
    public DataService(DbFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    public String sessionsCounter(String siteUrl) {
        List<Session> list = dbFacade.getSessionListBySiteUrl(siteUrl)
            .stream()
            .filter(session -> session.getSessionEnd() != null)
            .collect(Collectors.toList());

        return String.valueOf(list.size());
    }

    public String sessionsMedian(String siteUrl) {
        List<Double> list = dbFacade.getSessionListBySiteUrl(siteUrl)
            .stream()
            .filter(session -> session.getSessionEnd() != null)
            .map(this::calcSessionLength)
            .collect(Collectors.toList());

        return String.valueOf(median(list));
    }

    public String numUniqueVisitedSites(String visitorId) {
        return String.valueOf(dbFacade.getSitesListByVisitorId(visitorId).size());
    }

    private double calcSessionLength(Session session) {
        if (session.getSessionEnd() == null) {
            return 0.0;
        } else {
            String startEpochTime = session.getSessionStart();
            String endEpochTime = session.getSessionEnd();

            Date startTime = new Date(Long.parseLong(startEpochTime) * 1000);
            Date endTime = new Date(Long.parseLong(endEpochTime) * 1000);

            return (endTime.getTime() - startTime.getTime()) / 1000;
        }
    }

    public double median(List<Double> values) {
        Collections.sort(values);

        if (values.size() % 2 == 1)
            return values.get((values.size() + 1) / 2 - 1);
        else {
            double lower = values.get(values.size() / 2 - 1);
            double upper = values.get(values.size() / 2);
            return (lower + upper) / 2.0;
        }
    }
}
