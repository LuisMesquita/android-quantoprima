// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.util;


import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;

/**
 * A manager for {@code Ln}'s injections into static fields.
 */
public final class Ln$$StaticInjection extends StaticInjection {
  private Binding<br.com.mobiclub.quantoprima.util.Ln.BaseConfig> config;
  private Binding<br.com.mobiclub.quantoprima.util.Ln.Print> print;

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  public void attach(Linker linker) {
    config = (Binding<br.com.mobiclub.quantoprima.util.Ln.BaseConfig>) linker.requestBinding("br.com.mobiclub.quantoprima.util.Ln$BaseConfig", br.com.mobiclub.quantoprima.util.Ln.class);
    print = (Binding<br.com.mobiclub.quantoprima.util.Ln.Print>) linker.requestBinding("br.com.mobiclub.quantoprima.util.Ln$Print", br.com.mobiclub.quantoprima.util.Ln.class);
  }

  /**
   * Performs the injections of dependencies into static fields when requested by
   * the {@code dagger.ObjectGraph}.
   */
  @Override
  public void inject() {
    Ln.config = config.get();
    Ln.print = print.get();
  }
}
