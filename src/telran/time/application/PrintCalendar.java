package telran.time.application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

record MonthYear( int month, int year ) {
	
}


public class PrintCalendar {

	private static final int TITLE_OFFSET = 5;
	private static final int COLUMN_WIDTH = 4;
	private static int weekDaysNumber = DayOfWeek.values().length;

	public static void main(String[] args) {
		
		try {
			MonthYear monthYear = getMonthYear(args);
			DayOfWeek firstWeekDay = getFirstWeekDay(args);
			printCalendar(  monthYear, firstWeekDay );
		} catch (RuntimeException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			System.out.println( e.getMessage()); 
		} 
	}

	private static DayOfWeek getFirstWeekDay(String[] args) throws Exception {
		return args.length < 3 ? DayOfWeek.MONDAY : parseWeekDay(args[ 2 ]);
	}

	private static DayOfWeek parseWeekDay(String string) throws Exception {
		
		try {
			return DayOfWeek.valueOf(string.toUpperCase());
		} 
		catch (IllegalArgumentException e) {
			throw new Exception( String.format("Unknown argument %s: should be a weekday", string) );
		}
	}

	private static void printCalendar(MonthYear monthYear, DayOfWeek firstDayOfWeek) {
		printTitle( monthYear);
		printWeekDays( firstDayOfWeek );
		printDays( monthYear, firstDayOfWeek );
		
	}

	private static void printDays(MonthYear monthYear, DayOfWeek firstDayOfWeek) {
		int nDays = getDaysInMonth(monthYear);
		int indexOfCurrentWeekDay = getIndexOfWeekDay( monthYear, firstDayOfWeek);
		int firstOffset = getFirstOffset( indexOfCurrentWeekDay );
		System.out.printf("%s", " ".repeat(firstOffset));
		for ( int day = 1; day <= nDays; day++ ) {
			System.out.printf("%" + COLUMN_WIDTH + "d", day );
			if ( indexOfCurrentWeekDay == weekDaysNumber - 1 ) {
				indexOfCurrentWeekDay = 0;
				System.out.println();
			} else 
				indexOfCurrentWeekDay++;
		}
		
	}

	private static int getIndexOfWeekDay(MonthYear monthYear, DayOfWeek firstDayOfWeek) {
		LocalDate ld = LocalDate.of(monthYear.year(), monthYear.month(), 1);
		LocalDate ldAdjustedToWeekDay = ld.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
		return (int)ldAdjustedToWeekDay.until(ld, ChronoUnit.DAYS);
	}

	private static int getFirstOffset(int indexOfCurrentWeekDay) {
		
		return COLUMN_WIDTH * indexOfCurrentWeekDay;
	}

	private static int getDaysInMonth(MonthYear monthYear) {
		YearMonth m = YearMonth.of(monthYear.year(), monthYear.month());
		return m.lengthOfMonth();

	}

	private static void printWeekDays( DayOfWeek weekDay ) {
		System.out.printf("%s", " ".repeat( 1 ));
		for ( int i = 0; i < 7; i++ ) {
			System.out.printf("%" + COLUMN_WIDTH + "s", weekDay.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("en")));
			weekDay = weekDay.plus(1);
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
