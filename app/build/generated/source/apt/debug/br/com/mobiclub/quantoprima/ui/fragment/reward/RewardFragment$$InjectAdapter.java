// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.fragment.reward;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<RewardFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code RewardFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<RewardFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<RewardFragment>} and handling injection
 * of annotated fields.
 */
public final class RewardFragment$$InjectAdapter extends Binding<RewardFragment>
    implements Provider<RewardFragment>, MembersInjector<RewardFragment> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment> supertype;

  public RewardFragment$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.fragment.reward.RewardFragment", "members/br.com.mobiclub.quantoprima.ui.fragment.reward.RewardFragment", NOT_SINGLETON, RewardFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", RewardFragment.class);
    supertype = (Binding<br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment>) linker.requestBinding("members/br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment", RewardFragment.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<RewardFragment>}.
   */
  @Override
  public RewardFragment get() {
    RewardFragment result = new RewardFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<RewardFragment>}.
   */
  @Override
  public void injectMembers(RewardFragment object) {
    object.serviceProvider = serviceProvider.get();
    supertype.injectMembers(object);
  }
}
