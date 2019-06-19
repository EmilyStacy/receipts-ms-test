/**
 *
 */
package com.aa.fly.receipts.domain;

import java.util.List;

/**
 * @author Shiva.Narendrula
 */
public class WifiReceipt {
    private List<WifiLineItem> wifiLineItems;

    public List<WifiLineItem> getWifiLineItems() {
        return wifiLineItems;
    }

    public void setWifiLineItems(List<WifiLineItem> wifiLineItems) {
        this.wifiLineItems = wifiLineItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("WifiReceipt{");
        if (wifiLineItems != null && !wifiLineItems.isEmpty()) {
            wifiLineItems.forEach(item -> builder.append(item.toString()));
        }
        builder.append('}');
        return builder.toString();
    }
}
