package telran.time;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Friday13Range implements Iterable<Temporal> {
	
	Temporal from;
	Temporal to;
	
	public static Friday13Range from( Temporal from, Temporal to ) {
		if ( compare(from, to ) < 0 )
			return new Friday13Range( from, to );
		throw new IllegalArgumentException( "Impossible to create range: Temporal <from> must be earlier Temporal <to> " );
	}
	
	private Friday13Range( Temporal from, Temporal to ) {
		this.from = from;
		this.to = to;
	}
	
	private static int compare( Temporal temporal1, Temporal temporal2) {
		return (LocalDate.from(temporal1)).compareTo( LocalDate.from(temporal2));
	}
			
	@Override
	public Iterator<Temporal> iterator() {
		return new FridayIterator();
	}
	
	private class FridayIterator implements Iterator<Temporal> {
		
		private static NextFriday13 nextFri13 = new NextFriday13();
		private Temporal fri13 = Friday13Range.this.from.with( nextFri13 );
		

		@Override
		public boolean hasNext() {
			return compare(to, fri13) > 0;
		}

		@Override
		public Temporal next() {
			if ( !hasNext() )
				throw new NoSuchElementException();
			Temporal result = fri13;
			fri13 = fri13.with( nextFri13 ) ;
			return result;
		}
	}
}
