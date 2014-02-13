package be.thistledown.pacifier

import io.netty.handler.codec.http.DefaultHttpRequest
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpVersion
import org.junit.Test
import org.littleshoot.proxy.ChainedProxy

public class PacBasedChainedProxyManagerTest {

    @Test
    public void lookupChainedProxies() {
        lookupAndAssert("/always_direct.pac") { proxies ->
            assert proxies.isEmpty()
        }

        lookupAndAssert("/single_proxy.pac") { proxies ->
            assert 1 == proxies.size()
            assert new InetSocketAddress("some.proxy", 8080) == proxies[0].chainedProxyAddress
        }

        lookupAndAssert("/multiple_proxies.pac") { proxies ->
            assert 2 == proxies.size()
            assert new InetSocketAddress("some.proxy1", 8080) == proxies[0].chainedProxyAddress
            assert new InetSocketAddress("some.proxy2", 8080) == proxies[1].chainedProxyAddress
        }
    }

    private static lookupAndAssert(String pac, Closure checkThis) {
        PacBasedChainedProxyManager mgr = new PacBasedChainedProxyManager(this.class.getResource(pac) as String)

        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.google.be")

        Queue<ChainedProxy> proxies = new ArrayDeque<>()

        mgr.lookupChainedProxies(request, proxies)

        checkThis(proxies)
    }

}
