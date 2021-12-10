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
  recentMatches: {content: []},
  highlightedMatches: [],
  selectedMatch: null,
  codLookups: {},
  decorateMatch: (el) => {
    document.querySelectorAll('.match-card').forEach((m) => m.classList.remove('border', 'border-secondary', 'border-3'))
    if(el) {
      el.classList.add('border', 'border-secondary', 'border-3')
    }
  },
  selectMatch: (event, binding) => {
    window.model.decorateMatch(event.currentTarget)
    window.model.selectedMatch = binding.match;
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
  loadRecentMatches: (offset=0) => {
    window.model.toggleLoader(true);
    document.querySelector('#matches').classList.add('d-none')
    fetch(`/api/recentMatches/${offset}/20`)
      .then(result => result.json())
      .then(matches => {
        window.model.recentMatches = matches
        window.model.toggleLoader(false);
      });
      document.querySelector('#matches').classList.remove('d-none')
  },
  highlightMatch: (el, binding) => {
    fetch(`/api/highlightGame/${window.model.selectedMatch.id}`, {method: 'PUT'})
      .then(response => console.log(response))
  },
  paginate: (direction) => {
    window.model.selectedMatch = null;
    window.model.decorateMatch();
    let matches = window.model.recentMatches
    if(direction === 'prev') {
      window.model.loadRecentMatches(matches.pageable.number - 1)
    }
    else {
      window.model.loadRecentMatches(matches.pageable.number + 1)
    }
  },
};

const init = async () => {
  // this is a bit dangerous, relying on a third party service...
  let lookupRequest = await fetch('/api/codLookups');
  window.model.codLookups = await lookupRequest.json();
  window.model.loadRecentMatches();
  rivets.bind(document.querySelector('body'), {model: window.model})
};

document.addEventListener('DOMContentLoaded', init);