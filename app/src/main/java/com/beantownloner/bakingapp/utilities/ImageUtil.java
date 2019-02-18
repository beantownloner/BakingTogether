package com.beantownloner.bakingapp.utilities;

import com.beantownloner.bakingapp.R;

public class ImageUtil {
    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String BROWNIES = "Brownies";
    private static final String YELLOW_CAKE = "Yellow Cake";
    private static final String CHEESECAKE = "CheeseCake";

    public static int getImageResId(String name) {
        if (name.equalsIgnoreCase(NUTELLA_PIE)) {
            return R.drawable.nutella_pie;
        } else if (name.equalsIgnoreCase(BROWNIES)) {
            return R.drawable.brownies;
        } else if (name.equalsIgnoreCase(YELLOW_CAKE)) {
            return R.drawable.yellow_cake;
        } else if (name.equalsIgnoreCase(CHEESECAKE)) {
            return R.drawable.cheesecake;
        } else {
            return R.drawable.brownies;
        }

    }
}
