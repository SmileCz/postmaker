package org.smilecz.shared.cqrs;

import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers;

    public QueryBus(Collection<QueryHandler<?, ?>> handlers) {
        this.handlers = new HashMap<>();
        for (QueryHandler<?, ?> handler : handlers) {
            QueryHandler<?, ?> previous = this.handlers.put(handler.queryType(), handler);
            if (previous != null) {
                throw new IllegalStateException("Duplicate query handler for " + handler.queryType().getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R query(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalStateException("No query handler registered for " + query.getClass().getName());
        }
        return handler.handle(query);
    }
}
