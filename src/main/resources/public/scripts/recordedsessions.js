'use strict';

import {BrainCharts} from "./BrainCharts.js";

window.brainCharts = new BrainCharts();

window.attentionDatasource = [{
    pointBackgroundColor: "#f14343",
    backgroundColor: "#ee8e8e",
    borderColor: "#CC0000",
    data: [],
}];
window.meditationDatasource = [{
    pointBackgroundColor: "#2126c0",
    backgroundColor: "#2178c0",
    borderColor: "#216bc0",
    data: [],
}];
window.activityDatasource = [
    {
        label: 'Delta',
        borderColor: "#9400D3",
        backgroundColor: "#9400D3",
        pointBackgroundColor: "#9400D3",
        fill: false,
        data: []
    },
    {
        label: 'Theta',
        borderColor: "#4B0082",
        backgroundColor: "#4B0082",
        pointBackgroundColor: "#4B0082",
        fill: false,
        data: []
    },
    {
        label: 'Low Alpha',
        borderColor: "#0000FF",
        backgroundColor: "#0000FF",
        pointBackgroundColor: "#0000FF",
        fill: false,
        data: []
    },
    {
        label: 'High Alpha',
        borderColor: "#00FF00",
        backgroundColor: "#00FF00",
        pointBackgroundColor: "#00FF00",
        fill: false,
        data: []
    },
    {
        label: 'Low Beta',
        borderColor: "#FFFF00",
        backgroundColor: "#FFFF00",
        pointBackgroundColor: "#FFFF00",
        fill: false,
        data: []
    },
    {
        label: 'High Beta',
        borderColor: "#FF7F00",
        backgroundColor: "#FF7F00",
        pointBackgroundColor: "#FF7F00",
        fill: false,
        data: []
    },
    {
        label: 'Low Gamma',
        borderColor: "#fa61bd",
        backgroundColor: "#fa61bd",
        pointBackgroundColor: "#fa61bd",
        fill: false,
        data: []
    },
    {
        label: 'High Gamma',
        borderColor: "#FF0000",
        backgroundColor: "#FF0000",
        pointBackgroundColor: "#FF0000",
        fill: false,
        data: []
    },
];
window.attentionChart = null;
window.meditationChart = null;
window.activityChart = null;

const init = () => {

    document.getElementById('brainSessionDetails').addEventListener('hidden.bs.offcanvas', function() {
        if(window.attentionChart) window.attentionChart.destroy();
        if(window.meditationChart) window.meditationChart.destroy();
        if(window.activityChart) window.activityChart.destroy();
        document.querySelector('.offcanvas-body').scrollTop = 0;
    });

    document.querySelectorAll('.brain-session-details-trigger').forEach((a) => {
        a.addEventListener('click', function(evt) {
            const el = evt.target;
            const row = el.closest('tr');
            bootstrap.Offcanvas.getOrCreateInstance(document.querySelector('#brainSessionDetails')).show();
            fetch(`/api/brainSessionDetails/${el.getAttribute('data-id')}`, { method: 'GET' })
            .then(result => result.json())
            .then(response => {
                document.querySelector('#sessionName').innerHTML = row.querySelector('td:nth-of-type(1)').innerText;
                document.querySelector('#sessionNotes').innerHTML = el.getAttribute('data-notes');
                document.querySelector('#sessionStart').innerHTML = row.querySelector('td:nth-of-type(4)').innerText;
                document.querySelector('#sessionEnd').innerHTML = row.querySelector('td:nth-of-type(5)').innerText;
                window.attentionDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": it.attention}})
                window.meditationDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": it.meditation}})
                window.activityDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": it.delta / 1000}})
                window.activityDatasource[1].data = response.map((it) => {return {"x": it.createdOn, "y": it.theta / 1000}})
                window.activityDatasource[2].data = response.map((it) => {return {"x": it.createdOn, "y": it.lowAlpha / 1000}})
                window.activityDatasource[3].data = response.map((it) => {return {"x": it.createdOn, "y": it.highAlpha / 1000}})
                window.activityDatasource[4].data = response.map((it) => {return {"x": it.createdOn, "y": it.lowBeta / 1000}})
                window.activityDatasource[5].data = response.map((it) => {return {"x": it.createdOn, "y": it.highBeta / 1000}})
                window.activityDatasource[6].data = response.map((it) => {return {"x": it.createdOn, "y": it.lowGamma / 1000}})
                window.activityDatasource[7].data = response.map((it) => {return {"x": it.createdOn, "y": it.highGamma / 1000}})
                window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, window.brainCharts.xAxes, window.attentionDatasource);
                window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, window.brainCharts.xAxes, window.meditationDatasource);
                window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity', true, window.brainCharts.activityXAxes, window.activityDatasource);
            });
        });
    })
}

document.addEventListener('DOMContentLoaded', init);