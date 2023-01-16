/* globals IVSBroadcastClient bootstrap */
'use strict';

import Brain from './Brain.js';

const getConfig = async () => {
  const configRequest = await fetch('/web-broadcast-config');
  return await configRequest.json();
};

const handlePermissions = async () => {
  let permissions;
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    for (const track of stream.getTracks()) {
      track.stop();
    }
    permissions = { video: true, audio: true };
  }
  catch (err) {
    permissions = { video: false, audio: false };
    console.error(err.message);
  }
  if (!permissions.video) {
    console.error('Failed to get video permissions.');
  } else if (!permissions.audio) {
    console.error('Failed to get audio permissions.');
  }
};

const getDevices = async () => {
  const cameraSelect = document.getElementById('camera-select');
  const micSelect = document.getElementById('mic-select');
  const devices = await navigator.mediaDevices.enumerateDevices();
  window.videoDevices = devices.filter((d) => d.kind === 'videoinput');
  window.audioDevices = devices.filter((d) => d.kind === 'audioinput');
  window.videoDevices.forEach((device, idx) => {
    const opt = document.createElement('option');
    opt.value = device.deviceId;
    opt.innerHTML = device.label;
    if (idx === 0) {
      window.selectedVideoDeviceId = device.deviceId;
      opt.selected = true;
    }
    cameraSelect.appendChild(opt);
  });
  window.audioDevices.forEach((device, idx) => {
    const opt = document.createElement('option');
    opt.value = device.deviceId;
    opt.innerHTML = device.label;
    if (idx === 0) {
      window.selectedAudioDeviceId = device.deviceId;
      opt.selected = true;
    }
    micSelect.appendChild(opt);
  });
};

const previewVideo = () => {
  const previewEl = document.getElementById('broadcast-preview');
  window.broadcastClient.attachPreview(previewEl);
};

const selectCamera = async (e) => {
  window.selectedVideoDeviceId = e.target.value;
  createVideoStream();
};

const selectMic = async (e) => {
  window.selectedAudioDeviceId = e.target.value;
  createAudioStream();
};

const createVideoStream = async () => {
  if (window.broadcastClient && window.broadcastClient.getVideoInputDevice('camera1')) window.broadcastClient.removeVideoInputDevice('camera1');
  const streamConfig = IVSBroadcastClient.STANDARD_LANDSCAPE;
  window.videoStream = await navigator.mediaDevices.getUserMedia({
    video: {
      deviceId: {
        exact: window.selectedVideoDeviceId
      },
      pan: true,
      tilt: true,
      zoom: true,
      width: {
        ideal: streamConfig.maxResolution.width,
        max: streamConfig.maxResolution.width,
      },
      height: {
        ideal: streamConfig.maxResolution.height,
        max: streamConfig.maxResolution.height,
      },
    },
  });
  if (window.broadcastClient) window.broadcastClient.addVideoInputDevice(window.videoStream, 'camera1', { index: 0 });
};

const createAudioStream = async () => {
  if (window.broadcastClient && window.broadcastClient.getAudioInputDevice('mic1')) window.broadcastClient.removeAudioInputDevice('mic1');
  if (window.broadcastClient && window.broadcastClient.getAudioInputDevice('mic2')) window.broadcastClient.removeAudioInputDevice('mic2');
  window.audioStream = await navigator.mediaDevices.getUserMedia({
    audio: {
      deviceId: window.selectedAudioDeviceId
    },
  });
  if (window.broadcastClient) window.broadcastClient.addAudioInputDevice(window.audioStream, 'mic1');
};

const startBroadcast = () => {
  window.broadcastClient
      .startBroadcast(window.config.streamKey)
      .then(() => {
        window.isBroadcasting = true;
      })
      .catch((error) => {
        window.isBroadcasting = false;
        console.error(error);
      })
      .finally(() => {
        toggleIndicators();
      });
};

const stopBroadcast = async () => {
  window.broadcastClient.stopBroadcast();
  window.isBroadcasting = false;
  await createVideoStream();
  toggleIndicators();
};

const toggleIndicators = () => {
  const indicator = document.getElementById('online-indicator');
  const broadcastBtn = document.getElementById('broadcast-btn');

  if (window.isBroadcasting) {
    indicator.classList.remove('bg-white');
    indicator.classList.remove('text-dark');
    indicator.classList.add('bg-danger');
    indicator.classList.add('text-white');
    indicator.innerHTML = 'LIVE';
    broadcastBtn.classList.remove('btn-outline-success');
    broadcastBtn.classList.add('btn-danger');
    broadcastBtn.innerHTML = 'Stop Broadcast';
  }
  else {
    indicator.classList.remove('bg-danger');
    indicator.classList.remove('text-white');
    indicator.classList.add('bg-white');
    indicator.classList.add('text-dark');
    indicator.innerHTML = 'Offline';
    broadcastBtn.classList.add('btn-outline-success');
    broadcastBtn.classList.remove('btn-danger');
    broadcastBtn.innerHTML = 'Broadcast';
  }
}

const toggleBroadcast = async () => {

  if (!window.isBroadcasting) {
    startBroadcast();
  }
  else {
    stopBroadcast();
  }
};

const sendMetadata = async (metadata) => {
  const metaRequest = await fetch('https://8xyigjo5yg.execute-api.us-east-1.amazonaws.com/Prod/send-metadata', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      channelArn: window.config.channelArn,
      metadata: JSON.stringify(metadata),
    })
  });
  const metaResponse = await metaRequest.json();
  console.log(metaResponse);
};

const connectWebsocket = async () => {
  console.log('Connecting to WebSocket...')
  const protocol = location.protocol.indexOf("https") > -1 ? "wss" : "ws";
  window.ws = new WebSocket(`${protocol}://${location.hostname}:${location.port}/ws/demo`);
  window.ws.onopen = (msg) => {
    console.log('Connected!')
  };
  window.ws.onmessage = async (msg) => {
    const parsedMsg = JSON.parse(msg.data);
    if( parsedMsg.hasOwnProperty("joined") || parsedMsg.hasOwnProperty("closed") ) {
      return
    }
    const brain = new Brain(parsedMsg);
    console.log(brain);
    if(window.isBroadcasting) sendMetadata(brain);
  };
  window.ws.onclose = (e) => {
    console.log('WebSocket closed!')
  };
  window.ws.onerror = function(err) {
    console.error('Socket encountered error: ', err.message, 'Closing socket');
    window.ws.close();
  };
};


const init = async () => {
  let tooltipTriggers = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  let tooltips = tooltipTriggers.map(function (el) {
    return new bootstrap.Tooltip(el)
  });
  console.log('init');
  window.config = await getConfig();

  await handlePermissions();
  await getDevices();

  window.broadcastClient = IVSBroadcastClient.create({
    streamConfig: IVSBroadcastClient.STANDARD_LANDSCAPE,
    ingestEndpoint: window.config.ingestEndpoint,
  });

  await createVideoStream();
  await createAudioStream();

  previewVideo();

  document.getElementById('broadcast-btn').addEventListener('click', toggleBroadcast);
  document.getElementById('camera-select').addEventListener('change', selectCamera);
  document.getElementById('mic-select').addEventListener('change', selectMic);

  connectWebsocket();
};

document.addEventListener('DOMContentLoaded', init);