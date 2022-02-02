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
            fetch(`/api/brainSessionDetails/${el.getAttribute('data-id')}`, { method: 'GET' })
            .then(result => result.json())
            .then(response => {
                document.querySelector('#sessionName').innerHTML = row.querySelector('td:nth-of-type(1)').innerText;
                document.querySelector('#sessionNotes').innerHTML = el.getAttribute('data-notes');
                document.querySelector('#sessionStart').innerHTML = row.querySelector('td:nth-of-type(4)').innerText;
                document.querySelector('#sessionEnd').innerHTML = row.querySelector('td:nth-of-type(5)').innerText;
                document.querySelector('.avg-attention').innerHTML = row.querySelector('td:nth-of-type(2)').innerText;
                document.querySelector('.avg-meditation').innerHTML = row.querySelector('td:nth-of-type(3)').innerText
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
                window.attentionChart = window.brainCharts.renderLineChart('attentionChart', 'Attention', false, window.brainCharts.xAxes, window.brainCharts.defaultYAxes, window.attentionDatasource);
                window.meditationChart = window.brainCharts.renderLineChart('meditationChart', 'Meditation', false, window.brainCharts.xAxes, window.brainCharts.defaultYAxes, window.meditationDatasource);
                window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity', true, window.brainCharts.activityXAxes, window.brainCharts.defaultYAxes, window.activityDatasource);
            });
        });
    })
}

document.addEventListener('DOMContentLoaded', init);