package com.toyibnurseha.androidwebserver

import fi.iki.elonen.NanoHTTPD

class AndroidWebServer : NanoHTTPD {
    constructor(port: Int) : super(port) {}
    constructor(hostname: String?, port: Int) : super(hostname, port) {} //...
}