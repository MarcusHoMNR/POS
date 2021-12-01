package pos.machine;

import com.sun.tools.javac.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static pos.machine.ItemDataLoader.loadAllItemInfos;

public class PosMachine {

    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> itemInfos = getItemInfoByBarCodes(barcodes);
        List<ItemInfo> itemInfosWithDetail = getItemDetail(itemInfos);
        int total = getTotal(itemInfosWithDetail);

        StringBuilder receipt = new StringBuilder("***<store earning no money>Receipt***\n");

        for (ItemInfo itemInfo: itemInfosWithDetail) {
            receipt.append("Name: ").append(itemInfo.getName()).append(", Quantity: ").append(itemInfo.getQuantity()).append(", Unit price: ").append(itemInfo.getPrice()).append(" (yuan), Subtotal: ").append(itemInfo.getSubtotal()).append(" (yuan)\n");
        }

        receipt.append( "----------------------\n");
        receipt.append( "Total: " + total + " (yuan)\n");
        receipt.append( "**********************");
        return String.valueOf(receipt);
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
        List<ItemInfo> distinctItemInfo = getDistinctListAndSetQuantity(itemInfos);
        distinctItemInfo.forEach(this::setSubtotal);
        return distinctItemInfo;
    }

    private List<ItemInfo> getDistinctListAndSetQuantity(List<ItemInfo> itemInfos) {
        List<ItemInfo> distinctItemInfosWithQuantity = new ArrayList<>();
        itemInfos.forEach(item -> {
            item.setQuantity(Collections.frequency(itemInfos, item));
            if (!(distinctItemInfosWithQuantity.contains(item))) {
                distinctItemInfosWithQuantity.add(item);
            }
        });
        return distinctItemInfosWithQuantity;
    }

    private void setSubtotal (ItemInfo itemInfo) {
        itemInfo.setSubtotal(itemInfo.getQuantity() * itemInfo.getPrice());
    }

    private int getTotal (List<ItemInfo> distinctItemInfo) {
        int total = 0;
        for (ItemInfo item: distinctItemInfo) {
            total+= item.getSubtotal();
        }
        return total;
    }
}
