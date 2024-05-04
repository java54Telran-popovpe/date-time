package telran.time;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Friday13Range implements Iterable<Temporal> {
	
	Temporal from;
	Temporal to;
	
	public static Friday13Range from( Temporal from, Temporal to ) {
		if ( (LocalDate.from(from)).compareTo((LocalDate.from(to))) < 0 )
			return new Friday13Range( from, to );
		throw new IllegalArgumentException( "Impossible to create range: Temporal <from> must be earlier Temporal <to> " );
	}
	
	private Friday13Range( Temporal from, Temporal to ) {
		this.from = from;
		this.to = to;
	}
	@Override
	public Iterator<Temporal> iterator() {
		return new FridayIterator();
	}
	
	private class FridayIterator implements Iterator<Temporal> {
		
		private LocalDate fri13 = LocalDate.from( Friday13Range.this.from.with( new NextFriday13() ) );
		private LocalDate to = LocalDate.from( Friday13Range.this.to );

		@Override
		public boolean hasNext() {
			return to.compareTo(fri13) > 0;
		}

		@Override
		public Temporal next() {
			if ( !hasNext() )
				throw new NoSuchElementException();
			Temporal result = fri13;
			fri13 = fri13.with( new NextFriday13() ) ;
			return result;
		}
	}
}
