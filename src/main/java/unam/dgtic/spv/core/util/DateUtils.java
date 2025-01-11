package unam.dgtic.spv.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDateString(String dateString) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Date date = parser.parse(dateString);
            // Especifica el Locale español
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy 'a las' h:mm a", new Locale("es", "ES"));
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}