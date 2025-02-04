// To disable console logs in the production env
(function() {
    var noop = function() {};
    var methods = ['log', 'warn', 'error', 'info', 'debug'];
    //var methods = ['debug'];
    for (var i = 0; i < methods.length; i++) {
        console[methods[i]] = noop;
    }
})();
