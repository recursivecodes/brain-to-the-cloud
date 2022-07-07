import {BrainCharts} from "./BrainCharts.js";

window.brainChart = null;
window.activityChart = null
window.scoreAttentionChart = null;
window.scoreMeditationChart = null;

rivets.binders.mapsrc = function(el, value) {
    el.src = `/img/maps/${value}.jpg`;
};
rivets.binders.winlossbg = function(el, value) {
    el.classList.add('bg-success')
    el.classList.remove('bg-danger')
    if (value === 'loss') {
        el.classList.remove('bg-success')
        el.classList.add('bg-danger')
    }
};
rivets.formatters.map = function(value){
    // we're making an inflexible assumption here by hardcoding vanguard
    if( value === 'mp_jalo_oasis' ) return 'Casablanca'; //not in the lookup table yet
    if( value === 'mp_monsters' ) return 'Mayhem'; //not in the lookup table yet
    if( value === 'mp_gondola' ) return 'Gondola'; //not in the lookup table yet
    if( value === 'mp_shipmas_s4' ) return 'Shipmas'; //not in the lookup table yet
    if( value === 'mp_paradise' ) return 'Paradise'; //not in the lookup table yet
    if( value === 'mp_radar' ) return 'Radar'; //not in the lookup table yet
    if( value === 'mp_ar_alps' ) return 'Alps'; //not in the lookup table yet
    if( value === 'mp_growhouse' ) return 'Sphere'; //not in the lookup table yet
    if( value === 'mp_texas' ) return 'USS Texas 1945'; //not in the lookup table yet
    let map = `maps:vg-${value}:1`
    if(!window.model.codLookups.hasOwnProperty(map)) return value;
    return window.model.codLookups[map];
}
rivets.formatters.avg = function(arr, key){
    // takes an array of objects, returns an avg for 'key'
    return arr.map((item) => item[key]).reduce((a, b) => (a + b), 0) / arr.length;
}
rivets.formatters.gameMode = function(value){
    // we're making an inflexible assumption here by hardcoding vanguard
    if( value === "kspoint" ) return 'Armageddon';
    if( value === "control" ) return 'Control';
    if( value === "base" ) return 'Arms Race';
    if( value === "gun" ) return 'Gun Game';
    let mode = `game-modes:vg-${value}:1`
    return window.model.codLookups[mode];
}
rivets.formatters.date = function(value){
    return moment(value).format('MMM D [@] h:mm A');
}
rivets.formatters.time = function(value){
    return moment(value).format('h:mmA');
}
rivets.formatters.percent = function(value){
    return +(value * 100).toFixed(2);
}
rivets.formatters.tofixed = function(value){
    if(typeof value === 'undefined') value = 0;
    return Number(value).toFixed(2);
}
rivets.formatters.winLoss = function(value){
    return value === 'loss' ? 'L' : 'W';
}

rivets.formatters.showPrev = function(value){
    if(!value.hasOwnProperty("offset")) return false;
    return value.offset > 0;
}

rivets.formatters.showNext = function(value){
    if(!value.hasOwnProperty("numberOfElements") || !value.hasOwnProperty("pageable")) return false;
    return (value.numberOfElements + value.pageable.offset) < value.totalSize;
}

