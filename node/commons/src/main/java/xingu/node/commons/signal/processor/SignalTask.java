package xingu.node.commons.signal.processor;

import java.util.concurrent.Callable;

import xingu.node.commons.signal.Signal;

public interface SignalTask
    extends Callable<Signal>
{}
