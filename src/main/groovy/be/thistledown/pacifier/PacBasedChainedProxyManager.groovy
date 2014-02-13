package be.thistledown.pacifier

import be.thistledown.pacifier.util.UrlUtil
import groovy.util.logging.Log
import io.netty.handler.codec.http.HttpRequest
import org.littleshoot.proxy.ChainedProxy
import org.littleshoot.proxy.ChainedProxyAdapter
import org.littleshoot.proxy.ChainedProxyManager

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * ChainedProxyManager implementation that uses a pac file to lookup chained proxies.
 * <p>
 * The lookup is done using the jsr 223 ScriptEngine.
 */
@Log
class PacBasedChainedProxyManager implements ChainedProxyManager {

    private ScriptEngine engine

    public PacBasedChainedProxyManager(String pacUrl) {
        def manager = new ScriptEngineManager()
        engine = manager.getEngineByName("JavaScript")

        // load needed javascript pac-script functions
        def pac_utils = this.class.getResource("/pac_utils.js").text
        engine.eval(pac_utils)

        // load the pac file from the network
        def pac = new URL(pacUrl).text
        engine.eval(pac)
    }

    @Override
    void lookupChainedProxies(HttpRequest httpRequest, Queue<ChainedProxy> chainedProxies) {
        log.info "lookup proxies for $httpRequest.uri"

        def host = UrlUtil.getHost(httpRequest.uri)

        def result = engine.eval("FindProxyForURL('$httpRequest.uri', '$host')")

        // TODO implement caching? is it needed?

        log.info "pac returned $result"
        if ("DIRECT" == result) {
            log.info "direct connection"

            // no proxy
            return
        } else {
            result.split(/;/).each { proxy ->
                def (_, proxyHost, proxyPort) = (proxy.trim() =~ /PROXY (.*):(\d+)/)[0]

                log.info "proxy $proxyHost:$proxyPort"

                chainedProxies << new ChainedProxyAdapter() {
                    @Override
                    public InetSocketAddress getChainedProxyAddress() {
                        return new InetSocketAddress(proxyHost, proxyPort as int)
                    }
                }
            }
        }
    }
}
