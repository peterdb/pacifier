package be.thistledown.pacifier

import groovy.util.logging.Log
import org.littleshoot.proxy.HttpProxyServer
import org.littleshoot.proxy.impl.DefaultHttpProxyServer

@Log
public class Main {
    // TODO enable stop/restart
    // TODO check if pac file changes
    // TODO add systray icon
    // TODO add log console

    public static void main(String[] args) throws IOException {
        log.info "Starting Pacifier"

        Properties props = loadProperties()
        log.info "Properties: $props"

        HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
                .withPort(props.localPort as int)
                .withChainProxyManager(new PacBasedChainedProxyManager(props.pacUrl))
                .start()
        log.info "Pacifier server started on port $props.localPort"
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        new File('pacifier.properties').withInputStream {
            properties.load(it)
        }
        return properties
    }

}