package com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Policy;
import lombok.Data;

@Data
public class CheckmarxPolicy extends Policy {
    public Integer maxHighAllowed;
    public Integer maxLowAllowed;
    @Override
    public boolean invoke(ScanResults results) {
        return false;
    }
    public  String getName() {
     return "Checkmarx";
    }

}
