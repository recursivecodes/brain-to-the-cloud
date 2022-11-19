'use strict';

const init = () => {
    document.getElementById('game').addEventListener('change', function(evt) {
        const game = evt.currentTarget.value;
        window.location = `/reports/attention-meditation/${game}`;
    });
}

document.addEventListener('DOMContentLoaded', init);