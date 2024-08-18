package functional;
import bean.*;

@FunctionalInterface
public interface StorePredicate {
	boolean test(Store store);
}
