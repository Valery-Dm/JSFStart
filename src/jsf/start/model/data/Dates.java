package jsf.start.model.data;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Dates implements Serializable {

    private static final long serialVersionUID = 1L;
    // Off-by-one for consistency with other methods
    public static final int[] DAYS = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    // No mutation expected - therefore fields are not synchronized
    private static TreeSet<Integer> days;
    private static int[] years;
    private static Set<Integer> leapYears;
    private static Map<String, Integer> months;

    static {
        days = new TreeSet<>();
        for (int i = 0; i < 31;) days.add(++i);
        int thisYear = LocalDate.now().getYear();
//        int thisYear = Calendar.getInstance()
//                               .get(Calendar.YEAR);
        // set 20 years from now
        years = new int[20];
        for (int i = 0; i < 20;) years[i++] = thisYear++;
        leapYears = new HashSet<>();
        for (int year : years)
            if (isLeapYear(year))
                leapYears.add(year);

        String[] monthNames = new DateFormatSymbols().getMonths();
        months = new LinkedHashMap<>();
        for (int i = 0; i < 12;) months.put(monthNames[i], ++i);
    }

    // won't be called concurrently
    private static boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 400 == 0 || year % 100 != 0);
    }

    // I need 'in-range' methods of Tree DT
    public static TreeSet<Integer> getDays() {
        return days;
    }

    public static int[] getYears() {
        return years;
    }

    public static Set<Integer> getLeapYears() {
        return leapYears;
    }

    public static Map<String, Integer> getMonths() {
        return months;
    }

    /* Unit tests */
    public static void main(String[] args) {
        //Dates d = new Dates();
        StringBuilder sb = new StringBuilder("Days:\n");
        for (int day : getDays()) sb.append(day).append(", ");
        sb.delete(sb.length() - 2, sb.length());

        sb.append("\nMonths:\n");
        sb.append(String.join(", ", getMonths().keySet()));

        sb.append("\nYears:\n");
        for (int year : getYears()) {
            sb.append(year);
            if (getLeapYears().contains(year)) sb.append("*");
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        System.out.println(sb);
    }
}
