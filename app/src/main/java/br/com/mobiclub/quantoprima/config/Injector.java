package br.com.mobiclub.quantoprima.config;

import java.util.List;

import dagger.ObjectGraph;

public final class Injector
{
    public static ObjectGraph objectGraph = null;

    public static void init(final List<Object> rootModule) {

        if(objectGraph == null)
        {
            objectGraph = ObjectGraph.create(rootModule.toArray());
        }
        else
        {
            objectGraph = objectGraph.plus(rootModule);
        }

        // Inject statics
        objectGraph.injectStatics();

    }

    public static void init(final List<Object> rootModule, final Object target)
    {
        init(rootModule);
        inject(target);
    }

    public static final void inject(final Object target)
    {
        objectGraph.inject(target);
    }

    public static <T> T resolve(Class<T> type)
    {
        return objectGraph.get(type);
    }
}
