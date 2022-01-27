'use strict';

import {BrainCharts} from "./BrainCharts.js";

const init = () => {
    window.brainCharts = new BrainCharts();
    let modalEl = document.getElementById('tableChartModal');
    modalEl.addEventListener('hidden.bs.modal', function() {
        if(window.chart) window.chart.destroy();
    });

    let tables = document.querySelectorAll('table');

    tables.forEach((tbl) => {
        let allRows = tbl.querySelectorAll('tr');
        for(let r=0; r<allRows.length; r++) {
            let tr = allRows[r];
            let th = tr.querySelector('th:nth-of-type(1)');
            let checkBox = document.createElement('input')
            checkBox.type = 'checkbox'
            checkBox.checked = true;
            checkBox.classList.add('col-check', 'form-check-input', 'me-2');
            if(r === 0) checkBox.classList.add('check-all-col')
            th.prepend(checkBox);
        }

        tbl.querySelector('.check-all-col').addEventListener('change', function(evt) {
            evt.target.closest('table').querySelectorAll('.col-check:not(.check-all-col)').forEach((cbox) => {
                cbox.checked = evt.target.checked;
            })
        });

        let tbody = tbl.querySelector('tbody');
        let colCount = tbody.firstElementChild.childElementCount;
        let newTr = document.createElement('tr')

        for(let c=0; c<colCount; c++) {
            let newTd = document.createElement('td');
            newTd.classList.add('text-center', 'align-middle', 'bg-theme')
            if(c===0) {
                let anchorWrapper = document.createElement('span');
                anchorWrapper.tabIndex = 0;
                anchorWrapper.setAttribute('data-bs-toggle', 'tooltip');
                anchorWrapper.setAttribute('title', 'Select column(s) to chart');

                let anchor = document.createElement('a');
                anchor.setAttribute('href', '#');
                anchor.classList.add('table-chart-trigger', 'btn', 'btn-outline-light', 'd-block', 'disabled');

                new bootstrap.Tooltip(anchorWrapper);
                let i = document.createElement('i');
                i.classList.add('bi', 'bi-bar-chart');

                anchor.append(i);
                anchorWrapper.append(anchor);
                newTd.append(anchorWrapper);
                newTr.append(newTd)
            }
            else {
                let checkBox = document.createElement('input')
                checkBox.type = 'checkbox'
                checkBox.classList.add('data-check', 'form-check-input');
                newTd.append(checkBox);
                newTr.append(newTd)
            }
        }
        tbody.append(newTr);
    });

    document.querySelectorAll('.data-check').forEach((c) => {
        c.addEventListener('change', function(evt) {
            let trigger = evt.target.closest('tr').querySelector('.table-chart-trigger');
            let wrapper = trigger.closest('span');
            let tt = bootstrap.Tooltip.getOrCreateInstance(wrapper);

            if( document.querySelectorAll('.data-check:checked').length ) {
                trigger.classList.remove('disabled');
                tt.disable();
            }
            else {
                trigger.classList.add('disabled');
                tt.enable();
            }
        })
    })

    let triggers = document.querySelectorAll('.table-chart-trigger');
    triggers.forEach((link) => {
        link.classList.remove('d-none')
        link.addEventListener('click', function(evt) {
            const colorScheme = ['#8012ed', '#b301ca', '#de079a', '#f82165', '#fe4c35', '#ed7f12', '#cab301', '#9ade07', '#65f821', '#35fe4c', '#12ed7f', '#01cab3', '#079ade', '#2165f8', '#4c35fe'];
            let table = evt.target.closest('table');
            let tbody = table.querySelector('tbody');
            let thead = table.querySelector('thead');
            let headRow = Array.prototype.slice.call(thead.querySelector('tr').children);
            let rows = Array.prototype.slice.call(tbody.children);
            let lineChart = rows.length > 2;
            let datasource = lineChart ? [] : {
                labels: [],
                datasets: [{
                    label: table.parentElement.previousElementSibling.innerText,
                    data: [],
                    backgroundColor: [],
                    hoverOffset: 4
                }]
            };
            let tr = evt.target.closest('tr');
            let checkboxes = Array.prototype.slice.call(tr.querySelectorAll('.data-check'));

            for(let c=0; c<=checkboxes.length-1; c++) {
                if(checkboxes[c].checked) {
                    let dataset = {
                        label: headRow[c+1].innerText.trim(),
                        backgroundColor: colorScheme[c],
                        pointBackgroundColor: colorScheme[c],
                        borderColor: colorScheme[c],
                        data: [],
                    }
                    for(let i=0; i<=rows.length-2; i++) {
                        let row = rows[i].children;
                        let includeRow = row[0].querySelector('.col-check:checked')
                        if(includeRow) {
                            let x = row[0].innerText;
                            let y = (row[c+1].innerHTML).replace(',', '').replace('%', '');
                            if(lineChart) {
                                let datapoint = { x: x, y: parseFloat(y) };
                                dataset.data.push(datapoint)
                            }
                            else {
                                datasource.datasets[0].backgroundColor.push(colorScheme[c]);
                                datasource.labels.push(headRow[c+1].innerText.trim());
                                datasource.datasets[0].data.push(y);
                            }
                        }
                    }
                    if(lineChart) datasource.push(dataset);
                }
            }
            let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('tableChartModal'));
            if(lineChart) {
                window.chart = window.brainCharts.renderLineChart('tableChart', table.parentElement.previousElementSibling.innerText, true, window.brainCharts.defaultXAxes, datasource);
            }
            else {
                window.chart = window.brainCharts.renderDonutPieChart('tableChart', table.parentElement.previousElementSibling.innerText, true, datasource, 'pie');
            }
            modal.show();
            evt.stopPropagation();
            evt.preventDefault();
        })
    });
}

document.addEventListener('DOMContentLoaded', init);