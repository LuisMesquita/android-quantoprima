package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.domain.DpiEnum;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 *
 */
public class Image implements Serializable {

    @SerializedName("Id")
    private int id;

    @SerializedName("Index")
    private int index;

    @SerializedName("XDPI")
    private String XDPI;

    @SerializedName("HDPI")
    private String HDPI;

    @SerializedName("MDPI")
    private String MDPI;

    @SerializedName("LDPI")
    private String LDPI;

    @SerializedName("MINI")
    private String MINI;
    private DpiEnum preferedType;

    public Image(String url) {
        this.HDPI = url;
        preferedType = DpiEnum.MINI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getXDPI() {
        return XDPI;
    }

    public void setXDPI(String XDPI) {
        this.XDPI = XDPI;
    }

    public String getHDPI() {
        return HDPI;
    }

    public void setHDPI(String HDPI) {
        this.HDPI = HDPI;
    }

    public String getMDPI() {
        return MDPI;
    }

    public void setMDPI(String MDPI) {
        this.MDPI = MDPI;
    }

    public String getLDPI() {
        return LDPI;
    }

    public void setLDPI(String LDPI) {
        this.LDPI = LDPI;
    }

    public String getMINI() {
        return MINI;
    }

    public void setMINI(String MINI) {
        this.MINI = MINI;
    }

    public String getImageDPI() {
        if(getHDPI() == null && getLDPI() == null &&
                getMINI() == null && getXDPI() == null) {
            Ln.v("Nenhuma DPI de imagem para carregar.");
            return null;
        }
        if(getHDPI() != null) {
            return getHDPI();
        }
        else if(getXDPI() != null) {
            return getXDPI();
        }
        else if(getMINI() != null) {
            return getMINI();
        }
        else if(getLDPI() != null) {
            return getLDPI();
        }
        return null;
    }

    public DpiEnum getPreferedType() {
        return preferedType;
    }

    public void setPreferedType(DpiEnum preferedType) {
        this.preferedType = preferedType;
    }
}
