// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.activity.account;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<LostPasswordActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code LostPasswordActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<LostPasswordActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<LostPasswordActivity>} and handling injection
 * of annotated fields.
 */
public final class LostPasswordActivity$$InjectAdapter extends Binding<LostPasswordActivity>
    implements Provider<LostPasswordActivity>, MembersInjector<LostPasswordActivity> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<com.squareup.otto.Bus> bus;
  private Binding<br.com.mobiclub.quantoprima.ui.activity.MobiClubActionBarActivity> supertype;

  public LostPasswordActivity$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.activity.account.LostPasswordActivity", "members/br.com.mobiclub.quantoprima.ui.activity.account.LostPasswordActivity", NOT_SINGLETON, LostPasswordActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", LostPasswordActivity.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", LostPasswordActivity.class);
    supertype = (Binding<br.com.mobiclub.quantoprima.ui.activity.MobiClubActionBarActivity>) linker.requestBinding("members/br.com.mobiclub.quantoprima.ui.activity.MobiClubActionBarActivity", LostPasswordActivity.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(bus);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<LostPasswordActivity>}.
   */
  @Override
  public LostPasswordActivity get() {
    LostPasswordActivity result = new LostPasswordActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<LostPasswordActivity>}.
   */
  @Override
  public void injectMembers(LostPasswordActivity object) {
    object.serviceProvider = serviceProvider.get();
    object.bus = bus.get();
    supertype.injectMembers(object);
  }
}