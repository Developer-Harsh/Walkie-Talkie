window.onload = () => {
    document.getElementById('my-button').onclick = () => {
        init();
    }
}

let streamId = "123";

async function init() {
    const peer = createPeer(streamId);
    peer.addTransceiver("video", { direction: "recvonly" });
    peer.addTransceiver("audio", { direction: "recvonly" });
}

function createPeer(streamId) {
    const peer = new RTCPeerConnection({
        iceServers: [
            {
                urls: "stun:stun.stunprotocol.org"
            }
        ]
    });

    peer.ontrack = handleTrackEvent;
    peer.onnegotiationneeded = () => handleNegotiationNeededEvent(peer, streamId);

    return peer;
}

async function handleNegotiationNeededEvent(peer, streamId) {
    const offer = await peer.createOffer();
    await peer.setLocalDescription(offer);

    const payload = { streamId, sdp: peer.localDescription };

    try {
        const { data } = await axios.post('/consumer', payload);
        const desc = new RTCSessionDescription(data.sdp);
        peer.setRemoteDescription(desc);
    } catch (error) {
        console.error("Error during consuming stream:", error);
    }
}

function handleTrackEvent(event) {
    console.log("Track event received: ", event.streams[0]);
    document.getElementById("video").srcObject = event.streams[0];
}

