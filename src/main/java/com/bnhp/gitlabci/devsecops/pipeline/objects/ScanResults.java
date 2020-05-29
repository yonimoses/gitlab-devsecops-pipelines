package com.bnhp.gitlabci.devsecops.pipeline.objects;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScanResults {

    public Boolean success;
    public long total;
    public long high;
    public long medium;
    public long low;
    public String summary;
    public String technology;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }



    @Override
    public String toString() {
        return "ScanResults{" +
                "success=" + success +
                ", high=" + high +
                ", medium=" + medium +
                ", low=" + low +
                '}';
    }
}