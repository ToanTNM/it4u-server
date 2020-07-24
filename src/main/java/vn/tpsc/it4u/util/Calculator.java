package vn.tpsc.it4u.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.List;

public class Calculator {
    public String ConvertSecondToHHMMString(Integer secondtTime) {
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    String time = df.format(new Date(secondtTime*1000L));
    return time;
    }

    public String ConvertSecondToDate(long secondTime) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondTime));
        return time;
        // DateFormat simple = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
        // Date result = new Date(secondTime); 
        // // System.out.println(simple.format(result));
        // return simple.format(result).toString();
    }

    public String ConvertSecondToDateNotZone(long secondTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String time = df.format(new Date(secondTime));
        return time;
        // DateFormat simple = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        // Date result = new Date(secondTime);
        // // System.out.println(simple.format(result));
        // return simple.format(result).toString();
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
            try {
                long data1 = Math.round(bytes / (1024 * 1024 * 1024));
                data = Math.round(data1/1024);
                i = i + 4;
            } catch (Exception e) {
               System.out.print(e);
                //TODO: handle exception
            }
            
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
    public List<String> ConvertBytesPerSecond(long bytes) {
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
            case 1: unit = " Kbps";break;
            case 2: unit = " Mbps";break;
            case 3: unit = " Gbps";break;
            case 4: unit = " Tbps";break;
        }
        result.add(data.toString());
        result.add(unit);
        return result;
    }
    public double convertBytesToGb(long number) {
        double convert = (double)number;
        double convertToGb = convert / 1024 / 1024 / 1024;
        double result = Math.round(convertToGb * 100.0) / 100.0;
        return result;
    }

    public double convertBytesToMb(long number) {
        double convert = (double) number;
        double convertToGb = convert / 1024 / 1024;
        double result = Math.round(convertToGb * 100.0) / 100.0;
        return result;
    }
}