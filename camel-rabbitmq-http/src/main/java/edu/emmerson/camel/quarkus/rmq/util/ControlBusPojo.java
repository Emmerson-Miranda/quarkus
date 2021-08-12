package edu.emmerson.camel.quarkus.rmq.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ControlBusPojo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 148264867877721255L;

    private String action;
    private String loggingLevel;
    private String restartDelay;
    private String routeId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(String loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public String getRestartDelay() {
        return restartDelay;
    }

    public void setRestartDelay(String restartDelay) {
        this.restartDelay = restartDelay;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("controlbus:route?");

        sb.append("routeId=").append(routeId);

        if (!StringUtils.isEmpty(action)) {
            sb.append("&");
            sb.append("action=").append(action);
        }

        if (!StringUtils.isEmpty(loggingLevel)) {
            sb.append("&");
            sb.append("loggingLevel=").append(loggingLevel);
        }

        if (!StringUtils.isEmpty(restartDelay)) {
            sb.append("&");
            sb.append("restartDelay=").append(restartDelay);
        }

        return sb.toString();
    }

}
