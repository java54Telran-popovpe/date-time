package telran.time;

import java.time.temporal.Temporal;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Friday13Range implements Iterable<Temporal> {
	
	Temporal from;
	Temporal to;
	
	public static Friday13Range from( Temporal from, Temporal to ) {
		if ( !from.getClass().equals(to.getClass() ) )
			throw new IllegalArgumentException( "The arguments should belong to the same class." );
		if ( compare(from, to ) < 0 )
			return new Friday13Range( from, to );
		throw new IllegalArgumentException( "Impossible to create range: Temporal <from> must be earlier Temporal <to> " );
	}
	
	private Friday13Range( Temporal from, Temporal to ) {
		this.from = from;
		this.to = to;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> int compare( T temporal1, T temporal2) {
		return ((Comparable<T>) temporal1).compareTo( temporal2);
	}
	
	@Override
	public Iterator<Temporal> iterator() {
		return new FridayIterator();
	}
	
	private class FridayIterator implements Iterator<Temporal> {
		
		private Temporal fri13 = Friday13Range.this.from.with( new NextFriday13() );

		@Override
		public boolean hasNext() {
			return Friday13Range.compare(to, fri13) > 0;
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
