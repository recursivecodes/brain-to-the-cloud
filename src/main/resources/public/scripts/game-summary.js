'use strict';

const init = () => {
    document.getElementById('game').addEventListener('change', function(evt) {
        const game = evt.currentTarget.value;
        const withBrain = document.getElementById('withBrain').value === 'true';
        window.location = `/reports/game-summary/${withBrain}/${game}`;
    });
}

document.addEventListener('DOMContentLoaded', init);