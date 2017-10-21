// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.fragment.store;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<SurveyFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code SurveyFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<SurveyFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<SurveyFragment>} and handling injection
 * of annotated fields.
 */
public final class SurveyFragment$$InjectAdapter extends Binding<SurveyFragment>
    implements Provider<SurveyFragment>, MembersInjector<SurveyFragment> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<com.squareup.otto.Bus> bus;

  public SurveyFragment$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.fragment.store.SurveyFragment", "members/br.com.mobiclub.quantoprima.ui.fragment.store.SurveyFragment", NOT_SINGLETON, SurveyFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", SurveyFragment.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", SurveyFragment.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(bus);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<SurveyFragment>}.
   */
  @Override
  public SurveyFragment get() {
    SurveyFragment result = new SurveyFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<SurveyFragment>}.
   */
  @Override
  public void injectMembers(SurveyFragment object) {
    object.serviceProvider = serviceProvider.get();
    object.bus = bus.get();
  }
}