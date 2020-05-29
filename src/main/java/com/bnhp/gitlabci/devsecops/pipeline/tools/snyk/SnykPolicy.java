package com.bnhp.gitlabci.devsecops.pipeline.tools.snyk;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Policy;

public class SnykPolicy extends Policy {
    public Integer maxHighAllowed;
    public Integer maxLowAllowed;
    @Override
    public boolean invoke(ScanResults results) {
        return false;
    }

    public String getName() {
        return "Snyk";
    }

}
