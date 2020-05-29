package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import lombok.Data;

/**
 * represents a policy of a tool.
 * This policy should be set by AMAM.
 */
@Data
public abstract class Policy {

    public abstract boolean invoke(ScanResults results);
    public abstract String getName();

}
