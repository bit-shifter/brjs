'use strict';

var br = require('br/Core');
var Errors = require('br/Errors');
var ViewFixtureHandler = require('br/test/viewhandler/ViewFixtureHandler');
var Utils = require('br/test/Utils');

/**
 * @name br.test.viewhandler.MouseUp
 * @class
 * <code>MouseUp ViewFixtureHandler</code> can be used to trigger <code>mouseup</code> event for a view element.
 * Example usage:
 * <p>
 * <code>when("test.page.(#aRealButton).mouseUp => true");</code>
 * </p>
 * @constructor
 * @implements br.test.viewhandler.ViewFixtureHandler
 */
function MouseUp() {
}
br.implement(MouseUp, ViewFixtureHandler);

MouseUp.prototype.set = function(eElement, mValues) {
	Utils.fireMouseEvent(eElement, 'mouseup', mValues);
};

MouseUp.prototype.get = function(eElement) {
	throw new Errors.InvalidTestError("The mouseUp event cannot be used in a doGiven or doThen");
};

module.exports = MouseUp;
