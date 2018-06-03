package com.example.mehrab_patwary.bindas;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id;
    private String mEventName;
    private String mEventLocation;
    private String mEventDetails;
    private String mEventStartDate;
    private String mEventEndDate;
    private String mEventImage;
    private String mEventImageUrl;

    public Event() {
        // Empty constructor is important for firebase database
    }

    public Event(String id, String mEventName, String mEventLocation, String mEventDetails,
                 String mEventStartDate, String mEventEndDate) {
        if (mEventName.trim().equals("")){
            mEventName ="No name input";
        }else if (mEventLocation.trim().equals("")){
            mEventLocation = "No location input";
        }else if (mEventDetails.trim().equals("")){
            mEventDetails="No details input";
        }
        this.id = id;
        this.mEventName = mEventName;
        this.mEventLocation = mEventLocation;
        this.mEventDetails = mEventDetails;
        this.mEventStartDate = mEventStartDate;
        this.mEventEndDate = mEventEndDate;
        this.mEventImage = mEventImage;
        this.mEventImageUrl = mEventImageUrl;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getmEventLocation() {
        return mEventLocation;
    }

    public void setmEventLocation(String mEventLocation) {
        this.mEventLocation = mEventLocation;
    }

    public String getmEventDetails() {
        return mEventDetails;
    }

    public void setmEventDetails(String mEventDetails) {
        this.mEventDetails = mEventDetails;
    }

    public String getmEventStartDate() {
        return mEventStartDate;
    }

    public void setmEventStartDate(String mEventStartDate) {
        this.mEventStartDate = mEventStartDate;
    }

    public String getmEventEndDate() {
        return mEventEndDate;
    }

    public void setmEventEndDate(String mEventEndDate) {
        this.mEventEndDate = mEventEndDate;
    }

    public String getmEventImage() {
        return mEventImage;
    }

    public void setmEventImage(String mEventImage) {
        this.mEventImage = mEventImage;
    }

    public String getmEventImageUrl() {
        return mEventImageUrl;
    }

    public void setmEventImageUrl(String mEventImageUrl) {
        this.mEventImageUrl = mEventImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
