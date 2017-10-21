// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class StoreInformationFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.store.StoreInformationFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689800, "field 'labelStoreDistance'");
    target.labelStoreDistance = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689804, "field 'lytHowToWin'");
    target.lytHowToWin = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131689807, "field 'howToWinInfo'");
    target.howToWinInfo = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131689806, "field 'scrollView'");
    target.scrollView = (android.widget.ScrollView) view;
    view = finder.findRequiredView(source, 2131689793, "method 'onPhone'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onPhone();
        }
      });
    view = finder.findRequiredView(source, 2131689794, "method 'onSite'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSite();
        }
      });
    view = finder.findRequiredView(source, 2131689795, "method 'onCardapio'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onCardapio();
        }
      });
    view = finder.findRequiredView(source, 2131689796, "method 'onMap'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onMap();
        }
      });
    view = finder.findRequiredView(source, 2131689797, "method 'onEvaluate'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onEvaluate();
        }
      });
    target.buttons = Finder.listOf(
        (android.widget.ImageButton) finder.findRequiredView(source, 2131689793, "buttons"),
        (android.widget.ImageButton) finder.findRequiredView(source, 2131689794, "buttons"),
        (android.widget.ImageButton) finder.findRequiredView(source, 2131689795, "buttons"),
        (android.widget.ImageButton) finder.findRequiredView(source, 2131689796, "buttons"),
        (android.widget.ImageButton) finder.findRequiredView(source, 2131689797, "buttons")
    );  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.store.StoreInformationFragment target) {
    target.labelStoreDistance = null;
    target.lytHowToWin = null;
    target.howToWinInfo = null;
    target.scrollView = null;
    target.buttons = null;
  }
}
