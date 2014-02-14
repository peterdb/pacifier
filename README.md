pacifier
========

A lot of corporate environments enforce the use of a proxy server, and use [PAC](http://en.wikipedia.org/wiki/Proxy_auto-config) files to automatically choose the appropriate proxy server.

The problem is that only browsers are able to use PAC files. Most other tools only allow configuring the server and port to use as a proxy.

Pacifier is a proxy server that allows these tools to use a PAC file for proxy server selection.

Usage
=====

The pacifier.properties file contains the following properties:
- localPort: this is the local port
- pacUrl: url of the PAC file

First start pacifier, then configure the tools that need proxy access to use "localhost" as the proxy server, on the port you configured in the pacifier.properties file.

How does it work?
=================

Pacifier uses [LittleProxy](http://www.littleshoot.org/littleproxy/), a high-performance HTTP proxy written in Java.

Pacifier starts a LittleProxy http proxy server, and registers a ChainedProxyManager. This ChainedProxyManager is used to lookup "upstream" proxy servers.

Pacifier provides a ChainedProxyManager implementation that evaluates the PAC file, using the JSR 223 ScriptEngine. The result of the PAC evaluation is then parsed, and corresponding proxy server configuration is passed to LittleProxy.
