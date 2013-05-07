import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Person;


public class MovieCastTrie
{
	
	private Trie mCastTrie = null;
	private static MovieCastTrie mMovieCastTrie = null;
	
	
	private MovieCastTrie()
	{
		mCastTrie = new Trie();
	}
	
	public static MovieCastTrie getMovieCastTrie()
	{
		if(mMovieCastTrie == null)
			mMovieCastTrie = new MovieCastTrie();
		return mMovieCastTrie;
	}
	
	public void addCast(String castName, long taxonomyNodeId)
	{
		String[] tokens = castName.split(AppConstants.TWEET_DELIMITER_STRING);
		for(String token : tokens)
			mCastTrie.insertCast(token, taxonomyNodeId);
	}
	
	public List<Long> getMovieNodeIdList(String castName)
	{
		return mCastTrie.getMovieNodeIdList(castName);
	}
}