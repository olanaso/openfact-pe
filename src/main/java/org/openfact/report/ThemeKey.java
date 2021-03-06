package org.openfact.report;

public class ThemeKey {

    private String name;
    private ReportTheme.Type type;

    public static ThemeKey get(String name, ReportTheme.Type type) {
        return new ThemeKey(name, type);
    }

    private ThemeKey(String name, ReportTheme.Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportTheme.Type getType() {
        return type;
    }

    public void setType(ReportTheme.Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThemeKey themeKey = (ThemeKey) o;

        if (name != null ? !name.equals(themeKey.name) : themeKey.name != null) return false;
        if (type != themeKey.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

}
