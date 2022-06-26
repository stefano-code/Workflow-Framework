package com.workflow.test.framework;


public class CheckAction extends SingleAction
{
    private Predicate<Void> p;
    private Action thenAction;
    private Action elseAction;
    private Action action;

    private String msg;

    public CheckAction(Predicate<Void> condition)
    {
        p = condition;
        msg = getClass().getSimpleName();
    }
    public CheckAction(String message, Predicate<Void> condition)
    {
        p = condition;
        msg = message;
    }

    public Action thenElse(Action thenAction, Action elseAction)
    {
        this.thenAction = thenAction;
        this.elseAction = elseAction;
        return this;
    }

    public Action thenElse(Action thenAction, Predicate<Void> elsePredicate)
    {
        return thenElse(thenAction, new MethodReferenceAction("else", elsePredicate));
    }

    public Action thenElse( Predicate<Void> thenPredicate, Action elseAction)
    {
        return thenElse(new MethodReferenceAction("then", thenPredicate), elseAction);
    }

    public Action thenElse(Predicate<Void> thenPredicate, Predicate<Void> elsePredicate)
    {
        return thenElse(new MethodReferenceAction("if", thenPredicate) , new MethodReferenceAction("else", elsePredicate));
    }

    public Action then(Action thenAction)
    {
        this.thenAction = thenAction;
        this.elseAction = new EmptyAction();
        return this;
    }

    public Action then(String thenName, Predicate<Void> thenAction)
    {
        return then(new MethodReferenceAction(thenName, thenAction));
    }

    public Action then(Pair<String, Predicate<Void>> p)
    {
        return then(p.v1, p.v2);
    }

    @Override
    protected State doStep()
    {
        chooseAction();
        return action.execute();
    }

    @Override
    protected State getState()
    {
        chooseAction();
        return action.getState();
    }

    @Override
    protected boolean completed()
    {
        chooseAction();
        return action.completed();
    }

    public String getMsg()
    {
        return msg;
    }

    protected void logExecution()
    {
        if(action == null)
            Journal.Log(wfContext.tag, "executing check " + getMsg());
    }

    private void chooseAction()
    {
        if(action == null)
        {
            boolean value = p.test(null);
            Journal.Log(wfContext.tag, getMsg() + " value " + value);
            action =  (value) ? thenAction : elseAction;
        }
    }
    @Override
    public String getBlockingActions()
    {
        if( completed() )
            return "";
        else
        {
            chooseAction();

            String prevPath = wfContext.actionPathForDump;
            wfContext.actionPathForDump =  wfContext.actionPathForDump + "CheckAction." + (action==thenAction?"then":"else") + ".";

            String res = action.getBlockingActions();

            wfContext.actionPathForDump = prevPath;

            return res;
        }
    }


    @Override
    public void reset()
    {
        super.reset();
        action = null;
        thenAction.reset();
        elseAction.reset();
    }
}
