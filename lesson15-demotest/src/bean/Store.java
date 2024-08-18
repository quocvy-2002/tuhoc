package bean;
import functional.StorePredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Predicate;

public class Store {
	private String storeId;
	private String referenceStoreId;
	private Integer stockPreviousDay;
	private Integer expectedSales;
	private boolean isSelected;
	
	public Store() {
	}

	@Override
	public String toString() {
		return "Store [storeId=" + storeId + ", referenceStoreId=" + referenceStoreId + ", stockPreviousDay="
				+ stockPreviousDay + ", expectedSales=" + expectedSales + ", isSelected=" + isSelected + "]";
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getReferenceStoreId() {
		return referenceStoreId;
	}

	public void setReferenceStoreId(String referenceStoreId) {
		this.referenceStoreId = referenceStoreId;
	}

	public Integer getStockPreviousDay() {
		return stockPreviousDay;
	}

	public void setStockPreviousDay(Integer stockPreviousDay) {
		this.stockPreviousDay = stockPreviousDay;
	}

	public Integer getExpectedSales() {
		return expectedSales;
	}

	public void setExpectedSales(Integer expectedSales) {
		this.expectedSales = expectedSales;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Store(String storeId, String referenceStoreId, Integer stockPreviousDay, Integer expectedSales,
			boolean isSelected) {
		super();
		this.storeId = storeId;
		this.referenceStoreId = referenceStoreId;
		this.stockPreviousDay = stockPreviousDay;
		this.expectedSales = expectedSales;
		this.isSelected = isSelected;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expectedSales, isSelected, referenceStoreId, stockPreviousDay, storeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		return Objects.equals(expectedSales, other.expectedSales) && isSelected == other.isSelected
				&& Objects.equals(referenceStoreId, other.referenceStoreId)
				&& Objects.equals(stockPreviousDay, other.stockPreviousDay) && Objects.equals(storeId, other.storeId);
	}
	public static List<Store> ofSelectedItem(List<Store> inventory  , Predicate<Store> predicate){
		List<Store> result = new ArrayList<>();
		for (Store store: inventory) {
			if (predicate.test(store)) {
				result.add(store);
			}
		}
		return result;
	} 
	public static Map<String, Double> fillExpectedSales(List<Store> result,List<Store> inventory){
		Map<String, Double> fillexpectedsales = new HashMap<String, Double>();
		for (Store store: result) {
			if(store.expectedSales == null && store.referenceStoreId !=  null ) {
				fillexpectedsales.put(store.storeId, typecast(checkReferenceStoreId(result, store.referenceStoreId)));
			}
			else if(store.expectedSales == null && store.referenceStoreId ==  null ||checkReferenceStoreId(result, store.referenceStoreId)==null) {
				fillexpectedsales.put(store.storeId, meanValueExpectedSales(inventory));
			}else if( meanValueExpectedSales(result) ==0) {
				System.out.println( "Expected sales cannot be calculated. Please add a reference store or include stores with expected sales for interpolation");
				break;
			}
			else if(store.expectedSales != null) {
				fillexpectedsales.put(store.storeId, typecast(store.expectedSales));
			}
		}
		
		return fillexpectedsales;
	}
	public static Integer checkReferenceStoreId(List<Store> inventory, String referenceStoreId ) {
		Integer b  = 0;
		for (Store store: inventory) {
			if(store.storeId.equals(referenceStoreId)) {
				b  = store.expectedSales;
			}
				
		}
		return b;
	}
	public static double meanValueExpectedSales(List<Store> inventory ) {
		int sum = 0;
		double meanValue ;
		for (Store store: inventory) {
			sum += store.expectedSales;
		}
		meanValue = sum/inventory.size();
		return meanValue;
	}
	
	public static double typecast(Integer a ) {
		double expectedSalesDouble = (double) a;
		return expectedSalesDouble;

	}
	// bước 2
	public static Map<String, Double> allocationKeyStore(Map<String, Double> fillexpectedsales){
		Map<String, Double> allocationkeystore = new HashMap<String, Double>();
		double sum =0;
		for (Entry<String, Double> entry : fillexpectedsales.entrySet()) {
		    sum += entry.getValue();
		}
		for (Entry<String, Double> entry : fillexpectedsales.entrySet()) {
			allocationkeystore.put(entry.getKey(), entry.getValue()/sum);
		}
		return allocationkeystore;
	}
	// bước 3
	public static Map<String, Double> amountAllocated(Map<String, Double> allocationkeystore,Integer StockOfPreviousDayStore,Integer warehouseAllocationAmount,List<Store> inventory){
		Map<String, Double> amountallocated = new HashMap<String, Double>();
		for (Store store: inventory) {
			for (Entry<String, Double> entry : allocationkeystore.entrySet()) {
			    amountallocated.put(entry.getKey(),entry.getValue()*(warehouseAllocationAmount + StockOfPreviousDayStore )- store.stockPreviousDay);
		}
		}
		
		return amountallocated;
	}
	//Integer warehouseAllocationAmount số lượng hàng cần cấp phát từ kho
	public static Integer totalInventory(List<Store> inventory) {
		Integer sum = 0;
		for (Store store: inventory) {
			sum += store.stockPreviousDay;
		}
		return sum ;
	}
	public static void main(String[] args) {
		
		Integer warehouseAllocationAmount = 110;
		List<Store> inventory = new ArrayList<Store>();
        inventory.add(new Store("S001", null, 100, 120, true));
        inventory.add(new Store("S002", "S001", 50, 70, true));
        inventory.add(new Store("S003", null, 200, 150, false));
        inventory.add(new Store("S004", "S003", 30, 60, true));
        inventory.add(new Store("S005", null, 10, 20, false));
        inventory.add(new Store("S006", "S004", 90, 110, true));
        inventory.add(new Store("S007", "S002", 40, 80, true));
        inventory.add(new Store("S008", null, 130, 140, true));
        inventory.add(new Store("S009", "S008", 70, 100, false));
        inventory.add(new Store("S010", "S006", 60, 90, true));
		
        System.out.println(" những của hàng được chọn");
        List<Store> result  =ofSelectedItem(inventory, s -> s.isSelected ==true );
        for(Store store: result) {
        	System.out.println(store);
        }
        System.out.println("\n");
        System.out.println("===============");
        System.out.println("in ra id của hàng với ectedSales của mỗi của hàng");
        Map<String, Double> fillexpectedsales = fillExpectedSales(result,inventory);
        for (Map.Entry<String, Double> entry : fillexpectedsales.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        System.out.println("\n");
        System.out.println("===============");
        System.out.println("in ra id của hàng với allocationkeystore của mỗi của hàng");
        Map<String, Double> allocationkeystore = allocationKeyStore(fillexpectedsales);
        for (Map.Entry<String, Double> entry : allocationkeystore.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        System.out.println("\n");
        System.out.println("===============");
        System.out.println("in ra id của hàng với số sản phẩm thêm vào (chưa làm tròn) của mỗi của hàng");
        Map<String, Double> amountallocated = amountAllocated(allocationkeystore, totalInventory(result), 110,result);
        // nếu <0 thì trả về giá trị bằng 0
        for (Map.Entry<String, Double> entry : amountallocated.entrySet()) {
        	if(entry.getValue() < 0) {
        		amountallocated.put(entry.getKey(), 0d);
        	}
        }
        for (Map.Entry<String, Double> entry : amountallocated.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        double sum =0;
        for (Map.Entry<String, Double> entry : amountallocated.entrySet()) {
        	sum += entry.getValue();
        }
        System.out.println(sum);
	}
	
}
