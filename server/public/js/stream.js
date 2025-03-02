window.onload = () => {
    document.getElementById('my-button').onclick = () => {
        init();
    }
}

let streamId = "123";

async function init() {
    const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    document.getElementById("video").srcObject = stream;

    const peer = createPeer(streamId);
    stream.getTracks().forEach(track => peer.addTrack(track, stream));
}

function createPeer(streamId) {
    const peer = new RTCPeerConnection({
        iceServers: [
            {
                urls: "stun:stun.stunprotocol.org"
            }
        ]
    });

    peer.onnegotiationneeded = () => handleNegotiationNeededEvent(peer, streamId);

    return peer;
}

async function handleNegotiationNeededEvent(peer, streamId) {
    const offer = await peer.createOffer();
    await peer.setLocalDescription(offer);

    const payload = { streamId, sdp: peer.localDescription };

    try {
        const { data } = await axios.post('/broadcast', payload);
        const desc = new RTCSessionDescription(data.sdp);
        peer.setRemoteDescription(desc);
    } catch (error) {
        console.error("Error during broadcasting:", error);
    }
}
