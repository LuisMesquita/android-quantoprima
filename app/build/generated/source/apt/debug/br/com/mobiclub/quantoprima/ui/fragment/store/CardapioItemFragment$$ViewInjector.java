// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CardapioItemFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.store.CardapioItemFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689736, "field 'frameImage'");
    target.frameImage = (android.widget.FrameLayout) view;
    view = finder.findRequiredView(source, 2131689733, "field 'labelName'");
    target.labelName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689737, "field 'imageItem'");
    target.imageItem = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689729, "field 'next' and method 'onBtnNext'");
    target.next = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onBtnNext();
        }
      });
    view = finder.findRequiredView(source, 2131689728, "field 'prev' and method 'onBtnPrev'");
    target.prev = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onBtnPrev();
        }
      });
    view = finder.findRequiredView(source, 2131689738, "field 'labelDescription'");
    target.labelDescription = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689735, "field 'frameNext'");
    target.frameNext = (android.widget.FrameLayout) view;
    view = finder.findRequiredView(source, 2131689734, "field 'framePrev'");
    target.framePrev = (android.widget.FrameLayout) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.store.CardapioItemFragment target) {
    target.frameImage = null;
    target.labelName = null;
    target.imageItem = null;
    target.next = null;
    target.prev = null;
    target.labelDescription = null;
    target.frameNext = null;
    target.framePrev = null;
  }
}
