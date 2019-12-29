package vn.tpsc.it4u.common;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.List;

public class Calculator {
    public String ConvertSecondToHHMMString(Integer secondtTime) {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    df.setTimeZone(tz);
    String time = df.format(new Date(secondtTime*1000L));
    return time;
    }
    public List<String> ConvertBytes(long bytes) {
        String unit = "";
        Integer i = 0;
        Integer data = 0;
        long kb = 1024*1024;
        long mb = kb*1024;
        long gb = mb*1024;
        long tb = gb*1024;

        List<String> result = new ArrayList<>();
        if (bytes < kb) {
            data = Math.round(bytes/1024);
            i = i + 1;
        }
        else if (bytes < mb) {
            data = Math.round(bytes/(1024*1024));
            i = i + 2;
        }
        else if (bytes < gb) {
            data = Math.round(bytes/(1024*1024*1024));
            i = i + 3;
        }
        else if (bytes < tb) {
            data = Math.round(bytes/(1024*1024*1024*1024));
            i = i + 1;
        }
        switch(i) {
            case 1: unit = " KB";break;
            case 2: unit = " MB";break;
            case 3: unit = " GB";break;
            case 4: unit = " TB";break;
        }
        result.add(data.toString());
        result.add(unit);
        return result;
    }
}