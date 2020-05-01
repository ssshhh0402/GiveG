package com.dauction.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 패브릭 체인코드로부터 조회된 결과를 담기위한 클래스
 */
public class FabricRecord {
    private String assetId;
    private String recorder;
    private LocalDateTime createdAt;
    private String state;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String getAssetId()
    {
        return assetId;
    }

    public void setAssetId(final String assetId)
    {
        this.assetId = assetId;
    }

    public String getRecorder()
    {
        return recorder;
    }

    public void setRecorder(final String recorder)
    {
        this.recorder = recorder;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt)
    {
        if("FALSE".equals(createdAt))
            this.createdAt = null;
        else
            this.createdAt = LocalDateTime.parse(createdAt, formatter);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString(){
        return "{ assetId: " + this.assetId +
                "\n\trecorder: " + this.recorder +
                "\n\tcreatedAt: " + this.createdAt +
                "\n\tstate: " + this.state +
                " }";
    }
}
