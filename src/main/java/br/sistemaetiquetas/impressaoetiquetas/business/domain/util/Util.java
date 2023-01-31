package br.sistemaetiquetas.impressaoetiquetas.business.domain.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Util {


    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
