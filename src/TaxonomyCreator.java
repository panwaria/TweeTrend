import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.MovieDb;


public class TaxonomyCreator
{
	
	public static TaxonomyTree constructTaxonomyTree()
	{
		TaxonomyTree taxonomyTree = TaxonomyTree.getTaxonomyTree();
		
		TheMovieDbApi tmdb;
		try
		{
			tmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
			
			List<Genre> allGenres = tmdb.getGenreList("");
	        for(Genre genre : allGenres)
	        {
	        	//System.out.println(genre.getName());
	        	TaxonomyNode genreNode = taxonomyTree.createNode(genre.getName(), taxonomyTree.getRootNode());
	        	taxonomyTree.getRootNode().mChildNodeList.add(genreNode);
	        	for(int page = 1; page <= 1; page++)
	        	{
	        		List<MovieDb> allMovies = tmdb.getGenreMovies(genre.getId(), "", page);
	        		for(MovieDb movie : allMovies)
	            	{
	            		//System.out.println("\t" + movie.getTitle());
	        			TaxonomyNode movieNode = taxonomyTree.createNode(movie.getTitle(), genreNode, movie.getId());
	            		genreNode.mChildNodeList.add(movieNode);
	            	}
	        	}
	        	
	        	//List<Person> people2 = tmdb.getMovieCasts(results1.get(0).getId());
	            
	        	//List<MovieDb> results2 = tmdb.getGenreMovies(genre.getId(), "", 1);
	        	
	        }
		}
		catch (MovieDbException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        
        return taxonomyTree;
	}

}