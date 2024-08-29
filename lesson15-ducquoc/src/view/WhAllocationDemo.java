package view;

import bean.*;
import model.*;
import utils.*;

import static utils.NumberUtils.bd;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class WhAllocationDemo {

	public static void main(String[] args) {

		// use data from DataModel

		// do calculation

		// print result step by step
		Integer PlanningAmount = 88;
		Integer Minimum = 5;
		Integer RequiredMinPlanningAmount = 50;
		Map<Item, List<Store>> mockStoresOfRefItemA77 = DataModel.mockStoresOfRefItemA77();
		Map<Item, List<Store>> mockStoresOfRefItemA55 = DataModel.mockStoresOfRefItemA55();
		Map<Integer, Integer> mockRefStores = DataModel.mockRefStores();
		Map<Integer, BigDecimal> mockRefWeights = DataModel.mockRefWeights();
		Map<Item, Map<Integer, BigDecimal>> testA77 = new HashMap<>();
		Map<Item, Map<Integer, BigDecimal>> testA55 = new HashMap<>();
		Map<Integer, BigDecimal> resultA = new HashMap<>();
		Map<Integer, BigDecimal> resultA77 = new HashMap<>();
		Map<Integer, BigDecimal> StoreTrend = StoreTrend();
		Map<Integer, BigDecimal> Wh = Wh();
		List<Store> listStore = new ArrayList<>();
		for (Map.Entry<Item, List<Store>> entry : mockStoresOfRefItemA77.entrySet()) {
			listStore = entry.getValue();
		}
		if (CheckForPlanningAmount(PlanningAmount, RequiredMinPlanningAmount)) {
			testA77 = FillingGapsByReferencesOrAverage(mockStoresOfRefItemA77, mockRefStores);
			testA55 = FillingGapsByReferencesOrAverage(mockStoresOfRefItemA55, mockRefStores);
			System.out.println("\n");
			System.out.println(" step 2");
			System.out.println("\n");
			for (Map.Entry<Item, Map<Integer, BigDecimal>> entry : testA77.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
				// in ra kết quả step 2 với A77
			}
			for (Map.Entry<Item, Map<Integer, BigDecimal>> entry : testA55.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
				// in ra kết quả step 2 với A55
			}
			System.out.println("\n");
			System.out.println(" step 3");
			System.out.println("\n");
			resultA = resultStep3(testA77, testA55, mockRefWeights, StoreTrend);

			for (Map.Entry<Integer, BigDecimal> entry : resultA.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 4");
			System.out.println("\n");
			Map<Integer, Map<BigDecimal, Integer>> DemandWH = resultStep4(resultA, listStore);
			for (Map.Entry<Integer, Map<BigDecimal, Integer>> entry : DemandWH.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}

			Map<Integer, BigDecimal> SumWH = sumWh(DemandWH, Wh);
			for (Map.Entry<Integer, BigDecimal> entry : SumWH.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 5");
			System.out.println("\n");
			BigDecimal sum = sum(SumWH);
			Map<Integer, BigDecimal> Share = Share(SumWH, sum);
			for (Map.Entry<Integer, BigDecimal> entry : Share.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 6");
			System.out.println("\n");
			Map<Integer, BigDecimal> Allocate = Allocate(Share, PlanningAmount);
			for (Map.Entry<Integer, BigDecimal> entry : Allocate.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 7");
			System.out.println("\n");
			Map<Integer, Integer> amountOfStores = amountOfStores(DemandWH, Allocate);
			for (Map.Entry<Integer, Integer> entry : amountOfStores.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			Map<Integer, BigDecimal> ApplyMin = ApplyMin(Allocate, Minimum, amountOfStores);
			for (Map.Entry<Integer, BigDecimal> entry : ApplyMin.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 8");
			System.out.println("\n");
			Map<Integer, BigDecimal> recalculateShare = recalculateShare(Allocate, Minimum, amountOfStores);
			for (Map.Entry<Integer, BigDecimal> entry : recalculateShare.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			BigDecimal sumStep8 = sumStep8(recalculateShare);
			Map<Integer, BigDecimal> ShareStep8 = ShareStep8(recalculateShare, sumStep8);
			for (Map.Entry<Integer, BigDecimal> entry : ShareStep8.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 9");
			System.out.println("\n");
			BigDecimal remaining = remaining(Allocate, ApplyMin, PlanningAmount);
			Map<Integer, BigDecimal> Reallocate = Reallocate(ShareStep8, ApplyMin, remaining);
			for (Map.Entry<Integer, BigDecimal> entry : Reallocate.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
			System.out.println("\n");
			System.out.println(" step 10");
			System.out.println("\n");
			Map<Integer, Integer> roundValues = roundValues(Reallocate);
			for (Map.Entry<Integer, Integer> entry : roundValues.entrySet()) {
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}

			System.out.println("\n");
			System.out.println(" step 11");
			System.out.println("\n");
			// step 11
			Integer sumRoundValues = sumRoundValues(roundValues);
			System.out.println(sumRoundValues);
			if (sumRoundValues.compareTo(PlanningAmount) > 0) {
				Map<Integer, BigDecimal> Iteration = Iteration(Reallocate, roundValues);
				for (Map.Entry<Integer, BigDecimal> entry : Iteration.entrySet()) {
					System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
				}
				Map<Integer, Integer> fixRounding = fixRounding(Iteration, roundValues, sumRoundValues, PlanningAmount);
				for (Map.Entry<Integer, Integer> entry : fixRounding.entrySet()) {
					System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
				}
			}

		} else {
		}

	}

	public static Map<Integer, BigDecimal> Wh() {
		Map<Integer, BigDecimal> map = new HashMap<>();
		map.put(1, BigDecimal.valueOf(0.0));
		map.put(2, BigDecimal.valueOf(0.0));
		map.put(3, BigDecimal.valueOf(0.0));
		return map;
	}

	public static Map<Integer, BigDecimal> StoreTrend() {
		Map<Integer, BigDecimal> map = new HashMap<>();
		map.put(1, BigDecimal.valueOf(1.0));
		map.put(2, BigDecimal.valueOf(1.2));
		map.put(3, BigDecimal.valueOf(1.0));
		map.put(4, BigDecimal.valueOf(1.0));
		map.put(5, BigDecimal.valueOf(1.0));
		map.put(6, BigDecimal.valueOf(1.0));
		map.put(7, BigDecimal.valueOf(1.5));
		map.put(8, BigDecimal.valueOf(1.0));
		map.put(9, BigDecimal.valueOf(1.0));
		map.put(10, BigDecimal.valueOf(1.0));
		map.put(11, BigDecimal.valueOf(0.9));
		map.put(12, BigDecimal.valueOf(0.7));
		map.put(13, BigDecimal.valueOf(1.0));
		map.put(14, BigDecimal.valueOf(1.0));
		return map;
	}

	// step 1
	public static boolean CheckForPlanningAmount(Integer PlanningAmount, Integer RequiredMinPlanningAmount) {
		if (PlanningAmount > RequiredMinPlanningAmount) {
			return true;
		} else {
			return false;
		}

	}

	// step 2
	// lấy ra list trong map
	public static List<Store> returnList(Map<Item, List<Store>> mockStores) {
		List<Store> result = new ArrayList<>();
		for (Map.Entry<Item, List<Store>> entry : mockStores.entrySet()) {
			result.addAll(entry.getValue());
		}
		return result;
	}

// hàm trả kết quả step 2 
	public static Map<Item, Map<Integer, BigDecimal>> FillingGapsByReferencesOrAverage(
			Map<Item, List<Store>> mockStoresOfRefItem, Map<Integer, Integer> mockRefStores) {
		Map<Item, Map<Integer, BigDecimal>> test = new HashMap<>();

		for (Map.Entry<Item, List<Store>> entry : mockStoresOfRefItem.entrySet()) {
			Item item = entry.getKey();
			List<Store> listStore = entry.getValue();

			Map<Integer, BigDecimal> storage = new HashMap<>();

			for (Store store : listStore) {
				if (store.getPotential().compareTo(BigDecimal.ZERO) != 0) {
					storage.put(store.getId(), store.getPotential());
				} else {
					storage.put(store.getId(), getReferenceValue(listStore, store.getId(), mockRefStores));
				}
			}
			test.put(item, storage);
		}

		return test;
	}

// hàm tính giá trị trung bình
	public static BigDecimal TrungBinhValue(List<Store> listStore) {
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal count = BigDecimal.ZERO;
		BigDecimal cong = BigDecimal.ONE;

		for (Store store : listStore) {
			if (store.getPotential().compareTo(BigDecimal.ZERO) != 0) {
				sum = sum.add(store.getPotential());
				count = count.add(cong);
			}
		}

		if (count.compareTo(BigDecimal.ZERO) > 0) {
			return sum.divide(count, 4, RoundingMode.HALF_UP);
		} else {
			return BigDecimal.ZERO;
		}
	}

	// hàm trả về giá trị tham chiếu tham chiếu
	public static BigDecimal getReferenceValue(List<Store> listStore, Integer storeID,
			Map<Integer, Integer> mockRefStores) {
		BigDecimal getreferencevalue = new BigDecimal("0");
		for (Map.Entry<Integer, Integer> entry : mockRefStores.entrySet()) {
			if (storeID == entry.getKey()) {
				if (getStore(entry.getValue(), listStore).getPotential() != BigDecimal.ZERO) {
					getreferencevalue = getStore(entry.getValue(), listStore).getPotential();
				} else if (getStore(entry.getValue(), listStore).getPotential() == BigDecimal.ZERO) {
					getreferencevalue = TrungBinhValue(listStore);
				}
			} else {
				getreferencevalue = TrungBinhValue(listStore);
			}
		}
		return getreferencevalue;
	}

//hàm trả về store
	public static Store getStore(Integer storeId, List<Store> listStore) {
		Store store1 = new Store();
		for (Store store : listStore) {
			if (storeId == store.getId()) {
				store1 = store;
			}
		}
		return store1;

	}

	// step 3
	// map l;ưu item và map con lưu store và Demand new campaign

	public static Map<Integer, BigDecimal> resultStep3(
			Map<Item, Map<Integer, BigDecimal>> FillingGapsByReferencesOrAverage1,
			Map<Item, Map<Integer, BigDecimal>> FillingGapsByReferencesOrAverage2,
			Map<Integer, BigDecimal> mockRefWeights, Map<Integer, BigDecimal> StoreTrend) {

		Map<Integer, BigDecimal> result = new HashMap<>();
		Map<Integer, BigDecimal> result1 = new HashMap<>();
		Map<Integer, BigDecimal> result2 = new HashMap<>();
		BigDecimal getreferencevalue = BigDecimal.ZERO;

		for (Map.Entry<Item, Map<Integer, BigDecimal>> entry1 : FillingGapsByReferencesOrAverage1.entrySet()) {
			Item item = entry1.getKey();
			Map<Integer, BigDecimal> map = entry1.getValue();

			for (Map.Entry<Integer, BigDecimal> entry : mockRefWeights.entrySet()) {
				if (item.getId().equals(entry.getKey())) {
					for (Map.Entry<Integer, BigDecimal> entry2 : map.entrySet()) {
						result1.put(entry2.getKey(), entry2.getValue().multiply(entry.getValue()));
					}
				}
			}
		}

		for (Map.Entry<Item, Map<Integer, BigDecimal>> entry1 : FillingGapsByReferencesOrAverage2.entrySet()) {
			Item item = entry1.getKey();
			Map<Integer, BigDecimal> map = entry1.getValue();

			for (Map.Entry<Integer, BigDecimal> entry : mockRefWeights.entrySet()) {
				if (item.getId().equals(entry.getKey())) {
					for (Map.Entry<Integer, BigDecimal> entry2 : map.entrySet()) {
						result2.put(entry2.getKey(), entry2.getValue().multiply(entry.getValue()));
					}
				}
			}
		}

		for (Map.Entry<Integer, BigDecimal> entry : mockRefWeights.entrySet()) {
			getreferencevalue = getreferencevalue.add(entry.getValue());
		}

		for (Map.Entry<Integer, BigDecimal> entry1 : result1.entrySet()) {
			Integer a = entry1.getKey();
			for (Map.Entry<Integer, BigDecimal> entry2 : result2.entrySet()) {
				Integer b = entry2.getKey();
				for (Map.Entry<Integer, BigDecimal> entry3 : StoreTrend.entrySet())
					if (a.equals(b) && entry3.getKey().equals(b)) {
						result.put(a, entry1.getValue().add(entry2.getValue())
								.divide(getreferencevalue, 2, RoundingMode.HALF_UP).multiply(entry3.getValue()));
					}
			}
		}

		return result;
	}

	// step 4 lưu 1 map chứa key = item map con chứa key = kho value bằng Demand WH:

	public static Map<Integer, Map<BigDecimal, Integer>> resultStep4(Map<Integer, BigDecimal> resultStep3,
			List<Store> listStore) {
		Map<Integer, Map<BigDecimal, Integer>> result = new HashMap<>();

		for (Map.Entry<Integer, BigDecimal> entry : resultStep3.entrySet()) {
			Map<BigDecimal, Integer> valueMap = getValue(entry.getKey(), entry.getValue(), listStore);
			result.put(entry.getKey(), valueMap);
		}

		return result;
	}

	public static Map<BigDecimal, Integer> getValue(Integer key, BigDecimal value, List<Store> listStore) {
		Map<BigDecimal, Integer> result = new HashMap<>();

		for (Store store : listStore) {
			if (key.equals(store.getId())) {
				result.put(value, store.getWhId());
				break;
			}
		}

		return result;
	}

	// hàm tính tổng kho
	public static Map<Integer, BigDecimal> sumWh(Map<Integer, Map<BigDecimal, Integer>> resultStep4,
			Map<Integer, BigDecimal> Wh) {
		Map<Integer, BigDecimal> result = new HashMap<>(Wh);

		for (Map.Entry<Integer, Map<BigDecimal, Integer>> entry : resultStep4.entrySet()) {
			Map<BigDecimal, Integer> result1 = entry.getValue();
			for (Map.Entry<BigDecimal, Integer> entry1 : result1.entrySet()) {
				Integer whId = entry1.getValue();
				BigDecimal valueToAdd = entry1.getKey();
				result.put(whId, result.get(whId).add(valueToAdd));
			}
		}
		return result;
	}

	// lấy ra store từ nid
	public static Store getStore(List<Store> list, Integer id) {
		Store s = new Store();
		for (Store store : list) {
			if (store.getId() == id) {
				s = store;
			}
		}
		return s;
	}

	// step 5
	public static BigDecimal sum(Map<Integer, BigDecimal> sumWh) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Map.Entry<Integer, BigDecimal> entry1 : sumWh.entrySet()) {
			sum = entry1.getValue().add(sum);
		}
		return sum;
	}

	public static Map<Integer, BigDecimal> Share(Map<Integer, BigDecimal> sumWh, BigDecimal sum) {
		Map<Integer, BigDecimal> result = new HashMap<>(sumWh);
		BigDecimal a = new BigDecimal("100.0");
		for (Map.Entry<Integer, BigDecimal> entry1 : result.entrySet()) {
			result.put(entry1.getKey(), result.get(entry1.getKey()).divide(sum, 4, RoundingMode.HALF_UP).multiply(a));
		}

		return result;
	}

	// step 6
	public static Map<Integer, BigDecimal> Allocate(Map<Integer, BigDecimal> sumWh, Integer PlanningAmount) {
		Map<Integer, BigDecimal> result = new HashMap<>(sumWh);
		BigDecimal a = new BigDecimal(PlanningAmount);
		BigDecimal b = new BigDecimal("100.00");
		for (Map.Entry<Integer, BigDecimal> entry1 : sumWh.entrySet()) {
			result.put(entry1.getKey(), result.get(entry1.getKey()).divide(b, 4, RoundingMode.HALF_UP).multiply(a));
		}
		return result;
	}

	// step 7
	public static Map<Integer, Integer> amountOfStores(Map<Integer, Map<BigDecimal, Integer>> resultStep4,
			Map<Integer, BigDecimal> Allocate) {
		Map<Integer, Integer> result = new HashMap<>();

		for (Map.Entry<Integer, Map<BigDecimal, Integer>> entry : resultStep4.entrySet()) {
			Map<BigDecimal, Integer> result1 = entry.getValue();

			for (Map.Entry<Integer, BigDecimal> entry1 : Allocate.entrySet()) {
				for (Map.Entry<BigDecimal, Integer> entry2 : result1.entrySet()) {
					if (entry1.getKey().equals(entry2.getValue())) {
						result.put(entry1.getKey(), result.getOrDefault(entry1.getKey(), 0) + 1);
					}
				}
			}
		}

		return result;
	}

	public static Map<Integer, BigDecimal> ApplyMin(Map<Integer, BigDecimal> Allocate, Integer MinPerStore,
			Map<Integer, Integer> amountOfStores) {
		Map<Integer, BigDecimal> result = new HashMap<>(Allocate);
		BigDecimal a = new BigDecimal(MinPerStore);
		for (Map.Entry<Integer, BigDecimal> entry : Allocate.entrySet()) {
			for (Map.Entry<Integer, Integer> entry1 : amountOfStores.entrySet()) {
				BigDecimal b = new BigDecimal(entry1.getValue());
				if (entry.getKey().equals(entry1.getKey())) {
					if (entry.getValue().compareTo(b.multiply(a)) < 0) {
						result.put(entry.getKey(), a.multiply(b));
					}
				}

			}

		}
		return result;
	}
	// step 8

	public static Map<Integer, BigDecimal> recalculateShare(Map<Integer, BigDecimal> Allocate, Integer MinPerStore,
			Map<Integer, Integer> amountOfStores) {
		Map<Integer, BigDecimal> result = new HashMap<>(Allocate);
		BigDecimal a = new BigDecimal(MinPerStore);
		for (Map.Entry<Integer, BigDecimal> entry : Allocate.entrySet()) {
			for (Map.Entry<Integer, Integer> entry1 : amountOfStores.entrySet()) {
				BigDecimal b = new BigDecimal(entry1.getValue());
				if (entry.getKey().equals(entry1.getKey())) {
					if (entry.getValue().compareTo(b.multiply(a)) < 0) {
						result.put(entry.getKey(), BigDecimal.ZERO);
					}
				}

			}

		}
		return result;
	}

	public static BigDecimal sumStep8(Map<Integer, BigDecimal> recalculateShare) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Map.Entry<Integer, BigDecimal> entry1 : recalculateShare.entrySet()) {
			sum = entry1.getValue().add(sum);
		}
		return sum;
	}

	public static Map<Integer, BigDecimal> ShareStep8(Map<Integer, BigDecimal> recalculateShare, BigDecimal sumStep8) {
		Map<Integer, BigDecimal> result = new HashMap<>(recalculateShare);
		BigDecimal a = new BigDecimal("100.00");
		for (Map.Entry<Integer, BigDecimal> entry1 : result.entrySet()) {
			result.put(entry1.getKey(), result.get(entry1.getKey())
					.divide(sumStep8(recalculateShare), 4, RoundingMode.HALF_UP).multiply(a));
		}

		return result;
	}

	// step 9
	public static BigDecimal remaining(Map<Integer, BigDecimal> Allocate, Map<Integer, BigDecimal> ApplyMin,
			Integer PlanningAmount) {
		BigDecimal remaining = new BigDecimal(PlanningAmount);
		for (Map.Entry<Integer, BigDecimal> entry : Allocate.entrySet()) {
			for (Map.Entry<Integer, BigDecimal> entry1 : ApplyMin.entrySet()) {
				if (entry.getKey().equals(entry1.getKey()) && !entry.getValue().equals(entry1.getValue())) {
					remaining = remaining.subtract(entry1.getValue());
				}
			}
		}
		return remaining;
	}

	// tham số thứ 1 là % số 2 là step 7
	public static Map<Integer, BigDecimal> Reallocate(Map<Integer, BigDecimal> ShareStep8,
			Map<Integer, BigDecimal> ApplyMin, BigDecimal remaining) {
		Map<Integer, BigDecimal> result = new HashMap<>(ApplyMin);
		for (Map.Entry<Integer, BigDecimal> entry : ShareStep8.entrySet()) {
			for (Map.Entry<Integer, BigDecimal> entry1 : ApplyMin.entrySet()) {
				if (entry.getValue().equals(new BigDecimal("0.000000"))) {
					result.put(entry.getKey(), entry1.getValue());
				} else {
					result.put(entry.getKey(), entry.getValue().multiply(remaining).divide(new BigDecimal("100.0")));
				}
			}
		}
		return result;
	}

	// step 10
	public static Map<Integer, Integer> roundValues(Map<Integer, BigDecimal> Reallocate) {
		Map<Integer, Integer> result = new HashMap<>();

		for (Map.Entry<Integer, BigDecimal> entry : Reallocate.entrySet()) {
			BigDecimal value = entry.getValue();
			BigDecimal roundedValue = value.setScale(0, RoundingMode.HALF_UP);
			if (value.subtract(roundedValue).compareTo(BigDecimal.ZERO) > 0) {
				roundedValue = roundedValue.add(BigDecimal.ONE);
			}
			result.put(entry.getKey(), roundedValue.intValue());
		}
		return result;
	}

	// step 11
	public static Integer sumRoundValues(Map<Integer, Integer> roundValues) {
		Integer sum = 0;
		for (Map.Entry<Integer, Integer> entry1 : roundValues.entrySet()) {
			sum = entry1.getValue() + sum;
		}
		return sum;
	}

	public static Map<Integer, BigDecimal> Iteration(Map<Integer, BigDecimal> Reallocate,
			Map<Integer, Integer> roundValues) {
		Map<Integer, BigDecimal> result = new HashMap<>();
		for (Map.Entry<Integer, BigDecimal> entry : Reallocate.entrySet()) {
			for (Map.Entry<Integer, Integer> entry1 : roundValues.entrySet()) {
				BigDecimal a = new BigDecimal(entry1.getValue());
				if (entry.getKey().equals(entry1.getKey())) {
					result.put(entry.getKey(), a.subtract(entry.getValue()));
				}
			}
		}
		return result;
	}
	public static Map<Integer, Integer> fixRounding(
            Map<Integer, BigDecimal> iterationMap, 
            Map<Integer, Integer> roundValues, 
            Integer totalAllocation, 
            Integer planningAmountCountry) {
		
        Map<Integer, Integer> result = new HashMap<>(roundValues);
        Integer sub = totalAllocation - planningAmountCountry;
        for (Map.Entry<Integer, BigDecimal> entry : iterationMap.entrySet()) {
            if (entry.getValue().equals(findMaxValue(iterationMap))) {
                result.put(entry.getKey(), result.getOrDefault(entry.getKey(), 0) - 1);
                Integer sum = sumRoundValues(result);
                if (sum.equals(planningAmountCountry)) {
                    break; 
                }
            }
        }
        return result;
        
	}
	 public static BigDecimal findMaxValue(Map<Integer, BigDecimal> map) {
	        BigDecimal max = BigDecimal.ZERO;
	        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
	        	if(max.compareTo(entry.getValue())<0) {
	        		max = entry.getValue();
	        	}
	        }
	        return max;
	    }
}