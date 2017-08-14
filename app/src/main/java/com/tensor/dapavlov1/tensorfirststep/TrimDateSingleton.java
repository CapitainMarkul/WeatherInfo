package com.tensor.dapavlov1.tensorfirststep;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by da.pavlov1 on 04.08.2017.
 */

public class TrimDateSingleton {
    private static TrimDateSingleton instance;

    private TrimDateSingleton() {

    }

    public static TrimDateSingleton getInstance() {
        if (instance == null) {
            instance = new TrimDateSingleton();
        }
        return instance;
    }

    public String trimTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            return formatter.format(createDate(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public String trimDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return formatter.format(createDate(date));
        } catch (ParseException e) {
            return null;
        }
    }

    private Date createDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH", Locale.US);
        Date date = formatter.parse(dateString);
        return date;
    }

    public String getNowTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM',' HH:mm", myDateFormatSymbols);
        return dateFormat.format(currentDate);
    }

    private DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {
        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
    };
}
