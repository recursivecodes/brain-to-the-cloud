rivets.binders.mapsrc = function(el, value) {
  el.src = `/img/maps/${value}.jpg`;
};
rivets.binders.winlossbg = function(el, value) {
  el.classList.add('bg-success')
  el.classList.remove('bg-danger')
  console.log(value)
  if (value === 'loss') {
    el.classList.remove('bg-success')
    el.classList.add('bg-danger')
  }
};
rivets.formatters.map = function(value){
  // we're making an inflexible assumption here by hardcoding vanguard
  let map = `maps:vg-${value}:1`
  return window.model.codLookups[map];
}
rivets.formatters.gameMode = function(value){
  // we're making an inflexible assumption here by hardcoding vanguard
  let mode = `game-modes:vg-${value}:1`
  return window.model.codLookups[mode];
}
rivets.formatters.date = function(value){
  return moment(value).format('MM/D [@] h:mmA')
}
rivets.formatters.percent = function(value){
  return +(value * 100).toFixed(2)
}
rivets.formatters.winLoss = function(value){
  return value === 'loss' ? 'L' : 'W';
}

window.model = {
  recentMatches: {content: []},
  highlightedMatches: [],
  selectedMatch: null,
  codLookups: {},
  selectMatch: (event, binding) => {
    document.querySelectorAll('.match-card').forEach((m) => m.classList.remove('border', 'border-secondary', 'border-3'))
    event.currentTarget.classList.add('border', 'border-secondary', 'border-3')
    window.model.selectedMatch = binding.match;
  },
  loadRecentMatches: () => {
    fetch('/recentMatches/0/20')
      .then(result => result.json())
      .then(matches => window.model.recentMatches = matches)
  },

};

const init = async () => {
  // this is a bit dangerous, relying on a third party service...
  let lookupRequest = await fetch('/codLookups');
  window.model.codLookups = await lookupRequest.json();
  window.model.loadRecentMatches();
  rivets.bind(document.querySelector('body'), {model: window.model})
};

document.addEventListener('DOMContentLoaded', init);