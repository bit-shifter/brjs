var Errors = require('br/Errors');

/**
* @module br/services/HtmlResourceService
*/

/**
* A service that provides access to HTML templates.
* @class
* @interface
* @alias module:br/services/HtmlResourceService
*/
function HtmlResourceService() {};

/**
 * Access an HTML template by name.
 *
 * @param {String} templateId The identifier of the root element of the template you wish to retrieve.
 */
HtmlResourceService.prototype.getHTMLTemplate = function(templateId) {
	throw new Errors.UnimplementedInterfaceError("br.services.HtmlResourceService.getHTMLTemplate() has not been implemented.");
};

module.exports = HtmlResourceService;
