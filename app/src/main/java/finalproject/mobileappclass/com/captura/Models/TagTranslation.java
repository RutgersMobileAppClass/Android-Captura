package finalproject.mobileappclass.com.captura.Models;

/**
 * Created by viral on 12/6/16.
 */

public class TagTranslation {
    private String tag;
    private String translatedTag;

    public TagTranslation() { }

    public TagTranslation(String tag, String translatedTag) {
        this.tag = tag;
        this.translatedTag = translatedTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTranslatedTag() {
        return translatedTag;
    }

    public void setTranslatedTag(String translatedTag) {
        this.translatedTag = translatedTag;
    }

    public String toString() {
        return "" + tag.toString() + " --> " + translatedTag.toString();
    }
}
