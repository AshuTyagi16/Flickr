package com.sasuke.flickr.data.exception

import java.io.IOException

class NoConnectivityException : IOException() {

    override val message: String?
        get() = "Internet not connected"
}