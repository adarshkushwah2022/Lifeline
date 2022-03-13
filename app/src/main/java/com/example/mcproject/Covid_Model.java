package com.example.mcproject;

public class Covid_Model {
    private String State_name;
    private String active_case;
    private String total_case;
    private String recovery;
    private String total_deaths;

    public Covid_Model(String state_name, String active_case, String total_case, String recovery, String total_deaths) {
        State_name = state_name;
        this.active_case = active_case;
        this.total_case = total_case;
        this.recovery = recovery;
        this.total_deaths = total_deaths;
    }

    public String getState_name() {
        return State_name;
    }

    public void setState_name(String state_name) {
        State_name = state_name;
    }

    public String getActive_case() {
        return active_case;
    }

    public void setActive_case(String active_case) {
        this.active_case = active_case;
    }

    public String getTotal_case() {
        return total_case;
    }

    public void setTotal_case(String total_case) {
        this.total_case = total_case;
    }

    public String getRecovery() {
        return recovery;
    }

    public void setRecovery(String recovery) {
        this.recovery = recovery;
    }

    public String getTotal_deaths() {
        return total_deaths;
    }

    public void setTotal_deaths(String total_deaths) {
        this.total_deaths = total_deaths;
    }
}
