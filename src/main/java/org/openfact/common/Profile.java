package org.openfact.common;

public class Profile {

    private enum ProfileValue {
        PRODUCT, PREVIEW, COMMUNITY
    }

    private static ProfileValue value = ProfileValue.COMMUNITY;

    public static String getName() {
        return value.name().toLowerCase();
    }

    public static boolean isPreviewEnabled() {
        return value.ordinal() >= ProfileValue.PREVIEW.ordinal();
    }

}
