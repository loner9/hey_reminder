package info.project.hey.Class.util;

public class DaTiSorter {
    public int mIndex;
    public String mDateTime;


    public DaTiSorter(int index, String DateTime){
        mIndex = index;
        mDateTime = DateTime;
    }

    public DaTiSorter(){}


    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }
}