window.model = {
    recentGames: {content: []},
    selectedGame: null,
    brainDataForSelectedGame: [],
    GAME_TYPE_ALL: 'recentGames',
    GAME_TYPE_HIGHLIGHTED: 'highlightedGames',
    GAME_TYPE_WITH_BRAIN_READINGS: 'gamesWithBrainReadings',
    selectedGameType: 'recentGames',
    codLookups: {},
    decorateGame: (el) => {
        document.querySelectorAll('.game-card').forEach((m) => {
            m.classList.remove('game-card-selected');
        })
        if(el) {
            el.querySelector('.card').classList.add('game-card-selected');
        }

    },
    changeGameType: (el) => {
        window.model.selectedGameType = el.target.value;
        window.model.loadRecentGames(0);
    },
    selectGame: (event, binding) => {
        window.model.decorateGame(event.currentTarget)
        window.model.selectedGame = binding.game;
        document.querySelector('.offcanvas-body').scrollTop = 0;
        window.model.loadBrainDataForSelectedGame();
    },

    renderScoreChart: (element, chartId, title) => {
        if(typeof window[chartId] !== 'undefined' && window[chartId] != null) window[chartId].destroy();
        let attentionFactor = rivets.formatters.avg(window.model.brainDataForSelectedGame, element) / 100;
        let spm = Number(window.model.selectedGame.match.playerStats.scorePerMinute).toFixed(0);
        let adjustedSpm = spm * attentionFactor;
        adjustedSpm = Number(adjustedSpm).toFixed(0);
        let diff = spm - adjustedSpm;

        const data = {
            labels: [
                'Adjusted SPM',
                'Diff',
                'SPM'
            ],
            datasets: [{
                label: 'Adjusted Score',
                data: [adjustedSpm, diff, spm],
                backgroundColor: [
                    'rgb(140,11,192)',
                    'rgb(109,109,109)',
                    'rgb(99,23,126)',
                ],
                hoverOffset: 4
            }]
        };

        window[chartId] = window.brainCharts.renderDonutPieChart(chartId, title, true, data, 'doughnut');
        window[chartId].update();
    },

    loadBrainDataForSelectedGame: () => {
        window.model.brainDataForSelectedGame = [];
        if(window.brainChart != null) window.brainChart.destroy();
        if(window.activityChart != null) window.activityChart.destroy();

        fetch(`/api/brainForGame/${window.model.selectedGame.id}`)
            .then(result => result.json())
            .then(brainData => {
                if(brainData.length > 0) {
                    window.model.brainDataForSelectedGame = brainData
                    let datasource = [{
                        label: 'Attention',
                        pointBackgroundColor: '#f14343',
                        backgroundColor: '#ee8e8e',
                        borderColor: '#CC0000',
                        fill: false,
                        data: brainData.map(brain => {
                            return {y: Number(brain.attention).toFixed(2), x: new Date(brain.createdOn)}
                        }),
                    },
                        {
                            label: 'Meditation',
                            pointBackgroundColor: '#2126c0',
                            backgroundColor: '#2178c0',
                            borderColor: '#216bc0',
                            fill: false,
                            data: brainData.map(brain => {
                                return {y: Number(brain.meditation).toFixed(2), x: new Date(brain.createdOn)}
                            }),
                        }
                    ];
                    let activityDatasource = BrainCharts.defaultActivityDatasource();
                    activityDatasource[0].data = brainData.map(brain => {return {y: Number(Math.log(brain.delta)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[1].data = brainData.map(brain => {return {y: Number(Math.log(brain.theta)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[2].data = brainData.map(brain => {return {y: Number(Math.log(brain.lowAlpha)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[3].data = brainData.map(brain => {return {y: Number(Math.log(brain.highAlpha)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[4].data = brainData.map(brain => {return {y: Number(Math.log(brain.lowBeta)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[5].data = brainData.map(brain => {return {y: Number(Math.log(brain.highBeta)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[6].data = brainData.map(brain => {return {y: Number(Math.log(brain.lowGamma)).toFixed(2), x: new Date(brain.createdOn)}});
                    activityDatasource[7].data = brainData.map(brain => {return {y: Number(Math.log(brain.highGamma)).toFixed(2), x: new Date(brain.createdOn)}});

                    const scales = {xAxes: window.brainCharts.xAxes, yAxes: window.brainCharts.defaultYAxes};
                    const activityScales = {xAxes: window.brainCharts.activityXAxes, yAxes: window.brainCharts.defaultYAxes};
                    window.activityChart = window.brainCharts.renderLineChart('activityChart', 'Activity', true, activityScales, activityDatasource);
                    window.brainChart = window.brainCharts.renderLineChart('brainChart', 'Attention/Meditation', true, scales, datasource);
                    //window.model.renderScoreChart('meditation', 'scoreMeditationChart', 'Score Adjusted for Meditation');
                    //window.model.renderScoreChart('attention', 'scoreAttentionChart', 'Score Adjusted for Attention');
                }
            });
    },
    toggleLoader: (show) => {
        let loader = document.querySelector('#loader');
        if(show) {
            loader.classList.remove('d-none')
        }
        else {
            loader.classList.add('d-none')
        }
    },
    loadRecentGames: (offset=0) => {
        window.model.toggleLoader(true);
        document.querySelector('#games').classList.add('d-none')
        fetch(`/api/${window.model.selectedGameType}/${offset}/18`)
            .then(result => result.json())
            .then(games => {
                window.model.recentGames = games;
                window.model.toggleLoader(false);
            });
        document.querySelector('#games').classList.remove('d-none')
    },
    noteChanged: (el) => {
        console.log(el);
        window.model.selectedGame.notes = el.currentTarget.value;
    },
    saveNotes: () => {
        fetch(`/api/notes/${window.model.selectedGame.id}`,
            {
                method: 'PUT',
                headers: new Headers({'content-type': 'text/plain'}),
                body: window.model.selectedGame.notes,
            }
        )
            .then(response => {
                let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('gameNotesModal'));
                modal.hide();
            })
    },
    highlightGame: (el, binding) => {
        fetch(`/api/highlightGame/${window.model.selectedGame.id}`, {method: 'PUT'})
            .then(response => {
                if(response.status === 204) {
                    window.model.selectedGame.isHighlighted = true;
                }
            })
    },
    markDistracted: (el, binding) => {
        fetch(`/api/markGameDistracted/${window.model.selectedGame.id}`, {method: 'PUT'})
            .then(response => {
                if(response.status === 204) {
                    window.model.selectedGame.isDistracted = true;
                }
            })
    },
    paginate: (direction) => {
        window.scrollTo(0,0);
        window.model.selectedGame = null;
        window.model.decorateGame();
        let games = window.model.recentGames;
        if(direction === 'prev') {
            window.model.loadRecentGames(games.pageable.number - 1)
        }
        else {
            window.model.loadRecentGames(games.pageable.number + 1)
        }
    },
};

const init = async () => {
    window.brainCharts = new BrainCharts();
    // listen for offcanvas hide and destroy charts
    const oc = document.getElementById('gameDetails');
    oc.addEventListener('hidden.bs.offcanvas', function () {
       if(window.brainChart) window.brainChart.destroy();
       if(window.activityChart) window.activityChart.destroy();
    });
    // this is a bit dangerous, relying on a third party service...
    let lookupRequest = await fetch('/api/codLookups');
    window.model.codLookups = await lookupRequest.json();
    window.model.loadRecentGames();
    rivets.bind(document.querySelector('body'), {model: window.model});
};

document.addEventListener('DOMContentLoaded', init);

/* donut with multi series

const data = {
  datasets: [
    {
      labels: ['SPM', 'Average SPM'],
      data: [1350, 501],
      backgroundColor: Object.values(Utils.CHART_COLORS),
    },
    {
      labels: ['Attention', 'Average Attention'],
      data: [73, 60],
      backgroundColor: Object.values(Utils.CHART_COLORS),
    }
  ]
};

const config = {
  type: 'doughnut',
  data: data,
  options: {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Chart.js Doughnut Chart'
      },
      tooltip: {
          callbacks: {
              label: function (context) {
                  var label = context.dataset.labels[context.dataIndex];
                  var value = context.dataset.data[context.dataIndex];
                  return label + ': ' + value;
              }
          }
      }

    }
  },
};

*/