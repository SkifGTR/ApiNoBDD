package bankApi;

import java.util.Comparator;

public class CurrencyComparatorAscending implements Comparator<CurrencyData> {
    @Override
    public int compare(CurrencyData o1, CurrencyData o2) {
        double compared = Double.compare(o1.getRateCross(), o2.getRateCross());
        return (int) compared;
    }
}
