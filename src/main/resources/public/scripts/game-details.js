'use strict';

const init = () => {
    document.getElementById('gameType').addEventListener('change', function(evt) {
        const type = evt.currentTarget.value;
        window.location = `/reports/game-details/0/25/${type}`;
    });
    document.getElementById('game').addEventListener('change', function(evt) {
        const type = document.getElementById('gameType').value;
        const game = evt.currentTarget.value;
        window.location = `/reports/game-details/0/25/${type}/${game}`;
    });
}

document.addEventListener('DOMContentLoaded', init);