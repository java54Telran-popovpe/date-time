package telran.time;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class NextFriday13 implements TemporalAdjuster {

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return  getFirstFriday13StartingWithDateOf13( getFirstDateOf13( temporal ) ) ;
	}
	
	private Temporal getFirstFriday13StartingWithDateOf13(Temporal dateOf13 ) {
		Temporal result = dateOf13;
		while (result.get(ChronoField.DAY_OF_WEEK) != 5) {
			result = result.plus(1, ChronoUnit.MONTHS);
		}
		return result;
	}
	
	private Temporal getFirstDateOf13(Temporal temporal ) {
		Temporal result = temporal;
		do {
			result = result.plus(1, ChronoUnit.DAYS);
		} while (result.get(ChronoField.DAY_OF_MONTH) != 13);
		return result;
	}

}
