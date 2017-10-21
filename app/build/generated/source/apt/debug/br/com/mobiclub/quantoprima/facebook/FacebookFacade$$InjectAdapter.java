// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.facebook;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

/**
 * A {@code Binder<FacebookFacade>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code FacebookFacade} and its
 * dependencies.
 * 
 * Being a {@code Provider<FacebookFacade>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<FacebookFacade>} and handling injection
 * of annotated fields.
 */
public final class FacebookFacade$$InjectAdapter extends Binding<FacebookFacade>
    implements MembersInjector<FacebookFacade> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;

  public FacebookFacade$$InjectAdapter() {
    super(null, "members/br.com.mobiclub.quantoprima.facebook.FacebookFacade", NOT_SINGLETON, FacebookFacade.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", FacebookFacade.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<FacebookFacade>}.
   */
  @Override
  public void injectMembers(FacebookFacade object) {
    object.serviceProvider = serviceProvider.get();
  }
}