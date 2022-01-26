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
        let tbody = tbl.querySelector('tbody');
        let colCount = tbody.firstElementChild.childElementCount;
        let newTr = document.createElement('tr')
        for(let c=0; c<colCount; c++) {
            let newTd = document.createElement('td');
            newTd.classList.add('text-center', 'align-middle', 'bg-theme')
            if(c==0) {
                let anchor = document.createElement('a');
                anchor.setAttribute('href', '#');
                anchor.classList.add('table-chart-trigger', 'link-light');
                let i = document.createElement('i');
                i.classList.add('bi', 'bi-bar-chart');
                anchor.append(i);
                newTd.append(anchor);
                newTr.append(newTd)
            }
            else {
                let checkBox = document.createElement('input')
                checkBox.type = 'checkbox'
                checkBox.classList.add('data-check', 'form-input');
                newTd.append(checkBox);
                newTr.append(newTd)
            }
        }
        tbody.append(newTr);
    });

    let triggers = document.querySelectorAll('.table-chart-trigger');
    triggers.forEach((link) => {
        link.classList.remove('d-none')
        link.addEventListener('click', function(evt) {
            const colorScheme = ['#8012ed', '#9f05da', '#bd01c1', '#d604a4', '#eb0f85', '#f82165', '#fe3947', '#fc562d', '#f27517', '#e19508', '#cab301', '#aece02', '#8fe50a', '#70f51a', '#51fd31', '#35fe4c', '#1ef76a', '#0de88a', '#03d2a9', '#01b8c6', '#079ade', '#147af0', '#295bfb', '#423efe', '#6025fa'];
            let table = evt.target.closest('table');
            let tbody = table.querySelector('tbody');
            let thead = table.querySelector('thead');
            let headRow = Array.prototype.slice.call(thead.querySelector('tr').children);
            let rows = Array.prototype.slice.call(tbody.children);
            let datasource = [];
            let tr = evt.target.closest('tr');
            let checkboxes = Array.prototype.slice.call(tr.querySelectorAll('.data-check'));
            for(let c=0; c<=checkboxes.length-1; c++) {
                let checkbox = checkboxes[c];
                if(checkbox.checked) {
                    let ds = {
                        label: headRow[c+1].innerText.trim(),
                        borderColor: colorScheme[Math.floor(Math.random()*colorScheme.length)],
                        data: [],
                    };
                    for(let i=0; i<=rows.length-2; i++) {
                        let row = rows[i].children;
                        let y = (row[c+1].innerHTML).replace(',', '').replace('%', '');
                        let datapoint = {
                            x: row[0].innerHTML,
                            y: parseFloat(y),
                        }
                        ds.data.push(datapoint)
                    }
                    datasource.push(ds);
                }
            }
            let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('tableChartModal'));
            window.chart = window.brainCharts.renderLineChart('tableChart', table.parentElement.previousElementSibling.innerText, true, window.brainCharts.defaultXAxes, datasource);
            modal.show();
            evt.stopPropagation();
            evt.preventDefault();
        })
    });
}

document.addEventListener('DOMContentLoaded', init);