/* automatically generated by JSCoverage - do not edit */
if (typeof _$jscoverage === 'undefined') _$jscoverage = {};
if (! _$jscoverage['levels.js']) {
  _$jscoverage['levels.js'] = [];
  _$jscoverage['levels.js'][1] = 0;
  _$jscoverage['levels.js'][3] = 0;
  _$jscoverage['levels.js'][4] = 0;
  _$jscoverage['levels.js'][5] = 0;
  _$jscoverage['levels.js'][15] = 0;
  _$jscoverage['levels.js'][17] = 0;
  _$jscoverage['levels.js'][18] = 0;
  _$jscoverage['levels.js'][21] = 0;
  _$jscoverage['levels.js'][22] = 0;
  _$jscoverage['levels.js'][23] = 0;
  _$jscoverage['levels.js'][24] = 0;
  _$jscoverage['levels.js'][26] = 0;
  _$jscoverage['levels.js'][30] = 0;
  _$jscoverage['levels.js'][33] = 0;
  _$jscoverage['levels.js'][34] = 0;
  _$jscoverage['levels.js'][37] = 0;
  _$jscoverage['levels.js'][38] = 0;
  _$jscoverage['levels.js'][39] = 0;
  _$jscoverage['levels.js'][41] = 0;
  _$jscoverage['levels.js'][44] = 0;
  _$jscoverage['levels.js'][45] = 0;
  _$jscoverage['levels.js'][46] = 0;
  _$jscoverage['levels.js'][48] = 0;
  _$jscoverage['levels.js'][51] = 0;
  _$jscoverage['levels.js'][52] = 0;
  _$jscoverage['levels.js'][53] = 0;
  _$jscoverage['levels.js'][55] = 0;
  _$jscoverage['levels.js'][58] = 0;
}
_$jscoverage['levels.js'][1]++;
"use strict";
_$jscoverage['levels.js'][3]++;
function Level(level, levelStr) {
  _$jscoverage['levels.js'][4]++;
  this.level = level;
  _$jscoverage['levels.js'][5]++;
  this.levelStr = levelStr;
}
_$jscoverage['levels.js'][15]++;
function toLevel(sArg, defaultLevel) {
  _$jscoverage['levels.js'][17]++;
  if (! sArg) {
    _$jscoverage['levels.js'][18]++;
    return defaultLevel;
  }
  _$jscoverage['levels.js'][21]++;
  if (typeof sArg == "string") {
    _$jscoverage['levels.js'][22]++;
    var s = sArg.toUpperCase();
    _$jscoverage['levels.js'][23]++;
    if (module.exports[s]) {
      _$jscoverage['levels.js'][24]++;
      return module.exports[s];
    }
    else {
      _$jscoverage['levels.js'][26]++;
      return defaultLevel;
    }
  }
  _$jscoverage['levels.js'][30]++;
  return toLevel(sArg.toString());
}
_$jscoverage['levels.js'][33]++;
Level.prototype.toString = (function () {
  _$jscoverage['levels.js'][34]++;
  return this.levelStr;
});
_$jscoverage['levels.js'][37]++;
Level.prototype.isLessThanOrEqualTo = (function (otherLevel) {
  _$jscoverage['levels.js'][38]++;
  if (typeof otherLevel === "string") {
    _$jscoverage['levels.js'][39]++;
    otherLevel = toLevel(otherLevel);
  }
  _$jscoverage['levels.js'][41]++;
  return this.level <= otherLevel.level;
});
_$jscoverage['levels.js'][44]++;
Level.prototype.isGreaterThanOrEqualTo = (function (otherLevel) {
  _$jscoverage['levels.js'][45]++;
  if (typeof otherLevel === "string") {
    _$jscoverage['levels.js'][46]++;
    otherLevel = toLevel(otherLevel);
  }
  _$jscoverage['levels.js'][48]++;
  return this.level >= otherLevel.level;
});
_$jscoverage['levels.js'][51]++;
Level.prototype.isEqualTo = (function (otherLevel) {
  _$jscoverage['levels.js'][52]++;
  if (typeof otherLevel == "string") {
    _$jscoverage['levels.js'][53]++;
    otherLevel = toLevel(otherLevel);
  }
  _$jscoverage['levels.js'][55]++;
  return this.level === otherLevel.level;
});
_$jscoverage['levels.js'][58]++;
module.exports = {ALL: new Level(Number.MIN_VALUE, "ALL"), TRACE: new Level(5000, "TRACE"), DEBUG: new Level(10000, "DEBUG"), INFO: new Level(20000, "INFO"), WARN: new Level(30000, "WARN"), ERROR: new Level(40000, "ERROR"), FATAL: new Level(50000, "FATAL"), OFF: new Level(Number.MAX_VALUE, "OFF"), toLevel: toLevel};
_$jscoverage['levels.js'].source = ["\"use strict\";","","function Level(level, levelStr) {","  this.level = level;","  this.levelStr = levelStr;","}","","/**"," * converts given String to corresponding Level"," * @param {String} sArg String value of Level OR Log4js.Level"," * @param {Log4js.Level} defaultLevel default Level, if no String representation"," * @return Level object"," * @type Log4js.Level"," */","function toLevel(sArg, defaultLevel) {","","  if (!sArg) {","    return defaultLevel;","  }","","  if (typeof sArg == \"string\") {","    var s = sArg.toUpperCase();","    if (module.exports[s]) {","      return module.exports[s];","    } else {","      return defaultLevel;","    }","  }","","  return toLevel(sArg.toString());","}","","Level.prototype.toString = function() {","  return this.levelStr;","};","","Level.prototype.isLessThanOrEqualTo = function(otherLevel) {","  if (typeof otherLevel === \"string\") {","    otherLevel = toLevel(otherLevel);","  }","  return this.level &lt;= otherLevel.level;","};","","Level.prototype.isGreaterThanOrEqualTo = function(otherLevel) {","  if (typeof otherLevel === \"string\") {","    otherLevel = toLevel(otherLevel);","  }","  return this.level &gt;= otherLevel.level;","};","","Level.prototype.isEqualTo = function(otherLevel) {","  if (typeof otherLevel == \"string\") {","    otherLevel = toLevel(otherLevel);","  }","  return this.level === otherLevel.level;","};","","module.exports = {","  ALL: new Level(Number.MIN_VALUE, \"ALL\"), ","  TRACE: new Level(5000, \"TRACE\"), ","  DEBUG: new Level(10000, \"DEBUG\"), ","  INFO: new Level(20000, \"INFO\"), ","  WARN: new Level(30000, \"WARN\"), ","  ERROR: new Level(40000, \"ERROR\"), ","  FATAL: new Level(50000, \"FATAL\"), ","  OFF: new Level(Number.MAX_VALUE, \"OFF\"), ","  toLevel: toLevel","};"];