package app.com.connolly.dillon.popularmovies.Model;

import java.util.HashMap;
import java.util.Map;

public class Country {

    private String certification;
    private String iso31661;
    private boolean primary;
    private String releaseDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The certification
     */
    public String getCertification() {
        return certification;
    }

    /**
     *
     * @param certification
     * The certification
     */
    public void setCertification(String certification) {
        this.certification = certification;
    }

    /**
     *
     * @return
     * The iso31661
     */
    public String getIso31661() {
        return iso31661;
    }

    /**
     *
     * @param iso31661
     * The iso_3166_1
     */
    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    /**
     *
     * @return
     * The primary
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     *
     * @param primary
     * The primary
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}