package br.com.mobiclub.quantoprima.ui.view;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.domain.DpiEnum;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 *
 */
public class ImageLoader {

    private static ImageLoader instance;

    private DpiEnum dpi;
    private ImageView imageItem;

    public static ImageLoader getInstance() {
        if(instance == null)
            instance = new ImageLoader();
        return instance;
    }

    public void loadImages(ImageView imageView, List<Image> images, int placeholder) {
        for (int i = 0; i < images.size(); i++) {
            loadImage(images.get(i), imageView, placeholder);
        }
    }

    private void loadImage(Image image, ImageView imageItem, int placeholder) {
        loadImage(imageItem, image, placeholder);
    }

    public void loadImage(ImageView imageItem, Image image, int placehoolder) {
        this.imageItem = imageItem;
        String url = getImageDPI(image);
        if(url != null) {
            Ln.v("Carregando %s", url);
        }
        Picasso.with(MobiClubApplication.getInstance())
                .load(url)
                .placeholder(placehoolder)
                .into(imageItem);
        image.setPreferedType(DpiEnum.MINI);
    }

    private String getImageDPI(Image image) {
        if(image == null) {
            Ln.v("Nenhuma imagem para carregar.");
            return null;
        }
        String hdpi = image.getHDPI();
        String xdpi = image.getXDPI();
        String mini = image.getMINI();
        String ldpi = image.getLDPI();

        if(hdpi == null && ldpi == null &&
                mini == null && xdpi == null) {
            Ln.v("Nenhuma DPI de imagem para carregar.");
            return null;
        }

        if(image.getPreferedType() == DpiEnum.MINI &&
                mini != null) {
            Ln.d("carregando");
            return mini;
        }

        String imageResult = null;

        imageResult = loadOptimalImage(hdpi, xdpi, mini, ldpi);

        //fallback
        if(imageResult == null) {
            imageResult = loadFirstImage(hdpi, xdpi, mini, ldpi);
        }

        return imageResult;
    }

    private String loadFirstImage(String hdpi, String xdpi, String mini, String ldpi) {
        String imageResult = null;
        if(hdpi != null) {
            Ln.v("Imagem hdpi.");
            imageResult = hdpi;
        }
        else if(xdpi != null) {
            Ln.v("Imagem xdpi.");
            imageResult = xdpi;
        }
        else if(mini != null) {
            Ln.v("Imagem mini.");
            imageResult = mini;
        }
        else if(ldpi != null) {
            Ln.v("Imagem ldpi.");
            imageResult = ldpi;
        }
        return imageResult;
    }

    private String loadOptimalImage(String hdpi, String xdpi, String mini, String ldpi) {
        Ln.d("dpi == %s", dpi);
        String imageResult = null;
        if(hdpi != null && dpi == DpiEnum.HDPI) {
            Ln.v("Imagem hdpi.");
            imageResult = hdpi;
        }
        else if(xdpi != null && dpi == DpiEnum.XDPI) {
            Ln.v("Imagem xdpi.");
            imageResult = xdpi;
        }
        else if(mini != null && dpi == DpiEnum.LDPI) {
            Ln.v("Imagem mini.");
            imageResult = mini;
        }
        else if(ldpi != null && dpi == DpiEnum.LDPI) {
            Ln.v("Imagem ldpi.");
            imageResult = ldpi;
        }
        return imageResult;
    }

    public void setDpi(DpiEnum dpi) {
        this.dpi = dpi;
    }

    public class Placeholder {
        //public static final int DEFAULT = R.drawable.gravatar_icon;
        public static final int DEFAULT = R.drawable.placeholder_default;
        public static final int CARDAPIO_ITEM = DEFAULT;
        public static final int ESTABLISHMENT_LIST = DEFAULT;
        public static final int USER_PHOTO = R.drawable.foto_usuario_placeholder;
        public static final int NOTIFY_LOCATION = DEFAULT;
        public static final int REWARD_LIST = DEFAULT;
        public static final int CARDAPIO_LIST = DEFAULT;
        public static final int STORE_REWARD_LIST = DEFAULT;
        public static final int DIALOG_RESULT_FACTORY = DEFAULT;
        public static final int RECOMPENSA = R.drawable.t28_1_recompensa_placeholder;
    }
}