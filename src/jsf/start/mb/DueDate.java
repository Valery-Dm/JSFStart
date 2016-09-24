package jsf.start.mb;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import jsf.start.model.data.Dates;

@FacesComponent("jsf.start.mb.DueDate")
public class DueDate extends UIInput implements NamingContainer, Serializable {

    private static final long serialVersionUID = 1L;

//    private Calendar calendar = new GregorianCalendar();

    @Override
    protected Object getConvertedValue(FacesContext context,
                                       Object object) throws ConverterException {
        UIInput dayComponent   = (UIInput) findComponent("day");
        UIInput monthComponent = (UIInput) findComponent("month");
        UIInput yearComponent  = (UIInput) findComponent("year");
        int day =
                 Integer.parseInt((String) dayComponent.getSubmittedValue());
        int month =
                 Integer.parseInt((String) monthComponent.getSubmittedValue());
        int year =
                 Integer.parseInt((String) yearComponent.getSubmittedValue());
//        return new GregorianCalendar(year, month - 1, day).getTime();
        return LocalDate.of(year, month, day);
    }

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }

    @Override
    public Object getSubmittedValue() {
        return this;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        LocalDate date = (LocalDate) getValue();
//        calendar.setTime((Date) getValue());

        UIInput dayComponent   = (UIInput) findComponent("day");
        UIInput monthComponent = (UIInput) findComponent("month");
        UIInput yearComponent  = (UIInput) findComponent("year");

        dayComponent.setValue(date.getDayOfMonth());
        monthComponent.setValue(date.getMonthValue());
        yearComponent.setValue(date.getYear());

//        dayComponent.setValue(calendar.get(Calendar.DATE));
//        monthComponent.setValue(calendar.get(Calendar.MONTH) + 1);
//        yearComponent.setValue(calendar.get(Calendar.YEAR));

        super.encodeBegin(context);
    }

    /* Getter methods moved to component from Dates class */

    public Set<Integer> getDays() {
        LocalDate date = (LocalDate) getValue();
        int month = date.getMonthValue();
        int year = date.getYear();
//        calendar.setTime((Date) getValue());
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int year  = calendar.get(Calendar.YEAR);
        int end = Dates.DAYS[month];
        if (month == 2 && Dates.getLeapYears().contains(year)) end = 29;
        return Dates.getDays().headSet(end, true);
    }

    public Map<String, Integer> getMonths() {
        return Dates.getMonths();
    }

    public int[] getYears() {
        return Dates.getYears();
    }

}
