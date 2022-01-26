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
            const colorScheme = [
                "#25CCF7","#FD7272","#54a0ff","#00d2d3",
                "#1abc9c","#2ecc71","#3498db","#9b59b6","#34495e",
                "#16a085","#27ae60","#2980b9","#8e44ad","#2c3e50",
                "#f1c40f","#e67e22","#e74c3c","#ecf0f1","#95a5a6",
                "#f39c12","#d35400","#c0392b","#bdc3c7","#7f8c8d",
                "#55efc4","#81ecec","#74b9ff","#a29bfe","#dfe6e9",
                "#00b894","#00cec9","#0984e3","#6c5ce7","#ffeaa7",
                "#fab1a0","#ff7675","#fd79a8","#fdcb6e","#e17055",
                "#d63031","#feca57","#5f27cd","#54a0ff","#01a3a4"
            ]
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
                        borderColor: colorScheme[c],
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



            console.log(datasource);
            let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('tableChartModal'));
            window.chart = window.brainCharts.renderLineChart('tableChart', 'Data', true, window.brainCharts.defaultXAxes, datasource);
            modal.show();

            evt.stopPropagation();
            evt.preventDefault();
        })
    });
}

document.addEventListener('DOMContentLoaded', init);