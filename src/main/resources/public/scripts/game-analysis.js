import {BrainCharts} from "./BrainCharts.js";

window.brainChart = null;

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
  if( value === 'mp_paradise' ) return 'Paradise'; //not in the lookup table yet
  if( value === 'mp_radar' ) return 'Radar'; //not in the lookup table yet
  let map = `maps:vg-${value}:1`
  if(!window.model.codLookups.hasOwnProperty(map)) return value;
  return window.model.codLookups[map];
}
rivets.formatters.gameMode = function(value){
  // we're making an inflexible assumption here by hardcoding vanguard
  let mode = `game-modes:vg-${value}:1`
  return window.model.codLookups[mode];
}
rivets.formatters.date = function(value){
  return moment(value).format('MM/D [@] h:mmA');
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
    document.querySelectorAll('.game-card').forEach((m) => m.classList.remove('border', 'border-secondary', 'border-1', 'bg-dark', 'bg-gradient'))
    if(el) {
      el.classList.add('border', 'border-secondary', 'border-1', 'bg-dark', 'bg-gradient')
    }
  },
  changeGameType: (el) => {
    window.model.selectedGameType = el.target.value;
    window.model.loadRecentGames(0);
  },
  selectGame: (event, binding) => {
    window.model.decorateGame(event.currentTarget)
    window.model.selectedGame = binding.game;
    window.model.loadBrainDataForSelectedGame();
  },
  loadBrainDataForSelectedGame: () => {
    window.model.brainDataForSelectedGame = [];
    if(window.brainChart != null) window.brainChart.destroy();

    let start = window.model.selectedGame.match.utcStartSeconds;
    let end = window.model.selectedGame.match.utcEndSeconds;
    fetch(`/api/brainForGame/${start}/${end}`)
      .then(result => result.json())
      .then(brainData => {
        if(brainData.length > 0) {
          window.model.brainDataForSelectedGame = brainData
          brainChart
          let datasource = [{
              label: 'Attention',
              pointBackgroundColor: '#f14343',
              backgroundColor: '#ee8e8e',
              borderColor: '#CC0000',
              fill: false,
              data: brainData.map(brain => {
                return {y: brain.attention, x: new Date(brain.createdOn)}
              }),
            },
            {
              label: 'Meditation',
              pointBackgroundColor: '#2126c0',
              backgroundColor: '#2178c0',
              borderColor: '#216bc0',
              fill: false,
              data: brainData.map(brain => {
                return {y: brain.meditation, x: new Date(brain.createdOn)}
              }),
            }
          ];
          window.brainChart = window.brainCharts.renderLineChart('brainChart', 'Attention/Meditation', true, window.brainCharts.xAxes, datasource);
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
  // this is a bit dangerous, relying on a third party service...
  let lookupRequest = await fetch('/api/codLookups');
  window.model.codLookups = await lookupRequest.json();
  window.model.loadRecentGames();
  rivets.bind(document.querySelector('body'), {model: window.model})
};

document.addEventListener('DOMContentLoaded', init);