import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Person;


public class MovieCastCreator
{

	public static void writeToMovieCastXML(String fileName, TaxonomyTree taxonomyTree)
	{
		
		TaxonomyNode root = taxonomyTree.getRootNode();
		try
		{
			
			File file = new File(fileName);
			
			if (!file.exists())
				file.createNewFile();
			else
				return;
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("<moviecast>\n");
			
			TheMovieDbApi tmdb = new TheMovieDbApi(AppConstants.TMDB_API_KEY);
			for(TaxonomyNode genreNode : root.mChildNodeList) //All genres
			{
				List<TaxonomyNode> movieList = genreNode.mChildNodeList;
					
				for(TaxonomyNode movieNode : movieList) //All movies of that genre
				{
					bw.write("\t<movie name=\"" + movieNode.mNodeName.replace("&", "&amp;") + "\" " + "id=\"" + movieNode.mTmdbMovieId + "\">\n");
		        	
					List<Person> movieCast = tmdb.getMovieCasts(movieNode.mTmdbMovieId);
					int maxCastsPerMovie = 5;
					int i = 0;
					for(Person cast : movieCast)
					{
						if(i >= maxCastsPerMovie)
							break;
						bw.write("\t\t<cast name=\"" + cast.getName().replace('"', ' ') + "\"" + "/>\n");
			        	//addCast(cast.getName(), movieNode.mNodeID);
						i++;
					}
					bw.write("\t</movie>\n");
				}
			}
			
			bw.write("</moviecast>");
			bw.close();
			
		}
		catch (MovieDbException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
