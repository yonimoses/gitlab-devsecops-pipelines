package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import lombok.Data;

/**
 * represents a policy of a tool.
 * This policy should be set by AMAM.
 */
@Data
public abstract class EnginePolicy<T extends EngineResults> {

    public abstract boolean allowed(T results);

    public abstract String getName();

}
