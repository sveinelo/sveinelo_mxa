package no.mxa.altinn.ws.api;

import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

// Should be safe to ignore Handler Generic type argument
@SuppressWarnings("rawtypes")
public class ConfiguredHandlerResolver implements HandlerResolver {
	private final List<Handler> handlers;

	public ConfiguredHandlerResolver(List<Handler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		return handlers;
	}

}
