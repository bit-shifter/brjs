'use strict';

/**
 * @name br.test.FixtureRegistry
 * @class
 * The <code>FixtureRegistry</code> allows for registration of fixtures for a specified scope.
 * @interface
 */
function FixtureRegistry() {
};

/**
 * Adds a fixture to the registry.
 * 
 * @param {String} scope The scope to which the fixture should be registered.
 * @param {br.test.Fixture} fixture The fixture to register.
 */
FixtureRegistry.prototype.addFixture = function(scope, fixture) {
};

module.exports = FixtureRegistry;
