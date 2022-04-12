package App;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue()
	{
		assertTrue( true );
	}

	/**
	 * Tests whether the quicksort really works
	 */
	@Test
	public void testQuicksort() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>(100);
		// using current nanosecond time as random seed (Real scientific method!)
		Random rand = new Random(System.nanoTime());
		for (int i = 0; i < 100; i++) {
			// add random numbers (can be any int) inside list
			// .txt because the name is trimmed
			recipes.add(new Recipe(".txt", rand.nextInt()));
		}
		// sort list
		RecipeHelper.quicksort(recipes);

		// print the array
		//System.out.println("Test array output:");
		//recipes.stream().map(x->x.getCount()).forEachOrdered(System.out::println);

		// check if every number is less than or equal to the last
		// (because we want the biggest number first)
		int last = Integer.MAX_VALUE;
		for (int i = 0; i < recipes.size(); i++) {
			assertTrue(recipes.get(i).getCount() <= last);
			last = recipes.get(i).getCount();
		}
	}
}
