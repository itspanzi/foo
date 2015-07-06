package com.staples.runatic.model;

/**
 * This class represents a row in the session data store (either the internal or the third party data)
 */
public class SessionEntry {
    private final long orderId;
    private final int unitPriceInCents;
    private final int merchantDiscountInCents;
    private final int runaDiscountInCents;
    private final String sessionType;

    public SessionEntry(long orderId, int unitPriceInCents, int merchantDiscountInCents, int runaDiscountInCents, String sessionType) {
        this.orderId = orderId;
        this.unitPriceInCents = unitPriceInCents;
        this.merchantDiscountInCents = merchantDiscountInCents;
        this.runaDiscountInCents = runaDiscountInCents;
        this.sessionType = sessionType;
    }

    public static SessionEntry fromRunaDataStore(String[] headers, String[] cells) {
        return new SessionEntry(Long.parseLong(cells[0]), toInt(cells[1]), toInt(cells[2]), toInt(cells[3]), cells[4]);
    }

    public static SessionEntry fromExternalDataStore(String[] headers, String[] cells) {
        return new SessionEntry(Long.parseLong(cells[0]), toInt(toFloat(cells[1]) * 100), toInt(toFloat(cells[3]) * 100), toInt(toFloat(cells[2]) * 100), cells[4].toLowerCase());
    }

    private static int toInt(float rate) {
        return Math.round(rate);
    }

    private static float toFloat(String cell) {
        return Float.parseFloat(cell);
    }

    private static int toInt(String cell) {
        return Integer.parseInt(cell);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntry that = (SessionEntry) o;

        if (orderId != that.orderId) return false;
        if (unitPriceInCents != that.unitPriceInCents) return false;
        if (merchantDiscountInCents != that.merchantDiscountInCents) return false;
        if (runaDiscountInCents != that.runaDiscountInCents) return false;
        return sessionType.equals(that.sessionType);
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + unitPriceInCents;
        result = 31 * result + merchantDiscountInCents;
        result = 31 * result + runaDiscountInCents;
        result = 31 * result + sessionType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SessionEntry{" +
                "orderId=" + orderId +
                ", unitPriceInCents=" + unitPriceInCents +
                ", merchantDiscountInCents=" + merchantDiscountInCents +
                ", runaDiscountInCents=" + runaDiscountInCents +
                ", sessionType='" + sessionType + '\'' +
                '}';
    }
}
