'use strict';
import Brain from './Brain.js';
import {BrainCharts} from "./BrainCharts.js";
import {mean, median, mode} from "./utils.js";

window.ivsPlayer;
window.streamUrl = 'https://f99084460c35.us-east-1.playback.live-video.net/api/video/v1/us-east-1.639934345351.channel.NdZUmxybljXA.m3u8';
window.chartsInit = false;
window.maxPoints = 24;
window.sessionStart = null;
window.sessionEnd = null;

const initCharts = () => {
    window.brainCharts = new BrainCharts()

    let attentionDatasource = [{
        pointBackgroundColor: "#f14343",
        backgroundColor: "#ee8e8e",
        borderColor: "#CC0000",
        data: [],
    }];
    let meditationDatasource = [{
        pointBackgroundColor: "#2126c0",
        backgroundColor: "#2178c0",
        borderColor: "#216bc0",
        data: [],
    }];
    let activityDatasource = BrainCharts.defaultActivityDatasource();

    const scales = {xAxes: window.brainCharts.xAxes, yAxes: window.brainCharts.defaultYAxes};
    const activityScales = {xAxes: window.brainCharts.activityXAxes, yAxes: window.brainCharts.defaultYAxes};
    window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, scales, attentionDatasource);
    window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, scales, meditationDatasource);
}

const pollForVideo = (interval = 1000) => {
    if (!window.loadInterval) {
        window.loadInterval = setInterval(() => {
            window.ivsPlayer.load(window.streamUrl);
            window.ivsPlayer.play();
        }, interval);
    }
};

const stopPolling = () => {
    clearInterval(window.loadInterval);
    window.loadInterval = null;
}

const initPlayer = () => {
    if (IVSPlayer.isPlayerSupported) {
        // create player
        window.ivsPlayer = IVSPlayer.create();
        // attach to <video> element
        window.ivsPlayer.attachHTMLVideoElement(document.getElementById('video-player'));
        // load stream
        window.ivsPlayer.load(window.streamUrl);
        // play stream
        window.ivsPlayer.play();
        // listen for 'stream playing' event and toggle status indicator
        window.ivsPlayer.addEventListener(IVSPlayer.PlayerEventType.STATE_CHANGED, async (state) => {
            toggleIndicators(state.toLowerCase() === 'playing');
        });
        window.ivsPlayer.addEventListener(IVSPlayer.PlayerEventType.TEXT_METADATA_CUE, (e) => {
            handleMeta(JSON.parse(e.text));
        });
        window.ivsPlayer.addEventListener(IVSPlayer.PlayerState.PLAYING, (evt) => {
            window.isPlaying = true;
            stopPolling();
        });
        window.ivsPlayer.addEventListener(IVSPlayer.PlayerState.ENDED, (evt) => {
            window.isPlaying = false;
            pollForVideo();
        });
        window.ivsPlayer.addEventListener(IVSPlayer.PlayerEventType.ERROR, (evt) => {
            if(!window.isPlaying) pollForVideo();
        });

        setInterval(() => {
            document.getElementById('quality').innerHTML = ivsPlayer.getQuality().name;
            document.getElementById('bitrate').innerHTML = `${ivsPlayer.getQuality().bitrate / 1000} kbps`;
            document.getElementById('latency').innerHTML = `${ivsPlayer.getLiveLatency().toFixed(2)} seconds`;
            document.getElementById('buffer').innerHTML = `${ivsPlayer.getBufferDuration().toFixed(2)} seconds`;
        }, 1500);
    }
}

const toggleIndicators = (playing) => {
    const indicator = document.getElementById('online-indicator');

    if (playing) {
        indicator.classList.remove('bg-white');
        indicator.classList.remove('text-dark');
        indicator.classList.add('bg-danger');
        indicator.classList.add('text-white');
        indicator.innerHTML = 'LIVE';
    }
    else {
        indicator.classList.remove('bg-danger');
        indicator.classList.remove('text-white');
        indicator.classList.add('bg-white');
        indicator.classList.add('text-dark');
        indicator.innerHTML = 'Offline';
    }
}

const handleMeta = (msg) => {
    const brain = new Brain(msg);

    // keep the latest `maxPoints`
    if( window.attentionChart.data.datasets[0].data.length > window.maxPoints ) window.attentionChart.data.datasets[0].data.shift();
    if( window.meditationChart.data.datasets[0].data.length > window.maxPoints ) window.meditationChart.data.datasets[0].data.shift();

    window.brainCharts.brainReadings.push(brain);
    window.attentionChart.data.datasets[0].data.push({y: brain.attention, x: brain.createdOn});
    window.meditationChart.data.datasets[0].data.push({y: brain.meditation, x: brain.createdOn});

    if (window.chartsInit) {
        window.attentionChart.update();
        window.meditationChart.update();
    }

    if(brain.meditation > window.brainCharts.meditationHigh) window.brainCharts.meditationHigh = brain.meditation;
    if(brain.meditation < window.brainCharts.meditationLow) window.brainCharts.meditationLow = brain.meditation;
    if(brain.attention > window.brainCharts.attentionHigh) window.brainCharts.attentionHigh = brain.attention;
    if(brain.attention < window.brainCharts.attentionLow) window.brainCharts.attentionLow = brain.attention;

    document.querySelector('#meditationHigh').innerHTML = window.brainCharts.meditationHigh;
    document.querySelector('#meditationLow').innerHTML = window.brainCharts.meditationLow;
    let meditationVals = window.brainCharts.brainReadings.map(b => b.meditation);
    document.querySelector('#meditationMean').innerHTML = mean(meditationVals);
    document.querySelector('#meditationMedian').innerHTML = median(meditationVals);
    document.querySelector('#meditationMode').innerHTML = mode(meditationVals);
    document.querySelector('#attentionHigh').innerHTML = window.brainCharts.attentionHigh;
    document.querySelector('#attentionLow').innerHTML = window.brainCharts.attentionLow;
    let attentionVals = window.brainCharts.brainReadings.map(b => b.attention);
    document.querySelector('#attentionMean').innerHTML = mean(attentionVals);
    document.querySelector('#attentionMedian').innerHTML = median(attentionVals);
    document.querySelector('#attentionMode').innerHTML = mode(attentionVals);

    document.querySelector('#currentSignalStrength').innerHTML = brain.signalStrength;
    document.querySelector('#currentAttention').innerHTML = brain.attention;
    document.querySelector('#currentMeditation').innerHTML = brain.meditation;
}

const init = () => {
    let tooltipTriggers = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    let tooltips = tooltipTriggers.map(function (el) {
        return new bootstrap.Tooltip(el)
    });
    initCharts();
    initPlayer();
    window.chartsInit = true;
};

document.addEventListener('DOMContentLoaded', init);