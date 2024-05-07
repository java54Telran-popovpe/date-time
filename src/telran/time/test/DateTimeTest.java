package telran.time.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import telran.time.BarMizvaAdjuster;
import telran.time.Friday13Range;
import telran.time.NextFriday13;
import telran.util.Arrays;

class DateTimeTest {

	@Test
	void introductionTest() {
		Temporal result;
		LocalDate birthDateAS = LocalDate.of(2024, 6, 6);
		LocalDate dateOf13OfAMonth =  birthDateAS.with(TemporalAdjusters.firstDayOfMonth()).plus(12, ChronoUnit.DAYS);
		if (birthDateAS.compareTo(dateOf13OfAMonth) < 0 )
			result = getFirstFriday13StartingWithDateOf13(dateOf13OfAMonth);
		else
			result = getFirstFriday13StartingWithDateOf13(dateOf13OfAMonth.plusMonths(1));
		System.out.printf("Next thursday from asp birth is %s\n", dateOf13OfAMonth);
		System.out.printf("Next Fri13 from birth is %s\n", result);
		 birthDateAS = LocalDate.parse("1800-02-28");
		 birthDateAS = birthDateAS.plusDays(1);
		System.out.printf("bithdate of ASP in standart format is %s\n", birthDateAS);
		
		System.out.printf("bithdate of ASP in  format dd/mm/yyyy is %s\n", 
				birthDateAS.format(DateTimeFormatter.ofPattern("MMM d.yy", Locale.forLanguageTag("he"))));
	}
	private Temporal getFirstFriday13StartingWithDateOf13(Temporal dateOf13 ) {
		Temporal result = dateOf13;
		while (result.get(ChronoField.DAY_OF_WEEK) != 5) {
			result = result.plus(1, ChronoUnit.MONTHS);
		}
		return result;
	}
	@Test
	void introductionTest1() {
		LocalDateTime d1 = LocalDateTime.parse("1990-01-30T00:00:00");
		LocalDateTime d2 = LocalDateTime.parse("2000-10-20T00:00:00");
		ChronoUnit unit = ChronoUnit.SECONDS;
		System.out.printf("difference between %s and %s in %s is %d", d1, d2, unit, unit.between(d1, d2) );
	}
	
	@Test
	void barMizvaAdjusterTest() {
		LocalDate birthDate = LocalDate.of(1799, 6, 6);
		LocalDate expected = LocalDate.of(1812, 6, 6);
		assertEquals(expected, birthDate.with( new BarMizvaAdjuster()));
	}
	
	@Test
	void nextFriday13Test( ) {
		LocalDate aDayBeforeFri13 = LocalDate.of(2023, 10, 12);
		LocalDate aDayOfFri13 = LocalDate.of(2023, 10, 13);
		
		assertEquals(LocalDate.of(2023, 10, 13), aDayBeforeFri13.with( new NextFriday13()));
		assertEquals(LocalDate.of(2024, 9, 13), aDayOfFri13.with( new NextFriday13()));
		assertEquals(LocalDateTime.of(2024, 9, 13, 0, 0), LocalDateTime.now().with( new NextFriday13()).truncatedTo(ChronoUnit.DAYS));
	}
	
	@Test
	void nextFriday13RangeTest( ) {

		
		final int THIRTEEN = 13;
		LocalDate[] expectedFri13Array = { 	LocalDate.of(2022, 5, THIRTEEN ), 
									LocalDate.of(2023, 1, THIRTEEN ),
									LocalDate.of(2023, 10, THIRTEEN ),
									LocalDate.of(2024, 9, THIRTEEN ),
									LocalDate.of(2024, 12, THIRTEEN ),
									LocalDate.of(2025, 6, THIRTEEN ),
									LocalDate.of(2026, 2, THIRTEEN ),
									LocalDate.of(2026, 3, THIRTEEN ),
									LocalDate.of(2026, 11, THIRTEEN ),};
		LocalDate[] actualFri13Array = {};
		Friday13Range friday13Range = Friday13Range.from( LocalDate.of(2022, 1, 1),  LocalDate.of(2027, 1, 1));
		for( Temporal temporal: friday13Range ) {
			actualFri13Array = Arrays.add(actualFri13Array, LocalDate.from(temporal));
		}
		
		assertArrayEquals(expectedFri13Array, actualFri13Array);
		assertEquals(expectedFri13Array.length, actualFri13Array.length);
		
		expectedFri13Array = new LocalDate[] { 		LocalDate.of(2023, 10, THIRTEEN ),
													LocalDate.of(2024, 9, THIRTEEN ),
													LocalDate.of(2024, 12, THIRTEEN ),
													LocalDate.of(2025, 6, THIRTEEN ),
													LocalDate.of(2026, 2, THIRTEEN ),
													LocalDate.of(2026, 3, THIRTEEN ),};
		
		actualFri13Array = new LocalDate[] {};
		friday13Range = Friday13Range.from( LocalDate.of(2023, 1, THIRTEEN),  LocalDate.of(2026, 11, 13));
		for( Temporal temporal: friday13Range ) {
			actualFri13Array = Arrays.add(actualFri13Array, LocalDate.from(temporal));
		}
		
		assertArrayEquals(expectedFri13Array, actualFri13Array);
		assertEquals(expectedFri13Array.length, actualFri13Array.length);
		
		assertThrowsExactly(IllegalArgumentException.class, () -> Friday13Range.from( LocalDate.now(),  LocalDate.of(2021, 11, 13)));
		//assertThrowsExactly(IllegalArgumentException.class, () -> Friday13Range.from( Instant.now(),  LocalDate.of(2021, 11, 13)));
		
	}
	
	@Test
	void displayCurrentDateTimeTest( ) {
		//the following test implies printing out
		//all current date/time in Time Zones containing string Canada
		
		displayCurrentDateTime("Canada");
	}
	
	private void displayCurrentDateTime(String zonePart) {
		//the following test implies printing out
		//all current date/time in Time Zones containing string zonePart
		//format <year>-<month>-<day>-<hh:mm>-<zone name>
		// TODO Auto-generated method stub
		//using ZoneDateTimeClass
		Instant instant = Instant.now();
		var availableTimeZones = ZoneId.getAvailableZoneIds();
		for( String zone: availableTimeZones) {
			if (zone.contains(zonePart)) {
				ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(zone));
				System.out.printf("%s\n", 
					zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm-zzzz", Locale.forLanguageTag("en"))));
			}
				
		}
		
	}
}
