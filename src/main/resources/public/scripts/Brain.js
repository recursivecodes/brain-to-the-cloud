export default class Brain {
    signalStrength;
    attention;
    meditation;
    delta;
    theta;
    lowAlpha;
    highAlpha;
    lowBeta;
    highBeta;
    lowGamma;
    highGamma;
    createdOn;

    constructor(brain) {
        this.signalStrength = +parseInt(brain.signalStrength);
        this.attention = +parseInt(brain.attention);
        this.meditation = +parseInt(brain.meditation);
        this.delta = +parseInt(brain.delta);
        this.theta = +parseInt(brain.theta);
        this.lowAlpha = +parseInt(brain.lowAlpha);
        this.highAlpha = +parseInt(brain.highAlpha);
        this.lowBeta = +parseInt(brain.lowBeta);
        this.highBeta = +parseInt(brain.highBeta);
        this.lowGamma = +parseInt(brain.lowGamma);
        this.highGamma = +parseInt(brain.highGamma);
        this.createdOn = new Date();
    }

}