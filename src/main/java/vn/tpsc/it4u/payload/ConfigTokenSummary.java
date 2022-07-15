package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ConfigTokenSummary {
    private Long id;
    private String csrfToken;
    private String unifises;
}
