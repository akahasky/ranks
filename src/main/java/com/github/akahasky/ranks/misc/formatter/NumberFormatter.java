package com.github.akahasky.ranks.misc.formatter;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);


    static { NUMBER_FORMAT.setMaximumFractionDigits(2); }

    public static String format(double value) { return NUMBER_FORMAT.format(value); }

}
