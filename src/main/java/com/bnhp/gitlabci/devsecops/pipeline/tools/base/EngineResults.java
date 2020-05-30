package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import lombok.Builder;
import lombok.Data;

@Data
public abstract class EngineResults {

    public Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
      this.success = success;
    }


}