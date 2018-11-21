package in.ckd.calenderkhanado.data.network.model;

import okhttp3.MultipartBody;

public class MediaRequest {
    public int user_id;
    public MultipartBody.Part image;
    public int entity_id;
    public String entity_type;

}
