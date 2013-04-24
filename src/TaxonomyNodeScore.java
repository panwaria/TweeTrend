import java.util.ArrayList;
import java.util.List;

public class TaxonomyNodeScore
{
	public TaxonomyNodeScore()
	{
		mTweetIDList = new ArrayList<String>();
		mNodeScore = 0.0;
	}
	
	public List<String> mTweetIDList;
	public double mNodeScore; 
}
