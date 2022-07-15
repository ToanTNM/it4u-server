package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.model.Contract;

@AllArgsConstructor
@Getter
@Setter
public class CustomerBreakdownsSummary {
    private Long id;

    private Contract contract;

    private String receiver;

    private Long firstTime;

    private Long secondTime;

    private String influenceLevellString;

    private String reason;

    private String result;

    private String verify;

    private String status;

    private String classify;

}
