'use strict';

const init = () => {
    document.getElementById('gameType').addEventListener('change', function(evt) {
        const type = evt.currentTarget.value;
        window.location = `/reports/game-details/0/25/${type}`;
    })
}

document.addEventListener('DOMContentLoaded', init);