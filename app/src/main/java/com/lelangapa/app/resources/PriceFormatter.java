package com.lelangapa.app.resources;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Created by andre on 08/05/17.
 */

public class PriceFormatter {
    private static DecimalFormat decimalFormat;
    private static DecimalFormatSymbols symbols;
    private static Number number;

    public static String formatPrice(String price) {
        if (decimalFormat == null) {
            symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            decimalFormat = new DecimalFormat("#,###", symbols);
        }
        try {
            number = decimalFormat.parse(price);
            return decimalFormat.format(number);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return price;
    }
}
