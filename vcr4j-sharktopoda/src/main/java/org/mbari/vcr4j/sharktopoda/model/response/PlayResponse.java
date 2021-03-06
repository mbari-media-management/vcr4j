package org.mbari.vcr4j.sharktopoda.model.response;

import java.util.UUID;

/**
 * @author Brian Schlining
 * @since 2016-08-27T15:23:00
 */
public class PlayResponse {
    private String response;
    private String status;
    private UUID uuid;


    public PlayResponse(UUID uuid, String status) {
        this.response = "open";
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public String getStatus() {
        return status;
    }

    public UUID getUuid() {
        return uuid;
    }
}
