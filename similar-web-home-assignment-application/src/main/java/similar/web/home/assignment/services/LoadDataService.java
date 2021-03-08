package similar.web.home.assignment.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import similar.web.home.assignment.db.DbFacade;
import similar.web.home.assignment.dto.DataRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.PostConstruct;

@Slf4j
@Service
public class LoadDataService {

    private static final String COMMA_DELIMITER = ",";

    private PathMatchingResourcePatternResolver resolver;

    private DbFacade dbFacade;

    @Autowired
    public LoadDataService(PathMatchingResourcePatternResolver resourceLoader,
                           DbFacade dbFacade) {
        this.resolver = resourceLoader;
        this.dbFacade = dbFacade;
    }

    @PostConstruct
    public void init() throws IOException {
        log.info("init service started");
        log.info("try to load 'csv' files from resources");
        Resource[] resources = resolver.getResources("classpath:inputs/csv/**/*.csv");
        for (Resource resource : resources) {
            load(resource.getFile());
        }
        log.info("init service end");
    }

    @SneakyThrows
    public void load(File file) {
        log.info("start reading file: {}", file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                DataRecord dataRecord = new DataRecord().setVisitorId(values[0]).setSiteUrl(values[1]).setPageViewUrl(values[2]).setTimestamp(values[3]);
                dbFacade.insert(dataRecord);
            }
        }
    }
}
