package com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx;

import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EngineResults;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckmarxEngineResults extends EngineResults {

    public long malware;
    public Boolean success;
    public long total;
    public long high;
    public long medium;
    public long low;
    public String summary;
    public String technology;

    @Override
    public String toString() {
        return "AquaScanResults{" +
                "malware=" + malware +
                ", success=" + success +
                ", total=" + total +
                ", high=" + high +
                ", medium=" + medium +
                ", low=" + low +
                ", summary='" + summary + '\'' +
                ", technology='" + technology + '\'' +
                '}';
    }
}