package in.mummysfood.Location;

import java.util.List;

/**
 * Created by acer on 8/21/2018.
 */

public class LocationModel {
    public List<Predictions> predictions;
    public String status;

    public static class Matched_substrings {
        public int length;
        public int offset;
    }

    public static class Main_text_matched_substrings {
        public int length;
        public int offset;
    }

    public static class Structured_formatting {
        public String main_text;
        public List<Main_text_matched_substrings> main_text_matched_substrings;
        public String secondary_text;
    }

    public static class Terms {
        public int offset;
        public String value;
    }

    public static class Predictions {
        public String description;
        public String id;
        public List<Matched_substrings> matched_substrings;
        public String place_id;
        public String reference;
        public Structured_formatting structured_formatting;
        public List<Terms> terms;

    }
}
