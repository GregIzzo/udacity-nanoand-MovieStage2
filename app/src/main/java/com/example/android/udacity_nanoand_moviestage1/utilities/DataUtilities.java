package com.example.android.udacity_nanoand_moviestage1.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gizzo_000 on 5/24/2018.
 */

public class DataUtilities {


   public static String getFormattedDate(String strDate){
       if (strDate == null) return "";
       SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
       Date dateObj = new Date();
       try {
           dateObj = dFormat.parse(strDate);
       } catch (ParseException e){
           e.printStackTrace();
       }
       return (String)android.text.format.DateFormat.format("MMMM dd, yyyy", dateObj);
   }
    public static String getYearFromDate(String strDate){
        //Expect strDate to be of the form YYYY-MM-DD
        if (strDate == null) return "";
        String datePartsArray[]= strDate.split("-");
        if (datePartsArray.length > 0) {
            return datePartsArray[0];
        } else {
            return "";
        }
    }
}
