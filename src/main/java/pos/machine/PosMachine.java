package pos.machine;

import com.sun.tools.javac.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static pos.machine.ItemDataLoader.loadAllItemInfos;

public class PosMachine {

    public String printReceipt(List<String> barcodes) {
        return null;
    }

    private List<ItemInfo> getItemInfoByBarCodes(List<String> barcodes) {
        List<ItemInfo> allItemInfos = loadAllItemInfos();
        List<ItemInfo> mappedItem = new ArrayList<>();
        for (String barcode : barcodes) {
            allItemInfos.forEach(item -> {
                if (barcode.equals(item.getBarcode())) {
                    mappedItem.add(item);
                }
            });
        }
        return mappedItem;
    }

    private List<ItemInfo> getItemDetail(List<ItemInfo> itemInfos) {
        List<ItemInfo> distinctItemInfo = getQuantity(itemInfos);
        distinctItemInfo.forEach(item-> {
            getSubtotal(item);
        });
        return distinctItemInfo;
    }

    private List<ItemInfo> getQuantity(List<ItemInfo> itemInfos) {
        List<ItemInfo> itemInfosWithQuantity = new ArrayList<>();
        itemInfos.forEach(item -> {
            item.setQuantity(Collections.frequency(itemInfos, item));
            if (!(itemInfosWithQuantity.contains(item))) {
                itemInfosWithQuantity.add(item);
            }
        });
        return itemInfosWithQuantity;
    }
}
