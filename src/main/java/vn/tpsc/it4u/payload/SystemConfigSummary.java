package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SystemConfigSummary {
    private Long id;

    private String usernameUbnt;

    private String pwUbnt;

    private String tokenDev;

    private String tokenUcrm;

}
