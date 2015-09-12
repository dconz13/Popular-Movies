package app.com.connolly.dillon.popularmovies.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Auto generated using jsonschema2pojo
public class Reviews {

    private int page;
    private List<Result_> results = new ArrayList<Result_>();
    private int totalPages;
    private int totalResults;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result_> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result_> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}