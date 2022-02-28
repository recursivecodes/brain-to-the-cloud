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
window.activityDatasource = BrainCharts.defaultActivityDatasource();
window.attentionChart = null;
window.meditationChart = null;
window.activityChart = null;

const init = () => {

    document.querySelectorAll('.delete-brain-session').forEach((btn) => {
       btn.addEventListener('click', function(evt) {
           let trigger = evt.currentTarget;
           let id = trigger.getAttribute("data-id");
           let tr = trigger.closest('tr');
           fetch(`/api/brainSession/${id}`, { 'method': 'DELETE' })
               .then(() => {
                   tr.remove();
               })
       });
    });

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
            fetch(`/api/brainSessionDetails/${row.getAttribute('data-id')}`, { method: 'GET' })
            .then(result => result.json())
            .then(response => {
                document.querySelector('#sessionName').innerHTML = row.getAttribute('data-name');
                document.querySelector('#sessionNotes').innerHTML = row.getAttribute('data-notes');
                document.querySelector('#sessionStart').innerHTML = row.getAttribute('data-start');
                document.querySelector('#sessionEnd').innerHTML = row.getAttribute('data-end');
                document.querySelector('.avg-attention').innerHTML = row.getAttribute('data-attention');
                document.querySelector('.avg-meditation').innerHTML = row.getAttribute('data-meditation');
                document.querySelector('.avg-delta').innerHTML = row.getAttribute('data-avg-delta');
                document.querySelector('.avg-theta').innerHTML = row.getAttribute('data-avg-theta');
                document.querySelector('.avg-low-alpha').innerHTML = row.getAttribute('data-avg-low-alpha');
                document.querySelector('.avg-high-alpha').innerHTML = row.getAttribute('data-avg-high-alpha');
                document.querySelector('.avg-low-beta').innerHTML = row.getAttribute('data-avg-low-beta');
                document.querySelector('.avg-high-beta').innerHTML = row.getAttribute('data-avg-high-beta');
                document.querySelector('.avg-low-gamma').innerHTML = row.getAttribute('data-avg-low-gamma');
                document.querySelector('.avg-high-gamma').innerHTML = row.getAttribute('data-avg-high-gamma');
                window.attentionDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": it.attention}})
                window.meditationDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": it.meditation}})
                window.activityDatasource[0].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.delta)}})
                window.activityDatasource[1].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.theta)}})
                window.activityDatasource[2].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.lowAlpha)}})
                window.activityDatasource[3].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.highAlpha)}})
                window.activityDatasource[4].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.lowBeta)}})
                window.activityDatasource[5].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.highBeta)}})
                window.activityDatasource[6].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.lowGamma)}})
                window.activityDatasource[7].data = response.map((it) => {return {"x": it.createdOn, "y": Math.log(it.highGamma)}})
                const scales = {xAxes: window.brainCharts.xAxes, yAxes: window.brainCharts.defaultYAxes};
                const activityScales = {xAxes: window.brainCharts.activityXAxes, yAxes: window.brainCharts.defaultYAxes};
                window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, scales, window.attentionDatasource);
                window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, scales, window.meditationDatasource);
                window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity', true, activityScales, window.activityDatasource);
            });
        });
    })
}

document.addEventListener('DOMContentLoaded', init);