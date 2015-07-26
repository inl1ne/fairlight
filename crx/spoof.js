/**
 * Created by Ian on 7/21/2015.
 */


chrome.webRequest.onBeforeSendHeaders.addListener(
    function(details) {
        console.log("howdy!")
        for (var i = 0; i < details.requestHeaders.length; ++i) {
            if (details.requestHeaders[i].name === 'User-Agent') {
                details.requestHeaders[i].value = 'Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12B411';
            }
        }
        return {requestHeaders: details.requestHeaders};
    },
    { urls: ["https://companion.orerve.net/*"] },
    ["blocking", "requestHeaders"]
);

/**
 *
 * @param {chrome.runtime.Port} port
 */
function grabDataByForce(port) {
    var xhr = new XMLHttpRequest()
    xhr.open("GET", "https://companion.orerve.net/profile", true)
    xhr.addEventListener("load", function(e) {
        // Send results back to the webpage that requested them
        port.postMessage({what: "discreet-delivery", cargo: xhr.responseText})

    })
    xhr.addEventListener("error", function(e) {
        port.postMessage("oops!")
    })
    xhr.send()
}

chrome.runtime.onConnectExternal.addListener(
    function(port) {
        console.log("Connect!")
        port.onMessage.addListener(
            function(msg, port) {
                if (msg.what = "hijack") {
                    grabDataByForce(port)
                }
            }
        )
        port.postMessage({what: "ok"})
    }
)