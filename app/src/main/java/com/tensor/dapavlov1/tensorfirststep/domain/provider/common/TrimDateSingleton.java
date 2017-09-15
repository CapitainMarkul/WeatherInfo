package com.tensor.dapavlov1.tensorfirststep.domain.provider.common;

import android.support.annotation.Nullable;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by da.pavlov1 on 04.08.2017.
 */
// FIXME: 07.09.2017 Разобраться со Static Content
public class TrimDateSingleton {
    public static TrimDateSingleton getInstance() {
        return TrimDateSingletonLoader.INSTANCE;
    }

    private static final class TrimDateSingletonLoader {
        private static final TrimDateSingleton INSTANCE = new TrimDateSingleton();
    }

    private TrimDateSingleton() {
    }

    @Nullable
    public String trimTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            return formatter.format(createDate(date));
        } catch (ParseException e) {
            return null;
        }
    }

    @Nullable
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM',' HH:mm", dictionary);
        return dateFormat.format(currentDate);
    }

    private DateFormatSymbols dictionary = new DateFormatSymbols() {
        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
    };
}
