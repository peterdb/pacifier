package be.thistledown.pacifier.util

import org.junit.Test

class UrlUtilTest {

    @Test
    public void getHost() {
        assert 'www.google.com' == UrlUtil.getHost("http://www.google.com")
        assert 'www.google.com' == UrlUtil.getHost("https://www.google.com:443")
        assert 'google.com' == UrlUtil.getHost("google.com:443")
        assert 'google.com' == UrlUtil.getHost("google.com")
    }

}
