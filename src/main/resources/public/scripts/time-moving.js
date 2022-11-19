'use strict';

const init = () => {
    document.getElementById('game').addEventListener('change', function(evt) {
        const game = evt.currentTarget.value;
        window.location = `/reports/time-moving/${game}`;
    });
}

document.addEventListener('DOMContentLoaded', init);