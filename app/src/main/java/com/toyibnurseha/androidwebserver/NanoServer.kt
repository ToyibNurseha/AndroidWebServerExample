package com.toyibnurseha.androidwebserver

import fi.iki.elonen.NanoHTTPD

class NanoServer : NanoHTTPD {
    constructor(port : Int) : super(port) {}
    constructor(hostname : String, port : Int) : super(hostname, port) {}

    override fun serve(session: IHTTPSession): Response? {
        var msg = "<html><body><h1>Hello server</h1>\n"
        val parms = session.parms
        if (parms["username"] == null) {
            msg += "<form action='?' method='get'>\n"
            msg += "<p>Your name: <input type='text' name='username'></p>\n"
            msg += "</form>\n"
        } else {
            msg += "<p>Hello, " + parms["username"] + "!</p>"
        }
        return newFixedLengthResponse("$msg</body></html>\n")
    }
}