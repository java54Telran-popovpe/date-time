package telran.time.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

record MonthYear( int month, int year ) {
	
}


public class PrintCalendar {

	private static final int TITLE_OFFSET = 5;
	private static final int COLUMN_WIDTH = 4;
	private static DayOfWeek[] weekDays = DayOfWeek.values();

	public static void main(String[] args) {
		
		try {
			MonthYear monthYear = getMonthYear(args);
			DayOfWeek firstWeekDay = getFirstWeekDay(args);
			correctWeekDaysArray( firstWeekDay );
			printCalendar(  monthYear );
		} catch (RuntimeException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			System.out.println( e.getMessage()); 
		} 
		
	
	}

	private static void correctWeekDaysArray(DayOfWeek firstWeekDay) {
		if ( !firstWeekDay.equals(DayOfWeek.MONDAY)) {
			DayOfWeek[] correctedArray = new DayOfWeek[weekDays.length];
			int indexOfWeekDay = firstWeekDay.getValue() - 1;
			System.arraycopy(weekDays, indexOfWeekDay, 
					correctedArray, 0, 
					weekDays.length - indexOfWeekDay );
			System.arraycopy(weekDays, 0, 
					correctedArray, weekDays.length - indexOfWeekDay, 
					indexOfWeekDay );
			weekDays = correctedArray;
		}
	}

	private static DayOfWeek getFirstWeekDay(String[] args) throws Exception {
		return args.length < 3 ? DayOfWeek.MONDAY : parseWeekDay(args[ 2 ]);
	}

	private static DayOfWeek parseWeekDay(String string) throws Exception {
		String wdInUpperCase = string.toUpperCase();
		DayOfWeek[] allWeekDays = DayOfWeek.values();
		int i = 0;
		while ( i < allWeekDays.length && !allWeekDays[ i ].name().equals(wdInUpperCase)) {
			i++;
		}
		if ( i == allWeekDays.length )
			throw new Exception( String.format("Unknown argument %s: should be a weekday", string) );
		return allWeekDays[i];
	}

	private static void printCalendar(MonthYear monthYear) {
		printTitle( monthYear);
		printWeekDays();
		printDays( monthYear );
		
	}

	private static void printDays(MonthYear monthYear) {
		int nDays = getDaysInMonth(monthYear);
		int currentWeekDay = getFirstDayOfMonth( monthYear );
		int indexOfCurrentWeekDay = getIndexOfWeekDay( DayOfWeek.of(currentWeekDay));
		int firstOffset = getFirstOffset( indexOfCurrentWeekDay );
		System.out.printf("%s", " ".repeat(firstOffset));
		for ( int day = 1; day <= nDays; day++ ) {
			System.out.printf("%" + COLUMN_WIDTH + "d", day );
			if ( indexOfCurrentWeekDay == weekDays.length - 1 ) {
				indexOfCurrentWeekDay = 0;
				System.out.println();
			} else 
				indexOfCurrentWeekDay++;
		}
		
	}

	private static int getFirstOffset(int indexOfCurrentWeekDay) {
		
		return COLUMN_WIDTH * indexOfCurrentWeekDay;
	}

	private static int getIndexOfWeekDay( DayOfWeek weekDay) {
		int i = 0;
		while ( i < weekDays.length && !weekDays[i].equals(weekDay)) {
			i++;
		}
		return i; 
	}

	private static int getFirstDayOfMonth(MonthYear monthYear) {
		LocalDate ld = LocalDate.of(monthYear.year(), monthYear.month(), 1);
		return ld.get(ChronoField.DAY_OF_WEEK);
	}

	private static int getDaysInMonth(MonthYear monthYear) {
		YearMonth m = YearMonth.of(monthYear.year(), monthYear.month());
		return m.lengthOfMonth();

	}

	private static void printWeekDays() {
		System.out.printf("%s", " ".repeat( 1 ));
		for ( DayOfWeek weekDay: weekDays) {
			System.out.printf("%" + COLUMN_WIDTH + "s", weekDay.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("en")));
		}
		System.out.println();
	}

	private static void printTitle(MonthYear monthYear) {
		String monthName = Month.of(monthYear.month())
				.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("en"));
		System.out.printf("%s%s %d\n", " ".repeat(TITLE_OFFSET), monthName, monthYear.year());
		
		
	}

	private static MonthYear getMonthYear(String[] args) throws Exception {
		int monthNumber = getMonth(args);
		int year = getYear(args);
		return new MonthYear(monthNumber, year);
	}

	private static int getYear(String[] args) throws Exception {
		int year = args.length < 1 ? getCurrentYear() : getYear(args[1]);
		return year;
	}
	

	private static int getYear(String year) throws Exception {
		try {
			int result = Integer.parseInt(year);
			return result;
		}
		catch ( NumberFormatException e) {
			throw new Exception( "Year must be an integer number ");
			
		}
	}

	private static int getCurrentYear() {
		return LocalDate.now().get(ChronoField.YEAR);
	}

	private static int getMonth(String[] args) throws Exception {
		int month = args.length == 0 ? getCurrentMonth() : getMonthNumber(args[0]);
		return month;
	}

	private static int getMonthNumber(String monthStr) throws Exception {
		try {
			int result = Integer.parseInt(monthStr);
			if ( result < 1 ) {
				throw new Exception("Month value couldn't be less that 1");
			}
			if ( result > 12 ) {
				throw new Exception("Month value could't be greater then 12");
			}
			return result;
		} 
		catch (NumberFormatException e ) {
			throw new Exception("Month value must be an integer number");
		}
		
	}

	private static int getCurrentMonth() {
		return LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
	}

}
