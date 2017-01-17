package openlive.agora.io.videocompressorlib;

/**
 * Created by karan on 13/2/15.
 */
public interface InitListener {
    public void onLoadSuccess();
    public void onLoadFail(String reason);
}
