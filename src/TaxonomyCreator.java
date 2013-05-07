import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.xml.sax.InputSource;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.MovieDb;


public class TaxonomyCreator
{
	
	public static void constructTaxonomyTree(String fileName)
	{
		try
		{
			File file = new File(fileName);
			
			if (!file.exists())
				file.createNewFile();
			else
				return;
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("<taxonomy>\n");
			
			TheMovieDbApi tmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
			
			List<Genre> allGenres = tmdb.getGenreList("");
	        for(Genre genre : allGenres)
	        {
	        	bw.write("\t<node name=\"" + genre.getName() + "\" " + "id=\"" + "-1" + "\"" + ">\n");
	        	for(int page = 1; page <= 10; page++)
	        	{
	        		List<MovieDb> allMovies = tmdb.getGenreMovies(genre.getId(), "", page);
	        		for(MovieDb movie : allMovies)
	            	{
	        			bw.write("\t\t<node name=\"" + movie.getTitle().replace("&", "&amp;") + "\" " + "id=\"" + movie.getId() + "\"" + "/>\n");
	            	}
	        	}
	        	bw.write("\t</node>\n");
	        }
			
	        bw.write("</taxonomy>");
			bw.close();
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (MovieDbException e)
		{
			e.printStackTrace();
		}
	}

}