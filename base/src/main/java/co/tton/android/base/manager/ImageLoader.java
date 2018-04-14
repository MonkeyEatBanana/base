package co.tton.android.base.manager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import co.tton.android.base.R;

public class ImageLoader {

    private static ImageLoader sInstance;

    public static ImageLoader get() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    private ImageLoader() {

    }

    public void load(ImageView view, String uri, int placeHolderId) {
        if (uri == null) return;

        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .placeholder(placeHolderId)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    public void loadPreview(ImageView view, String uri) {
        if (uri == null) return;

        Glide.with(view.getContext().getApplicationContext())
                .load(uri)
                .placeholder(R.drawable.img_default)
                .fitCenter()
                .dontAnimate()
                .into(view);
    }
}
