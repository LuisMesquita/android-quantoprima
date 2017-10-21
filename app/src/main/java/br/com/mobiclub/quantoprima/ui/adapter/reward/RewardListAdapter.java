package br.com.mobiclub.quantoprima.ui.adapter.reward;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.MultiTypeAdapter;

import java.util.Date;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Util;

/**
 *
 */
public class RewardListAdapter extends MultiTypeAdapter {

    private Resources resources;
    private List<Reward> items;

    private boolean[] typeShowed = new boolean[] { false, false };
    private int first1 = -1;
    private int first2 = -1;
    private boolean show = false;
    private static int positionReward;
    private static int positionRewardExpired;
    private static int positionRewardRedeemed;
    private static boolean headValid = false;
    private static boolean headExpirado = false;
    private static boolean headRedeemed = false;
    private View.OnClickListener clickListener;

    public RewardListAdapter(LayoutInflater layoutInflater,
                             Resources resources, List<Reward> items, View.OnClickListener click) {
        super(layoutInflater);
        this.resources = resources;
        this.clickListener = click;
        addMultiTypeItems(items);
    }

    @Override
    public long getItemId(int position) {
        Reward item = (Reward) getItem(position);
        return item.getRewardAt().hashCode();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    public void addMultiTypeItems(List<Reward> items) {
        this.items = items;
        for(Reward item : items) {
            Integer typeId = item.getTypeId();
            if(typeId != null)
                addItem(typeId, item);
        }
    }

    /**
     * Get layout id for type
     *
     * @param type
     * @return layout id
     */
    @Override
    protected int getChildLayoutId(int type) {
        if(type == Reward.REWARD_TYPE) {
            return R.layout.list_item_reward_valid;
        }
        //presente eh o mesmo do buy
        else if(type == Reward.REWARD_GIFT_ID) {
            return R.layout.list_item_reward_valid;
        } else if(type == Reward.REWARD_BUY_EXPIRED_ID) {
            return  R.layout.list_item_reward_expired;
        } else if(type == Reward.REWARD_REDEEMED) {
            return  R.layout.list_item_reward_redeemed;
        }
        return R.layout.list_item_reward_valid;
    }

    /**
     * Get child view ids for type
     * <p>
     * The index of each id in the returned array should be used when using the
     * helpers to update a specific child view
     *
     * @param type
     * @return array of view ids
     */
    @Override
    protected int[] getChildViewIds(int type) {
        return new int[] {
                R.id.header_reward_type, // Header Normal
                R.id.image_reward,
                R.id.label_name,
                R.id.label_establishment,
                R.id.label_expiration,
                R.id.header_reward_type_expired, // Header Expirado
                R.id.imgBtExcluir
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.REWARD_LIST);
    }

    /**
     * Update view for item
     *
     * @param position
     * @param item
     * @param type
     */
    @Override
    protected void update(int position, Object item, int type) {
        if(!(item instanceof Reward))
            return;

        // Resetando os valores iniciais
        if (0 == position) {
            headValid = false;
            positionReward = -1;
            headRedeemed = false;
            positionRewardRedeemed = -1;
            headExpirado = false;
            positionRewardExpired = -1;
        }

        LinearLayout header = getView(0, LinearLayout.class);
        LinearLayout header2 = getView(5, LinearLayout.class);

        switch (type) {
            case Reward.REWARD_TYPE:
                if(!headValid || positionReward == position) {
                    headValid = true;
                    positionReward = position;

                    header.setVisibility(View.VISIBLE);
                } else if(position != positionReward) {
                    header.setVisibility(View.GONE);
                }
                break;

            case Reward.REWARD_REDEEMED:
                getView(6, ImageButton.class).setOnClickListener(clickListener);
                getView(6, ImageButton.class).setTag(item);

                if(!headRedeemed || positionRewardRedeemed == position) {
                    headRedeemed = true;
                    positionRewardRedeemed = position;
                    header.setVisibility(View.VISIBLE);
                } else if(position != positionRewardRedeemed) {
                    header.setVisibility(View.GONE);
                }
                break;

            case Reward.REWARD_BUY_EXPIRED_ID:
                getView(6, ImageButton.class).setOnClickListener(clickListener);
                getView(6, ImageButton.class).setTag(item);

                if(!headExpirado || positionRewardExpired == position) {
                    headExpirado = true;
                    positionRewardExpired = position;
                    header2.setVisibility(View.VISIBLE);
                } else if(position != positionRewardExpired) {
                    header2.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }

        Reward reward = (Reward) item;

        //Imagem
        loadImage(reward.getImage(), imageView(1));

        // Título
        String title = reward.getTitle();
        if(title != null) {
            setText(2, title);
        }

        // Estabelecimento
        String establishmentName = reward.getEstablishmentName();
        if(establishmentName != null) {
            setText(3, establishmentName);
        }

        // Data de Expiração
        String expirationAtDate = reward.getExpirationAt();
        Date date = Util.paserTPatternDate(expirationAtDate);
        expirationAtDate = Util.getDateTimeString(date);
        String expirationAt = resources.getString(R.string.list_item_reward_label_expiration, expirationAtDate);
        if(expirationAt != null) {
            setText(4, expirationAt);
        }

        // Trocando as cores
        if(type == Reward.REWARD_BUY_EXPIRED_ID) {
            TextView labelName = getView(2, TextView.class);
            labelName.setTextColor(resources.getColor(R.color.grey6));
            TextView labellocation = getView(3, TextView.class);
            labellocation.setTextColor(resources.getColor(R.color.grey6));
            TextView labelExpiration = getView(4, TextView.class);
            labelExpiration.setTextColor(resources.getColor(R.color.grey6));
        }

    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }
}
