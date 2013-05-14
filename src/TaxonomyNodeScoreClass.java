
public class TaxonomyNodeScoreClass implements Comparable<TaxonomyNodeScoreClass>
{
	public TaxonomyNodeScoreClass(String nodeName, TaxonomyNodeScore nodeScore)
	{
		mNodeName = nodeName;
		mTaxonomyNodeScore = nodeScore;
	}
	
	@Override
    public int compareTo(TaxonomyNodeScoreClass obj)
    {
		Double thisNodeScore = new Double(this.mTaxonomyNodeScore.mNodeScore);
		Double newNodeScore = new Double(obj.mTaxonomyNodeScore.mNodeScore);
		
		return newNodeScore.compareTo(thisNodeScore);
    }
	
	public String mNodeName;
	public TaxonomyNodeScore mTaxonomyNodeScore;
}
