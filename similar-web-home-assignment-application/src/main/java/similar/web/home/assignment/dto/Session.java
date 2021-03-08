package similar.web.home.assignment.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Session {

    private String sessionStart;

    private String sessionEnd;
}
