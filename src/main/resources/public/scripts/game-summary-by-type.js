'use strict';

const init = () => {
    document.getElementById('game').addEventListener('change', function(evt) {
        const game = evt.currentTarget.value;
        const type = document.getElementById('type').value;
        window.location = `/reports/game-summary-by-type/${type}/${game}`;
    });
}

document.addEventListener('DOMContentLoaded', init);