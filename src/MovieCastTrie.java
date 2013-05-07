import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Person;


public class MovieCastTrie
{
	
	private Trie mCastTrie;
	
	public MovieCastTrie()
	{
		mCastTrie = new Trie();
	}
	
	public Trie getCastTrie()
	{
		return mCastTrie;
	}
	
	public void addCast(String castName, long taxonomyNodeId)
	{
		mCastTrie.insertCast(castName, taxonomyNodeId);
	}
	
	public void updateMovieCastTrie(TaxonomyTree taxonomyTree)
	{
		TaxonomyNode root = taxonomyTree.getRootNode();
		try
		{
			TheMovieDbApi tmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
			for(TaxonomyNode genreNode : root.mChildNodeList) //All genres
			{
				List<TaxonomyNode> movieList = genreNode.mChildNodeList;
					
				for(TaxonomyNode movieNode : movieList) //All movies of that genre
				{
					List<Person> movieCast = tmdb.getMovieCasts(movieNode.mTmdbMovieId);
					for(Person cast : movieCast)
					{
						addCast(cast.getName(), movieNode.mNodeID);
						//System.out.println("Adding cast \"" + cast.getName() + "\" for movie \"" + movieNode.mNodeName + "\"");
					}
				}
			}
		}
		catch (MovieDbException e)
		{
			e.printStackTrace();
		}
	}
}