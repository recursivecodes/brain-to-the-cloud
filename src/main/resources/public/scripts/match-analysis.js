rivets.binders.src = function(el, value) {
  el.src = `/img/maps/${value}.jpg`;
};

rivets.formatters.map = function(value){
  let map = `maps:${value}`
  return window.model.codLookups[map];
}

window.model = {
  recentMatches: [],
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
  let lookupRequest = await fetch('https://my.callofduty.com/content/atvi/callofduty/mycod/web/en/data/json/iq-content-xweb.js');
  window.model.codLookups = await lookupRequest.json();
  window.model.loadRecentMatches();
  rivets.bind(document.querySelector('body'), {model: window.model})
};

document.addEventListener('DOMContentLoaded', init);