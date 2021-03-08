package similar.web.home.assignment.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataRecord {

    private String visitorId;

    private String siteUrl;

    private String pageViewUrl;

    private String timestamp;
}