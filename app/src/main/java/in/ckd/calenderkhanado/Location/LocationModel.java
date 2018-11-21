package in.ckd.calenderkhanado.Location;

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

    }

    public static class Terms {

    }

    public static class Predictions {
        public String description;


    }
}
