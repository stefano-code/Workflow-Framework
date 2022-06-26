package com.workflow.test;

import com.workflow.test.framework.AsyncAwaitAction;
import com.workflow.test.framework.Predicate;

public class AsyncActionTest extends AsyncAwaitAction {
    public AsyncActionTest(String actionName, Predicate<Void> f) {
        super(actionName, f);
    }
}
