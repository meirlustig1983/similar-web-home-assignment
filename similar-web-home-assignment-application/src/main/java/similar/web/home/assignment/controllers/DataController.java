package similar.web.home.assignment.controllers;

import similar.web.home.assignment.services.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/")
public class DataController {

    private DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("health")
    public String health() {
        log.info("health check request");
        return "OK";
    }

    @GetMapping("help")
    public String help() {
        log.info("health checking request");
        return "Server is up and running.<br>"
            + "* health - checking service health.<br>"
            + "* sessionsCounter?siteUrl={SITE_URL} - get the number of sessions for site url<br>"
            + "* sessionsMedian?siteUrl={SITE_URL} - get the median of sessions length for site url<br>"
            + "* numUniqueVisitedSites?visitorId={VISITOR_ID} - get the number of unique site urls by visitorId<br>"
            + "";
    }

    @GetMapping("sessionsCounter")
    public String sessionsCounter(String siteUrl) {
        log.info("get number of sessions by siteUrl: {}", siteUrl);
        return dataService.sessionsCounter(siteUrl);
    }

    @GetMapping("sessionsMedian")
    public String sessionsMedian(String siteUrl) {
        log.info("get sessions median by siteUrl: {}", siteUrl);
        return dataService.sessionsMedian(siteUrl);
    }

    @GetMapping("numUniqueVisitedSites")
    public String numUniqueVisitedSites(String visitorId) {
        log.info("get number of unique site urls by visitorId: {}", visitorId);
        return dataService.numUniqueVisitedSites(visitorId);
    }
}