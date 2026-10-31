(() => {
  var __create = Object.create;
  var __defProp = Object.defineProperty;
  var __defProps = Object.defineProperties;
  var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
  var __getOwnPropDescs = Object.getOwnPropertyDescriptors;
  var __getOwnPropNames = Object.getOwnPropertyNames;
  var __getOwnPropSymbols = Object.getOwnPropertySymbols;
  var __getProtoOf = Object.getPrototypeOf;
  var __hasOwnProp = Object.prototype.hasOwnProperty;
  var __propIsEnum = Object.prototype.propertyIsEnumerable;
  var __knownSymbol = (name, symbol) => (symbol = Symbol[name]) ? symbol : Symbol.for("Symbol." + name);
  var __typeError = (msg) => {
    throw TypeError(msg);
  };
  var __defNormalProp = (obj, key, value) => key in obj ? __defProp(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
  var __spreadValues = (a, b) => {
    for (var prop in b || (b = {}))
      if (__hasOwnProp.call(b, prop))
        __defNormalProp(a, prop, b[prop]);
    if (__getOwnPropSymbols)
      for (var prop of __getOwnPropSymbols(b)) {
        if (__propIsEnum.call(b, prop))
          __defNormalProp(a, prop, b[prop]);
      }
    return a;
  };
  var __spreadProps = (a, b) => __defProps(a, __getOwnPropDescs(b));
  var __esm = (fn, res) => function __init() {
    return fn && (res = (0, fn[__getOwnPropNames(fn)[0]])(fn = 0)), res;
  };
  var __commonJS = (cb, mod) => function __require() {
    return mod || (0, cb[__getOwnPropNames(cb)[0]])((mod = { exports: {} }).exports, mod), mod.exports;
  };
  var __export = (target, all) => {
    for (var name in all)
      __defProp(target, name, { get: all[name], enumerable: true });
  };
  var __copyProps = (to, from, except, desc) => {
    if (from && typeof from === "object" || typeof from === "function") {
      for (let key of __getOwnPropNames(from))
        if (!__hasOwnProp.call(to, key) && key !== except)
          __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
    }
    return to;
  };
  var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
    // If the importer is in node compatibility mode or this is not an ESM
    // file that has been converted to a CommonJS file using a Babel-
    // compatible transform (i.e. "__esModule" has not been set), then set
    // "default" to the CommonJS "module.exports" for node compatibility.
    isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
    mod
  ));
  var __publicField = (obj, key, value) => __defNormalProp(obj, typeof key !== "symbol" ? key + "" : key, value);
  var __accessCheck = (obj, member, msg) => member.has(obj) || __typeError("Cannot " + msg);
  var __privateGet = (obj, member, getter) => (__accessCheck(obj, member, "read from private field"), getter ? getter.call(obj) : member.get(obj));
  var __privateAdd = (obj, member, value) => member.has(obj) ? __typeError("Cannot add the same private member more than once") : member instanceof WeakSet ? member.add(obj) : member.set(obj, value);
  var __privateSet = (obj, member, value, setter) => (__accessCheck(obj, member, "write to private field"), setter ? setter.call(obj, value) : member.set(obj, value), value);
  var __privateMethod = (obj, member, method) => (__accessCheck(obj, member, "access private method"), method);
  var __privateWrapper = (obj, member, setter, getter) => ({
    set _(value) {
      __privateSet(obj, member, value, setter);
    },
    get _() {
      return __privateGet(obj, member, getter);
    }
  });
  var __await = function(promise, isYieldStar) {
    this[0] = promise;
    this[1] = isYieldStar;
  };
  var __asyncGenerator = (__this, __arguments, generator) => {
    var resume = (k, v, yes, no) => {
      try {
        var x = generator[k](v), isAwait = (v = x.value) instanceof __await, done = x.done;
        Promise.resolve(isAwait ? v[0] : v).then((y) => isAwait ? resume(k === "return" ? k : "next", v[1] ? { done: y.done, value: y.value } : y, yes, no) : yes({ value: y, done })).catch((e) => resume("throw", e, yes, no));
      } catch (e) {
        no(e);
      }
    }, method = (k) => it[k] = (x) => new Promise((yes, no) => resume(k, x, yes, no)), it = {};
    return generator = generator.apply(__this, __arguments), it[__knownSymbol("asyncIterator")] = () => it, method("next"), method("throw"), method("return"), it;
  };
  var __forAwait = (obj, it, method) => (it = obj[__knownSymbol("asyncIterator")]) ? it.call(obj) : (obj = obj[__knownSymbol("iterator")](), it = {}, method = (key, fn) => (fn = obj[key]) && (it[key] = (arg) => new Promise((yes, no, done) => (arg = fn.call(obj, arg), done = arg.done, Promise.resolve(arg.value).then((value) => yes({ value, done }), no)))), method("next"), method("return"), it);

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/global-this.js
  var require_global_this = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/global-this.js"(exports, module) {
      "use strict";
      var check = function(it) {
        return it && it.Math === Math && it;
      };
      module.exports = // eslint-disable-next-line es/no-global-this -- safe
      check(typeof globalThis == "object" && globalThis) || check(typeof window == "object" && window) || // eslint-disable-next-line no-restricted-globals -- safe
      check(typeof self == "object" && self) || check(typeof global == "object" && global) || check(typeof exports == "object" && exports) || // eslint-disable-next-line no-new-func -- fallback
      /* @__PURE__ */ (function() {
        return this;
      })() || Function("return this")();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/fails.js
  var require_fails = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/fails.js"(exports, module) {
      "use strict";
      module.exports = function(exec) {
        try {
          return !!exec();
        } catch (error) {
          return true;
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/descriptors.js
  var require_descriptors = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/descriptors.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      module.exports = !fails(function() {
        return Object.defineProperty({}, 1, { get: function() {
          return 7;
        } })[1] !== 7;
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-bind-native.js
  var require_function_bind_native = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-bind-native.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      module.exports = !fails(function() {
        var test = (function() {
        }).bind();
        return typeof test != "function" || test.hasOwnProperty("prototype");
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-call.js
  var require_function_call = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-call.js"(exports, module) {
      "use strict";
      var NATIVE_BIND = require_function_bind_native();
      var call = Function.prototype.call;
      module.exports = NATIVE_BIND ? call.bind(call) : function() {
        return call.apply(call, arguments);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-property-is-enumerable.js
  var require_object_property_is_enumerable = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-property-is-enumerable.js"(exports) {
      "use strict";
      var $propertyIsEnumerable = {}.propertyIsEnumerable;
      var getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
      var NASHORN_BUG = getOwnPropertyDescriptor && !$propertyIsEnumerable.call({ 1: 2 }, 1);
      exports.f = NASHORN_BUG ? function propertyIsEnumerable(V) {
        var descriptor = getOwnPropertyDescriptor(this, V);
        return !!descriptor && descriptor.enumerable;
      } : $propertyIsEnumerable;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-property-descriptor.js
  var require_create_property_descriptor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-property-descriptor.js"(exports, module) {
      "use strict";
      module.exports = function(bitmap, value) {
        return {
          enumerable: !(bitmap & 1),
          configurable: !(bitmap & 2),
          writable: !(bitmap & 4),
          value
        };
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this.js
  var require_function_uncurry_this = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this.js"(exports, module) {
      "use strict";
      var NATIVE_BIND = require_function_bind_native();
      var FunctionPrototype = Function.prototype;
      var call = FunctionPrototype.call;
      var uncurryThisWithBind = NATIVE_BIND && FunctionPrototype.bind.bind(call, call);
      module.exports = NATIVE_BIND ? uncurryThisWithBind : function(fn) {
        return function() {
          return call.apply(fn, arguments);
        };
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/classof-raw.js
  var require_classof_raw = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/classof-raw.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var toString2 = uncurryThis({}.toString);
      var stringSlice = uncurryThis("".slice);
      module.exports = function(it) {
        return stringSlice(toString2(it), 8, -1);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/indexed-object.js
  var require_indexed_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/indexed-object.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var fails = require_fails();
      var classof = require_classof_raw();
      var $Object = Object;
      var split = uncurryThis("".split);
      module.exports = fails(function() {
        return !$Object("z").propertyIsEnumerable(0);
      }) ? function(it) {
        return classof(it) === "String" ? split(it, "") : $Object(it);
      } : $Object;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-null-or-undefined.js
  var require_is_null_or_undefined = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-null-or-undefined.js"(exports, module) {
      "use strict";
      module.exports = function(it) {
        return it === null || it === void 0;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/require-object-coercible.js
  var require_require_object_coercible = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/require-object-coercible.js"(exports, module) {
      "use strict";
      var isNullOrUndefined = require_is_null_or_undefined();
      var $TypeError = TypeError;
      module.exports = function(it) {
        if (isNullOrUndefined(it)) throw new $TypeError("Can't call method on " + it);
        return it;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-indexed-object.js
  var require_to_indexed_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-indexed-object.js"(exports, module) {
      "use strict";
      var IndexedObject = require_indexed_object();
      var requireObjectCoercible = require_require_object_coercible();
      module.exports = function(it) {
        return IndexedObject(requireObjectCoercible(it));
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-callable.js
  var require_is_callable = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-callable.js"(exports, module) {
      "use strict";
      var documentAll = typeof document == "object" && document.all;
      module.exports = typeof documentAll == "undefined" && documentAll !== void 0 ? function(argument) {
        return typeof argument == "function" || argument === documentAll;
      } : function(argument) {
        return typeof argument == "function";
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-object.js
  var require_is_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-object.js"(exports, module) {
      "use strict";
      var isCallable = require_is_callable();
      module.exports = function(it) {
        return typeof it == "object" ? it !== null : isCallable(it);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-built-in.js
  var require_get_built_in = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-built-in.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var isCallable = require_is_callable();
      var aFunction = function(argument) {
        return isCallable(argument) ? argument : void 0;
      };
      module.exports = function(namespace, method) {
        return arguments.length < 2 ? aFunction(globalThis2[namespace]) : globalThis2[namespace] && globalThis2[namespace][method];
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-is-prototype-of.js
  var require_object_is_prototype_of = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-is-prototype-of.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      module.exports = uncurryThis({}.isPrototypeOf);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-user-agent.js
  var require_environment_user_agent = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-user-agent.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var navigator = globalThis2.navigator;
      var userAgent = navigator && navigator.userAgent;
      module.exports = userAgent ? String(userAgent) : "";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-v8-version.js
  var require_environment_v8_version = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-v8-version.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var userAgent = require_environment_user_agent();
      var process = globalThis2.process;
      var Deno2 = globalThis2.Deno;
      var versions = process && process.versions || Deno2 && Deno2.version;
      var v8 = versions && versions.v8;
      var match;
      var version;
      if (v8) {
        match = v8.split(".");
        version = match[0] > 0 && match[0] < 4 ? 1 : +(match[0] + match[1]);
      }
      if (!version && userAgent) {
        match = userAgent.match(/Edge\/(\d+)/);
        if (!match || match[1] >= 74) {
          match = userAgent.match(/Chrome\/(\d+)/);
          if (match) version = +match[1];
        }
      }
      module.exports = version;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/symbol-constructor-detection.js
  var require_symbol_constructor_detection = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/symbol-constructor-detection.js"(exports, module) {
      "use strict";
      var V8_VERSION = require_environment_v8_version();
      var fails = require_fails();
      var globalThis2 = require_global_this();
      var $String = globalThis2.String;
      module.exports = !!Object.getOwnPropertySymbols && !fails(function() {
        var symbol = Symbol("symbol detection");
        return !$String(symbol) || !(Object(symbol) instanceof Symbol) || // Chrome 38-40 symbols are not inherited from DOM collections prototypes to instances
        !Symbol.sham && V8_VERSION && V8_VERSION < 41;
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/use-symbol-as-uid.js
  var require_use_symbol_as_uid = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/use-symbol-as-uid.js"(exports, module) {
      "use strict";
      var NATIVE_SYMBOL = require_symbol_constructor_detection();
      module.exports = NATIVE_SYMBOL && !Symbol.sham && typeof Symbol.iterator == "symbol";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-symbol.js
  var require_is_symbol = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-symbol.js"(exports, module) {
      "use strict";
      var getBuiltIn = require_get_built_in();
      var isCallable = require_is_callable();
      var isPrototypeOf = require_object_is_prototype_of();
      var USE_SYMBOL_AS_UID = require_use_symbol_as_uid();
      var $Object = Object;
      module.exports = USE_SYMBOL_AS_UID ? function(it) {
        return typeof it == "symbol";
      } : function(it) {
        var $Symbol = getBuiltIn("Symbol");
        return isCallable($Symbol) && isPrototypeOf($Symbol.prototype, $Object(it));
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/try-to-string.js
  var require_try_to_string = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/try-to-string.js"(exports, module) {
      "use strict";
      var $String = String;
      module.exports = function(argument) {
        try {
          return $String(argument);
        } catch (error) {
          return "Object";
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-callable.js
  var require_a_callable = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-callable.js"(exports, module) {
      "use strict";
      var isCallable = require_is_callable();
      var tryToString = require_try_to_string();
      var $TypeError = TypeError;
      module.exports = function(argument) {
        if (isCallable(argument)) return argument;
        throw new $TypeError(tryToString(argument) + " is not a function");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-method.js
  var require_get_method = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-method.js"(exports, module) {
      "use strict";
      var aCallable = require_a_callable();
      var isNullOrUndefined = require_is_null_or_undefined();
      module.exports = function(V, P) {
        var func = V[P];
        return isNullOrUndefined(func) ? void 0 : aCallable(func);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/ordinary-to-primitive.js
  var require_ordinary_to_primitive = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/ordinary-to-primitive.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var $TypeError = TypeError;
      module.exports = function(input, pref) {
        var fn, val;
        if (pref === "string" && isCallable(fn = input.toString) && !isObject(val = call(fn, input))) return val;
        if (isCallable(fn = input.valueOf) && !isObject(val = call(fn, input))) return val;
        if (pref !== "string" && isCallable(fn = input.toString) && !isObject(val = call(fn, input))) return val;
        throw new $TypeError("Can't convert object to primitive value");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-pure.js
  var require_is_pure = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-pure.js"(exports, module) {
      "use strict";
      module.exports = false;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-global-property.js
  var require_define_global_property = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-global-property.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var defineProperty = Object.defineProperty;
      module.exports = function(key, value) {
        try {
          defineProperty(globalThis2, key, { value, configurable: true, writable: true });
        } catch (error) {
          globalThis2[key] = value;
        }
        return value;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared-store.js
  var require_shared_store = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared-store.js"(exports, module) {
      "use strict";
      var IS_PURE = require_is_pure();
      var globalThis2 = require_global_this();
      var defineGlobalProperty = require_define_global_property();
      var SHARED = "__core-js_shared__";
      var store = module.exports = globalThis2[SHARED] || defineGlobalProperty(SHARED, {});
      (store.versions || (store.versions = [])).push({
        version: "3.46.0",
        mode: IS_PURE ? "pure" : "global",
        copyright: "\xA9 2014-2025 Denis Pushkarev (zloirock.ru), 2025 CoreJS Company (core-js.io)",
        license: "https://github.com/zloirock/core-js/blob/v3.46.0/LICENSE",
        source: "https://github.com/zloirock/core-js"
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared.js
  var require_shared = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared.js"(exports, module) {
      "use strict";
      var store = require_shared_store();
      module.exports = function(key, value) {
        return store[key] || (store[key] = value || {});
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-object.js
  var require_to_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-object.js"(exports, module) {
      "use strict";
      var requireObjectCoercible = require_require_object_coercible();
      var $Object = Object;
      module.exports = function(argument) {
        return $Object(requireObjectCoercible(argument));
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/has-own-property.js
  var require_has_own_property = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/has-own-property.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var toObject = require_to_object();
      var hasOwnProperty = uncurryThis({}.hasOwnProperty);
      module.exports = Object.hasOwn || function hasOwn(it, key) {
        return hasOwnProperty(toObject(it), key);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/uid.js
  var require_uid = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/uid.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var id = 0;
      var postfix = Math.random();
      var toString2 = uncurryThis(1.1.toString);
      module.exports = function(key) {
        return "Symbol(" + (key === void 0 ? "" : key) + ")_" + toString2(++id + postfix, 36);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol.js
  var require_well_known_symbol = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var shared = require_shared();
      var hasOwn = require_has_own_property();
      var uid = require_uid();
      var NATIVE_SYMBOL = require_symbol_constructor_detection();
      var USE_SYMBOL_AS_UID = require_use_symbol_as_uid();
      var Symbol2 = globalThis2.Symbol;
      var WellKnownSymbolsStore = shared("wks");
      var createWellKnownSymbol = USE_SYMBOL_AS_UID ? Symbol2["for"] || Symbol2 : Symbol2 && Symbol2.withoutSetter || uid;
      module.exports = function(name) {
        if (!hasOwn(WellKnownSymbolsStore, name)) {
          WellKnownSymbolsStore[name] = NATIVE_SYMBOL && hasOwn(Symbol2, name) ? Symbol2[name] : createWellKnownSymbol("Symbol." + name);
        }
        return WellKnownSymbolsStore[name];
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-primitive.js
  var require_to_primitive = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-primitive.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var isObject = require_is_object();
      var isSymbol = require_is_symbol();
      var getMethod = require_get_method();
      var ordinaryToPrimitive = require_ordinary_to_primitive();
      var wellKnownSymbol = require_well_known_symbol();
      var $TypeError = TypeError;
      var TO_PRIMITIVE = wellKnownSymbol("toPrimitive");
      module.exports = function(input, pref) {
        if (!isObject(input) || isSymbol(input)) return input;
        var exoticToPrim = getMethod(input, TO_PRIMITIVE);
        var result;
        if (exoticToPrim) {
          if (pref === void 0) pref = "default";
          result = call(exoticToPrim, input, pref);
          if (!isObject(result) || isSymbol(result)) return result;
          throw new $TypeError("Can't convert object to primitive value");
        }
        if (pref === void 0) pref = "number";
        return ordinaryToPrimitive(input, pref);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-property-key.js
  var require_to_property_key = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-property-key.js"(exports, module) {
      "use strict";
      var toPrimitive = require_to_primitive();
      var isSymbol = require_is_symbol();
      module.exports = function(argument) {
        var key = toPrimitive(argument, "string");
        return isSymbol(key) ? key : key + "";
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/document-create-element.js
  var require_document_create_element = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/document-create-element.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var isObject = require_is_object();
      var document2 = globalThis2.document;
      var EXISTS = isObject(document2) && isObject(document2.createElement);
      module.exports = function(it) {
        return EXISTS ? document2.createElement(it) : {};
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/ie8-dom-define.js
  var require_ie8_dom_define = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/ie8-dom-define.js"(exports, module) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var fails = require_fails();
      var createElement = require_document_create_element();
      module.exports = !DESCRIPTORS && !fails(function() {
        return Object.defineProperty(createElement("div"), "a", {
          get: function() {
            return 7;
          }
        }).a !== 7;
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-descriptor.js
  var require_object_get_own_property_descriptor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-descriptor.js"(exports) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var call = require_function_call();
      var propertyIsEnumerableModule = require_object_property_is_enumerable();
      var createPropertyDescriptor = require_create_property_descriptor();
      var toIndexedObject = require_to_indexed_object();
      var toPropertyKey = require_to_property_key();
      var hasOwn = require_has_own_property();
      var IE8_DOM_DEFINE = require_ie8_dom_define();
      var $getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
      exports.f = DESCRIPTORS ? $getOwnPropertyDescriptor : function getOwnPropertyDescriptor(O, P) {
        O = toIndexedObject(O);
        P = toPropertyKey(P);
        if (IE8_DOM_DEFINE) try {
          return $getOwnPropertyDescriptor(O, P);
        } catch (error) {
        }
        if (hasOwn(O, P)) return createPropertyDescriptor(!call(propertyIsEnumerableModule.f, O, P), O[P]);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/v8-prototype-define-bug.js
  var require_v8_prototype_define_bug = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/v8-prototype-define-bug.js"(exports, module) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var fails = require_fails();
      module.exports = DESCRIPTORS && fails(function() {
        return Object.defineProperty(function() {
        }, "prototype", {
          value: 42,
          writable: false
        }).prototype !== 42;
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/an-object.js
  var require_an_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/an-object.js"(exports, module) {
      "use strict";
      var isObject = require_is_object();
      var $String = String;
      var $TypeError = TypeError;
      module.exports = function(argument) {
        if (isObject(argument)) return argument;
        throw new $TypeError($String(argument) + " is not an object");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-define-property.js
  var require_object_define_property = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-define-property.js"(exports) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var IE8_DOM_DEFINE = require_ie8_dom_define();
      var V8_PROTOTYPE_DEFINE_BUG = require_v8_prototype_define_bug();
      var anObject = require_an_object();
      var toPropertyKey = require_to_property_key();
      var $TypeError = TypeError;
      var $defineProperty = Object.defineProperty;
      var $getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
      var ENUMERABLE = "enumerable";
      var CONFIGURABLE = "configurable";
      var WRITABLE = "writable";
      exports.f = DESCRIPTORS ? V8_PROTOTYPE_DEFINE_BUG ? function defineProperty(O, P, Attributes) {
        anObject(O);
        P = toPropertyKey(P);
        anObject(Attributes);
        if (typeof O === "function" && P === "prototype" && "value" in Attributes && WRITABLE in Attributes && !Attributes[WRITABLE]) {
          var current = $getOwnPropertyDescriptor(O, P);
          if (current && current[WRITABLE]) {
            O[P] = Attributes.value;
            Attributes = {
              configurable: CONFIGURABLE in Attributes ? Attributes[CONFIGURABLE] : current[CONFIGURABLE],
              enumerable: ENUMERABLE in Attributes ? Attributes[ENUMERABLE] : current[ENUMERABLE],
              writable: false
            };
          }
        }
        return $defineProperty(O, P, Attributes);
      } : $defineProperty : function defineProperty(O, P, Attributes) {
        anObject(O);
        P = toPropertyKey(P);
        anObject(Attributes);
        if (IE8_DOM_DEFINE) try {
          return $defineProperty(O, P, Attributes);
        } catch (error) {
        }
        if ("get" in Attributes || "set" in Attributes) throw new $TypeError("Accessors not supported");
        if ("value" in Attributes) O[P] = Attributes.value;
        return O;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-non-enumerable-property.js
  var require_create_non_enumerable_property = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-non-enumerable-property.js"(exports, module) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var definePropertyModule = require_object_define_property();
      var createPropertyDescriptor = require_create_property_descriptor();
      module.exports = DESCRIPTORS ? function(object, key, value) {
        return definePropertyModule.f(object, key, createPropertyDescriptor(1, value));
      } : function(object, key, value) {
        object[key] = value;
        return object;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-name.js
  var require_function_name = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-name.js"(exports, module) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var hasOwn = require_has_own_property();
      var FunctionPrototype = Function.prototype;
      var getDescriptor = DESCRIPTORS && Object.getOwnPropertyDescriptor;
      var EXISTS = hasOwn(FunctionPrototype, "name");
      var PROPER = EXISTS && (function something() {
      }).name === "something";
      var CONFIGURABLE = EXISTS && (!DESCRIPTORS || DESCRIPTORS && getDescriptor(FunctionPrototype, "name").configurable);
      module.exports = {
        EXISTS,
        PROPER,
        CONFIGURABLE
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/inspect-source.js
  var require_inspect_source = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/inspect-source.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var isCallable = require_is_callable();
      var store = require_shared_store();
      var functionToString = uncurryThis(Function.toString);
      if (!isCallable(store.inspectSource)) {
        store.inspectSource = function(it) {
          return functionToString(it);
        };
      }
      module.exports = store.inspectSource;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/weak-map-basic-detection.js
  var require_weak_map_basic_detection = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/weak-map-basic-detection.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var isCallable = require_is_callable();
      var WeakMap2 = globalThis2.WeakMap;
      module.exports = isCallable(WeakMap2) && /native code/.test(String(WeakMap2));
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared-key.js
  var require_shared_key = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/shared-key.js"(exports, module) {
      "use strict";
      var shared = require_shared();
      var uid = require_uid();
      var keys = shared("keys");
      module.exports = function(key) {
        return keys[key] || (keys[key] = uid(key));
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/hidden-keys.js
  var require_hidden_keys = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/hidden-keys.js"(exports, module) {
      "use strict";
      module.exports = {};
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/internal-state.js
  var require_internal_state = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/internal-state.js"(exports, module) {
      "use strict";
      var NATIVE_WEAK_MAP = require_weak_map_basic_detection();
      var globalThis2 = require_global_this();
      var isObject = require_is_object();
      var createNonEnumerableProperty = require_create_non_enumerable_property();
      var hasOwn = require_has_own_property();
      var shared = require_shared_store();
      var sharedKey = require_shared_key();
      var hiddenKeys = require_hidden_keys();
      var OBJECT_ALREADY_INITIALIZED = "Object already initialized";
      var TypeError2 = globalThis2.TypeError;
      var WeakMap2 = globalThis2.WeakMap;
      var set;
      var get;
      var has;
      var enforce = function(it) {
        return has(it) ? get(it) : set(it, {});
      };
      var getterFor = function(TYPE) {
        return function(it) {
          var state2;
          if (!isObject(it) || (state2 = get(it)).type !== TYPE) {
            throw new TypeError2("Incompatible receiver, " + TYPE + " required");
          }
          return state2;
        };
      };
      if (NATIVE_WEAK_MAP || shared.state) {
        store = shared.state || (shared.state = new WeakMap2());
        store.get = store.get;
        store.has = store.has;
        store.set = store.set;
        set = function(it, metadata) {
          if (store.has(it)) throw new TypeError2(OBJECT_ALREADY_INITIALIZED);
          metadata.facade = it;
          store.set(it, metadata);
          return metadata;
        };
        get = function(it) {
          return store.get(it) || {};
        };
        has = function(it) {
          return store.has(it);
        };
      } else {
        STATE = sharedKey("state");
        hiddenKeys[STATE] = true;
        set = function(it, metadata) {
          if (hasOwn(it, STATE)) throw new TypeError2(OBJECT_ALREADY_INITIALIZED);
          metadata.facade = it;
          createNonEnumerableProperty(it, STATE, metadata);
          return metadata;
        };
        get = function(it) {
          return hasOwn(it, STATE) ? it[STATE] : {};
        };
        has = function(it) {
          return hasOwn(it, STATE);
        };
      }
      var store;
      var STATE;
      module.exports = {
        set,
        get,
        has,
        enforce,
        getterFor
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/make-built-in.js
  var require_make_built_in = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/make-built-in.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var fails = require_fails();
      var isCallable = require_is_callable();
      var hasOwn = require_has_own_property();
      var DESCRIPTORS = require_descriptors();
      var CONFIGURABLE_FUNCTION_NAME = require_function_name().CONFIGURABLE;
      var inspectSource = require_inspect_source();
      var InternalStateModule = require_internal_state();
      var enforceInternalState = InternalStateModule.enforce;
      var getInternalState = InternalStateModule.get;
      var $String = String;
      var defineProperty = Object.defineProperty;
      var stringSlice = uncurryThis("".slice);
      var replace = uncurryThis("".replace);
      var join = uncurryThis([].join);
      var CONFIGURABLE_LENGTH = DESCRIPTORS && !fails(function() {
        return defineProperty(function() {
        }, "length", { value: 8 }).length !== 8;
      });
      var TEMPLATE = String(String).split("String");
      var makeBuiltIn = module.exports = function(value, name, options) {
        if (stringSlice($String(name), 0, 7) === "Symbol(") {
          name = "[" + replace($String(name), /^Symbol\(([^)]*)\).*$/, "$1") + "]";
        }
        if (options && options.getter) name = "get " + name;
        if (options && options.setter) name = "set " + name;
        if (!hasOwn(value, "name") || CONFIGURABLE_FUNCTION_NAME && value.name !== name) {
          if (DESCRIPTORS) defineProperty(value, "name", { value: name, configurable: true });
          else value.name = name;
        }
        if (CONFIGURABLE_LENGTH && options && hasOwn(options, "arity") && value.length !== options.arity) {
          defineProperty(value, "length", { value: options.arity });
        }
        try {
          if (options && hasOwn(options, "constructor") && options.constructor) {
            if (DESCRIPTORS) defineProperty(value, "prototype", { writable: false });
          } else if (value.prototype) value.prototype = void 0;
        } catch (error) {
        }
        var state2 = enforceInternalState(value);
        if (!hasOwn(state2, "source")) {
          state2.source = join(TEMPLATE, typeof name == "string" ? name : "");
        }
        return value;
      };
      Function.prototype.toString = makeBuiltIn(function toString2() {
        return isCallable(this) && getInternalState(this).source || inspectSource(this);
      }, "toString");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-in.js
  var require_define_built_in = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-in.js"(exports, module) {
      "use strict";
      var isCallable = require_is_callable();
      var definePropertyModule = require_object_define_property();
      var makeBuiltIn = require_make_built_in();
      var defineGlobalProperty = require_define_global_property();
      module.exports = function(O, key, value, options) {
        if (!options) options = {};
        var simple = options.enumerable;
        var name = options.name !== void 0 ? options.name : key;
        if (isCallable(value)) makeBuiltIn(value, name, options);
        if (options.global) {
          if (simple) O[key] = value;
          else defineGlobalProperty(key, value);
        } else {
          try {
            if (!options.unsafe) delete O[key];
            else if (O[key]) simple = true;
          } catch (error) {
          }
          if (simple) O[key] = value;
          else definePropertyModule.f(O, key, {
            value,
            enumerable: false,
            configurable: !options.nonConfigurable,
            writable: !options.nonWritable
          });
        }
        return O;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/math-trunc.js
  var require_math_trunc = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/math-trunc.js"(exports, module) {
      "use strict";
      var ceil = Math.ceil;
      var floor = Math.floor;
      module.exports = Math.trunc || function trunc(x) {
        var n = +x;
        return (n > 0 ? floor : ceil)(n);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-integer-or-infinity.js
  var require_to_integer_or_infinity = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-integer-or-infinity.js"(exports, module) {
      "use strict";
      var trunc = require_math_trunc();
      module.exports = function(argument) {
        var number = +argument;
        return number !== number || number === 0 ? 0 : trunc(number);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-absolute-index.js
  var require_to_absolute_index = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-absolute-index.js"(exports, module) {
      "use strict";
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var max2 = Math.max;
      var min = Math.min;
      module.exports = function(index2, length) {
        var integer = toIntegerOrInfinity(index2);
        return integer < 0 ? max2(integer + length, 0) : min(integer, length);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-length.js
  var require_to_length = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-length.js"(exports, module) {
      "use strict";
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var min = Math.min;
      module.exports = function(argument) {
        var len = toIntegerOrInfinity(argument);
        return len > 0 ? min(len, 9007199254740991) : 0;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/length-of-array-like.js
  var require_length_of_array_like = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/length-of-array-like.js"(exports, module) {
      "use strict";
      var toLength = require_to_length();
      module.exports = function(obj) {
        return toLength(obj.length);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-includes.js
  var require_array_includes = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-includes.js"(exports, module) {
      "use strict";
      var toIndexedObject = require_to_indexed_object();
      var toAbsoluteIndex = require_to_absolute_index();
      var lengthOfArrayLike = require_length_of_array_like();
      var createMethod = function(IS_INCLUDES) {
        return function($this, el, fromIndex) {
          var O = toIndexedObject($this);
          var length = lengthOfArrayLike(O);
          if (length === 0) return !IS_INCLUDES && -1;
          var index2 = toAbsoluteIndex(fromIndex, length);
          var value;
          if (IS_INCLUDES && el !== el) while (length > index2) {
            value = O[index2++];
            if (value !== value) return true;
          }
          else for (; length > index2; index2++) {
            if ((IS_INCLUDES || index2 in O) && O[index2] === el) return IS_INCLUDES || index2 || 0;
          }
          return !IS_INCLUDES && -1;
        };
      };
      module.exports = {
        // `Array.prototype.includes` method
        // https://tc39.es/ecma262/#sec-array.prototype.includes
        includes: createMethod(true),
        // `Array.prototype.indexOf` method
        // https://tc39.es/ecma262/#sec-array.prototype.indexof
        indexOf: createMethod(false)
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-keys-internal.js
  var require_object_keys_internal = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-keys-internal.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var hasOwn = require_has_own_property();
      var toIndexedObject = require_to_indexed_object();
      var indexOf = require_array_includes().indexOf;
      var hiddenKeys = require_hidden_keys();
      var push = uncurryThis([].push);
      module.exports = function(object, names) {
        var O = toIndexedObject(object);
        var i = 0;
        var result = [];
        var key;
        for (key in O) !hasOwn(hiddenKeys, key) && hasOwn(O, key) && push(result, key);
        while (names.length > i) if (hasOwn(O, key = names[i++])) {
          ~indexOf(result, key) || push(result, key);
        }
        return result;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/enum-bug-keys.js
  var require_enum_bug_keys = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/enum-bug-keys.js"(exports, module) {
      "use strict";
      module.exports = [
        "constructor",
        "hasOwnProperty",
        "isPrototypeOf",
        "propertyIsEnumerable",
        "toLocaleString",
        "toString",
        "valueOf"
      ];
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-names.js
  var require_object_get_own_property_names = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-names.js"(exports) {
      "use strict";
      var internalObjectKeys = require_object_keys_internal();
      var enumBugKeys = require_enum_bug_keys();
      var hiddenKeys = enumBugKeys.concat("length", "prototype");
      exports.f = Object.getOwnPropertyNames || function getOwnPropertyNames(O) {
        return internalObjectKeys(O, hiddenKeys);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-symbols.js
  var require_object_get_own_property_symbols = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-symbols.js"(exports) {
      "use strict";
      exports.f = Object.getOwnPropertySymbols;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/own-keys.js
  var require_own_keys = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/own-keys.js"(exports, module) {
      "use strict";
      var getBuiltIn = require_get_built_in();
      var uncurryThis = require_function_uncurry_this();
      var getOwnPropertyNamesModule = require_object_get_own_property_names();
      var getOwnPropertySymbolsModule = require_object_get_own_property_symbols();
      var anObject = require_an_object();
      var concat = uncurryThis([].concat);
      module.exports = getBuiltIn("Reflect", "ownKeys") || function ownKeys(it) {
        var keys = getOwnPropertyNamesModule.f(anObject(it));
        var getOwnPropertySymbols = getOwnPropertySymbolsModule.f;
        return getOwnPropertySymbols ? concat(keys, getOwnPropertySymbols(it)) : keys;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/copy-constructor-properties.js
  var require_copy_constructor_properties = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/copy-constructor-properties.js"(exports, module) {
      "use strict";
      var hasOwn = require_has_own_property();
      var ownKeys = require_own_keys();
      var getOwnPropertyDescriptorModule = require_object_get_own_property_descriptor();
      var definePropertyModule = require_object_define_property();
      module.exports = function(target, source, exceptions) {
        var keys = ownKeys(source);
        var defineProperty = definePropertyModule.f;
        var getOwnPropertyDescriptor = getOwnPropertyDescriptorModule.f;
        for (var i = 0; i < keys.length; i++) {
          var key = keys[i];
          if (!hasOwn(target, key) && !(exceptions && hasOwn(exceptions, key))) {
            defineProperty(target, key, getOwnPropertyDescriptor(source, key));
          }
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-forced.js
  var require_is_forced = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-forced.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var isCallable = require_is_callable();
      var replacement = /#|\.prototype\./;
      var isForced = function(feature, detection) {
        var value = data[normalize(feature)];
        return value === POLYFILL ? true : value === NATIVE ? false : isCallable(detection) ? fails(detection) : !!detection;
      };
      var normalize = isForced.normalize = function(string) {
        return String(string).replace(replacement, ".").toLowerCase();
      };
      var data = isForced.data = {};
      var NATIVE = isForced.NATIVE = "N";
      var POLYFILL = isForced.POLYFILL = "P";
      module.exports = isForced;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/export.js
  var require_export = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/export.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var getOwnPropertyDescriptor = require_object_get_own_property_descriptor().f;
      var createNonEnumerableProperty = require_create_non_enumerable_property();
      var defineBuiltIn = require_define_built_in();
      var defineGlobalProperty = require_define_global_property();
      var copyConstructorProperties = require_copy_constructor_properties();
      var isForced = require_is_forced();
      module.exports = function(options, source) {
        var TARGET = options.target;
        var GLOBAL = options.global;
        var STATIC = options.stat;
        var FORCED, target, key, targetProperty, sourceProperty, descriptor;
        if (GLOBAL) {
          target = globalThis2;
        } else if (STATIC) {
          target = globalThis2[TARGET] || defineGlobalProperty(TARGET, {});
        } else {
          target = globalThis2[TARGET] && globalThis2[TARGET].prototype;
        }
        if (target) for (key in source) {
          sourceProperty = source[key];
          if (options.dontCallGetSet) {
            descriptor = getOwnPropertyDescriptor(target, key);
            targetProperty = descriptor && descriptor.value;
          } else targetProperty = target[key];
          FORCED = isForced(GLOBAL ? key : TARGET + (STATIC ? "." : "#") + key, options.forced);
          if (!FORCED && targetProperty !== void 0) {
            if (typeof sourceProperty == typeof targetProperty) continue;
            copyConstructorProperties(sourceProperty, targetProperty);
          }
          if (options.sham || targetProperty && targetProperty.sham) {
            createNonEnumerableProperty(sourceProperty, "sham", true);
          }
          defineBuiltIn(target, key, sourceProperty, options);
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-keys.js
  var require_object_keys = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-keys.js"(exports, module) {
      "use strict";
      var internalObjectKeys = require_object_keys_internal();
      var enumBugKeys = require_enum_bug_keys();
      module.exports = Object.keys || function keys(O) {
        return internalObjectKeys(O, enumBugKeys);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-define-properties.js
  var require_object_define_properties = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-define-properties.js"(exports) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var V8_PROTOTYPE_DEFINE_BUG = require_v8_prototype_define_bug();
      var definePropertyModule = require_object_define_property();
      var anObject = require_an_object();
      var toIndexedObject = require_to_indexed_object();
      var objectKeys = require_object_keys();
      exports.f = DESCRIPTORS && !V8_PROTOTYPE_DEFINE_BUG ? Object.defineProperties : function defineProperties(O, Properties) {
        anObject(O);
        var props = toIndexedObject(Properties);
        var keys = objectKeys(Properties);
        var length = keys.length;
        var index2 = 0;
        var key;
        while (length > index2) definePropertyModule.f(O, key = keys[index2++], props[key]);
        return O;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/html.js
  var require_html = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/html.js"(exports, module) {
      "use strict";
      var getBuiltIn = require_get_built_in();
      module.exports = getBuiltIn("document", "documentElement");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-create.js
  var require_object_create = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-create.js"(exports, module) {
      "use strict";
      var anObject = require_an_object();
      var definePropertiesModule = require_object_define_properties();
      var enumBugKeys = require_enum_bug_keys();
      var hiddenKeys = require_hidden_keys();
      var html = require_html();
      var documentCreateElement = require_document_create_element();
      var sharedKey = require_shared_key();
      var GT = ">";
      var LT = "<";
      var PROTOTYPE = "prototype";
      var SCRIPT = "script";
      var IE_PROTO = sharedKey("IE_PROTO");
      var EmptyConstructor = function() {
      };
      var scriptTag = function(content) {
        return LT + SCRIPT + GT + content + LT + "/" + SCRIPT + GT;
      };
      var NullProtoObjectViaActiveX = function(activeXDocument2) {
        activeXDocument2.write(scriptTag(""));
        activeXDocument2.close();
        var temp = activeXDocument2.parentWindow.Object;
        activeXDocument2 = null;
        return temp;
      };
      var NullProtoObjectViaIFrame = function() {
        var iframe = documentCreateElement("iframe");
        var JS = "java" + SCRIPT + ":";
        var iframeDocument;
        iframe.style.display = "none";
        html.appendChild(iframe);
        iframe.src = String(JS);
        iframeDocument = iframe.contentWindow.document;
        iframeDocument.open();
        iframeDocument.write(scriptTag("document.F=Object"));
        iframeDocument.close();
        return iframeDocument.F;
      };
      var activeXDocument;
      var NullProtoObject = function() {
        try {
          activeXDocument = new ActiveXObject("htmlfile");
        } catch (error) {
        }
        NullProtoObject = typeof document != "undefined" ? document.domain && activeXDocument ? NullProtoObjectViaActiveX(activeXDocument) : NullProtoObjectViaIFrame() : NullProtoObjectViaActiveX(activeXDocument);
        var length = enumBugKeys.length;
        while (length--) delete NullProtoObject[PROTOTYPE][enumBugKeys[length]];
        return NullProtoObject();
      };
      hiddenKeys[IE_PROTO] = true;
      module.exports = Object.create || function create(O, Properties) {
        var result;
        if (O !== null) {
          EmptyConstructor[PROTOTYPE] = anObject(O);
          result = new EmptyConstructor();
          EmptyConstructor[PROTOTYPE] = null;
          result[IE_PROTO] = O;
        } else result = NullProtoObject();
        return Properties === void 0 ? result : definePropertiesModule.f(result, Properties);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/add-to-unscopables.js
  var require_add_to_unscopables = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/add-to-unscopables.js"(exports, module) {
      "use strict";
      var wellKnownSymbol = require_well_known_symbol();
      var create = require_object_create();
      var defineProperty = require_object_define_property().f;
      var UNSCOPABLES = wellKnownSymbol("unscopables");
      var ArrayPrototype = Array.prototype;
      if (ArrayPrototype[UNSCOPABLES] === void 0) {
        defineProperty(ArrayPrototype, UNSCOPABLES, {
          configurable: true,
          value: create(null)
        });
      }
      module.exports = function(key) {
        ArrayPrototype[UNSCOPABLES][key] = true;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.at.js
  var require_es_array_at = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.at.js"() {
      "use strict";
      var $ = require_export();
      var toObject = require_to_object();
      var lengthOfArrayLike = require_length_of_array_like();
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var addToUnscopables = require_add_to_unscopables();
      $({ target: "Array", proto: true }, {
        at: function at(index2) {
          var O = toObject(this);
          var len = lengthOfArrayLike(O);
          var relativeIndex = toIntegerOrInfinity(index2);
          var k = relativeIndex >= 0 ? relativeIndex : len + relativeIndex;
          return k < 0 || k >= len ? void 0 : O[k];
        }
      });
      addToUnscopables("at");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/entry-unbind.js
  var require_entry_unbind = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/entry-unbind.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var uncurryThis = require_function_uncurry_this();
      module.exports = function(CONSTRUCTOR, METHOD) {
        return uncurryThis(globalThis2[CONSTRUCTOR].prototype[METHOD]);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/at.js
  var require_at = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/at.js"(exports, module) {
      "use strict";
      require_es_array_at();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("Array", "at");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/at.js
  var require_at2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/at.js"(exports, module) {
      "use strict";
      var parent = require_at();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/at.js
  var require_at3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/at.js"(exports, module) {
      "use strict";
      var parent = require_at2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-array.js
  var require_is_array = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-array.js"(exports, module) {
      "use strict";
      var classof = require_classof_raw();
      module.exports = Array.isArray || function isArray(argument) {
        return classof(argument) === "Array";
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/does-not-exceed-safe-integer.js
  var require_does_not_exceed_safe_integer = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/does-not-exceed-safe-integer.js"(exports, module) {
      "use strict";
      var $TypeError = TypeError;
      var MAX_SAFE_INTEGER = 9007199254740991;
      module.exports = function(it) {
        if (it > MAX_SAFE_INTEGER) throw $TypeError("Maximum allowed index exceeded");
        return it;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this-clause.js
  var require_function_uncurry_this_clause = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this-clause.js"(exports, module) {
      "use strict";
      var classofRaw = require_classof_raw();
      var uncurryThis = require_function_uncurry_this();
      module.exports = function(fn) {
        if (classofRaw(fn) === "Function") return uncurryThis(fn);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-bind-context.js
  var require_function_bind_context = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-bind-context.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this_clause();
      var aCallable = require_a_callable();
      var NATIVE_BIND = require_function_bind_native();
      var bind = uncurryThis(uncurryThis.bind);
      module.exports = function(fn, that) {
        aCallable(fn);
        return that === void 0 ? fn : NATIVE_BIND ? bind(fn, that) : function() {
          return fn.apply(that, arguments);
        };
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/flatten-into-array.js
  var require_flatten_into_array = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/flatten-into-array.js"(exports, module) {
      "use strict";
      var isArray = require_is_array();
      var lengthOfArrayLike = require_length_of_array_like();
      var doesNotExceedSafeInteger = require_does_not_exceed_safe_integer();
      var bind = require_function_bind_context();
      var flattenIntoArray = function(target, original, source, sourceLen, start, depth, mapper, thisArg) {
        var targetIndex = start;
        var sourceIndex = 0;
        var mapFn = mapper ? bind(mapper, thisArg) : false;
        var element, elementLen;
        while (sourceIndex < sourceLen) {
          if (sourceIndex in source) {
            element = mapFn ? mapFn(source[sourceIndex], sourceIndex, original) : source[sourceIndex];
            if (depth > 0 && isArray(element)) {
              elementLen = lengthOfArrayLike(element);
              targetIndex = flattenIntoArray(target, original, element, elementLen, targetIndex, depth - 1) - 1;
            } else {
              doesNotExceedSafeInteger(targetIndex + 1);
              target[targetIndex] = element;
            }
            targetIndex++;
          }
          sourceIndex++;
        }
        return targetIndex;
      };
      module.exports = flattenIntoArray;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-string-tag-support.js
  var require_to_string_tag_support = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-string-tag-support.js"(exports, module) {
      "use strict";
      var wellKnownSymbol = require_well_known_symbol();
      var TO_STRING_TAG = wellKnownSymbol("toStringTag");
      var test = {};
      test[TO_STRING_TAG] = "z";
      module.exports = String(test) === "[object z]";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/classof.js
  var require_classof = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/classof.js"(exports, module) {
      "use strict";
      var TO_STRING_TAG_SUPPORT = require_to_string_tag_support();
      var isCallable = require_is_callable();
      var classofRaw = require_classof_raw();
      var wellKnownSymbol = require_well_known_symbol();
      var TO_STRING_TAG = wellKnownSymbol("toStringTag");
      var $Object = Object;
      var CORRECT_ARGUMENTS = classofRaw(/* @__PURE__ */ (function() {
        return arguments;
      })()) === "Arguments";
      var tryGet = function(it, key) {
        try {
          return it[key];
        } catch (error) {
        }
      };
      module.exports = TO_STRING_TAG_SUPPORT ? classofRaw : function(it) {
        var O, tag, result;
        return it === void 0 ? "Undefined" : it === null ? "Null" : typeof (tag = tryGet(O = $Object(it), TO_STRING_TAG)) == "string" ? tag : CORRECT_ARGUMENTS ? classofRaw(O) : (result = classofRaw(O)) === "Object" && isCallable(O.callee) ? "Arguments" : result;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-constructor.js
  var require_is_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-constructor.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var fails = require_fails();
      var isCallable = require_is_callable();
      var classof = require_classof();
      var getBuiltIn = require_get_built_in();
      var inspectSource = require_inspect_source();
      var noop = function() {
      };
      var construct = getBuiltIn("Reflect", "construct");
      var constructorRegExp = /^\s*(?:class|function)\b/;
      var exec = uncurryThis(constructorRegExp.exec);
      var INCORRECT_TO_STRING = !constructorRegExp.test(noop);
      var isConstructorModern = function isConstructor(argument) {
        if (!isCallable(argument)) return false;
        try {
          construct(noop, [], argument);
          return true;
        } catch (error) {
          return false;
        }
      };
      var isConstructorLegacy = function isConstructor(argument) {
        if (!isCallable(argument)) return false;
        switch (classof(argument)) {
          case "AsyncFunction":
          case "GeneratorFunction":
          case "AsyncGeneratorFunction":
            return false;
        }
        try {
          return INCORRECT_TO_STRING || !!exec(constructorRegExp, inspectSource(argument));
        } catch (error) {
          return true;
        }
      };
      isConstructorLegacy.sham = true;
      module.exports = !construct || fails(function() {
        var called;
        return isConstructorModern(isConstructorModern.call) || !isConstructorModern(Object) || !isConstructorModern(function() {
          called = true;
        }) || called;
      }) ? isConstructorLegacy : isConstructorModern;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-species-constructor.js
  var require_array_species_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-species-constructor.js"(exports, module) {
      "use strict";
      var isArray = require_is_array();
      var isConstructor = require_is_constructor();
      var isObject = require_is_object();
      var wellKnownSymbol = require_well_known_symbol();
      var SPECIES = wellKnownSymbol("species");
      var $Array = Array;
      module.exports = function(originalArray) {
        var C;
        if (isArray(originalArray)) {
          C = originalArray.constructor;
          if (isConstructor(C) && (C === $Array || isArray(C.prototype))) C = void 0;
          else if (isObject(C)) {
            C = C[SPECIES];
            if (C === null) C = void 0;
          }
        }
        return C === void 0 ? $Array : C;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-species-create.js
  var require_array_species_create = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-species-create.js"(exports, module) {
      "use strict";
      var arraySpeciesConstructor = require_array_species_constructor();
      module.exports = function(originalArray, length) {
        return new (arraySpeciesConstructor(originalArray))(length === 0 ? 0 : length);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.flat.js
  var require_es_array_flat = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.flat.js"() {
      "use strict";
      var $ = require_export();
      var flattenIntoArray = require_flatten_into_array();
      var toObject = require_to_object();
      var lengthOfArrayLike = require_length_of_array_like();
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var arraySpeciesCreate = require_array_species_create();
      $({ target: "Array", proto: true }, {
        flat: function flat() {
          var depthArg = arguments.length ? arguments[0] : void 0;
          var O = toObject(this);
          var sourceLen = lengthOfArrayLike(O);
          var A = arraySpeciesCreate(O, 0);
          A.length = flattenIntoArray(A, O, O, sourceLen, 0, depthArg === void 0 ? 1 : toIntegerOrInfinity(depthArg));
          return A;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.unscopables.flat.js
  var require_es_array_unscopables_flat = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.unscopables.flat.js"() {
      "use strict";
      var addToUnscopables = require_add_to_unscopables();
      addToUnscopables("flat");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/flat.js
  var require_flat = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/flat.js"(exports, module) {
      "use strict";
      require_es_array_flat();
      require_es_array_unscopables_flat();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("Array", "flat");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/flat.js
  var require_flat2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/flat.js"(exports, module) {
      "use strict";
      var parent = require_flat();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/flat.js
  var require_flat3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/flat.js"(exports, module) {
      "use strict";
      var parent = require_flat2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-iteration-from-last.js
  var require_array_iteration_from_last = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-iteration-from-last.js"(exports, module) {
      "use strict";
      var bind = require_function_bind_context();
      var IndexedObject = require_indexed_object();
      var toObject = require_to_object();
      var lengthOfArrayLike = require_length_of_array_like();
      var createMethod = function(TYPE) {
        var IS_FIND_LAST_INDEX = TYPE === 1;
        return function($this, callbackfn, that) {
          var O = toObject($this);
          var self2 = IndexedObject(O);
          var index2 = lengthOfArrayLike(self2);
          var boundFunction = bind(callbackfn, that);
          var value, result;
          while (index2-- > 0) {
            value = self2[index2];
            result = boundFunction(value, index2, O);
            if (result) switch (TYPE) {
              case 0:
                return value;
              // findLast
              case 1:
                return index2;
            }
          }
          return IS_FIND_LAST_INDEX ? -1 : void 0;
        };
      };
      module.exports = {
        // `Array.prototype.findLast` method
        // https://github.com/tc39/proposal-array-find-from-last
        findLast: createMethod(0),
        // `Array.prototype.findLastIndex` method
        // https://github.com/tc39/proposal-array-find-from-last
        findLastIndex: createMethod(1)
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.find-last-index.js
  var require_es_array_find_last_index = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.find-last-index.js"() {
      "use strict";
      var $ = require_export();
      var $findLastIndex = require_array_iteration_from_last().findLastIndex;
      var addToUnscopables = require_add_to_unscopables();
      $({ target: "Array", proto: true }, {
        findLastIndex: function findLastIndex(callbackfn) {
          return $findLastIndex(this, callbackfn, arguments.length > 1 ? arguments[1] : void 0);
        }
      });
      addToUnscopables("findLastIndex");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.array.find-last-index.js
  var require_esnext_array_find_last_index = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.array.find-last-index.js"() {
      "use strict";
      require_es_array_find_last_index();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/find-last-index.js
  var require_find_last_index = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/array/find-last-index.js"(exports, module) {
      "use strict";
      require_es_array_find_last_index();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("Array", "findLastIndex");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/find-last-index.js
  var require_find_last_index2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/array/find-last-index.js"(exports, module) {
      "use strict";
      module.exports = require_find_last_index();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/find-last-index.js
  var require_find_last_index3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/array/find-last-index.js"(exports, module) {
      "use strict";
      require_esnext_array_find_last_index();
      var parent = require_find_last_index2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterators.js
  var require_iterators = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterators.js"(exports, module) {
      "use strict";
      module.exports = {};
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/correct-prototype-getter.js
  var require_correct_prototype_getter = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/correct-prototype-getter.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      module.exports = !fails(function() {
        function F() {
        }
        F.prototype.constructor = null;
        return Object.getPrototypeOf(new F()) !== F.prototype;
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-prototype-of.js
  var require_object_get_prototype_of = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-prototype-of.js"(exports, module) {
      "use strict";
      var hasOwn = require_has_own_property();
      var isCallable = require_is_callable();
      var toObject = require_to_object();
      var sharedKey = require_shared_key();
      var CORRECT_PROTOTYPE_GETTER = require_correct_prototype_getter();
      var IE_PROTO = sharedKey("IE_PROTO");
      var $Object = Object;
      var ObjectPrototype = $Object.prototype;
      module.exports = CORRECT_PROTOTYPE_GETTER ? $Object.getPrototypeOf : function(O) {
        var object = toObject(O);
        if (hasOwn(object, IE_PROTO)) return object[IE_PROTO];
        var constructor = object.constructor;
        if (isCallable(constructor) && object instanceof constructor) {
          return constructor.prototype;
        }
        return object instanceof $Object ? ObjectPrototype : null;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterators-core.js
  var require_iterators_core = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterators-core.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var create = require_object_create();
      var getPrototypeOf = require_object_get_prototype_of();
      var defineBuiltIn = require_define_built_in();
      var wellKnownSymbol = require_well_known_symbol();
      var IS_PURE = require_is_pure();
      var ITERATOR = wellKnownSymbol("iterator");
      var BUGGY_SAFARI_ITERATORS = false;
      var IteratorPrototype;
      var PrototypeOfArrayIteratorPrototype;
      var arrayIterator;
      if ([].keys) {
        arrayIterator = [].keys();
        if (!("next" in arrayIterator)) BUGGY_SAFARI_ITERATORS = true;
        else {
          PrototypeOfArrayIteratorPrototype = getPrototypeOf(getPrototypeOf(arrayIterator));
          if (PrototypeOfArrayIteratorPrototype !== Object.prototype) IteratorPrototype = PrototypeOfArrayIteratorPrototype;
        }
      }
      var NEW_ITERATOR_PROTOTYPE = !isObject(IteratorPrototype) || fails(function() {
        var test = {};
        return IteratorPrototype[ITERATOR].call(test) !== test;
      });
      if (NEW_ITERATOR_PROTOTYPE) IteratorPrototype = {};
      else if (IS_PURE) IteratorPrototype = create(IteratorPrototype);
      if (!isCallable(IteratorPrototype[ITERATOR])) {
        defineBuiltIn(IteratorPrototype, ITERATOR, function() {
          return this;
        });
      }
      module.exports = {
        IteratorPrototype,
        BUGGY_SAFARI_ITERATORS
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/set-to-string-tag.js
  var require_set_to_string_tag = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/set-to-string-tag.js"(exports, module) {
      "use strict";
      var defineProperty = require_object_define_property().f;
      var hasOwn = require_has_own_property();
      var wellKnownSymbol = require_well_known_symbol();
      var TO_STRING_TAG = wellKnownSymbol("toStringTag");
      module.exports = function(target, TAG, STATIC) {
        if (target && !STATIC) target = target.prototype;
        if (target && !hasOwn(target, TO_STRING_TAG)) {
          defineProperty(target, TO_STRING_TAG, { configurable: true, value: TAG });
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-create-constructor.js
  var require_iterator_create_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-create-constructor.js"(exports, module) {
      "use strict";
      var IteratorPrototype = require_iterators_core().IteratorPrototype;
      var create = require_object_create();
      var createPropertyDescriptor = require_create_property_descriptor();
      var setToStringTag = require_set_to_string_tag();
      var Iterators = require_iterators();
      var returnThis = function() {
        return this;
      };
      module.exports = function(IteratorConstructor, NAME, next, ENUMERABLE_NEXT) {
        var TO_STRING_TAG = NAME + " Iterator";
        IteratorConstructor.prototype = create(IteratorPrototype, { next: createPropertyDescriptor(+!ENUMERABLE_NEXT, next) });
        setToStringTag(IteratorConstructor, TO_STRING_TAG, false, true);
        Iterators[TO_STRING_TAG] = returnThis;
        return IteratorConstructor;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this-accessor.js
  var require_function_uncurry_this_accessor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-uncurry-this-accessor.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var aCallable = require_a_callable();
      module.exports = function(object, key, method) {
        try {
          return uncurryThis(aCallable(Object.getOwnPropertyDescriptor(object, key)[method]));
        } catch (error) {
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-possible-prototype.js
  var require_is_possible_prototype = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-possible-prototype.js"(exports, module) {
      "use strict";
      var isObject = require_is_object();
      module.exports = function(argument) {
        return isObject(argument) || argument === null;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-possible-prototype.js
  var require_a_possible_prototype = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-possible-prototype.js"(exports, module) {
      "use strict";
      var isPossiblePrototype = require_is_possible_prototype();
      var $String = String;
      var $TypeError = TypeError;
      module.exports = function(argument) {
        if (isPossiblePrototype(argument)) return argument;
        throw new $TypeError("Can't set " + $String(argument) + " as a prototype");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-set-prototype-of.js
  var require_object_set_prototype_of = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-set-prototype-of.js"(exports, module) {
      "use strict";
      var uncurryThisAccessor = require_function_uncurry_this_accessor();
      var isObject = require_is_object();
      var requireObjectCoercible = require_require_object_coercible();
      var aPossiblePrototype = require_a_possible_prototype();
      module.exports = Object.setPrototypeOf || ("__proto__" in {} ? (function() {
        var CORRECT_SETTER = false;
        var test = {};
        var setter;
        try {
          setter = uncurryThisAccessor(Object.prototype, "__proto__", "set");
          setter(test, []);
          CORRECT_SETTER = test instanceof Array;
        } catch (error) {
        }
        return function setPrototypeOf(O, proto) {
          requireObjectCoercible(O);
          aPossiblePrototype(proto);
          if (!isObject(O)) return O;
          if (CORRECT_SETTER) setter(O, proto);
          else O.__proto__ = proto;
          return O;
        };
      })() : void 0);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-define.js
  var require_iterator_define = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-define.js"(exports, module) {
      "use strict";
      var $ = require_export();
      var call = require_function_call();
      var IS_PURE = require_is_pure();
      var FunctionName = require_function_name();
      var isCallable = require_is_callable();
      var createIteratorConstructor = require_iterator_create_constructor();
      var getPrototypeOf = require_object_get_prototype_of();
      var setPrototypeOf = require_object_set_prototype_of();
      var setToStringTag = require_set_to_string_tag();
      var createNonEnumerableProperty = require_create_non_enumerable_property();
      var defineBuiltIn = require_define_built_in();
      var wellKnownSymbol = require_well_known_symbol();
      var Iterators = require_iterators();
      var IteratorsCore = require_iterators_core();
      var PROPER_FUNCTION_NAME = FunctionName.PROPER;
      var CONFIGURABLE_FUNCTION_NAME = FunctionName.CONFIGURABLE;
      var IteratorPrototype = IteratorsCore.IteratorPrototype;
      var BUGGY_SAFARI_ITERATORS = IteratorsCore.BUGGY_SAFARI_ITERATORS;
      var ITERATOR = wellKnownSymbol("iterator");
      var KEYS = "keys";
      var VALUES = "values";
      var ENTRIES = "entries";
      var returnThis = function() {
        return this;
      };
      module.exports = function(Iterable, NAME, IteratorConstructor, next, DEFAULT, IS_SET, FORCED) {
        createIteratorConstructor(IteratorConstructor, NAME, next);
        var getIterationMethod = function(KIND) {
          if (KIND === DEFAULT && defaultIterator) return defaultIterator;
          if (!BUGGY_SAFARI_ITERATORS && KIND && KIND in IterablePrototype) return IterablePrototype[KIND];
          switch (KIND) {
            case KEYS:
              return function keys() {
                return new IteratorConstructor(this, KIND);
              };
            case VALUES:
              return function values() {
                return new IteratorConstructor(this, KIND);
              };
            case ENTRIES:
              return function entries() {
                return new IteratorConstructor(this, KIND);
              };
          }
          return function() {
            return new IteratorConstructor(this);
          };
        };
        var TO_STRING_TAG = NAME + " Iterator";
        var INCORRECT_VALUES_NAME = false;
        var IterablePrototype = Iterable.prototype;
        var nativeIterator = IterablePrototype[ITERATOR] || IterablePrototype["@@iterator"] || DEFAULT && IterablePrototype[DEFAULT];
        var defaultIterator = !BUGGY_SAFARI_ITERATORS && nativeIterator || getIterationMethod(DEFAULT);
        var anyNativeIterator = NAME === "Array" ? IterablePrototype.entries || nativeIterator : nativeIterator;
        var CurrentIteratorPrototype, methods, KEY;
        if (anyNativeIterator) {
          CurrentIteratorPrototype = getPrototypeOf(anyNativeIterator.call(new Iterable()));
          if (CurrentIteratorPrototype !== Object.prototype && CurrentIteratorPrototype.next) {
            if (!IS_PURE && getPrototypeOf(CurrentIteratorPrototype) !== IteratorPrototype) {
              if (setPrototypeOf) {
                setPrototypeOf(CurrentIteratorPrototype, IteratorPrototype);
              } else if (!isCallable(CurrentIteratorPrototype[ITERATOR])) {
                defineBuiltIn(CurrentIteratorPrototype, ITERATOR, returnThis);
              }
            }
            setToStringTag(CurrentIteratorPrototype, TO_STRING_TAG, true, true);
            if (IS_PURE) Iterators[TO_STRING_TAG] = returnThis;
          }
        }
        if (PROPER_FUNCTION_NAME && DEFAULT === VALUES && nativeIterator && nativeIterator.name !== VALUES) {
          if (!IS_PURE && CONFIGURABLE_FUNCTION_NAME) {
            createNonEnumerableProperty(IterablePrototype, "name", VALUES);
          } else {
            INCORRECT_VALUES_NAME = true;
            defaultIterator = function values() {
              return call(nativeIterator, this);
            };
          }
        }
        if (DEFAULT) {
          methods = {
            values: getIterationMethod(VALUES),
            keys: IS_SET ? defaultIterator : getIterationMethod(KEYS),
            entries: getIterationMethod(ENTRIES)
          };
          if (FORCED) for (KEY in methods) {
            if (BUGGY_SAFARI_ITERATORS || INCORRECT_VALUES_NAME || !(KEY in IterablePrototype)) {
              defineBuiltIn(IterablePrototype, KEY, methods[KEY]);
            }
          }
          else $({ target: NAME, proto: true, forced: BUGGY_SAFARI_ITERATORS || INCORRECT_VALUES_NAME }, methods);
        }
        if ((!IS_PURE || FORCED) && IterablePrototype[ITERATOR] !== defaultIterator) {
          defineBuiltIn(IterablePrototype, ITERATOR, defaultIterator, { name: DEFAULT });
        }
        Iterators[NAME] = defaultIterator;
        return methods;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-iter-result-object.js
  var require_create_iter_result_object = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-iter-result-object.js"(exports, module) {
      "use strict";
      module.exports = function(value, done) {
        return { value, done };
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.iterator.js
  var require_es_array_iterator = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.array.iterator.js"(exports, module) {
      "use strict";
      var toIndexedObject = require_to_indexed_object();
      var addToUnscopables = require_add_to_unscopables();
      var Iterators = require_iterators();
      var InternalStateModule = require_internal_state();
      var defineProperty = require_object_define_property().f;
      var defineIterator = require_iterator_define();
      var createIterResultObject = require_create_iter_result_object();
      var IS_PURE = require_is_pure();
      var DESCRIPTORS = require_descriptors();
      var ARRAY_ITERATOR = "Array Iterator";
      var setInternalState = InternalStateModule.set;
      var getInternalState = InternalStateModule.getterFor(ARRAY_ITERATOR);
      module.exports = defineIterator(Array, "Array", function(iterated, kind) {
        setInternalState(this, {
          type: ARRAY_ITERATOR,
          target: toIndexedObject(iterated),
          // target
          index: 0,
          // next index
          kind
          // kind
        });
      }, function() {
        var state2 = getInternalState(this);
        var target = state2.target;
        var index2 = state2.index++;
        if (!target || index2 >= target.length) {
          state2.target = null;
          return createIterResultObject(void 0, true);
        }
        switch (state2.kind) {
          case "keys":
            return createIterResultObject(index2, false);
          case "values":
            return createIterResultObject(target[index2], false);
        }
        return createIterResultObject([index2, target[index2]], false);
      }, "values");
      var values = Iterators.Arguments = Iterators.Array;
      addToUnscopables("keys");
      addToUnscopables("values");
      addToUnscopables("entries");
      if (!IS_PURE && DESCRIPTORS && values.name !== "values") try {
        defineProperty(values, "name", { value: "values" });
      } catch (error) {
      }
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-array-iterator-method.js
  var require_is_array_iterator_method = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-array-iterator-method.js"(exports, module) {
      "use strict";
      var wellKnownSymbol = require_well_known_symbol();
      var Iterators = require_iterators();
      var ITERATOR = wellKnownSymbol("iterator");
      var ArrayPrototype = Array.prototype;
      module.exports = function(it) {
        return it !== void 0 && (Iterators.Array === it || ArrayPrototype[ITERATOR] === it);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-iterator-method.js
  var require_get_iterator_method = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-iterator-method.js"(exports, module) {
      "use strict";
      var classof = require_classof();
      var getMethod = require_get_method();
      var isNullOrUndefined = require_is_null_or_undefined();
      var Iterators = require_iterators();
      var wellKnownSymbol = require_well_known_symbol();
      var ITERATOR = wellKnownSymbol("iterator");
      module.exports = function(it) {
        if (!isNullOrUndefined(it)) return getMethod(it, ITERATOR) || getMethod(it, "@@iterator") || Iterators[classof(it)];
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-iterator.js
  var require_get_iterator = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-iterator.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var aCallable = require_a_callable();
      var anObject = require_an_object();
      var tryToString = require_try_to_string();
      var getIteratorMethod = require_get_iterator_method();
      var $TypeError = TypeError;
      module.exports = function(argument, usingIterator) {
        var iteratorMethod = arguments.length < 2 ? getIteratorMethod(argument) : usingIterator;
        if (aCallable(iteratorMethod)) return anObject(call(iteratorMethod, argument));
        throw new $TypeError(tryToString(argument) + " is not iterable");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-close.js
  var require_iterator_close = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterator-close.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var anObject = require_an_object();
      var getMethod = require_get_method();
      module.exports = function(iterator, kind, value) {
        var innerResult, innerError;
        anObject(iterator);
        try {
          innerResult = getMethod(iterator, "return");
          if (!innerResult) {
            if (kind === "throw") throw value;
            return value;
          }
          innerResult = call(innerResult, iterator);
        } catch (error) {
          innerError = true;
          innerResult = error;
        }
        if (kind === "throw") throw value;
        if (innerError) throw innerResult;
        anObject(innerResult);
        return value;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterate.js
  var require_iterate = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/iterate.js"(exports, module) {
      "use strict";
      var bind = require_function_bind_context();
      var call = require_function_call();
      var anObject = require_an_object();
      var tryToString = require_try_to_string();
      var isArrayIteratorMethod = require_is_array_iterator_method();
      var lengthOfArrayLike = require_length_of_array_like();
      var isPrototypeOf = require_object_is_prototype_of();
      var getIterator = require_get_iterator();
      var getIteratorMethod = require_get_iterator_method();
      var iteratorClose = require_iterator_close();
      var $TypeError = TypeError;
      var Result = function(stopped, result) {
        this.stopped = stopped;
        this.result = result;
      };
      var ResultPrototype = Result.prototype;
      module.exports = function(iterable, unboundFunction, options) {
        var that = options && options.that;
        var AS_ENTRIES = !!(options && options.AS_ENTRIES);
        var IS_RECORD = !!(options && options.IS_RECORD);
        var IS_ITERATOR = !!(options && options.IS_ITERATOR);
        var INTERRUPTED = !!(options && options.INTERRUPTED);
        var fn = bind(unboundFunction, that);
        var iterator, iterFn, index2, length, result, next, step;
        var stop = function(condition) {
          if (iterator) iteratorClose(iterator, "normal");
          return new Result(true, condition);
        };
        var callFn = function(value) {
          if (AS_ENTRIES) {
            anObject(value);
            return INTERRUPTED ? fn(value[0], value[1], stop) : fn(value[0], value[1]);
          }
          return INTERRUPTED ? fn(value, stop) : fn(value);
        };
        if (IS_RECORD) {
          iterator = iterable.iterator;
        } else if (IS_ITERATOR) {
          iterator = iterable;
        } else {
          iterFn = getIteratorMethod(iterable);
          if (!iterFn) throw new $TypeError(tryToString(iterable) + " is not iterable");
          if (isArrayIteratorMethod(iterFn)) {
            for (index2 = 0, length = lengthOfArrayLike(iterable); length > index2; index2++) {
              result = callFn(iterable[index2]);
              if (result && isPrototypeOf(ResultPrototype, result)) return result;
            }
            return new Result(false);
          }
          iterator = getIterator(iterable, iterFn);
        }
        next = IS_RECORD ? iterable.next : iterator.next;
        while (!(step = call(next, iterator)).done) {
          try {
            result = callFn(step.value);
          } catch (error) {
            iteratorClose(iterator, "throw", error);
          }
          if (typeof result == "object" && result && isPrototypeOf(ResultPrototype, result)) return result;
        }
        return new Result(false);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-property.js
  var require_create_property = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/create-property.js"(exports, module) {
      "use strict";
      var DESCRIPTORS = require_descriptors();
      var definePropertyModule = require_object_define_property();
      var createPropertyDescriptor = require_create_property_descriptor();
      module.exports = function(object, key, value) {
        if (DESCRIPTORS) definePropertyModule.f(object, key, createPropertyDescriptor(0, value));
        else object[key] = value;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.from-entries.js
  var require_es_object_from_entries = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.from-entries.js"() {
      "use strict";
      var $ = require_export();
      var iterate = require_iterate();
      var createProperty = require_create_property();
      $({ target: "Object", stat: true }, {
        fromEntries: function fromEntries(iterable) {
          var obj = {};
          iterate(iterable, function(k, v) {
            createProperty(obj, k, v);
          }, { AS_ENTRIES: true });
          return obj;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/path.js
  var require_path = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/path.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      module.exports = globalThis2;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/object/from-entries.js
  var require_from_entries = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/object/from-entries.js"(exports, module) {
      "use strict";
      require_es_array_iterator();
      require_es_object_from_entries();
      var path = require_path();
      module.exports = path.Object.fromEntries;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/dom-iterables.js
  var require_dom_iterables = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/dom-iterables.js"(exports, module) {
      "use strict";
      module.exports = {
        CSSRuleList: 0,
        CSSStyleDeclaration: 0,
        CSSValueList: 0,
        ClientRectList: 0,
        DOMRectList: 0,
        DOMStringList: 0,
        DOMTokenList: 1,
        DataTransferItemList: 0,
        FileList: 0,
        HTMLAllCollection: 0,
        HTMLCollection: 0,
        HTMLFormElement: 0,
        HTMLSelectElement: 0,
        MediaList: 0,
        MimeTypeArray: 0,
        NamedNodeMap: 0,
        NodeList: 1,
        PaintRequestList: 0,
        Plugin: 0,
        PluginArray: 0,
        SVGLengthList: 0,
        SVGNumberList: 0,
        SVGPathSegList: 0,
        SVGPointList: 0,
        SVGStringList: 0,
        SVGTransformList: 0,
        SourceBufferList: 0,
        StyleSheetList: 0,
        TextTrackCueList: 0,
        TextTrackList: 0,
        TouchList: 0
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/dom-token-list-prototype.js
  var require_dom_token_list_prototype = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/dom-token-list-prototype.js"(exports, module) {
      "use strict";
      var documentCreateElement = require_document_create_element();
      var classList = documentCreateElement("span").classList;
      var DOMTokenListPrototype = classList && classList.constructor && classList.constructor.prototype;
      module.exports = DOMTokenListPrototype === Object.prototype ? void 0 : DOMTokenListPrototype;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/web.dom-collections.iterator.js
  var require_web_dom_collections_iterator = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/web.dom-collections.iterator.js"() {
      "use strict";
      var globalThis2 = require_global_this();
      var DOMIterables = require_dom_iterables();
      var DOMTokenListPrototype = require_dom_token_list_prototype();
      var ArrayIteratorMethods = require_es_array_iterator();
      var createNonEnumerableProperty = require_create_non_enumerable_property();
      var setToStringTag = require_set_to_string_tag();
      var wellKnownSymbol = require_well_known_symbol();
      var ITERATOR = wellKnownSymbol("iterator");
      var ArrayValues = ArrayIteratorMethods.values;
      var handlePrototype = function(CollectionPrototype, COLLECTION_NAME2) {
        if (CollectionPrototype) {
          if (CollectionPrototype[ITERATOR] !== ArrayValues) try {
            createNonEnumerableProperty(CollectionPrototype, ITERATOR, ArrayValues);
          } catch (error) {
            CollectionPrototype[ITERATOR] = ArrayValues;
          }
          setToStringTag(CollectionPrototype, COLLECTION_NAME2, true);
          if (DOMIterables[COLLECTION_NAME2]) for (var METHOD_NAME in ArrayIteratorMethods) {
            if (CollectionPrototype[METHOD_NAME] !== ArrayIteratorMethods[METHOD_NAME]) try {
              createNonEnumerableProperty(CollectionPrototype, METHOD_NAME, ArrayIteratorMethods[METHOD_NAME]);
            } catch (error) {
              CollectionPrototype[METHOD_NAME] = ArrayIteratorMethods[METHOD_NAME];
            }
          }
        }
      };
      for (COLLECTION_NAME in DOMIterables) {
        handlePrototype(globalThis2[COLLECTION_NAME] && globalThis2[COLLECTION_NAME].prototype, COLLECTION_NAME);
      }
      var COLLECTION_NAME;
      handlePrototype(DOMTokenListPrototype, "DOMTokenList");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/object/from-entries.js
  var require_from_entries2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/object/from-entries.js"(exports, module) {
      "use strict";
      var parent = require_from_entries();
      require_web_dom_collections_iterator();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/object/from-entries.js
  var require_from_entries3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/object/from-entries.js"(exports, module) {
      "use strict";
      var parent = require_from_entries2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.create.js
  var require_es_object_create = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.create.js"() {
      "use strict";
      var $ = require_export();
      var DESCRIPTORS = require_descriptors();
      var create = require_object_create();
      $({ target: "Object", stat: true, sham: !DESCRIPTORS }, {
        create
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.group-by.js
  var require_es_object_group_by = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.group-by.js"() {
      "use strict";
      var $ = require_export();
      var createProperty = require_create_property();
      var getBuiltIn = require_get_built_in();
      var uncurryThis = require_function_uncurry_this();
      var aCallable = require_a_callable();
      var requireObjectCoercible = require_require_object_coercible();
      var toPropertyKey = require_to_property_key();
      var iterate = require_iterate();
      var fails = require_fails();
      var nativeGroupBy = Object.groupBy;
      var create = getBuiltIn("Object", "create");
      var push = uncurryThis([].push);
      var DOES_NOT_WORK_WITH_PRIMITIVES = !nativeGroupBy || fails(function() {
        return nativeGroupBy("ab", function(it) {
          return it;
        }).a.length !== 1;
      });
      $({ target: "Object", stat: true, forced: DOES_NOT_WORK_WITH_PRIMITIVES }, {
        groupBy: function groupBy(items, callbackfn) {
          requireObjectCoercible(items);
          aCallable(callbackfn);
          var obj = create(null);
          var k = 0;
          iterate(items, function(value) {
            var key = toPropertyKey(callbackfn(value, k++));
            if (key in obj) push(obj[key], value);
            else createProperty(obj, key, [value]);
          });
          return obj;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/object/group-by.js
  var require_group_by = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/object/group-by.js"(exports, module) {
      "use strict";
      require_es_object_create();
      require_es_object_group_by();
      var path = require_path();
      module.exports = path.Object.groupBy;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/object/group-by.js
  var require_group_by2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/object/group-by.js"(exports, module) {
      "use strict";
      var parent = require_group_by();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.object.group-by.js
  var require_esnext_object_group_by = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.object.group-by.js"() {
      "use strict";
      require_es_object_group_by();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/object/group-by.js
  var require_group_by3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/object/group-by.js"(exports, module) {
      "use strict";
      var parent = require_group_by2();
      require_esnext_object_group_by();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-to-string.js
  var require_object_to_string = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-to-string.js"(exports, module) {
      "use strict";
      var TO_STRING_TAG_SUPPORT = require_to_string_tag_support();
      var classof = require_classof();
      module.exports = TO_STRING_TAG_SUPPORT ? {}.toString : function toString2() {
        return "[object " + classof(this) + "]";
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.to-string.js
  var require_es_object_to_string = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.object.to-string.js"() {
      "use strict";
      var TO_STRING_TAG_SUPPORT = require_to_string_tag_support();
      var defineBuiltIn = require_define_built_in();
      var toString2 = require_object_to_string();
      if (!TO_STRING_TAG_SUPPORT) {
        defineBuiltIn(Object.prototype, "toString", toString2, { unsafe: true });
      }
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-slice.js
  var require_array_slice = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-slice.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      module.exports = uncurryThis([].slice);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-names-external.js
  var require_object_get_own_property_names_external = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-get-own-property-names-external.js"(exports, module) {
      "use strict";
      var classof = require_classof_raw();
      var toIndexedObject = require_to_indexed_object();
      var $getOwnPropertyNames = require_object_get_own_property_names().f;
      var arraySlice = require_array_slice();
      var windowNames = typeof window == "object" && window && Object.getOwnPropertyNames ? Object.getOwnPropertyNames(window) : [];
      var getWindowNames = function(it) {
        try {
          return $getOwnPropertyNames(it);
        } catch (error) {
          return arraySlice(windowNames);
        }
      };
      module.exports.f = function getOwnPropertyNames(it) {
        return windowNames && classof(it) === "Window" ? getWindowNames(it) : $getOwnPropertyNames(toIndexedObject(it));
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-buffer-non-extensible.js
  var require_array_buffer_non_extensible = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/array-buffer-non-extensible.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      module.exports = fails(function() {
        if (typeof ArrayBuffer == "function") {
          var buffer = new ArrayBuffer(8);
          if (Object.isExtensible(buffer)) Object.defineProperty(buffer, "a", { value: 8 });
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-is-extensible.js
  var require_object_is_extensible = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/object-is-extensible.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var isObject = require_is_object();
      var classof = require_classof_raw();
      var ARRAY_BUFFER_NON_EXTENSIBLE = require_array_buffer_non_extensible();
      var $isExtensible = Object.isExtensible;
      var FAILS_ON_PRIMITIVES = fails(function() {
        $isExtensible(1);
      });
      module.exports = FAILS_ON_PRIMITIVES || ARRAY_BUFFER_NON_EXTENSIBLE ? function isExtensible(it) {
        if (!isObject(it)) return false;
        if (ARRAY_BUFFER_NON_EXTENSIBLE && classof(it) === "ArrayBuffer") return false;
        return $isExtensible ? $isExtensible(it) : true;
      } : $isExtensible;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/freezing.js
  var require_freezing = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/freezing.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      module.exports = !fails(function() {
        return Object.isExtensible(Object.preventExtensions({}));
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/internal-metadata.js
  var require_internal_metadata = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/internal-metadata.js"(exports, module) {
      "use strict";
      var $ = require_export();
      var uncurryThis = require_function_uncurry_this();
      var hiddenKeys = require_hidden_keys();
      var isObject = require_is_object();
      var hasOwn = require_has_own_property();
      var defineProperty = require_object_define_property().f;
      var getOwnPropertyNamesModule = require_object_get_own_property_names();
      var getOwnPropertyNamesExternalModule = require_object_get_own_property_names_external();
      var isExtensible = require_object_is_extensible();
      var uid = require_uid();
      var FREEZING = require_freezing();
      var REQUIRED = false;
      var METADATA = uid("meta");
      var id = 0;
      var setMetadata = function(it) {
        defineProperty(it, METADATA, { value: {
          objectID: "O" + id++,
          // object ID
          weakData: {}
          // weak collections IDs
        } });
      };
      var fastKey = function(it, create) {
        if (!isObject(it)) return typeof it == "symbol" ? it : (typeof it == "string" ? "S" : "P") + it;
        if (!hasOwn(it, METADATA)) {
          if (!isExtensible(it)) return "F";
          if (!create) return "E";
          setMetadata(it);
        }
        return it[METADATA].objectID;
      };
      var getWeakData = function(it, create) {
        if (!hasOwn(it, METADATA)) {
          if (!isExtensible(it)) return true;
          if (!create) return false;
          setMetadata(it);
        }
        return it[METADATA].weakData;
      };
      var onFreeze = function(it) {
        if (FREEZING && REQUIRED && isExtensible(it) && !hasOwn(it, METADATA)) setMetadata(it);
        return it;
      };
      var enable = function() {
        meta.enable = function() {
        };
        REQUIRED = true;
        var getOwnPropertyNames = getOwnPropertyNamesModule.f;
        var splice = uncurryThis([].splice);
        var test = {};
        test[METADATA] = 1;
        if (getOwnPropertyNames(test).length) {
          getOwnPropertyNamesModule.f = function(it) {
            var result = getOwnPropertyNames(it);
            for (var i = 0, length = result.length; i < length; i++) {
              if (result[i] === METADATA) {
                splice(result, i, 1);
                break;
              }
            }
            return result;
          };
          $({ target: "Object", stat: true, forced: true }, {
            getOwnPropertyNames: getOwnPropertyNamesExternalModule.f
          });
        }
      };
      var meta = module.exports = {
        enable,
        fastKey,
        getWeakData,
        onFreeze
      };
      hiddenKeys[METADATA] = true;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/an-instance.js
  var require_an_instance = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/an-instance.js"(exports, module) {
      "use strict";
      var isPrototypeOf = require_object_is_prototype_of();
      var $TypeError = TypeError;
      module.exports = function(it, Prototype) {
        if (isPrototypeOf(Prototype, it)) return it;
        throw new $TypeError("Incorrect invocation");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/check-correctness-of-iteration.js
  var require_check_correctness_of_iteration = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/check-correctness-of-iteration.js"(exports, module) {
      "use strict";
      var wellKnownSymbol = require_well_known_symbol();
      var ITERATOR = wellKnownSymbol("iterator");
      var SAFE_CLOSING = false;
      try {
        called = 0;
        iteratorWithReturn = {
          next: function() {
            return { done: !!called++ };
          },
          "return": function() {
            SAFE_CLOSING = true;
          }
        };
        iteratorWithReturn[ITERATOR] = function() {
          return this;
        };
        Array.from(iteratorWithReturn, function() {
          throw 2;
        });
      } catch (error) {
      }
      var called;
      var iteratorWithReturn;
      module.exports = function(exec, SKIP_CLOSING) {
        try {
          if (!SKIP_CLOSING && !SAFE_CLOSING) return false;
        } catch (error) {
          return false;
        }
        var ITERATION_SUPPORT = false;
        try {
          var object = {};
          object[ITERATOR] = function() {
            return {
              next: function() {
                return { done: ITERATION_SUPPORT = true };
              }
            };
          };
          exec(object);
        } catch (error) {
        }
        return ITERATION_SUPPORT;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/inherit-if-required.js
  var require_inherit_if_required = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/inherit-if-required.js"(exports, module) {
      "use strict";
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var setPrototypeOf = require_object_set_prototype_of();
      module.exports = function($this, dummy, Wrapper) {
        var NewTarget, NewTargetPrototype;
        if (
          // it can work only with native `setPrototypeOf`
          setPrototypeOf && // we haven't completely correct pre-ES6 way for getting `new.target`, so use this
          isCallable(NewTarget = dummy.constructor) && NewTarget !== Wrapper && isObject(NewTargetPrototype = NewTarget.prototype) && NewTargetPrototype !== Wrapper.prototype
        ) setPrototypeOf($this, NewTargetPrototype);
        return $this;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/collection.js
  var require_collection = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/collection.js"(exports, module) {
      "use strict";
      var $ = require_export();
      var globalThis2 = require_global_this();
      var uncurryThis = require_function_uncurry_this();
      var isForced = require_is_forced();
      var defineBuiltIn = require_define_built_in();
      var InternalMetadataModule = require_internal_metadata();
      var iterate = require_iterate();
      var anInstance = require_an_instance();
      var isCallable = require_is_callable();
      var isNullOrUndefined = require_is_null_or_undefined();
      var isObject = require_is_object();
      var fails = require_fails();
      var checkCorrectnessOfIteration = require_check_correctness_of_iteration();
      var setToStringTag = require_set_to_string_tag();
      var inheritIfRequired = require_inherit_if_required();
      module.exports = function(CONSTRUCTOR_NAME, wrapper, common) {
        var IS_MAP = CONSTRUCTOR_NAME.indexOf("Map") !== -1;
        var IS_WEAK = CONSTRUCTOR_NAME.indexOf("Weak") !== -1;
        var ADDER = IS_MAP ? "set" : "add";
        var NativeConstructor = globalThis2[CONSTRUCTOR_NAME];
        var NativePrototype = NativeConstructor && NativeConstructor.prototype;
        var Constructor = NativeConstructor;
        var exported = {};
        var fixMethod = function(KEY) {
          var uncurriedNativeMethod = uncurryThis(NativePrototype[KEY]);
          defineBuiltIn(
            NativePrototype,
            KEY,
            KEY === "add" ? function add(value) {
              uncurriedNativeMethod(this, value === 0 ? 0 : value);
              return this;
            } : KEY === "delete" ? function(key) {
              return IS_WEAK && !isObject(key) ? false : uncurriedNativeMethod(this, key === 0 ? 0 : key);
            } : KEY === "get" ? function get(key) {
              return IS_WEAK && !isObject(key) ? void 0 : uncurriedNativeMethod(this, key === 0 ? 0 : key);
            } : KEY === "has" ? function has(key) {
              return IS_WEAK && !isObject(key) ? false : uncurriedNativeMethod(this, key === 0 ? 0 : key);
            } : function set(key, value) {
              uncurriedNativeMethod(this, key === 0 ? 0 : key, value);
              return this;
            }
          );
        };
        var REPLACE = isForced(
          CONSTRUCTOR_NAME,
          !isCallable(NativeConstructor) || !(IS_WEAK || NativePrototype.forEach && !fails(function() {
            new NativeConstructor().entries().next();
          }))
        );
        if (REPLACE) {
          Constructor = common.getConstructor(wrapper, CONSTRUCTOR_NAME, IS_MAP, ADDER);
          InternalMetadataModule.enable();
        } else if (isForced(CONSTRUCTOR_NAME, true)) {
          var instance = new Constructor();
          var HASNT_CHAINING = instance[ADDER](IS_WEAK ? {} : -0, 1) !== instance;
          var THROWS_ON_PRIMITIVES = fails(function() {
            instance.has(1);
          });
          var ACCEPT_ITERABLES = checkCorrectnessOfIteration(function(iterable) {
            new NativeConstructor(iterable);
          });
          var BUGGY_ZERO = !IS_WEAK && fails(function() {
            var $instance = new NativeConstructor();
            var index2 = 5;
            while (index2--) $instance[ADDER](index2, index2);
            return !$instance.has(-0);
          });
          if (!ACCEPT_ITERABLES) {
            Constructor = wrapper(function(dummy, iterable) {
              anInstance(dummy, NativePrototype);
              var that = inheritIfRequired(new NativeConstructor(), dummy, Constructor);
              if (!isNullOrUndefined(iterable)) iterate(iterable, that[ADDER], { that, AS_ENTRIES: IS_MAP });
              return that;
            });
            Constructor.prototype = NativePrototype;
            NativePrototype.constructor = Constructor;
          }
          if (THROWS_ON_PRIMITIVES || BUGGY_ZERO) {
            fixMethod("delete");
            fixMethod("has");
            IS_MAP && fixMethod("get");
          }
          if (BUGGY_ZERO || HASNT_CHAINING) fixMethod(ADDER);
          if (IS_WEAK && NativePrototype.clear) delete NativePrototype.clear;
        }
        exported[CONSTRUCTOR_NAME] = Constructor;
        $({ global: true, constructor: true, forced: Constructor !== NativeConstructor }, exported);
        setToStringTag(Constructor, CONSTRUCTOR_NAME);
        if (!IS_WEAK) common.setStrong(Constructor, CONSTRUCTOR_NAME, IS_MAP);
        return Constructor;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-in-accessor.js
  var require_define_built_in_accessor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-in-accessor.js"(exports, module) {
      "use strict";
      var makeBuiltIn = require_make_built_in();
      var defineProperty = require_object_define_property();
      module.exports = function(target, name, descriptor) {
        if (descriptor.get) makeBuiltIn(descriptor.get, name, { getter: true });
        if (descriptor.set) makeBuiltIn(descriptor.set, name, { setter: true });
        return defineProperty.f(target, name, descriptor);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-ins.js
  var require_define_built_ins = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/define-built-ins.js"(exports, module) {
      "use strict";
      var defineBuiltIn = require_define_built_in();
      module.exports = function(target, src, options) {
        for (var key in src) defineBuiltIn(target, key, src[key], options);
        return target;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/set-species.js
  var require_set_species = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/set-species.js"(exports, module) {
      "use strict";
      var getBuiltIn = require_get_built_in();
      var defineBuiltInAccessor = require_define_built_in_accessor();
      var wellKnownSymbol = require_well_known_symbol();
      var DESCRIPTORS = require_descriptors();
      var SPECIES = wellKnownSymbol("species");
      module.exports = function(CONSTRUCTOR_NAME) {
        var Constructor = getBuiltIn(CONSTRUCTOR_NAME);
        if (DESCRIPTORS && Constructor && !Constructor[SPECIES]) {
          defineBuiltInAccessor(Constructor, SPECIES, {
            configurable: true,
            get: function() {
              return this;
            }
          });
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/collection-strong.js
  var require_collection_strong = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/collection-strong.js"(exports, module) {
      "use strict";
      var create = require_object_create();
      var defineBuiltInAccessor = require_define_built_in_accessor();
      var defineBuiltIns = require_define_built_ins();
      var bind = require_function_bind_context();
      var anInstance = require_an_instance();
      var isNullOrUndefined = require_is_null_or_undefined();
      var iterate = require_iterate();
      var defineIterator = require_iterator_define();
      var createIterResultObject = require_create_iter_result_object();
      var setSpecies = require_set_species();
      var DESCRIPTORS = require_descriptors();
      var fastKey = require_internal_metadata().fastKey;
      var InternalStateModule = require_internal_state();
      var setInternalState = InternalStateModule.set;
      var internalStateGetterFor = InternalStateModule.getterFor;
      module.exports = {
        getConstructor: function(wrapper, CONSTRUCTOR_NAME, IS_MAP, ADDER) {
          var Constructor = wrapper(function(that, iterable) {
            anInstance(that, Prototype);
            setInternalState(that, {
              type: CONSTRUCTOR_NAME,
              index: create(null),
              first: null,
              last: null,
              size: 0
            });
            if (!DESCRIPTORS) that.size = 0;
            if (!isNullOrUndefined(iterable)) iterate(iterable, that[ADDER], { that, AS_ENTRIES: IS_MAP });
          });
          var Prototype = Constructor.prototype;
          var getInternalState = internalStateGetterFor(CONSTRUCTOR_NAME);
          var define = function(that, key, value) {
            var state2 = getInternalState(that);
            var entry = getEntry(that, key);
            var previous, index2;
            if (entry) {
              entry.value = value;
            } else {
              state2.last = entry = {
                index: index2 = fastKey(key, true),
                key,
                value,
                previous: previous = state2.last,
                next: null,
                removed: false
              };
              if (!state2.first) state2.first = entry;
              if (previous) previous.next = entry;
              if (DESCRIPTORS) state2.size++;
              else that.size++;
              if (index2 !== "F") state2.index[index2] = entry;
            }
            return that;
          };
          var getEntry = function(that, key) {
            var state2 = getInternalState(that);
            var index2 = fastKey(key);
            var entry;
            if (index2 !== "F") return state2.index[index2];
            for (entry = state2.first; entry; entry = entry.next) {
              if (entry.key === key) return entry;
            }
          };
          defineBuiltIns(Prototype, {
            // `{ Map, Set }.prototype.clear()` methods
            // https://tc39.es/ecma262/#sec-map.prototype.clear
            // https://tc39.es/ecma262/#sec-set.prototype.clear
            clear: function clear() {
              var that = this;
              var state2 = getInternalState(that);
              var entry = state2.first;
              while (entry) {
                entry.removed = true;
                if (entry.previous) entry.previous = entry.previous.next = null;
                entry = entry.next;
              }
              state2.first = state2.last = null;
              state2.index = create(null);
              if (DESCRIPTORS) state2.size = 0;
              else that.size = 0;
            },
            // `{ Map, Set }.prototype.delete(key)` methods
            // https://tc39.es/ecma262/#sec-map.prototype.delete
            // https://tc39.es/ecma262/#sec-set.prototype.delete
            "delete": function(key) {
              var that = this;
              var state2 = getInternalState(that);
              var entry = getEntry(that, key);
              if (entry) {
                var next = entry.next;
                var prev = entry.previous;
                delete state2.index[entry.index];
                entry.removed = true;
                if (prev) prev.next = next;
                if (next) next.previous = prev;
                if (state2.first === entry) state2.first = next;
                if (state2.last === entry) state2.last = prev;
                if (DESCRIPTORS) state2.size--;
                else that.size--;
              }
              return !!entry;
            },
            // `{ Map, Set }.prototype.forEach(callbackfn, thisArg = undefined)` methods
            // https://tc39.es/ecma262/#sec-map.prototype.foreach
            // https://tc39.es/ecma262/#sec-set.prototype.foreach
            forEach: function forEach(callbackfn) {
              var state2 = getInternalState(this);
              var boundFunction = bind(callbackfn, arguments.length > 1 ? arguments[1] : void 0);
              var entry;
              while (entry = entry ? entry.next : state2.first) {
                boundFunction(entry.value, entry.key, this);
                while (entry && entry.removed) entry = entry.previous;
              }
            },
            // `{ Map, Set}.prototype.has(key)` methods
            // https://tc39.es/ecma262/#sec-map.prototype.has
            // https://tc39.es/ecma262/#sec-set.prototype.has
            has: function has(key) {
              return !!getEntry(this, key);
            }
          });
          defineBuiltIns(Prototype, IS_MAP ? {
            // `Map.prototype.get(key)` method
            // https://tc39.es/ecma262/#sec-map.prototype.get
            get: function get(key) {
              var entry = getEntry(this, key);
              return entry && entry.value;
            },
            // `Map.prototype.set(key, value)` method
            // https://tc39.es/ecma262/#sec-map.prototype.set
            set: function set(key, value) {
              return define(this, key === 0 ? 0 : key, value);
            }
          } : {
            // `Set.prototype.add(value)` method
            // https://tc39.es/ecma262/#sec-set.prototype.add
            add: function add(value) {
              return define(this, value = value === 0 ? 0 : value, value);
            }
          });
          if (DESCRIPTORS) defineBuiltInAccessor(Prototype, "size", {
            configurable: true,
            get: function() {
              return getInternalState(this).size;
            }
          });
          return Constructor;
        },
        setStrong: function(Constructor, CONSTRUCTOR_NAME, IS_MAP) {
          var ITERATOR_NAME = CONSTRUCTOR_NAME + " Iterator";
          var getInternalCollectionState = internalStateGetterFor(CONSTRUCTOR_NAME);
          var getInternalIteratorState = internalStateGetterFor(ITERATOR_NAME);
          defineIterator(Constructor, CONSTRUCTOR_NAME, function(iterated, kind) {
            setInternalState(this, {
              type: ITERATOR_NAME,
              target: iterated,
              state: getInternalCollectionState(iterated),
              kind,
              last: null
            });
          }, function() {
            var state2 = getInternalIteratorState(this);
            var kind = state2.kind;
            var entry = state2.last;
            while (entry && entry.removed) entry = entry.previous;
            if (!state2.target || !(state2.last = entry = entry ? entry.next : state2.state.first)) {
              state2.target = null;
              return createIterResultObject(void 0, true);
            }
            if (kind === "keys") return createIterResultObject(entry.key, false);
            if (kind === "values") return createIterResultObject(entry.value, false);
            return createIterResultObject([entry.key, entry.value], false);
          }, IS_MAP ? "entries" : "values", !IS_MAP, true);
          setSpecies(CONSTRUCTOR_NAME);
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.constructor.js
  var require_es_map_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.constructor.js"() {
      "use strict";
      var collection = require_collection();
      var collectionStrong = require_collection_strong();
      collection("Map", function(init) {
        return function Map2() {
          return init(this, arguments.length ? arguments[0] : void 0);
        };
      }, collectionStrong);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.js
  var require_es_map = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.js"() {
      "use strict";
      require_es_map_constructor();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/map-helpers.js
  var require_map_helpers = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/map-helpers.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var MapPrototype = Map.prototype;
      module.exports = {
        // eslint-disable-next-line es/no-map -- safe
        Map,
        set: uncurryThis(MapPrototype.set),
        get: uncurryThis(MapPrototype.get),
        has: uncurryThis(MapPrototype.has),
        remove: uncurryThis(MapPrototype["delete"]),
        proto: MapPrototype
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.group-by.js
  var require_es_map_group_by = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.map.group-by.js"() {
      "use strict";
      var $ = require_export();
      var uncurryThis = require_function_uncurry_this();
      var aCallable = require_a_callable();
      var requireObjectCoercible = require_require_object_coercible();
      var iterate = require_iterate();
      var MapHelpers = require_map_helpers();
      var IS_PURE = require_is_pure();
      var fails = require_fails();
      var Map2 = MapHelpers.Map;
      var has = MapHelpers.has;
      var get = MapHelpers.get;
      var set = MapHelpers.set;
      var push = uncurryThis([].push);
      var DOES_NOT_WORK_WITH_PRIMITIVES = IS_PURE || fails(function() {
        return Map2.groupBy("ab", function(it) {
          return it;
        }).get("a").length !== 1;
      });
      $({ target: "Map", stat: true, forced: IS_PURE || DOES_NOT_WORK_WITH_PRIMITIVES }, {
        groupBy: function groupBy(items, callbackfn) {
          requireObjectCoercible(items);
          aCallable(callbackfn);
          var map = new Map2();
          var k = 0;
          iterate(items, function(value) {
            var key = callbackfn(value, k++);
            if (!has(map, key)) set(map, key, [value]);
            else push(get(map, key), value);
          });
          return map;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/map/group-by.js
  var require_group_by4 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/map/group-by.js"(exports, module) {
      "use strict";
      require_es_object_to_string();
      require_es_map();
      require_es_map_group_by();
      var path = require_path();
      module.exports = path.Map.groupBy;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/map/group-by.js
  var require_group_by5 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/map/group-by.js"(exports, module) {
      "use strict";
      var parent = require_group_by4();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.map.group-by.js
  var require_esnext_map_group_by = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/esnext.map.group-by.js"() {
      "use strict";
      require_es_map_group_by();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/map/group-by.js
  var require_group_by6 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/map/group-by.js"(exports, module) {
      "use strict";
      var parent = require_group_by5();
      require_esnext_map_group_by();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.global-this.js
  var require_es_global_this = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.global-this.js"() {
      "use strict";
      var $ = require_export();
      var globalThis2 = require_global_this();
      $({ global: true, forced: globalThis2.globalThis !== globalThis2 }, {
        globalThis: globalThis2
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/global-this.js
  var require_global_this2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/global-this.js"(exports, module) {
      "use strict";
      require_es_global_this();
      module.exports = require_global_this();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/global-this.js
  var require_global_this3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/global-this.js"(exports, module) {
      "use strict";
      var parent = require_global_this2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/global-this.js
  var require_global_this4 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/global-this.js"(exports, module) {
      "use strict";
      var parent = require_global_this3();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-string.js
  var require_to_string = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/to-string.js"(exports, module) {
      "use strict";
      var classof = require_classof();
      var $String = String;
      module.exports = function(argument) {
        if (classof(argument) === "Symbol") throw new TypeError("Cannot convert a Symbol value to a string");
        return $String(argument);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-flags.js
  var require_regexp_flags = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-flags.js"(exports, module) {
      "use strict";
      var anObject = require_an_object();
      module.exports = function() {
        var that = anObject(this);
        var result = "";
        if (that.hasIndices) result += "d";
        if (that.global) result += "g";
        if (that.ignoreCase) result += "i";
        if (that.multiline) result += "m";
        if (that.dotAll) result += "s";
        if (that.unicode) result += "u";
        if (that.unicodeSets) result += "v";
        if (that.sticky) result += "y";
        return result;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-sticky-helpers.js
  var require_regexp_sticky_helpers = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-sticky-helpers.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var globalThis2 = require_global_this();
      var $RegExp = globalThis2.RegExp;
      var UNSUPPORTED_Y = fails(function() {
        var re = $RegExp("a", "y");
        re.lastIndex = 2;
        return re.exec("abcd") !== null;
      });
      var MISSED_STICKY = UNSUPPORTED_Y || fails(function() {
        return !$RegExp("a", "y").sticky;
      });
      var BROKEN_CARET = UNSUPPORTED_Y || fails(function() {
        var re = $RegExp("^r", "gy");
        re.lastIndex = 2;
        return re.exec("str") !== null;
      });
      module.exports = {
        BROKEN_CARET,
        MISSED_STICKY,
        UNSUPPORTED_Y
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-unsupported-dot-all.js
  var require_regexp_unsupported_dot_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-unsupported-dot-all.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var globalThis2 = require_global_this();
      var $RegExp = globalThis2.RegExp;
      module.exports = fails(function() {
        var re = $RegExp(".", "s");
        return !(re.dotAll && re.test("\n") && re.flags === "s");
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-unsupported-ncg.js
  var require_regexp_unsupported_ncg = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-unsupported-ncg.js"(exports, module) {
      "use strict";
      var fails = require_fails();
      var globalThis2 = require_global_this();
      var $RegExp = globalThis2.RegExp;
      module.exports = fails(function() {
        var re = $RegExp("(?<a>b)", "g");
        return re.exec("b").groups.a !== "b" || "b".replace(re, "$<a>c") !== "bc";
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-exec.js
  var require_regexp_exec = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-exec.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var uncurryThis = require_function_uncurry_this();
      var toString2 = require_to_string();
      var regexpFlags = require_regexp_flags();
      var stickyHelpers = require_regexp_sticky_helpers();
      var shared = require_shared();
      var create = require_object_create();
      var getInternalState = require_internal_state().get;
      var UNSUPPORTED_DOT_ALL = require_regexp_unsupported_dot_all();
      var UNSUPPORTED_NCG = require_regexp_unsupported_ncg();
      var nativeReplace = shared("native-string-replace", String.prototype.replace);
      var nativeExec = RegExp.prototype.exec;
      var patchedExec = nativeExec;
      var charAt = uncurryThis("".charAt);
      var indexOf = uncurryThis("".indexOf);
      var replace = uncurryThis("".replace);
      var stringSlice = uncurryThis("".slice);
      var UPDATES_LAST_INDEX_WRONG = (function() {
        var re1 = /a/;
        var re2 = /b*/g;
        call(nativeExec, re1, "a");
        call(nativeExec, re2, "a");
        return re1.lastIndex !== 0 || re2.lastIndex !== 0;
      })();
      var UNSUPPORTED_Y = stickyHelpers.BROKEN_CARET;
      var NPCG_INCLUDED = /()??/.exec("")[1] !== void 0;
      var PATCH = UPDATES_LAST_INDEX_WRONG || NPCG_INCLUDED || UNSUPPORTED_Y || UNSUPPORTED_DOT_ALL || UNSUPPORTED_NCG;
      if (PATCH) {
        patchedExec = function exec(string) {
          var re = this;
          var state2 = getInternalState(re);
          var str = toString2(string);
          var raw = state2.raw;
          var result, reCopy, lastIndex, match, i, object, group;
          if (raw) {
            raw.lastIndex = re.lastIndex;
            result = call(patchedExec, raw, str);
            re.lastIndex = raw.lastIndex;
            return result;
          }
          var groups = state2.groups;
          var sticky = UNSUPPORTED_Y && re.sticky;
          var flags = call(regexpFlags, re);
          var source = re.source;
          var charsAdded = 0;
          var strCopy = str;
          if (sticky) {
            flags = replace(flags, "y", "");
            if (indexOf(flags, "g") === -1) {
              flags += "g";
            }
            strCopy = stringSlice(str, re.lastIndex);
            if (re.lastIndex > 0 && (!re.multiline || re.multiline && charAt(str, re.lastIndex - 1) !== "\n")) {
              source = "(?: " + source + ")";
              strCopy = " " + strCopy;
              charsAdded++;
            }
            reCopy = new RegExp("^(?:" + source + ")", flags);
          }
          if (NPCG_INCLUDED) {
            reCopy = new RegExp("^" + source + "$(?!\\s)", flags);
          }
          if (UPDATES_LAST_INDEX_WRONG) lastIndex = re.lastIndex;
          match = call(nativeExec, sticky ? reCopy : re, strCopy);
          if (sticky) {
            if (match) {
              match.input = stringSlice(match.input, charsAdded);
              match[0] = stringSlice(match[0], charsAdded);
              match.index = re.lastIndex;
              re.lastIndex += match[0].length;
            } else re.lastIndex = 0;
          } else if (UPDATES_LAST_INDEX_WRONG && match) {
            re.lastIndex = re.global ? match.index + match[0].length : lastIndex;
          }
          if (NPCG_INCLUDED && match && match.length > 1) {
            call(nativeReplace, match[0], reCopy, function() {
              for (i = 1; i < arguments.length - 2; i++) {
                if (arguments[i] === void 0) match[i] = void 0;
              }
            });
          }
          if (match && groups) {
            match.groups = object = create(null);
            for (i = 0; i < groups.length; i++) {
              group = groups[i];
              object[group[0]] = match[group[1]];
            }
          }
          return match;
        };
      }
      module.exports = patchedExec;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.regexp.exec.js
  var require_es_regexp_exec = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.regexp.exec.js"() {
      "use strict";
      var $ = require_export();
      var exec = require_regexp_exec();
      $({ target: "RegExp", proto: true, forced: /./.exec !== exec }, {
        exec
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-regexp.js
  var require_is_regexp = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/is-regexp.js"(exports, module) {
      "use strict";
      var isObject = require_is_object();
      var classof = require_classof_raw();
      var wellKnownSymbol = require_well_known_symbol();
      var MATCH = wellKnownSymbol("match");
      module.exports = function(it) {
        var isRegExp;
        return isObject(it) && ((isRegExp = it[MATCH]) !== void 0 ? !!isRegExp : classof(it) === "RegExp");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-flags-detection.js
  var require_regexp_flags_detection = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-flags-detection.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var fails = require_fails();
      var RegExp2 = globalThis2.RegExp;
      var FLAGS_GETTER_IS_CORRECT = !fails(function() {
        var INDICES_SUPPORT = true;
        try {
          RegExp2(".", "d");
        } catch (error) {
          INDICES_SUPPORT = false;
        }
        var O = {};
        var calls = "";
        var expected = INDICES_SUPPORT ? "dgimsy" : "gimsy";
        var addGetter = function(key2, chr) {
          Object.defineProperty(O, key2, { get: function() {
            calls += chr;
            return true;
          } });
        };
        var pairs = {
          dotAll: "s",
          global: "g",
          ignoreCase: "i",
          multiline: "m",
          sticky: "y"
        };
        if (INDICES_SUPPORT) pairs.hasIndices = "d";
        for (var key in pairs) addGetter(key, pairs[key]);
        var result = Object.getOwnPropertyDescriptor(RegExp2.prototype, "flags").get.call(O);
        return result !== expected || calls !== expected;
      });
      module.exports = { correct: FLAGS_GETTER_IS_CORRECT };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-get-flags.js
  var require_regexp_get_flags = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-get-flags.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var hasOwn = require_has_own_property();
      var isPrototypeOf = require_object_is_prototype_of();
      var regExpFlagsDetection = require_regexp_flags_detection();
      var regExpFlagsGetterImplementation = require_regexp_flags();
      var RegExpPrototype = RegExp.prototype;
      module.exports = regExpFlagsDetection.correct ? function(it) {
        return it.flags;
      } : function(it) {
        return !regExpFlagsDetection.correct && isPrototypeOf(RegExpPrototype, it) && !hasOwn(it, "flags") ? call(regExpFlagsGetterImplementation, it) : it.flags;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-constructor.js
  var require_a_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/a-constructor.js"(exports, module) {
      "use strict";
      var isConstructor = require_is_constructor();
      var tryToString = require_try_to_string();
      var $TypeError = TypeError;
      module.exports = function(argument) {
        if (isConstructor(argument)) return argument;
        throw new $TypeError(tryToString(argument) + " is not a constructor");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/species-constructor.js
  var require_species_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/species-constructor.js"(exports, module) {
      "use strict";
      var anObject = require_an_object();
      var aConstructor = require_a_constructor();
      var isNullOrUndefined = require_is_null_or_undefined();
      var wellKnownSymbol = require_well_known_symbol();
      var SPECIES = wellKnownSymbol("species");
      module.exports = function(O, defaultConstructor) {
        var C = anObject(O).constructor;
        var S;
        return C === void 0 || isNullOrUndefined(S = anObject(C)[SPECIES]) ? defaultConstructor : aConstructor(S);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-multibyte.js
  var require_string_multibyte = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-multibyte.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var toString2 = require_to_string();
      var requireObjectCoercible = require_require_object_coercible();
      var charAt = uncurryThis("".charAt);
      var charCodeAt = uncurryThis("".charCodeAt);
      var stringSlice = uncurryThis("".slice);
      var createMethod = function(CONVERT_TO_STRING) {
        return function($this, pos) {
          var S = toString2(requireObjectCoercible($this));
          var position = toIntegerOrInfinity(pos);
          var size = S.length;
          var first, second;
          if (position < 0 || position >= size) return CONVERT_TO_STRING ? "" : void 0;
          first = charCodeAt(S, position);
          return first < 55296 || first > 56319 || position + 1 === size || (second = charCodeAt(S, position + 1)) < 56320 || second > 57343 ? CONVERT_TO_STRING ? charAt(S, position) : first : CONVERT_TO_STRING ? stringSlice(S, position, position + 2) : (first - 55296 << 10) + (second - 56320) + 65536;
        };
      };
      module.exports = {
        // `String.prototype.codePointAt` method
        // https://tc39.es/ecma262/#sec-string.prototype.codepointat
        codeAt: createMethod(false),
        // `String.prototype.at` method
        // https://github.com/mathiasbynens/String.prototype.at
        charAt: createMethod(true)
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/advance-string-index.js
  var require_advance_string_index = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/advance-string-index.js"(exports, module) {
      "use strict";
      var charAt = require_string_multibyte().charAt;
      module.exports = function(S, index2, unicode) {
        return index2 + (unicode ? charAt(S, index2).length : 1);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-exec-abstract.js
  var require_regexp_exec_abstract = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/regexp-exec-abstract.js"(exports, module) {
      "use strict";
      var call = require_function_call();
      var anObject = require_an_object();
      var isCallable = require_is_callable();
      var classof = require_classof_raw();
      var regexpExec = require_regexp_exec();
      var $TypeError = TypeError;
      module.exports = function(R, S) {
        var exec = R.exec;
        if (isCallable(exec)) {
          var result = call(exec, R, S);
          if (result !== null) anObject(result);
          return result;
        }
        if (classof(R) === "RegExp") return call(regexpExec, R, S);
        throw new $TypeError("RegExp#exec called on incompatible receiver");
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.match-all.js
  var require_es_string_match_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.match-all.js"() {
      "use strict";
      var $ = require_export();
      var call = require_function_call();
      var uncurryThis = require_function_uncurry_this_clause();
      var createIteratorConstructor = require_iterator_create_constructor();
      var createIterResultObject = require_create_iter_result_object();
      var requireObjectCoercible = require_require_object_coercible();
      var toLength = require_to_length();
      var toString2 = require_to_string();
      var anObject = require_an_object();
      var isObject = require_is_object();
      var classof = require_classof_raw();
      var isRegExp = require_is_regexp();
      var getRegExpFlags = require_regexp_get_flags();
      var getMethod = require_get_method();
      var defineBuiltIn = require_define_built_in();
      var fails = require_fails();
      var wellKnownSymbol = require_well_known_symbol();
      var speciesConstructor = require_species_constructor();
      var advanceStringIndex = require_advance_string_index();
      var regExpExec = require_regexp_exec_abstract();
      var InternalStateModule = require_internal_state();
      var IS_PURE = require_is_pure();
      var MATCH_ALL = wellKnownSymbol("matchAll");
      var REGEXP_STRING = "RegExp String";
      var REGEXP_STRING_ITERATOR = REGEXP_STRING + " Iterator";
      var setInternalState = InternalStateModule.set;
      var getInternalState = InternalStateModule.getterFor(REGEXP_STRING_ITERATOR);
      var RegExpPrototype = RegExp.prototype;
      var $TypeError = TypeError;
      var stringIndexOf = uncurryThis("".indexOf);
      var nativeMatchAll = uncurryThis("".matchAll);
      var WORKS_WITH_NON_GLOBAL_REGEX = !!nativeMatchAll && !fails(function() {
        nativeMatchAll("a", /./);
      });
      var $RegExpStringIterator = createIteratorConstructor(function RegExpStringIterator(regexp, string, $global, fullUnicode) {
        setInternalState(this, {
          type: REGEXP_STRING_ITERATOR,
          regexp,
          string,
          global: $global,
          unicode: fullUnicode,
          done: false
        });
      }, REGEXP_STRING, function next() {
        var state2 = getInternalState(this);
        if (state2.done) return createIterResultObject(void 0, true);
        var R = state2.regexp;
        var S = state2.string;
        var match = regExpExec(R, S);
        if (match === null) {
          state2.done = true;
          return createIterResultObject(void 0, true);
        }
        if (state2.global) {
          if (toString2(match[0]) === "") R.lastIndex = advanceStringIndex(S, toLength(R.lastIndex), state2.unicode);
          return createIterResultObject(match, false);
        }
        state2.done = true;
        return createIterResultObject(match, false);
      });
      var $matchAll = function(string) {
        var R = anObject(this);
        var S = toString2(string);
        var C = speciesConstructor(R, RegExp);
        var flags = toString2(getRegExpFlags(R));
        var matcher, $global, fullUnicode;
        matcher = new C(C === RegExp ? R.source : R, flags);
        $global = !!~stringIndexOf(flags, "g");
        fullUnicode = !!~stringIndexOf(flags, "u");
        matcher.lastIndex = toLength(R.lastIndex);
        return new $RegExpStringIterator(matcher, S, $global, fullUnicode);
      };
      $({ target: "String", proto: true, forced: WORKS_WITH_NON_GLOBAL_REGEX }, {
        matchAll: function matchAll(regexp) {
          var O = requireObjectCoercible(this);
          var flags, S, matcher, rx;
          if (isObject(regexp)) {
            if (isRegExp(regexp)) {
              flags = toString2(requireObjectCoercible(getRegExpFlags(regexp)));
              if (!~stringIndexOf(flags, "g")) throw new $TypeError("`.matchAll` does not allow non-global regexes");
            }
            if (WORKS_WITH_NON_GLOBAL_REGEX) return nativeMatchAll(O, regexp);
            matcher = getMethod(regexp, MATCH_ALL);
            if (matcher === void 0 && IS_PURE && classof(regexp) === "RegExp") matcher = $matchAll;
            if (matcher) return call(matcher, regexp, O);
          } else if (WORKS_WITH_NON_GLOBAL_REGEX) return nativeMatchAll(O, regexp);
          S = toString2(O);
          rx = new RegExp(regexp, "g");
          return IS_PURE ? call($matchAll, rx, S) : rx[MATCH_ALL](S);
        }
      });
      IS_PURE || MATCH_ALL in RegExpPrototype || defineBuiltIn(RegExpPrototype, MATCH_ALL, $matchAll);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/match-all.js
  var require_match_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/match-all.js"(exports, module) {
      "use strict";
      require_es_object_to_string();
      require_es_regexp_exec();
      require_es_string_match_all();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("String", "matchAll");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/match-all.js
  var require_match_all2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/match-all.js"(exports, module) {
      "use strict";
      var parent = require_match_all();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/match-all.js
  var require_match_all3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/match-all.js"(exports, module) {
      "use strict";
      var parent = require_match_all2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-apply.js
  var require_function_apply = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/function-apply.js"(exports, module) {
      "use strict";
      var NATIVE_BIND = require_function_bind_native();
      var FunctionPrototype = Function.prototype;
      var apply = FunctionPrototype.apply;
      var call = FunctionPrototype.call;
      module.exports = typeof Reflect == "object" && Reflect.apply || (NATIVE_BIND ? call.bind(apply) : function() {
        return call.apply(apply, arguments);
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/fix-regexp-well-known-symbol-logic.js
  var require_fix_regexp_well_known_symbol_logic = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/fix-regexp-well-known-symbol-logic.js"(exports, module) {
      "use strict";
      require_es_regexp_exec();
      var call = require_function_call();
      var defineBuiltIn = require_define_built_in();
      var regexpExec = require_regexp_exec();
      var fails = require_fails();
      var wellKnownSymbol = require_well_known_symbol();
      var createNonEnumerableProperty = require_create_non_enumerable_property();
      var SPECIES = wellKnownSymbol("species");
      var RegExpPrototype = RegExp.prototype;
      module.exports = function(KEY, exec, FORCED, SHAM) {
        var SYMBOL = wellKnownSymbol(KEY);
        var DELEGATES_TO_SYMBOL = !fails(function() {
          var O = {};
          O[SYMBOL] = function() {
            return 7;
          };
          return ""[KEY](O) !== 7;
        });
        var DELEGATES_TO_EXEC = DELEGATES_TO_SYMBOL && !fails(function() {
          var execCalled = false;
          var re = /a/;
          if (KEY === "split") {
            re = {};
            re.constructor = {};
            re.constructor[SPECIES] = function() {
              return re;
            };
            re.flags = "";
            re[SYMBOL] = /./[SYMBOL];
          }
          re.exec = function() {
            execCalled = true;
            return null;
          };
          re[SYMBOL]("");
          return !execCalled;
        });
        if (!DELEGATES_TO_SYMBOL || !DELEGATES_TO_EXEC || FORCED) {
          var nativeRegExpMethod = /./[SYMBOL];
          var methods = exec(SYMBOL, ""[KEY], function(nativeMethod, regexp, str, arg2, forceStringMethod) {
            var $exec = regexp.exec;
            if ($exec === regexpExec || $exec === RegExpPrototype.exec) {
              if (DELEGATES_TO_SYMBOL && !forceStringMethod) {
                return { done: true, value: call(nativeRegExpMethod, regexp, str, arg2) };
              }
              return { done: true, value: call(nativeMethod, str, regexp, arg2) };
            }
            return { done: false };
          });
          defineBuiltIn(String.prototype, KEY, methods[0]);
          defineBuiltIn(RegExpPrototype, SYMBOL, methods[1]);
        }
        if (SHAM) createNonEnumerableProperty(RegExpPrototype[SYMBOL], "sham", true);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-substitution.js
  var require_get_substitution = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/get-substitution.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var toObject = require_to_object();
      var floor = Math.floor;
      var charAt = uncurryThis("".charAt);
      var replace = uncurryThis("".replace);
      var stringSlice = uncurryThis("".slice);
      var SUBSTITUTION_SYMBOLS = /\$([$&'`]|\d{1,2}|<[^>]*>)/g;
      var SUBSTITUTION_SYMBOLS_NO_NAMED = /\$([$&'`]|\d{1,2})/g;
      module.exports = function(matched, str, position, captures, namedCaptures, replacement) {
        var tailPos = position + matched.length;
        var m = captures.length;
        var symbols = SUBSTITUTION_SYMBOLS_NO_NAMED;
        if (namedCaptures !== void 0) {
          namedCaptures = toObject(namedCaptures);
          symbols = SUBSTITUTION_SYMBOLS;
        }
        return replace(replacement, symbols, function(match, ch) {
          var capture;
          switch (charAt(ch, 0)) {
            case "$":
              return "$";
            case "&":
              return matched;
            case "`":
              return stringSlice(str, 0, position);
            case "'":
              return stringSlice(str, tailPos);
            case "<":
              capture = namedCaptures[stringSlice(ch, 1, -1)];
              break;
            default:
              var n = +ch;
              if (n === 0) return match;
              if (n > m) {
                var f = floor(n / 10);
                if (f === 0) return match;
                if (f <= m) return captures[f - 1] === void 0 ? charAt(ch, 1) : captures[f - 1] + charAt(ch, 1);
                return match;
              }
              capture = captures[n - 1];
          }
          return capture === void 0 ? "" : capture;
        });
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.replace.js
  var require_es_string_replace = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.replace.js"() {
      "use strict";
      var apply = require_function_apply();
      var call = require_function_call();
      var uncurryThis = require_function_uncurry_this();
      var fixRegExpWellKnownSymbolLogic = require_fix_regexp_well_known_symbol_logic();
      var fails = require_fails();
      var anObject = require_an_object();
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var toIntegerOrInfinity = require_to_integer_or_infinity();
      var toLength = require_to_length();
      var toString2 = require_to_string();
      var requireObjectCoercible = require_require_object_coercible();
      var advanceStringIndex = require_advance_string_index();
      var getMethod = require_get_method();
      var getSubstitution = require_get_substitution();
      var getRegExpFlags = require_regexp_get_flags();
      var regExpExec = require_regexp_exec_abstract();
      var wellKnownSymbol = require_well_known_symbol();
      var REPLACE = wellKnownSymbol("replace");
      var max2 = Math.max;
      var min = Math.min;
      var concat = uncurryThis([].concat);
      var push = uncurryThis([].push);
      var stringIndexOf = uncurryThis("".indexOf);
      var stringSlice = uncurryThis("".slice);
      var maybeToString = function(it) {
        return it === void 0 ? it : String(it);
      };
      var REPLACE_KEEPS_$0 = (function() {
        return "a".replace(/./, "$0") === "$0";
      })();
      var REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE = (function() {
        if (/./[REPLACE]) {
          return /./[REPLACE]("a", "$0") === "";
        }
        return false;
      })();
      var REPLACE_SUPPORTS_NAMED_GROUPS = !fails(function() {
        var re = /./;
        re.exec = function() {
          var result = [];
          result.groups = { a: "7" };
          return result;
        };
        return "".replace(re, "$<a>") !== "7";
      });
      fixRegExpWellKnownSymbolLogic("replace", function(_, nativeReplace, maybeCallNative) {
        var UNSAFE_SUBSTITUTE = REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE ? "$" : "$0";
        return [
          // `String.prototype.replace` method
          // https://tc39.es/ecma262/#sec-string.prototype.replace
          function replace(searchValue, replaceValue) {
            var O = requireObjectCoercible(this);
            var replacer = isObject(searchValue) ? getMethod(searchValue, REPLACE) : void 0;
            return replacer ? call(replacer, searchValue, O, replaceValue) : call(nativeReplace, toString2(O), searchValue, replaceValue);
          },
          // `RegExp.prototype[@@replace]` method
          // https://tc39.es/ecma262/#sec-regexp.prototype-@@replace
          function(string, replaceValue) {
            var rx = anObject(this);
            var S = toString2(string);
            if (typeof replaceValue == "string" && stringIndexOf(replaceValue, UNSAFE_SUBSTITUTE) === -1 && stringIndexOf(replaceValue, "$<") === -1) {
              var res = maybeCallNative(nativeReplace, rx, S, replaceValue);
              if (res.done) return res.value;
            }
            var functionalReplace = isCallable(replaceValue);
            if (!functionalReplace) replaceValue = toString2(replaceValue);
            var flags = toString2(getRegExpFlags(rx));
            var global2 = stringIndexOf(flags, "g") !== -1;
            var fullUnicode;
            if (global2) {
              fullUnicode = stringIndexOf(flags, "u") !== -1;
              rx.lastIndex = 0;
            }
            var results = [];
            var result;
            while (true) {
              result = regExpExec(rx, S);
              if (result === null) break;
              push(results, result);
              if (!global2) break;
              var matchStr = toString2(result[0]);
              if (matchStr === "") rx.lastIndex = advanceStringIndex(S, toLength(rx.lastIndex), fullUnicode);
            }
            var accumulatedResult = "";
            var nextSourcePosition = 0;
            for (var i = 0; i < results.length; i++) {
              result = results[i];
              var matched = toString2(result[0]);
              var position = max2(min(toIntegerOrInfinity(result.index), S.length), 0);
              var captures = [];
              var replacement;
              for (var j = 1; j < result.length; j++) push(captures, maybeToString(result[j]));
              var namedCaptures = result.groups;
              if (functionalReplace) {
                var replacerArgs = concat([matched], captures, position, S);
                if (namedCaptures !== void 0) push(replacerArgs, namedCaptures);
                replacement = toString2(apply(replaceValue, void 0, replacerArgs));
              } else {
                replacement = getSubstitution(matched, S, position, captures, namedCaptures, replaceValue);
              }
              if (position >= nextSourcePosition) {
                accumulatedResult += stringSlice(S, nextSourcePosition, position) + replacement;
                nextSourcePosition = position + matched.length;
              }
            }
            return accumulatedResult + stringSlice(S, nextSourcePosition);
          }
        ];
      }, !REPLACE_SUPPORTS_NAMED_GROUPS || !REPLACE_KEEPS_$0 || REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.replace-all.js
  var require_es_string_replace_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.replace-all.js"() {
      "use strict";
      var $ = require_export();
      var call = require_function_call();
      var uncurryThis = require_function_uncurry_this();
      var requireObjectCoercible = require_require_object_coercible();
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var isRegExp = require_is_regexp();
      var toString2 = require_to_string();
      var getMethod = require_get_method();
      var getRegExpFlags = require_regexp_get_flags();
      var getSubstitution = require_get_substitution();
      var wellKnownSymbol = require_well_known_symbol();
      var IS_PURE = require_is_pure();
      var REPLACE = wellKnownSymbol("replace");
      var $TypeError = TypeError;
      var indexOf = uncurryThis("".indexOf);
      var replace = uncurryThis("".replace);
      var stringSlice = uncurryThis("".slice);
      var max2 = Math.max;
      $({ target: "String", proto: true }, {
        replaceAll: function replaceAll(searchValue, replaceValue) {
          var O = requireObjectCoercible(this);
          var IS_REG_EXP, flags, replacer, string, searchString, functionalReplace, searchLength, advanceBy, position, replacement;
          var endOfLastMatch = 0;
          var result = "";
          if (isObject(searchValue)) {
            IS_REG_EXP = isRegExp(searchValue);
            if (IS_REG_EXP) {
              flags = toString2(requireObjectCoercible(getRegExpFlags(searchValue)));
              if (!~indexOf(flags, "g")) throw new $TypeError("`.replaceAll` does not allow non-global regexes");
            }
            replacer = getMethod(searchValue, REPLACE);
            if (replacer) return call(replacer, searchValue, O, replaceValue);
            if (IS_PURE && IS_REG_EXP) return replace(toString2(O), searchValue, replaceValue);
          }
          string = toString2(O);
          searchString = toString2(searchValue);
          functionalReplace = isCallable(replaceValue);
          if (!functionalReplace) replaceValue = toString2(replaceValue);
          searchLength = searchString.length;
          advanceBy = max2(1, searchLength);
          position = indexOf(string, searchString);
          while (position !== -1) {
            replacement = functionalReplace ? toString2(replaceValue(searchString, position, string)) : getSubstitution(searchString, string, position, [], void 0, replaceValue);
            result += stringSlice(string, endOfLastMatch, position) + replacement;
            endOfLastMatch = position + searchLength;
            position = position + advanceBy > string.length ? -1 : indexOf(string, searchString, position + advanceBy);
          }
          if (endOfLastMatch < string.length) {
            result += stringSlice(string, endOfLastMatch);
          }
          return result;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/replace-all.js
  var require_replace_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/replace-all.js"(exports, module) {
      "use strict";
      require_es_regexp_exec();
      require_es_string_replace();
      require_es_string_replace_all();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("String", "replaceAll");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/replace-all.js
  var require_replace_all2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/replace-all.js"(exports, module) {
      "use strict";
      var parent = require_replace_all();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/replace-all.js
  var require_replace_all3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/replace-all.js"(exports, module) {
      "use strict";
      var parent = require_replace_all2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/whitespaces.js
  var require_whitespaces = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/whitespaces.js"(exports, module) {
      "use strict";
      module.exports = "	\n\v\f\r \xA0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u202F\u205F\u3000\u2028\u2029\uFEFF";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim.js
  var require_string_trim = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim.js"(exports, module) {
      "use strict";
      var uncurryThis = require_function_uncurry_this();
      var requireObjectCoercible = require_require_object_coercible();
      var toString2 = require_to_string();
      var whitespaces = require_whitespaces();
      var replace = uncurryThis("".replace);
      var ltrim = RegExp("^[" + whitespaces + "]+");
      var rtrim = RegExp("(^|[^" + whitespaces + "])[" + whitespaces + "]+$");
      var createMethod = function(TYPE) {
        return function($this) {
          var string = toString2(requireObjectCoercible($this));
          if (TYPE & 1) string = replace(string, ltrim, "");
          if (TYPE & 2) string = replace(string, rtrim, "$1");
          return string;
        };
      };
      module.exports = {
        // `String.prototype.{ trimLeft, trimStart }` methods
        // https://tc39.es/ecma262/#sec-string.prototype.trimstart
        start: createMethod(1),
        // `String.prototype.{ trimRight, trimEnd }` methods
        // https://tc39.es/ecma262/#sec-string.prototype.trimend
        end: createMethod(2),
        // `String.prototype.trim` method
        // https://tc39.es/ecma262/#sec-string.prototype.trim
        trim: createMethod(3)
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-forced.js
  var require_string_trim_forced = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-forced.js"(exports, module) {
      "use strict";
      var PROPER_FUNCTION_NAME = require_function_name().PROPER;
      var fails = require_fails();
      var whitespaces = require_whitespaces();
      var non = "\u200B\x85\u180E";
      module.exports = function(METHOD_NAME) {
        return fails(function() {
          return !!whitespaces[METHOD_NAME]() || non[METHOD_NAME]() !== non || PROPER_FUNCTION_NAME && whitespaces[METHOD_NAME].name !== METHOD_NAME;
        });
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-start.js
  var require_string_trim_start = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-start.js"(exports, module) {
      "use strict";
      var $trimStart = require_string_trim().start;
      var forcedStringTrimMethod = require_string_trim_forced();
      module.exports = forcedStringTrimMethod("trimStart") ? function trimStart() {
        return $trimStart(this);
      } : "".trimStart;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-left.js
  var require_es_string_trim_left = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-left.js"() {
      "use strict";
      var $ = require_export();
      var trimStart = require_string_trim_start();
      $({ target: "String", proto: true, name: "trimStart", forced: "".trimLeft !== trimStart }, {
        trimLeft: trimStart
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-start.js
  var require_es_string_trim_start = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-start.js"() {
      "use strict";
      require_es_string_trim_left();
      var $ = require_export();
      var trimStart = require_string_trim_start();
      $({ target: "String", proto: true, name: "trimStart", forced: "".trimStart !== trimStart }, {
        trimStart
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/trim-start.js
  var require_trim_start = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/trim-start.js"(exports, module) {
      "use strict";
      require_es_string_trim_start();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("String", "trimLeft");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/trim-start.js
  var require_trim_start2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/trim-start.js"(exports, module) {
      "use strict";
      var parent = require_trim_start();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/trim-start.js
  var require_trim_start3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/trim-start.js"(exports, module) {
      "use strict";
      var parent = require_trim_start2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-end.js
  var require_string_trim_end = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/string-trim-end.js"(exports, module) {
      "use strict";
      var $trimEnd = require_string_trim().end;
      var forcedStringTrimMethod = require_string_trim_forced();
      module.exports = forcedStringTrimMethod("trimEnd") ? function trimEnd() {
        return $trimEnd(this);
      } : "".trimEnd;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-right.js
  var require_es_string_trim_right = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-right.js"() {
      "use strict";
      var $ = require_export();
      var trimEnd = require_string_trim_end();
      $({ target: "String", proto: true, name: "trimEnd", forced: "".trimRight !== trimEnd }, {
        trimRight: trimEnd
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-end.js
  var require_es_string_trim_end = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.string.trim-end.js"() {
      "use strict";
      require_es_string_trim_right();
      var $ = require_export();
      var trimEnd = require_string_trim_end();
      $({ target: "String", proto: true, name: "trimEnd", forced: "".trimEnd !== trimEnd }, {
        trimEnd
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/trim-end.js
  var require_trim_end = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/string/trim-end.js"(exports, module) {
      "use strict";
      require_es_string_trim_end();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("String", "trimRight");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/trim-end.js
  var require_trim_end2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/string/trim-end.js"(exports, module) {
      "use strict";
      var parent = require_trim_end();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/trim-end.js
  var require_trim_end3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/string/trim-end.js"(exports, module) {
      "use strict";
      var parent = require_trim_end2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol-wrapped.js
  var require_well_known_symbol_wrapped = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol-wrapped.js"(exports) {
      "use strict";
      var wellKnownSymbol = require_well_known_symbol();
      exports.f = wellKnownSymbol;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol-define.js
  var require_well_known_symbol_define = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/well-known-symbol-define.js"(exports, module) {
      "use strict";
      var path = require_path();
      var hasOwn = require_has_own_property();
      var wrappedWellKnownSymbolModule = require_well_known_symbol_wrapped();
      var defineProperty = require_object_define_property().f;
      module.exports = function(NAME) {
        var Symbol2 = path.Symbol || (path.Symbol = {});
        if (!hasOwn(Symbol2, NAME)) defineProperty(Symbol2, NAME, {
          value: wrappedWellKnownSymbolModule.f(NAME)
        });
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.symbol.async-iterator.js
  var require_es_symbol_async_iterator = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.symbol.async-iterator.js"() {
      "use strict";
      var defineWellKnownSymbol = require_well_known_symbol_define();
      defineWellKnownSymbol("asyncIterator");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/symbol/async-iterator.js
  var require_async_iterator = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/symbol/async-iterator.js"(exports, module) {
      "use strict";
      require_es_symbol_async_iterator();
      var WrappedWellKnownSymbolModule = require_well_known_symbol_wrapped();
      module.exports = WrappedWellKnownSymbolModule.f("asyncIterator");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/symbol/async-iterator.js
  var require_async_iterator2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/symbol/async-iterator.js"(exports, module) {
      "use strict";
      var parent = require_async_iterator();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/symbol/async-iterator.js
  var require_async_iterator3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/symbol/async-iterator.js"(exports, module) {
      "use strict";
      var parent = require_async_iterator2();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment.js
  var require_environment = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var userAgent = require_environment_user_agent();
      var classof = require_classof_raw();
      var userAgentStartsWith = function(string) {
        return userAgent.slice(0, string.length) === string;
      };
      module.exports = (function() {
        if (userAgentStartsWith("Bun/")) return "BUN";
        if (userAgentStartsWith("Cloudflare-Workers")) return "CLOUDFLARE";
        if (userAgentStartsWith("Deno/")) return "DENO";
        if (userAgentStartsWith("Node.js/")) return "NODE";
        if (globalThis2.Bun && typeof Bun.version == "string") return "BUN";
        if (globalThis2.Deno && typeof Deno.version == "object") return "DENO";
        if (classof(globalThis2.process) === "process") return "NODE";
        if (globalThis2.window && globalThis2.document) return "BROWSER";
        return "REST";
      })();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-node.js
  var require_environment_is_node = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-node.js"(exports, module) {
      "use strict";
      var ENVIRONMENT = require_environment();
      module.exports = ENVIRONMENT === "NODE";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/validate-arguments-length.js
  var require_validate_arguments_length = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/validate-arguments-length.js"(exports, module) {
      "use strict";
      var $TypeError = TypeError;
      module.exports = function(passed, required) {
        if (passed < required) throw new $TypeError("Not enough arguments");
        return passed;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-ios.js
  var require_environment_is_ios = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-ios.js"(exports, module) {
      "use strict";
      var userAgent = require_environment_user_agent();
      module.exports = /(?:ipad|iphone|ipod).*applewebkit/i.test(userAgent);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/task.js
  var require_task = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/task.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var apply = require_function_apply();
      var bind = require_function_bind_context();
      var isCallable = require_is_callable();
      var hasOwn = require_has_own_property();
      var fails = require_fails();
      var html = require_html();
      var arraySlice = require_array_slice();
      var createElement = require_document_create_element();
      var validateArgumentsLength = require_validate_arguments_length();
      var IS_IOS = require_environment_is_ios();
      var IS_NODE = require_environment_is_node();
      var set = globalThis2.setImmediate;
      var clear = globalThis2.clearImmediate;
      var process = globalThis2.process;
      var Dispatch = globalThis2.Dispatch;
      var Function2 = globalThis2.Function;
      var MessageChannel = globalThis2.MessageChannel;
      var String2 = globalThis2.String;
      var counter = 0;
      var queue = {};
      var ONREADYSTATECHANGE = "onreadystatechange";
      var $location;
      var defer;
      var channel;
      var port2;
      fails(function() {
        $location = globalThis2.location;
      });
      var run = function(id) {
        if (hasOwn(queue, id)) {
          var fn = queue[id];
          delete queue[id];
          fn();
        }
      };
      var runner = function(id) {
        return function() {
          run(id);
        };
      };
      var eventListener = function(event) {
        run(event.data);
      };
      var globalPostMessageDefer = function(id) {
        globalThis2.postMessage(String2(id), $location.protocol + "//" + $location.host);
      };
      if (!set || !clear) {
        set = function setImmediate(handler) {
          validateArgumentsLength(arguments.length, 1);
          var fn = isCallable(handler) ? handler : Function2(handler);
          var args = arraySlice(arguments, 1);
          queue[++counter] = function() {
            apply(fn, void 0, args);
          };
          defer(counter);
          return counter;
        };
        clear = function clearImmediate(id) {
          delete queue[id];
        };
        if (IS_NODE) {
          defer = function(id) {
            process.nextTick(runner(id));
          };
        } else if (Dispatch && Dispatch.now) {
          defer = function(id) {
            Dispatch.now(runner(id));
          };
        } else if (MessageChannel && !IS_IOS) {
          channel = new MessageChannel();
          port2 = channel.port2;
          channel.port1.onmessage = eventListener;
          defer = bind(port2.postMessage, port2);
        } else if (globalThis2.addEventListener && isCallable(globalThis2.postMessage) && !globalThis2.importScripts && $location && $location.protocol !== "file:" && !fails(globalPostMessageDefer)) {
          defer = globalPostMessageDefer;
          globalThis2.addEventListener("message", eventListener, false);
        } else if (ONREADYSTATECHANGE in createElement("script")) {
          defer = function(id) {
            html.appendChild(createElement("script"))[ONREADYSTATECHANGE] = function() {
              html.removeChild(this);
              run(id);
            };
          };
        } else {
          defer = function(id) {
            setTimeout(runner(id), 0);
          };
        }
      }
      module.exports = {
        set,
        clear
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/safe-get-built-in.js
  var require_safe_get_built_in = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/safe-get-built-in.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var DESCRIPTORS = require_descriptors();
      var getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
      module.exports = function(name) {
        if (!DESCRIPTORS) return globalThis2[name];
        var descriptor = getOwnPropertyDescriptor(globalThis2, name);
        return descriptor && descriptor.value;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/queue.js
  var require_queue = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/queue.js"(exports, module) {
      "use strict";
      var Queue = function() {
        this.head = null;
        this.tail = null;
      };
      Queue.prototype = {
        add: function(item) {
          var entry = { item, next: null };
          var tail = this.tail;
          if (tail) tail.next = entry;
          else this.head = entry;
          this.tail = entry;
        },
        get: function() {
          var entry = this.head;
          if (entry) {
            var next = this.head = entry.next;
            if (next === null) this.tail = null;
            return entry.item;
          }
        }
      };
      module.exports = Queue;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-ios-pebble.js
  var require_environment_is_ios_pebble = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-ios-pebble.js"(exports, module) {
      "use strict";
      var userAgent = require_environment_user_agent();
      module.exports = /ipad|iphone|ipod/i.test(userAgent) && typeof Pebble != "undefined";
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-webos-webkit.js
  var require_environment_is_webos_webkit = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/environment-is-webos-webkit.js"(exports, module) {
      "use strict";
      var userAgent = require_environment_user_agent();
      module.exports = /web0s(?!.*chrome)/i.test(userAgent);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/microtask.js
  var require_microtask = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/microtask.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var safeGetBuiltIn = require_safe_get_built_in();
      var bind = require_function_bind_context();
      var macrotask = require_task().set;
      var Queue = require_queue();
      var IS_IOS = require_environment_is_ios();
      var IS_IOS_PEBBLE = require_environment_is_ios_pebble();
      var IS_WEBOS_WEBKIT = require_environment_is_webos_webkit();
      var IS_NODE = require_environment_is_node();
      var MutationObserver2 = globalThis2.MutationObserver || globalThis2.WebKitMutationObserver;
      var document2 = globalThis2.document;
      var process = globalThis2.process;
      var Promise2 = globalThis2.Promise;
      var microtask = safeGetBuiltIn("queueMicrotask");
      var notify2;
      var toggle;
      var node;
      var promise;
      var then;
      if (!microtask) {
        queue = new Queue();
        flush = function() {
          var parent, fn;
          if (IS_NODE && (parent = process.domain)) parent.exit();
          while (fn = queue.get()) try {
            fn();
          } catch (error) {
            if (queue.head) notify2();
            throw error;
          }
          if (parent) parent.enter();
        };
        if (!IS_IOS && !IS_NODE && !IS_WEBOS_WEBKIT && MutationObserver2 && document2) {
          toggle = true;
          node = document2.createTextNode("");
          new MutationObserver2(flush).observe(node, { characterData: true });
          notify2 = function() {
            node.data = toggle = !toggle;
          };
        } else if (!IS_IOS_PEBBLE && Promise2 && Promise2.resolve) {
          promise = Promise2.resolve(void 0);
          promise.constructor = Promise2;
          then = bind(promise.then, promise);
          notify2 = function() {
            then(flush);
          };
        } else if (IS_NODE) {
          notify2 = function() {
            process.nextTick(flush);
          };
        } else {
          macrotask = bind(macrotask, globalThis2);
          notify2 = function() {
            macrotask(flush);
          };
        }
        microtask = function(fn) {
          if (!queue.head) notify2();
          queue.add(fn);
        };
      }
      var queue;
      var flush;
      module.exports = microtask;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/host-report-errors.js
  var require_host_report_errors = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/host-report-errors.js"(exports, module) {
      "use strict";
      module.exports = function(a, b) {
        try {
          arguments.length === 1 ? console.error(a) : console.error(a, b);
        } catch (error) {
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/perform.js
  var require_perform = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/perform.js"(exports, module) {
      "use strict";
      module.exports = function(exec) {
        try {
          return { error: false, value: exec() };
        } catch (error) {
          return { error: true, value: error };
        }
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-native-constructor.js
  var require_promise_native_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-native-constructor.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      module.exports = globalThis2.Promise;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-constructor-detection.js
  var require_promise_constructor_detection = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-constructor-detection.js"(exports, module) {
      "use strict";
      var globalThis2 = require_global_this();
      var NativePromiseConstructor = require_promise_native_constructor();
      var isCallable = require_is_callable();
      var isForced = require_is_forced();
      var inspectSource = require_inspect_source();
      var wellKnownSymbol = require_well_known_symbol();
      var ENVIRONMENT = require_environment();
      var IS_PURE = require_is_pure();
      var V8_VERSION = require_environment_v8_version();
      var NativePromisePrototype = NativePromiseConstructor && NativePromiseConstructor.prototype;
      var SPECIES = wellKnownSymbol("species");
      var SUBCLASSING = false;
      var NATIVE_PROMISE_REJECTION_EVENT = isCallable(globalThis2.PromiseRejectionEvent);
      var FORCED_PROMISE_CONSTRUCTOR = isForced("Promise", function() {
        var PROMISE_CONSTRUCTOR_SOURCE = inspectSource(NativePromiseConstructor);
        var GLOBAL_CORE_JS_PROMISE = PROMISE_CONSTRUCTOR_SOURCE !== String(NativePromiseConstructor);
        if (!GLOBAL_CORE_JS_PROMISE && V8_VERSION === 66) return true;
        if (IS_PURE && !(NativePromisePrototype["catch"] && NativePromisePrototype["finally"])) return true;
        if (!V8_VERSION || V8_VERSION < 51 || !/native code/.test(PROMISE_CONSTRUCTOR_SOURCE)) {
          var promise = new NativePromiseConstructor(function(resolve) {
            resolve(1);
          });
          var FakePromise = function(exec) {
            exec(function() {
            }, function() {
            });
          };
          var constructor = promise.constructor = {};
          constructor[SPECIES] = FakePromise;
          SUBCLASSING = promise.then(function() {
          }) instanceof FakePromise;
          if (!SUBCLASSING) return true;
        }
        return !GLOBAL_CORE_JS_PROMISE && (ENVIRONMENT === "BROWSER" || ENVIRONMENT === "DENO") && !NATIVE_PROMISE_REJECTION_EVENT;
      });
      module.exports = {
        CONSTRUCTOR: FORCED_PROMISE_CONSTRUCTOR,
        REJECTION_EVENT: NATIVE_PROMISE_REJECTION_EVENT,
        SUBCLASSING
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/new-promise-capability.js
  var require_new_promise_capability = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/new-promise-capability.js"(exports, module) {
      "use strict";
      var aCallable = require_a_callable();
      var $TypeError = TypeError;
      var PromiseCapability = function(C) {
        var resolve, reject;
        this.promise = new C(function($$resolve, $$reject) {
          if (resolve !== void 0 || reject !== void 0) throw new $TypeError("Bad Promise constructor");
          resolve = $$resolve;
          reject = $$reject;
        });
        this.resolve = aCallable(resolve);
        this.reject = aCallable(reject);
      };
      module.exports.f = function(C) {
        return new PromiseCapability(C);
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.constructor.js
  var require_es_promise_constructor = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.constructor.js"() {
      "use strict";
      var $ = require_export();
      var IS_PURE = require_is_pure();
      var IS_NODE = require_environment_is_node();
      var globalThis2 = require_global_this();
      var path = require_path();
      var call = require_function_call();
      var defineBuiltIn = require_define_built_in();
      var setPrototypeOf = require_object_set_prototype_of();
      var setToStringTag = require_set_to_string_tag();
      var setSpecies = require_set_species();
      var aCallable = require_a_callable();
      var isCallable = require_is_callable();
      var isObject = require_is_object();
      var anInstance = require_an_instance();
      var speciesConstructor = require_species_constructor();
      var task = require_task().set;
      var microtask = require_microtask();
      var hostReportErrors = require_host_report_errors();
      var perform = require_perform();
      var Queue = require_queue();
      var InternalStateModule = require_internal_state();
      var NativePromiseConstructor = require_promise_native_constructor();
      var PromiseConstructorDetection = require_promise_constructor_detection();
      var newPromiseCapabilityModule = require_new_promise_capability();
      var PROMISE = "Promise";
      var FORCED_PROMISE_CONSTRUCTOR = PromiseConstructorDetection.CONSTRUCTOR;
      var NATIVE_PROMISE_REJECTION_EVENT = PromiseConstructorDetection.REJECTION_EVENT;
      var NATIVE_PROMISE_SUBCLASSING = PromiseConstructorDetection.SUBCLASSING;
      var getInternalPromiseState = InternalStateModule.getterFor(PROMISE);
      var setInternalState = InternalStateModule.set;
      var NativePromisePrototype = NativePromiseConstructor && NativePromiseConstructor.prototype;
      var PromiseConstructor = NativePromiseConstructor;
      var PromisePrototype = NativePromisePrototype;
      var TypeError2 = globalThis2.TypeError;
      var document2 = globalThis2.document;
      var process = globalThis2.process;
      var newPromiseCapability = newPromiseCapabilityModule.f;
      var newGenericPromiseCapability = newPromiseCapability;
      var DISPATCH_EVENT = !!(document2 && document2.createEvent && globalThis2.dispatchEvent);
      var UNHANDLED_REJECTION = "unhandledrejection";
      var REJECTION_HANDLED = "rejectionhandled";
      var PENDING = 0;
      var FULFILLED = 1;
      var REJECTED = 2;
      var HANDLED = 1;
      var UNHANDLED = 2;
      var Internal;
      var OwnPromiseCapability;
      var PromiseWrapper;
      var nativeThen;
      var isThenable = function(it) {
        var then;
        return isObject(it) && isCallable(then = it.then) ? then : false;
      };
      var callReaction = function(reaction, state2) {
        var value = state2.value;
        var ok = state2.state === FULFILLED;
        var handler = ok ? reaction.ok : reaction.fail;
        var resolve = reaction.resolve;
        var reject = reaction.reject;
        var domain = reaction.domain;
        var result, then, exited;
        try {
          if (handler) {
            if (!ok) {
              if (state2.rejection === UNHANDLED) onHandleUnhandled(state2);
              state2.rejection = HANDLED;
            }
            if (handler === true) result = value;
            else {
              if (domain) domain.enter();
              result = handler(value);
              if (domain) {
                domain.exit();
                exited = true;
              }
            }
            if (result === reaction.promise) {
              reject(new TypeError2("Promise-chain cycle"));
            } else if (then = isThenable(result)) {
              call(then, result, resolve, reject);
            } else resolve(result);
          } else reject(value);
        } catch (error) {
          if (domain && !exited) domain.exit();
          reject(error);
        }
      };
      var notify2 = function(state2, isReject) {
        if (state2.notified) return;
        state2.notified = true;
        microtask(function() {
          var reactions = state2.reactions;
          var reaction;
          while (reaction = reactions.get()) {
            callReaction(reaction, state2);
          }
          state2.notified = false;
          if (isReject && !state2.rejection) onUnhandled(state2);
        });
      };
      var dispatchEvent = function(name, promise, reason) {
        var event, handler;
        if (DISPATCH_EVENT) {
          event = document2.createEvent("Event");
          event.promise = promise;
          event.reason = reason;
          event.initEvent(name, false, true);
          globalThis2.dispatchEvent(event);
        } else event = { promise, reason };
        if (!NATIVE_PROMISE_REJECTION_EVENT && (handler = globalThis2["on" + name])) handler(event);
        else if (name === UNHANDLED_REJECTION) hostReportErrors("Unhandled promise rejection", reason);
      };
      var onUnhandled = function(state2) {
        call(task, globalThis2, function() {
          var promise = state2.facade;
          var value = state2.value;
          var IS_UNHANDLED = isUnhandled(state2);
          var result;
          if (IS_UNHANDLED) {
            result = perform(function() {
              if (IS_NODE) {
                process.emit("unhandledRejection", value, promise);
              } else dispatchEvent(UNHANDLED_REJECTION, promise, value);
            });
            state2.rejection = IS_NODE || isUnhandled(state2) ? UNHANDLED : HANDLED;
            if (result.error) throw result.value;
          }
        });
      };
      var isUnhandled = function(state2) {
        return state2.rejection !== HANDLED && !state2.parent;
      };
      var onHandleUnhandled = function(state2) {
        call(task, globalThis2, function() {
          var promise = state2.facade;
          if (IS_NODE) {
            process.emit("rejectionHandled", promise);
          } else dispatchEvent(REJECTION_HANDLED, promise, state2.value);
        });
      };
      var bind = function(fn, state2, unwrap2) {
        return function(value) {
          fn(state2, value, unwrap2);
        };
      };
      var internalReject = function(state2, value, unwrap2) {
        if (state2.done) return;
        state2.done = true;
        if (unwrap2) state2 = unwrap2;
        state2.value = value;
        state2.state = REJECTED;
        notify2(state2, true);
      };
      var internalResolve = function(state2, value, unwrap2) {
        if (state2.done) return;
        state2.done = true;
        if (unwrap2) state2 = unwrap2;
        try {
          if (state2.facade === value) throw new TypeError2("Promise can't be resolved itself");
          var then = isThenable(value);
          if (then) {
            microtask(function() {
              var wrapper = { done: false };
              try {
                call(
                  then,
                  value,
                  bind(internalResolve, wrapper, state2),
                  bind(internalReject, wrapper, state2)
                );
              } catch (error) {
                internalReject(wrapper, error, state2);
              }
            });
          } else {
            state2.value = value;
            state2.state = FULFILLED;
            notify2(state2, false);
          }
        } catch (error) {
          internalReject({ done: false }, error, state2);
        }
      };
      if (FORCED_PROMISE_CONSTRUCTOR) {
        PromiseConstructor = function Promise2(executor) {
          anInstance(this, PromisePrototype);
          aCallable(executor);
          call(Internal, this);
          var state2 = getInternalPromiseState(this);
          try {
            executor(bind(internalResolve, state2), bind(internalReject, state2));
          } catch (error) {
            internalReject(state2, error);
          }
        };
        PromisePrototype = PromiseConstructor.prototype;
        Internal = function Promise2(executor) {
          setInternalState(this, {
            type: PROMISE,
            done: false,
            notified: false,
            parent: false,
            reactions: new Queue(),
            rejection: false,
            state: PENDING,
            value: null
          });
        };
        Internal.prototype = defineBuiltIn(PromisePrototype, "then", function then(onFulfilled, onRejected) {
          var state2 = getInternalPromiseState(this);
          var reaction = newPromiseCapability(speciesConstructor(this, PromiseConstructor));
          state2.parent = true;
          reaction.ok = isCallable(onFulfilled) ? onFulfilled : true;
          reaction.fail = isCallable(onRejected) && onRejected;
          reaction.domain = IS_NODE ? process.domain : void 0;
          if (state2.state === PENDING) state2.reactions.add(reaction);
          else microtask(function() {
            callReaction(reaction, state2);
          });
          return reaction.promise;
        });
        OwnPromiseCapability = function() {
          var promise = new Internal();
          var state2 = getInternalPromiseState(promise);
          this.promise = promise;
          this.resolve = bind(internalResolve, state2);
          this.reject = bind(internalReject, state2);
        };
        newPromiseCapabilityModule.f = newPromiseCapability = function(C) {
          return C === PromiseConstructor || C === PromiseWrapper ? new OwnPromiseCapability(C) : newGenericPromiseCapability(C);
        };
        if (!IS_PURE && isCallable(NativePromiseConstructor) && NativePromisePrototype !== Object.prototype) {
          nativeThen = NativePromisePrototype.then;
          if (!NATIVE_PROMISE_SUBCLASSING) {
            defineBuiltIn(NativePromisePrototype, "then", function then(onFulfilled, onRejected) {
              var that = this;
              return new PromiseConstructor(function(resolve, reject) {
                call(nativeThen, that, resolve, reject);
              }).then(onFulfilled, onRejected);
            }, { unsafe: true });
          }
          try {
            delete NativePromisePrototype.constructor;
          } catch (error) {
          }
          if (setPrototypeOf) {
            setPrototypeOf(NativePromisePrototype, PromisePrototype);
          }
        }
      }
      $({ global: true, constructor: true, wrap: true, forced: FORCED_PROMISE_CONSTRUCTOR }, {
        Promise: PromiseConstructor
      });
      PromiseWrapper = path.Promise;
      setToStringTag(PromiseConstructor, PROMISE, false, true);
      setSpecies(PROMISE);
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-statics-incorrect-iteration.js
  var require_promise_statics_incorrect_iteration = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-statics-incorrect-iteration.js"(exports, module) {
      "use strict";
      var NativePromiseConstructor = require_promise_native_constructor();
      var checkCorrectnessOfIteration = require_check_correctness_of_iteration();
      var FORCED_PROMISE_CONSTRUCTOR = require_promise_constructor_detection().CONSTRUCTOR;
      module.exports = FORCED_PROMISE_CONSTRUCTOR || !checkCorrectnessOfIteration(function(iterable) {
        NativePromiseConstructor.all(iterable).then(void 0, function() {
        });
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.all.js
  var require_es_promise_all = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.all.js"() {
      "use strict";
      var $ = require_export();
      var call = require_function_call();
      var aCallable = require_a_callable();
      var newPromiseCapabilityModule = require_new_promise_capability();
      var perform = require_perform();
      var iterate = require_iterate();
      var PROMISE_STATICS_INCORRECT_ITERATION = require_promise_statics_incorrect_iteration();
      $({ target: "Promise", stat: true, forced: PROMISE_STATICS_INCORRECT_ITERATION }, {
        all: function all(iterable) {
          var C = this;
          var capability = newPromiseCapabilityModule.f(C);
          var resolve = capability.resolve;
          var reject = capability.reject;
          var result = perform(function() {
            var $promiseResolve = aCallable(C.resolve);
            var values = [];
            var counter = 0;
            var remaining = 1;
            iterate(iterable, function(promise) {
              var index2 = counter++;
              var alreadyCalled = false;
              remaining++;
              call($promiseResolve, C, promise).then(function(value) {
                if (alreadyCalled) return;
                alreadyCalled = true;
                values[index2] = value;
                --remaining || resolve(values);
              }, reject);
            });
            --remaining || resolve(values);
          });
          if (result.error) reject(result.value);
          return capability.promise;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.catch.js
  var require_es_promise_catch = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.catch.js"() {
      "use strict";
      var $ = require_export();
      var IS_PURE = require_is_pure();
      var FORCED_PROMISE_CONSTRUCTOR = require_promise_constructor_detection().CONSTRUCTOR;
      var NativePromiseConstructor = require_promise_native_constructor();
      var getBuiltIn = require_get_built_in();
      var isCallable = require_is_callable();
      var defineBuiltIn = require_define_built_in();
      var NativePromisePrototype = NativePromiseConstructor && NativePromiseConstructor.prototype;
      $({ target: "Promise", proto: true, forced: FORCED_PROMISE_CONSTRUCTOR, real: true }, {
        "catch": function(onRejected) {
          return this.then(void 0, onRejected);
        }
      });
      if (!IS_PURE && isCallable(NativePromiseConstructor)) {
        method = getBuiltIn("Promise").prototype["catch"];
        if (NativePromisePrototype["catch"] !== method) {
          defineBuiltIn(NativePromisePrototype, "catch", method, { unsafe: true });
        }
      }
      var method;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.race.js
  var require_es_promise_race = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.race.js"() {
      "use strict";
      var $ = require_export();
      var call = require_function_call();
      var aCallable = require_a_callable();
      var newPromiseCapabilityModule = require_new_promise_capability();
      var perform = require_perform();
      var iterate = require_iterate();
      var PROMISE_STATICS_INCORRECT_ITERATION = require_promise_statics_incorrect_iteration();
      $({ target: "Promise", stat: true, forced: PROMISE_STATICS_INCORRECT_ITERATION }, {
        race: function race(iterable) {
          var C = this;
          var capability = newPromiseCapabilityModule.f(C);
          var reject = capability.reject;
          var result = perform(function() {
            var $promiseResolve = aCallable(C.resolve);
            iterate(iterable, function(promise) {
              call($promiseResolve, C, promise).then(capability.resolve, reject);
            });
          });
          if (result.error) reject(result.value);
          return capability.promise;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.reject.js
  var require_es_promise_reject = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.reject.js"() {
      "use strict";
      var $ = require_export();
      var newPromiseCapabilityModule = require_new_promise_capability();
      var FORCED_PROMISE_CONSTRUCTOR = require_promise_constructor_detection().CONSTRUCTOR;
      $({ target: "Promise", stat: true, forced: FORCED_PROMISE_CONSTRUCTOR }, {
        reject: function reject(r) {
          var capability = newPromiseCapabilityModule.f(this);
          var capabilityReject = capability.reject;
          capabilityReject(r);
          return capability.promise;
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-resolve.js
  var require_promise_resolve = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/internals/promise-resolve.js"(exports, module) {
      "use strict";
      var anObject = require_an_object();
      var isObject = require_is_object();
      var newPromiseCapability = require_new_promise_capability();
      module.exports = function(C, x) {
        anObject(C);
        if (isObject(x) && x.constructor === C) return x;
        var promiseCapability = newPromiseCapability.f(C);
        var resolve = promiseCapability.resolve;
        resolve(x);
        return promiseCapability.promise;
      };
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.resolve.js
  var require_es_promise_resolve = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.resolve.js"() {
      "use strict";
      var $ = require_export();
      var getBuiltIn = require_get_built_in();
      var IS_PURE = require_is_pure();
      var NativePromiseConstructor = require_promise_native_constructor();
      var FORCED_PROMISE_CONSTRUCTOR = require_promise_constructor_detection().CONSTRUCTOR;
      var promiseResolve = require_promise_resolve();
      var PromiseConstructorWrapper = getBuiltIn("Promise");
      var CHECK_WRAPPER = IS_PURE && !FORCED_PROMISE_CONSTRUCTOR;
      $({ target: "Promise", stat: true, forced: IS_PURE || FORCED_PROMISE_CONSTRUCTOR }, {
        resolve: function resolve(x) {
          return promiseResolve(CHECK_WRAPPER && this === PromiseConstructorWrapper ? NativePromiseConstructor : this, x);
        }
      });
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.js
  var require_es_promise = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.js"() {
      "use strict";
      require_es_promise_constructor();
      require_es_promise_all();
      require_es_promise_catch();
      require_es_promise_race();
      require_es_promise_reject();
      require_es_promise_resolve();
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.finally.js
  var require_es_promise_finally = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/modules/es.promise.finally.js"() {
      "use strict";
      var $ = require_export();
      var IS_PURE = require_is_pure();
      var NativePromiseConstructor = require_promise_native_constructor();
      var fails = require_fails();
      var getBuiltIn = require_get_built_in();
      var isCallable = require_is_callable();
      var speciesConstructor = require_species_constructor();
      var promiseResolve = require_promise_resolve();
      var defineBuiltIn = require_define_built_in();
      var NativePromisePrototype = NativePromiseConstructor && NativePromiseConstructor.prototype;
      var NON_GENERIC = !!NativePromiseConstructor && fails(function() {
        NativePromisePrototype["finally"].call({ then: function() {
        } }, function() {
        });
      });
      $({ target: "Promise", proto: true, real: true, forced: NON_GENERIC }, {
        "finally": function(onFinally) {
          var C = speciesConstructor(this, getBuiltIn("Promise"));
          var isFunction = isCallable(onFinally);
          return this.then(
            isFunction ? function(x) {
              return promiseResolve(C, onFinally()).then(function() {
                return x;
              });
            } : onFinally,
            isFunction ? function(e) {
              return promiseResolve(C, onFinally()).then(function() {
                throw e;
              });
            } : onFinally
          );
        }
      });
      if (!IS_PURE && isCallable(NativePromiseConstructor)) {
        method = getBuiltIn("Promise").prototype["finally"];
        if (NativePromisePrototype["finally"] !== method) {
          defineBuiltIn(NativePromisePrototype, "finally", method, { unsafe: true });
        }
      }
      var method;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/promise/finally.js
  var require_finally = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/es/promise/finally.js"(exports, module) {
      "use strict";
      require_es_object_to_string();
      require_es_promise();
      require_es_promise_finally();
      var entryUnbind = require_entry_unbind();
      module.exports = entryUnbind("Promise", "finally");
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/promise/finally.js
  var require_finally2 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/stable/promise/finally.js"(exports, module) {
      "use strict";
      var parent = require_finally();
      module.exports = parent;
    }
  });

  // node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/promise/finally.js
  var require_finally3 = __commonJS({
    "node_modules/.pnpm/core-js@3.46.0/node_modules/core-js/actual/promise/finally.js"(exports, module) {
      "use strict";
      var parent = require_finally2();
      module.exports = parent;
    }
  });

  // vendor/foliate/fixed-layout.js
  var fixed_layout_exports = {};
  __export(fixed_layout_exports, {
    FixedLayout: () => FixedLayout
  });
  var parseViewport, getViewport, _root, _observer, _spreads, _index, _portrait, _left, _right, _center, _side, _zoom, _FixedLayout_instances, createFrame_fn, render_fn, showSpread_fn, goLeft_fn, goRight_fn, reportLocation_fn, FixedLayout;
  var init_fixed_layout = __esm({
    "vendor/foliate/fixed-layout.js"() {
      parseViewport = (str) => {
        var _a2, _b2;
        return (_b2 = (_a2 = str == null ? void 0 : str.split(/[,;\s]/)) == null ? void 0 : _a2.filter((x) => x)) == null ? void 0 : _b2.map((x) => x.split("=").map((x2) => x2.trim()));
      };
      getViewport = (doc, viewport) => {
        var _a2, _b2, _c;
        if (doc.documentElement.localName === "svg") {
          const [, , width, height] = (_b2 = (_a2 = doc.documentElement.getAttribute("viewBox")) == null ? void 0 : _a2.split(/\s/)) != null ? _b2 : [];
          return { width, height };
        }
        const meta = parseViewport((_c = doc.querySelector('meta[name="viewport"]')) == null ? void 0 : _c.getAttribute("content"));
        if (meta) return Object.fromEntries(meta);
        if (typeof viewport === "string") return parseViewport(viewport);
        if ((viewport == null ? void 0 : viewport.width) && viewport.height) return viewport;
        const img = doc.querySelector("img");
        if (img) return { width: img.naturalWidth, height: img.naturalHeight };
        console.warn(new Error("Missing viewport properties"));
        return { width: 1e3, height: 2e3 };
      };
      FixedLayout = class extends HTMLElement {
        constructor() {
          super();
          __privateAdd(this, _FixedLayout_instances);
          __privateAdd(this, _root, this.attachShadow({ mode: "closed" }));
          __privateAdd(this, _observer, new ResizeObserver(() => __privateMethod(this, _FixedLayout_instances, render_fn).call(this)));
          __privateAdd(this, _spreads);
          __privateAdd(this, _index, -1);
          __publicField(this, "defaultViewport");
          __publicField(this, "spread");
          __privateAdd(this, _portrait, false);
          __privateAdd(this, _left);
          __privateAdd(this, _right);
          __privateAdd(this, _center);
          __privateAdd(this, _side);
          __privateAdd(this, _zoom);
          const androidStyle = document.createElement("style");
          this._androidStyle = androidStyle;
          __privateGet(this, _root).append(androidStyle);
          androidStyle.textContent = ":host {\n            width: 100%;\n            height: 100%;\n            display: flex;\n            justify-content: center;\n            align-items: center;\n            overflow: auto;\n        }";
          __privateGet(this, _observer).observe(this);
        }
        attributeChangedCallback(name, _, value) {
          switch (name) {
            case "zoom":
              __privateSet(this, _zoom, value !== "fit-width" && value !== "fit-page" ? parseFloat(value) : value);
              __privateMethod(this, _FixedLayout_instances, render_fn).call(this);
              break;
          }
        }
        open(book) {
          this.book = book;
          const { rendition } = book;
          this.spread = rendition == null ? void 0 : rendition.spread;
          this.defaultViewport = rendition == null ? void 0 : rendition.viewport;
          const rtl = book.dir === "rtl";
          const ltr = !rtl;
          this.rtl = rtl;
          if ((rendition == null ? void 0 : rendition.spread) === "none")
            __privateSet(this, _spreads, book.sections.map((section) => ({ center: section })));
          else __privateSet(this, _spreads, book.sections.reduce((arr, section, i) => {
            const last = arr[arr.length - 1];
            const { pageSpread } = section;
            const newSpread = () => {
              const spread = {};
              arr.push(spread);
              return spread;
            };
            if (pageSpread === "center") {
              const spread = last.left || last.right ? newSpread() : last;
              spread.center = section;
            } else if (pageSpread === "left") {
              const spread = last.center || last.left || ltr && i ? newSpread() : last;
              spread.left = section;
            } else if (pageSpread === "right") {
              const spread = last.center || last.right || rtl && i ? newSpread() : last;
              spread.right = section;
            } else if (ltr) {
              if (last.center || last.right) newSpread().left = section;
              else if (last.left || !i) last.right = section;
              else last.left = section;
            } else {
              if (last.center || last.left) newSpread().right = section;
              else if (last.right || !i) last.left = section;
              else last.right = section;
            }
            return arr;
          }, [{}]));
        }
        get index() {
          var _a2, _b2, _c;
          const spread = __privateGet(this, _spreads)[__privateGet(this, _index)];
          const section = (_c = spread == null ? void 0 : spread.center) != null ? _c : __privateGet(this, _side) === "left" ? (_a2 = spread.left) != null ? _a2 : spread.right : (_b2 = spread.right) != null ? _b2 : spread.left;
          return this.book.sections.indexOf(section);
        }
        getSpreadOf(section) {
          const spreads = __privateGet(this, _spreads);
          for (let index2 = 0; index2 < spreads.length; index2++) {
            const { left, right, center } = spreads[index2];
            if (left === section) return { index: index2, side: "left" };
            if (right === section) return { index: index2, side: "right" };
            if (center === section) return { index: index2, side: "center" };
          }
        }
        async goToSpread(index2, side, reason) {
          var _a2, _b2, _c, _d, _e, _f2;
          if (index2 < 0 || index2 > __privateGet(this, _spreads).length - 1) return;
          if (index2 === __privateGet(this, _index)) {
            __privateMethod(this, _FixedLayout_instances, render_fn).call(this, side);
            return;
          }
          __privateSet(this, _index, index2);
          const spread = __privateGet(this, _spreads)[index2];
          if (spread.center) {
            const index3 = this.book.sections.indexOf(spread.center);
            const src = await ((_b2 = (_a2 = spread.center) == null ? void 0 : _a2.load) == null ? void 0 : _b2.call(_a2));
            await __privateMethod(this, _FixedLayout_instances, showSpread_fn).call(this, { center: { index: index3, src } });
          } else {
            const indexL = this.book.sections.indexOf(spread.left);
            const indexR = this.book.sections.indexOf(spread.right);
            const srcL = await ((_d = (_c = spread.left) == null ? void 0 : _c.load) == null ? void 0 : _d.call(_c));
            const srcR = await ((_f2 = (_e = spread.right) == null ? void 0 : _e.load) == null ? void 0 : _f2.call(_e));
            const left = { index: indexL, src: srcL };
            const right = { index: indexR, src: srcR };
            await __privateMethod(this, _FixedLayout_instances, showSpread_fn).call(this, { left, right, side });
          }
          __privateMethod(this, _FixedLayout_instances, reportLocation_fn).call(this, reason);
        }
        async select(target) {
          await this.goTo(target);
        }
        async goTo(target) {
          const { book } = this;
          const resolved = await target;
          const section = book.sections[resolved.index];
          if (!section) return;
          const { index: index2, side } = this.getSpreadOf(section);
          await this.goToSpread(index2, side);
        }
        async next() {
          const s = this.rtl ? __privateMethod(this, _FixedLayout_instances, goLeft_fn).call(this) : __privateMethod(this, _FixedLayout_instances, goRight_fn).call(this);
          if (!s) return this.goToSpread(__privateGet(this, _index) + 1, this.rtl ? "right" : "left", "page");
        }
        async prev() {
          const s = this.rtl ? __privateMethod(this, _FixedLayout_instances, goRight_fn).call(this) : __privateMethod(this, _FixedLayout_instances, goLeft_fn).call(this);
          if (!s) return this.goToSpread(__privateGet(this, _index) - 1, this.rtl ? "left" : "right", "page");
        }
        getContents() {
          return Array.from(__privateGet(this, _root).querySelectorAll("iframe"), (frame) => ({
            doc: frame.contentDocument
            // TODO: index, overlayer
          }));
        }
        destroy() {
          __privateGet(this, _observer).unobserve(this);
        }
      };
      _root = new WeakMap();
      _observer = new WeakMap();
      _spreads = new WeakMap();
      _index = new WeakMap();
      _portrait = new WeakMap();
      _left = new WeakMap();
      _right = new WeakMap();
      _center = new WeakMap();
      _side = new WeakMap();
      _zoom = new WeakMap();
      _FixedLayout_instances = new WeakSet();
      createFrame_fn = async function({ index: index2, src: srcOption }) {
        const srcOptionIsString = typeof srcOption === "string";
        const src = srcOptionIsString ? srcOption : srcOption == null ? void 0 : srcOption.src;
        const onZoom = srcOptionIsString ? null : srcOption == null ? void 0 : srcOption.onZoom;
        const element = document.createElement("div");
        element.setAttribute("dir", "ltr");
        const iframe = document.createElement("iframe");
        element.append(iframe);
        Object.assign(iframe.style, {
          border: "0",
          display: "none",
          overflow: "hidden"
        });
        iframe.setAttribute("sandbox", "allow-same-origin");
        iframe.setAttribute("scrolling", "no");
        iframe.setAttribute("part", "filter");
        __privateGet(this, _root).append(element);
        if (!src) return { blank: true, element, iframe };
        return new Promise((resolve) => {
          iframe.addEventListener("load", () => {
            const doc = iframe.contentDocument;
            this.dispatchEvent(new CustomEvent("load", { detail: { doc, index: index2 } }));
            const { width, height } = getViewport(doc, this.defaultViewport);
            resolve({
              element,
              iframe,
              width: parseFloat(width),
              height: parseFloat(height),
              onZoom
            });
          }, { once: true });
          iframe.src = src;
        });
      };
      render_fn = function(side = __privateGet(this, _side)) {
        var _a2, _b2, _c, _d, _e, _f2, _g, _h, _i, _j, _k, _l, _m, _n, _o, _p;
        if (!side) return;
        const left = (_a2 = __privateGet(this, _left)) != null ? _a2 : {};
        const right = (_c = (_b2 = __privateGet(this, _center)) != null ? _b2 : __privateGet(this, _right)) != null ? _c : {};
        const target = side === "left" ? left : right;
        const { width, height } = this.getBoundingClientRect();
        const portrait = this.spread !== "both" && this.spread !== "portrait" && height > width;
        __privateSet(this, _portrait, portrait);
        const blankWidth = (_e = (_d = left.width) != null ? _d : right.width) != null ? _e : 0;
        const blankHeight = (_g = (_f2 = left.height) != null ? _f2 : right.height) != null ? _g : 0;
        const scale = typeof __privateGet(this, _zoom) === "number" && !isNaN(__privateGet(this, _zoom)) ? __privateGet(this, _zoom) : (__privateGet(this, _zoom) === "fit-width" ? portrait || __privateGet(this, _center) ? width / ((_h = target.width) != null ? _h : blankWidth) : width / (((_i = left.width) != null ? _i : blankWidth) + ((_j = right.width) != null ? _j : blankWidth)) : portrait || __privateGet(this, _center) ? Math.min(
          width / ((_k = target.width) != null ? _k : blankWidth),
          height / ((_l = target.height) != null ? _l : blankHeight)
        ) : Math.min(
          width / (((_m = left.width) != null ? _m : blankWidth) + ((_n = right.width) != null ? _n : blankWidth)),
          height / Math.max(
            (_o = left.height) != null ? _o : blankHeight,
            (_p = right.height) != null ? _p : blankHeight
          )
        )) || 1;
        const transform = (frame) => {
          let { element, iframe, width: width2, height: height2, blank, onZoom } = frame;
          if (!iframe) return;
          if (onZoom) onZoom({ doc: frame.iframe.contentDocument, scale });
          const iframeScale = onZoom ? scale : 1;
          Object.assign(iframe.style, {
            width: "".concat(width2 * iframeScale, "px"),
            height: "".concat(height2 * iframeScale, "px"),
            transform: onZoom ? "none" : "scale(".concat(scale, ")"),
            transformOrigin: "top left",
            display: blank ? "none" : "block"
          });
          Object.assign(element.style, {
            width: "".concat((width2 != null ? width2 : blankWidth) * scale, "px"),
            height: "".concat((height2 != null ? height2 : blankHeight) * scale, "px"),
            overflow: "hidden",
            display: "block",
            flexShrink: "0",
            marginBlock: "auto"
          });
          if (portrait && frame !== target) {
            element.style.display = "none";
          }
        };
        if (__privateGet(this, _center)) {
          transform(__privateGet(this, _center));
        } else {
          transform(left);
          transform(right);
        }
      };
      showSpread_fn = async function({ left, right, center, side }) {
        __privateGet(this, _root).replaceChildren(this._androidStyle);
        __privateSet(this, _left, null);
        __privateSet(this, _right, null);
        __privateSet(this, _center, null);
        if (center) {
          __privateSet(this, _center, await __privateMethod(this, _FixedLayout_instances, createFrame_fn).call(this, center));
          __privateSet(this, _side, "center");
          __privateMethod(this, _FixedLayout_instances, render_fn).call(this);
        } else {
          __privateSet(this, _left, await __privateMethod(this, _FixedLayout_instances, createFrame_fn).call(this, left));
          __privateSet(this, _right, await __privateMethod(this, _FixedLayout_instances, createFrame_fn).call(this, right));
          __privateSet(this, _side, __privateGet(this, _left).blank ? "right" : __privateGet(this, _right).blank ? "left" : side);
          __privateMethod(this, _FixedLayout_instances, render_fn).call(this);
        }
      };
      goLeft_fn = function() {
        var _a2, _b2, _c, _d;
        if (__privateGet(this, _center) || ((_a2 = __privateGet(this, _left)) == null ? void 0 : _a2.blank)) return;
        if (__privateGet(this, _portrait) && ((_d = (_c = (_b2 = __privateGet(this, _left)) == null ? void 0 : _b2.element) == null ? void 0 : _c.style) == null ? void 0 : _d.display) === "none") {
          __privateSet(this, _side, "left");
          __privateMethod(this, _FixedLayout_instances, render_fn).call(this);
          __privateMethod(this, _FixedLayout_instances, reportLocation_fn).call(this, "page");
          return true;
        }
      };
      goRight_fn = function() {
        var _a2, _b2, _c, _d;
        if (__privateGet(this, _center) || ((_a2 = __privateGet(this, _right)) == null ? void 0 : _a2.blank)) return;
        if (__privateGet(this, _portrait) && ((_d = (_c = (_b2 = __privateGet(this, _right)) == null ? void 0 : _b2.element) == null ? void 0 : _c.style) == null ? void 0 : _d.display) === "none") {
          __privateSet(this, _side, "right");
          __privateMethod(this, _FixedLayout_instances, render_fn).call(this);
          __privateMethod(this, _FixedLayout_instances, reportLocation_fn).call(this, "page");
          return true;
        }
      };
      reportLocation_fn = function(reason) {
        this.dispatchEvent(new CustomEvent("relocate", { detail: { reason, range: null, index: this.index, fraction: 0, size: 1 } }));
      };
      __publicField(FixedLayout, "observedAttributes", ["zoom"]);
      customElements.define("foliate-fxl", FixedLayout);
    }
  });

  // vendor/foliate/paginator.js
  var paginator_exports = {};
  __export(paginator_exports, {
    Paginator: () => Paginator
  });
  var wait, debounce, lerp, easeOutQuad, animate, uncollapse, makeRange, bisectNode, SHOW_ELEMENT, SHOW_TEXT, SHOW_CDATA_SECTION, FILTER_ACCEPT, FILTER_REJECT, FILTER_SKIP, filter2, getBoundingClientRect, getVisibleRange, selectionIsBackward, setSelectionTo, getDirection, getBackground, makeMarginals, setStylesImportant, _observer2, _element, _iframe, _contentRange, _overlayer, _vertical, _rtl, _column, _size, _layout, View, _root2, _observer3, _top, _background, _container, _header, _footer, _view, _vertical2, _rtl2, _margin, _index2, _anchor, _justAnchored, _locked, _styles, _styleMap, _mediaQuery, _mediaQueryListener, _scrollBounds, _touchState, _touchScrolled, _lastVisibleRange, _Paginator_instances, createView_fn, beforeRender_fn, onTouchStart_fn, onTouchMove_fn, onTouchEnd_fn, getRectMapper_fn, scrollToRect_fn, scrollTo_fn, scrollToPage_fn, scrollToAnchor_fn, getVisibleRange_fn, afterScroll_fn, display_fn, canGoToIndex_fn, goTo_fn, scrollPrev_fn, scrollNext_fn, adjacentIndex_fn, turnPage_fn, Paginator;
  var init_paginator = __esm({
    "vendor/foliate/paginator.js"() {
      wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));
      debounce = (f, wait2, immediate) => {
        let timeout;
        return (...args) => {
          const later = () => {
            timeout = null;
            if (!immediate) f(...args);
          };
          const callNow = immediate && !timeout;
          if (timeout) clearTimeout(timeout);
          timeout = setTimeout(later, wait2);
          if (callNow) f(...args);
        };
      };
      lerp = (min, max2, x) => x * (max2 - min) + min;
      easeOutQuad = (x) => 1 - (1 - x) * (1 - x);
      animate = (a, b, duration, ease, render) => new Promise((resolve) => {
        let start;
        const step = (now) => {
          if (document.hidden) {
            render(lerp(a, b, 1));
            return resolve();
          }
          start != null ? start : start = now;
          const fraction = Math.min(1, (now - start) / duration);
          render(lerp(a, b, ease(fraction)));
          if (fraction < 1) requestAnimationFrame(step);
          else resolve();
        };
        if (document.hidden) {
          render(lerp(a, b, 1));
          return resolve();
        }
        requestAnimationFrame(step);
      });
      uncollapse = (range) => {
        if (!(range == null ? void 0 : range.collapsed)) return range;
        const { endOffset, endContainer } = range;
        if (endContainer.nodeType === 1) {
          const node = endContainer.childNodes[endOffset];
          if ((node == null ? void 0 : node.nodeType) === 1) return node;
          return endContainer;
        }
        if (endOffset + 1 < endContainer.length) range.setEnd(endContainer, endOffset + 1);
        else if (endOffset > 1) range.setStart(endContainer, endOffset - 1);
        else return endContainer.parentNode;
        return range;
      };
      makeRange = (doc, node, start, end = start) => {
        const range = doc.createRange();
        range.setStart(node, start);
        range.setEnd(node, end);
        return range;
      };
      bisectNode = (doc, node, cb, start = 0, end = node.nodeValue.length) => {
        if (end - start === 1) {
          const result2 = cb(makeRange(doc, node, start), makeRange(doc, node, end));
          return result2 < 0 ? start : end;
        }
        const mid = Math.floor(start + (end - start) / 2);
        const result = cb(makeRange(doc, node, start, mid), makeRange(doc, node, mid, end));
        return result < 0 ? bisectNode(doc, node, cb, start, mid) : result > 0 ? bisectNode(doc, node, cb, mid, end) : mid;
      };
      ({
        SHOW_ELEMENT,
        SHOW_TEXT,
        SHOW_CDATA_SECTION,
        FILTER_ACCEPT,
        FILTER_REJECT,
        FILTER_SKIP
      } = NodeFilter);
      filter2 = SHOW_ELEMENT | SHOW_TEXT | SHOW_CDATA_SECTION;
      getBoundingClientRect = (target) => {
        let top = Infinity, right = -Infinity, left = Infinity, bottom = -Infinity;
        for (const rect of target.getClientRects()) {
          left = Math.min(left, rect.left);
          top = Math.min(top, rect.top);
          right = Math.max(right, rect.right);
          bottom = Math.max(bottom, rect.bottom);
        }
        return new DOMRect(left, top, right - left, bottom - top);
      };
      getVisibleRange = (doc, start, end, mapRect) => {
        var _a2, _b2;
        const acceptNode2 = (node) => {
          var _a3, _b3;
          const name = (_a3 = node.localName) == null ? void 0 : _a3.toLowerCase();
          if (name === "script" || name === "style") return FILTER_REJECT;
          if (node.nodeType === 1) {
            const { left, right } = mapRect(node.getBoundingClientRect());
            if (right < start || left > end) return FILTER_REJECT;
            if (left >= start && right <= end) return FILTER_ACCEPT;
          } else {
            if (!((_b3 = node.nodeValue) == null ? void 0 : _b3.trim())) return FILTER_SKIP;
            const range2 = doc.createRange();
            range2.selectNodeContents(node);
            const { left, right } = mapRect(range2.getBoundingClientRect());
            if (right >= start && left <= end) return FILTER_ACCEPT;
          }
          return FILTER_SKIP;
        };
        const walker = doc.createTreeWalker(doc.body, filter2, { acceptNode: acceptNode2 });
        const nodes = [];
        for (let node = walker.nextNode(); node; node = walker.nextNode())
          nodes.push(node);
        const from = (_a2 = nodes[0]) != null ? _a2 : doc.body;
        const to = (_b2 = nodes[nodes.length - 1]) != null ? _b2 : from;
        const startOffset = from.nodeType === 1 ? 0 : bisectNode(doc, from, (a, b) => {
          const p = mapRect(getBoundingClientRect(a));
          const q = mapRect(getBoundingClientRect(b));
          if (p.right < start && q.left > start) return 0;
          return q.left > start ? -1 : 1;
        });
        const endOffset = to.nodeType === 1 ? 0 : bisectNode(doc, to, (a, b) => {
          const p = mapRect(getBoundingClientRect(a));
          const q = mapRect(getBoundingClientRect(b));
          if (p.right < end && q.left > end) return 0;
          return q.left > end ? -1 : 1;
        });
        const range = doc.createRange();
        range.setStart(from, startOffset);
        range.setEnd(to, endOffset);
        return range;
      };
      selectionIsBackward = (sel) => {
        const range = document.createRange();
        range.setStart(sel.anchorNode, sel.anchorOffset);
        range.setEnd(sel.focusNode, sel.focusOffset);
        return range.collapsed;
      };
      setSelectionTo = (target, collapse2) => {
        let range;
        if (target.startContainer) range = target.cloneRange();
        else if (target.nodeType) {
          range = document.createRange();
          range.selectNode(target);
        }
        if (range) {
          const sel = range.startContainer.ownerDocument.defaultView.getSelection();
          if (sel) {
            sel.removeAllRanges();
            if (collapse2 === -1) range.collapse(true);
            else if (collapse2 === 1) range.collapse();
            sel.addRange(range);
          }
        }
      };
      getDirection = (doc) => {
        const { defaultView } = doc;
        const { writingMode, direction } = defaultView.getComputedStyle(doc.body);
        const vertical = writingMode === "vertical-rl" || writingMode === "vertical-lr";
        const rtl = doc.body.dir === "rtl" || direction === "rtl" || doc.documentElement.dir === "rtl";
        return { vertical, rtl };
      };
      getBackground = (doc) => {
        const bodyStyle = doc.defaultView.getComputedStyle(doc.body);
        return bodyStyle.backgroundColor === "rgba(0, 0, 0, 0)" && bodyStyle.backgroundImage === "none" ? doc.defaultView.getComputedStyle(doc.documentElement).background : bodyStyle.background;
      };
      makeMarginals = (length, part) => Array.from({ length }, () => {
        const div = document.createElement("div");
        const child = document.createElement("div");
        div.append(child);
        child.setAttribute("part", part);
        return div;
      });
      setStylesImportant = (el, styles) => {
        const { style: style2 } = el;
        for (const [k, v] of Object.entries(styles)) style2.setProperty(k, v, "important");
      };
      View = class {
        constructor({ container, onExpand }) {
          __privateAdd(this, _observer2, new ResizeObserver(() => this.expand()));
          __privateAdd(this, _element, document.createElement("div"));
          __privateAdd(this, _iframe, document.createElement("iframe"));
          __privateAdd(this, _contentRange, document.createRange());
          __privateAdd(this, _overlayer);
          __privateAdd(this, _vertical, false);
          __privateAdd(this, _rtl, false);
          __privateAdd(this, _column, true);
          __privateAdd(this, _size);
          __privateAdd(this, _layout, {});
          this.container = container;
          this.onExpand = onExpand;
          __privateGet(this, _iframe).setAttribute("part", "filter");
          __privateGet(this, _element).append(__privateGet(this, _iframe));
          Object.assign(__privateGet(this, _element).style, {
            boxSizing: "content-box",
            position: "relative",
            overflow: "hidden",
            flex: "0 0 auto",
            width: "100%",
            height: "100%",
            display: "flex",
            justifyContent: "center",
            alignItems: "center"
          });
          Object.assign(__privateGet(this, _iframe).style, {
            overflow: "hidden",
            border: "0",
            display: "none",
            width: "100%",
            height: "100%"
          });
          __privateGet(this, _iframe).setAttribute("sandbox", "allow-same-origin");
          __privateGet(this, _iframe).setAttribute("scrolling", "no");
        }
        get element() {
          return __privateGet(this, _element);
        }
        get document() {
          return __privateGet(this, _iframe).contentDocument;
        }
        async load(src, afterLoad, beforeRender) {
          if (typeof src !== "string") throw new Error("".concat(src, " is not string"));
          return new Promise((resolve) => {
            __privateGet(this, _iframe).addEventListener("load", () => {
              const doc = this.document;
              afterLoad == null ? void 0 : afterLoad(doc);
              __privateGet(this, _iframe).style.display = "block";
              const { vertical, rtl } = getDirection(doc);
              const background = getBackground(doc);
              __privateGet(this, _iframe).style.display = "none";
              __privateSet(this, _vertical, vertical);
              __privateSet(this, _rtl, rtl);
              __privateGet(this, _contentRange).selectNodeContents(doc.body);
              const layout = beforeRender == null ? void 0 : beforeRender({ vertical, rtl, background });
              __privateGet(this, _iframe).style.display = "block";
              this.render(layout);
              __privateGet(this, _observer2).observe(doc.body);
              doc.fonts.ready.then(() => this.expand());
              resolve();
            }, { once: true });
            __privateGet(this, _iframe).src = src;
          });
        }
        render(layout) {
          if (!layout) return;
          __privateSet(this, _column, layout.flow !== "scrolled");
          __privateSet(this, _layout, layout);
          if (__privateGet(this, _column)) this.columnize(layout);
          else this.scrolled(layout);
        }
        scrolled({ gap, columnWidth }) {
          const vertical = __privateGet(this, _vertical);
          const doc = this.document;
          setStylesImportant(doc.documentElement, {
            "box-sizing": "border-box",
            "padding": vertical ? "".concat(gap, "px 0") : "0 ".concat(gap, "px"),
            "column-width": "auto",
            "height": "auto",
            "width": "auto"
          });
          setStylesImportant(doc.body, {
            [vertical ? "max-height" : "max-width"]: "".concat(columnWidth, "px"),
            "margin": "auto"
          });
          this.setImageSize();
          this.expand();
        }
        columnize({ width, height, gap, columnWidth }) {
          const vertical = __privateGet(this, _vertical);
          __privateSet(this, _size, vertical ? height : width);
          const doc = this.document;
          setStylesImportant(doc.documentElement, __spreadProps(__spreadValues({
            "box-sizing": "border-box",
            "column-width": "".concat(Math.trunc(columnWidth), "px"),
            "column-gap": "".concat(gap, "px"),
            "column-fill": "auto"
          }, vertical ? { "width": "".concat(width, "px") } : { "height": "".concat(height, "px") }), {
            "padding": vertical ? "".concat(gap / 2, "px 0") : "0 ".concat(gap / 2, "px"),
            "overflow": "hidden",
            // force wrap long words
            "overflow-wrap": "break-word",
            // reset some potentially problematic props
            "position": "static",
            "border": "0",
            "margin": "0",
            "max-height": "none",
            "max-width": "none",
            "min-height": "none",
            "min-width": "none",
            // fix glyph clipping in WebKit
            "-webkit-line-box-contain": "block glyphs replaced"
          }));
          setStylesImportant(doc.body, {
            "max-height": "none",
            "max-width": "none",
            "margin": "0"
          });
          this.setImageSize();
          this.expand();
        }
        setImageSize() {
          const { width, height, margin } = __privateGet(this, _layout);
          const vertical = __privateGet(this, _vertical);
          const doc = this.document;
          for (const el of doc.body.querySelectorAll("img, svg, video")) {
            const { maxHeight, maxWidth } = doc.defaultView.getComputedStyle(el);
            setStylesImportant(el, {
              "max-height": vertical ? maxHeight !== "none" && maxHeight !== "0px" ? maxHeight : "100%" : "".concat(height - margin * 2, "px"),
              "max-width": vertical ? "".concat(width - margin * 2, "px") : maxWidth !== "none" && maxWidth !== "0px" ? maxWidth : "100%",
              "object-fit": "contain",
              "page-break-inside": "avoid",
              "break-inside": "avoid",
              "box-sizing": "border-box"
            });
          }
        }
        expand() {
          const { documentElement } = this.document;
          if (__privateGet(this, _column)) {
            const side = __privateGet(this, _vertical) ? "height" : "width";
            const otherSide = __privateGet(this, _vertical) ? "width" : "height";
            const contentRect = __privateGet(this, _contentRange).getBoundingClientRect();
            const rootRect = documentElement.getBoundingClientRect();
            const contentStart = __privateGet(this, _vertical) ? 0 : __privateGet(this, _rtl) ? rootRect.right - contentRect.right : contentRect.left - rootRect.left;
            const contentSize = contentStart + contentRect[side];
            const pageCount = Math.ceil(contentSize / __privateGet(this, _size));
            const expandedSize = pageCount * __privateGet(this, _size);
            __privateGet(this, _element).style.padding = "0";
            __privateGet(this, _iframe).style[side] = "".concat(expandedSize, "px");
            __privateGet(this, _element).style[side] = "".concat(expandedSize + __privateGet(this, _size) * 2, "px");
            __privateGet(this, _iframe).style[otherSide] = "100%";
            __privateGet(this, _element).style[otherSide] = "100%";
            documentElement.style[side] = "".concat(__privateGet(this, _size), "px");
            if (__privateGet(this, _overlayer)) {
              __privateGet(this, _overlayer).element.style.margin = "0";
              __privateGet(this, _overlayer).element.style.left = __privateGet(this, _vertical) ? "0" : "".concat(__privateGet(this, _size), "px");
              __privateGet(this, _overlayer).element.style.top = __privateGet(this, _vertical) ? "".concat(__privateGet(this, _size), "px") : "0";
              __privateGet(this, _overlayer).element.style[side] = "".concat(expandedSize, "px");
              __privateGet(this, _overlayer).redraw();
            }
          } else {
            const side = __privateGet(this, _vertical) ? "width" : "height";
            const otherSide = __privateGet(this, _vertical) ? "height" : "width";
            const contentSize = documentElement.getBoundingClientRect()[side];
            const expandedSize = contentSize;
            const { margin } = __privateGet(this, _layout);
            const padding = __privateGet(this, _vertical) ? "0 ".concat(margin, "px") : "".concat(margin, "px 0");
            __privateGet(this, _element).style.padding = padding;
            __privateGet(this, _iframe).style[side] = "".concat(expandedSize, "px");
            __privateGet(this, _element).style[side] = "".concat(expandedSize, "px");
            __privateGet(this, _iframe).style[otherSide] = "100%";
            __privateGet(this, _element).style[otherSide] = "100%";
            if (__privateGet(this, _overlayer)) {
              __privateGet(this, _overlayer).element.style.margin = padding;
              __privateGet(this, _overlayer).element.style.left = "0";
              __privateGet(this, _overlayer).element.style.top = "0";
              __privateGet(this, _overlayer).element.style[side] = "".concat(expandedSize, "px");
              __privateGet(this, _overlayer).redraw();
            }
          }
          this.onExpand();
        }
        set overlayer(overlayer) {
          __privateSet(this, _overlayer, overlayer);
          __privateGet(this, _element).append(overlayer.element);
        }
        get overlayer() {
          return __privateGet(this, _overlayer);
        }
        destroy() {
          if (this.document) __privateGet(this, _observer2).unobserve(this.document.body);
        }
      };
      _observer2 = new WeakMap();
      _element = new WeakMap();
      _iframe = new WeakMap();
      _contentRange = new WeakMap();
      _overlayer = new WeakMap();
      _vertical = new WeakMap();
      _rtl = new WeakMap();
      _column = new WeakMap();
      _size = new WeakMap();
      _layout = new WeakMap();
      Paginator = class extends HTMLElement {
        constructor() {
          super();
          __privateAdd(this, _Paginator_instances);
          __privateAdd(this, _root2, this.attachShadow({ mode: "closed" }));
          __privateAdd(this, _observer3, new ResizeObserver(() => this.render()));
          __privateAdd(this, _top);
          __privateAdd(this, _background);
          __privateAdd(this, _container);
          __privateAdd(this, _header);
          __privateAdd(this, _footer);
          __privateAdd(this, _view);
          __privateAdd(this, _vertical2, false);
          __privateAdd(this, _rtl2, false);
          __privateAdd(this, _margin, 0);
          __privateAdd(this, _index2, -1);
          __privateAdd(this, _anchor, 0);
          // anchor view to a fraction (0-1), Range, or Element
          __privateAdd(this, _justAnchored, false);
          __privateAdd(this, _locked, false);
          // while true, prevent any further navigation
          __privateAdd(this, _styles);
          __privateAdd(this, _styleMap, /* @__PURE__ */ new WeakMap());
          __privateAdd(this, _mediaQuery, matchMedia("(prefers-color-scheme: dark)"));
          __privateAdd(this, _mediaQueryListener);
          __privateAdd(this, _scrollBounds);
          __privateAdd(this, _touchState);
          __privateAdd(this, _touchScrolled);
          __privateAdd(this, _lastVisibleRange);
          __privateGet(this, _root2).innerHTML = '<style>:host {\n  display: block;\n  container-type: size;\n}\n:host,\n#top {\n  box-sizing: border-box;\n  position: relative;\n  overflow: hidden;\n  width: 100%;\n  height: 100%;\n}\n#top {\n  --_gap: 7%;\n  --_margin: 48px;\n  --_max-inline-size: 720px;\n  --_max-block-size: 1440px;\n  --_max-column-count: 2;\n  --_max-column-count-portrait: 1;\n  --_max-column-count-spread: var(--_max-column-count);\n  --_half-gap: calc(var(--_gap) / 2);\n  --_max-width: calc(var(--_max-inline-size) * var(--_max-column-count-spread));\n  --_max-height: var(--_max-block-size);\n  display: grid;\n  grid-template-columns: minmax(var(--_half-gap), 1fr) var(--_half-gap) minmax(0, calc(var(--_max-width) - var(--_gap))) var(--_half-gap) minmax(var(--_half-gap), 1fr);\n  grid-template-rows: minmax(var(--_margin), 1fr) minmax(0, var(--_max-height)) minmax(var(--_margin), 1fr);\n}\n#top.vertical {\n  --_max-column-count-spread: var(--_max-column-count-portrait);\n  --_max-width: var(--_max-block-size);\n  --_max-height: calc(var(--_max-inline-size) * var(--_max-column-count-spread));\n}\n@media (orientation: portrait) {\n  #top {\n    --_max-column-count-spread: var(--_max-column-count-portrait);\n  }\n  #top.vertical {\n    --_max-column-count-spread: var(--_max-column-count);\n  }\n}\n#background {\n  grid-column: 1 / -1;\n  grid-row: 1 / -1;\n}\n#container {\n  grid-column: 2 / 5;\n  grid-row: 2;\n  overflow: hidden;\n}\n:host([flow="scrolled"]) #container {\n  grid-column: 1 / -1;\n  grid-row: 1 / -1;\n  overflow: auto;\n}\n#header {\n  grid-column: 3 / 4;\n  grid-row: 1;\n}\n#footer {\n  grid-column: 3 / 4;\n  grid-row: 3;\n  align-self: end;\n}\n#header,\n#footer {\n  display: grid;\n  height: var(--_margin);\n}\n#header > *,\n#footer > * {\n  display: flex;\n  align-items: center;\n  min-width: 0;\n}\n#header > * > *,\n#footer > * > * {\n  width: 100%;\n  overflow: hidden;\n  white-space: nowrap;\n  text-overflow: ellipsis;\n  text-align: center;\n  font-size: .75em;\n  opacity: .6;\n}\n</style>\n        <div id="top">\n            <div id="background" part="filter"></div>\n            <div id="header"></div>\n            <div id="container"></div>\n            <div id="footer"></div>\n        </div>\n        ';
          __privateSet(this, _top, __privateGet(this, _root2).getElementById("top"));
          __privateSet(this, _background, __privateGet(this, _root2).getElementById("background"));
          __privateSet(this, _container, __privateGet(this, _root2).getElementById("container"));
          __privateSet(this, _header, __privateGet(this, _root2).getElementById("header"));
          __privateSet(this, _footer, __privateGet(this, _root2).getElementById("footer"));
          __privateGet(this, _observer3).observe(__privateGet(this, _container));
          __privateGet(this, _container).addEventListener("scroll", () => this.dispatchEvent(new Event("scroll")));
          __privateGet(this, _container).addEventListener("scroll", debounce(() => {
            if (this.scrolled) {
              if (__privateGet(this, _justAnchored)) __privateSet(this, _justAnchored, false);
              else __privateMethod(this, _Paginator_instances, afterScroll_fn).call(this, "scroll");
            }
          }, 250));
          const opts = { passive: false };
          this.addEventListener("touchstart", __privateMethod(this, _Paginator_instances, onTouchStart_fn).bind(this), opts);
          this.addEventListener("touchmove", __privateMethod(this, _Paginator_instances, onTouchMove_fn).bind(this), opts);
          this.addEventListener("touchend", __privateMethod(this, _Paginator_instances, onTouchEnd_fn).bind(this));
          this.addEventListener("load", ({ detail: { doc } }) => {
            doc.addEventListener("touchstart", __privateMethod(this, _Paginator_instances, onTouchStart_fn).bind(this), opts);
            doc.addEventListener("touchmove", __privateMethod(this, _Paginator_instances, onTouchMove_fn).bind(this), opts);
            doc.addEventListener("touchend", __privateMethod(this, _Paginator_instances, onTouchEnd_fn).bind(this));
          });
          this.addEventListener("relocate", ({ detail }) => {
            if (detail.reason === "selection") setSelectionTo(__privateGet(this, _anchor), 0);
            else if (detail.reason === "navigation") {
              if (__privateGet(this, _anchor) === 1) setSelectionTo(detail.range, 1);
              else if (typeof __privateGet(this, _anchor) === "number")
                setSelectionTo(detail.range, -1);
              else setSelectionTo(__privateGet(this, _anchor), -1);
            }
          });
          const checkPointerSelection = debounce((range, sel) => {
            if (!sel.rangeCount) return;
            const selRange = sel.getRangeAt(0);
            const backward = selectionIsBackward(sel);
            if (backward && selRange.compareBoundaryPoints(Range.START_TO_START, range) < 0)
              this.prev();
            else if (!backward && selRange.compareBoundaryPoints(Range.END_TO_END, range) > 0)
              this.next();
          }, 700);
          this.addEventListener("load", ({ detail: { doc } }) => {
            let isPointerSelecting = false;
            doc.addEventListener("pointerdown", () => isPointerSelecting = true);
            doc.addEventListener("pointerup", () => isPointerSelecting = false);
            let isKeyboardSelecting = false;
            doc.addEventListener("keydown", () => isKeyboardSelecting = true);
            doc.addEventListener("keyup", () => isKeyboardSelecting = false);
            doc.addEventListener("selectionchange", () => {
              if (this.scrolled) return;
              const range = __privateGet(this, _lastVisibleRange);
              if (!range) return;
              const sel = doc.getSelection();
              if (!sel.rangeCount) return;
              if (isPointerSelecting && sel.type === "Range")
                checkPointerSelection(range, sel);
              else if (isKeyboardSelecting) {
                const selRange = sel.getRangeAt(0).cloneRange();
                const backward = selectionIsBackward(sel);
                if (!backward) selRange.collapse();
                __privateMethod(this, _Paginator_instances, scrollToAnchor_fn).call(this, selRange);
              }
            });
            doc.addEventListener("focusin", (e) => this.scrolled ? null : (
              // NOTE: `requestAnimationFrame` is needed in WebKit
              requestAnimationFrame(() => __privateMethod(this, _Paginator_instances, scrollToAnchor_fn).call(this, e.target))
            ));
          });
          __privateSet(this, _mediaQueryListener, () => {
            if (!__privateGet(this, _view)) return;
            __privateGet(this, _background).style.background = getBackground(__privateGet(this, _view).document);
          });
          __privateGet(this, _mediaQuery).addEventListener("change", __privateGet(this, _mediaQueryListener));
        }
        attributeChangedCallback(name, _, value) {
          switch (name) {
            case "flow":
              this.render();
              break;
            case "gap":
            case "margin":
            case "max-block-size":
            case "max-column-count":
              __privateGet(this, _top).style.setProperty("--_" + name, value);
              break;
            case "max-inline-size":
              __privateGet(this, _top).style.setProperty("--_" + name, value);
              this.render();
              break;
          }
        }
        open(book) {
          var _a2;
          this.bookDir = book.dir;
          this.sections = book.sections;
          (_a2 = book.transformTarget) == null ? void 0 : _a2.addEventListener("data", ({ detail }) => {
            if (detail.type !== "text/css") return;
            const w = innerWidth;
            const h = innerHeight;
            detail.data = Promise.resolve(detail.data).then((data) => data.replace(new RegExp("(?<=[{\\s;])-epub-", "gi"), "").replace(/(\d*\.?\d+)vw/gi, (_, d) => parseFloat(d) * w / 100 + "px").replace(/(\d*\.?\d+)vh/gi, (_, d) => parseFloat(d) * h / 100 + "px").replace(/page-break-(after|before|inside)\s*:/gi, (_, x) => "-webkit-column-break-".concat(x, ":")).replace(/break-(after|before|inside)\s*:\s*(avoid-)?page/gi, (_, x, y) => "break-".concat(x, ": ").concat(y != null ? y : "", "column")));
          });
        }
        render() {
          if (!__privateGet(this, _view)) return;
          __privateGet(this, _view).render(__privateMethod(this, _Paginator_instances, beforeRender_fn).call(this, {
            vertical: __privateGet(this, _vertical2),
            rtl: __privateGet(this, _rtl2)
          }));
          __privateMethod(this, _Paginator_instances, scrollToAnchor_fn).call(this, __privateGet(this, _anchor));
        }
        get scrolled() {
          return this.getAttribute("flow") === "scrolled";
        }
        get scrollProp() {
          const { scrolled } = this;
          return __privateGet(this, _vertical2) ? scrolled ? "scrollLeft" : "scrollTop" : scrolled ? "scrollTop" : "scrollLeft";
        }
        get sideProp() {
          const { scrolled } = this;
          return __privateGet(this, _vertical2) ? scrolled ? "width" : "height" : scrolled ? "height" : "width";
        }
        get size() {
          return __privateGet(this, _container).getBoundingClientRect()[this.sideProp];
        }
        get viewSize() {
          return __privateGet(this, _view).element.getBoundingClientRect()[this.sideProp];
        }
        get start() {
          return Math.abs(__privateGet(this, _container)[this.scrollProp]);
        }
        get end() {
          return this.start + this.size;
        }
        get page() {
          return Math.floor((this.start + this.end) / 2 / this.size);
        }
        get pages() {
          return Math.round(this.viewSize / this.size);
        }
        scrollBy(dx, dy) {
          const delta = __privateGet(this, _vertical2) ? dy : dx;
          const element = __privateGet(this, _container);
          const { scrollProp } = this;
          const [offset, a, b] = __privateGet(this, _scrollBounds);
          const rtl = __privateGet(this, _rtl2);
          const min = rtl ? offset - b : offset - a;
          const max2 = rtl ? offset + a : offset + b;
          element[scrollProp] = Math.max(min, Math.min(
            max2,
            element[scrollProp] + delta
          ));
        }
        snap(vx, vy) {
          const velocity = __privateGet(this, _vertical2) ? vy : vx;
          const [offset, a, b] = __privateGet(this, _scrollBounds);
          const { start, end, pages, size } = this;
          const min = Math.abs(offset) - a;
          const max2 = Math.abs(offset) + b;
          const d = velocity * (__privateGet(this, _rtl2) ? -size : size);
          const page = Math.floor(
            Math.max(min, Math.min(max2, (start + end) / 2 + (isNaN(d) ? 0 : d))) / size
          );
          __privateMethod(this, _Paginator_instances, scrollToPage_fn).call(this, page, "snap").then(() => {
            const dir = page <= 0 ? -1 : page >= pages - 1 ? 1 : null;
            if (dir) return __privateMethod(this, _Paginator_instances, goTo_fn).call(this, {
              index: __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, dir),
              anchor: dir < 0 ? () => 1 : () => 0
            });
          });
        }
        async scrollToAnchor(anchor, select) {
          return __privateMethod(this, _Paginator_instances, scrollToAnchor_fn).call(this, anchor, select ? "selection" : "navigation");
        }
        async goTo(target) {
          if (__privateGet(this, _locked)) return;
          const resolved = await target;
          if (__privateMethod(this, _Paginator_instances, canGoToIndex_fn).call(this, resolved.index)) return __privateMethod(this, _Paginator_instances, goTo_fn).call(this, resolved);
        }
        get atStart() {
          return __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, -1) == null && this.page <= 1;
        }
        get atEnd() {
          return __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, 1) == null && this.page >= this.pages - 2;
        }
        prev(distance) {
          return __privateMethod(this, _Paginator_instances, turnPage_fn).call(this, -1, distance);
        }
        next(distance) {
          return __privateMethod(this, _Paginator_instances, turnPage_fn).call(this, 1, distance);
        }
        prevSection() {
          return this.goTo({ index: __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, -1) });
        }
        nextSection() {
          return this.goTo({ index: __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, 1) });
        }
        firstSection() {
          const index2 = this.sections.findIndex((section) => section.linear !== "no");
          return this.goTo({ index: index2 });
        }
        lastSection() {
          const index2 = this.sections.findLastIndex((section) => section.linear !== "no");
          return this.goTo({ index: index2 });
        }
        getContents() {
          if (__privateGet(this, _view)) return [{
            index: __privateGet(this, _index2),
            overlayer: __privateGet(this, _view).overlayer,
            doc: __privateGet(this, _view).document
          }];
          return [];
        }
        setStyles(styles) {
          var _a2, _b2, _c, _d, _e;
          __privateSet(this, _styles, styles);
          const $$styles = __privateGet(this, _styleMap).get((_a2 = __privateGet(this, _view)) == null ? void 0 : _a2.document);
          if (!$$styles) return;
          const [$beforeStyle, $style] = $$styles;
          if (Array.isArray(styles)) {
            const [beforeStyle, style2] = styles;
            $beforeStyle.textContent = beforeStyle;
            $style.textContent = style2;
          } else $style.textContent = styles;
          requestAnimationFrame(() => __privateGet(this, _background).style.background = getBackground(__privateGet(this, _view).document));
          (_e = (_d = (_c = (_b2 = __privateGet(this, _view)) == null ? void 0 : _b2.document) == null ? void 0 : _c.fonts) == null ? void 0 : _d.ready) == null ? void 0 : _e.then(() => __privateGet(this, _view).expand());
        }
        focusView() {
          __privateGet(this, _view).document.defaultView.focus();
        }
        destroy() {
          var _a2, _b2;
          __privateGet(this, _observer3).unobserve(this);
          __privateGet(this, _view).destroy();
          __privateSet(this, _view, null);
          (_b2 = (_a2 = this.sections[__privateGet(this, _index2)]) == null ? void 0 : _a2.unload) == null ? void 0 : _b2.call(_a2);
          __privateGet(this, _mediaQuery).removeEventListener("change", __privateGet(this, _mediaQueryListener));
        }
      };
      _root2 = new WeakMap();
      _observer3 = new WeakMap();
      _top = new WeakMap();
      _background = new WeakMap();
      _container = new WeakMap();
      _header = new WeakMap();
      _footer = new WeakMap();
      _view = new WeakMap();
      _vertical2 = new WeakMap();
      _rtl2 = new WeakMap();
      _margin = new WeakMap();
      _index2 = new WeakMap();
      _anchor = new WeakMap();
      _justAnchored = new WeakMap();
      _locked = new WeakMap();
      _styles = new WeakMap();
      _styleMap = new WeakMap();
      _mediaQuery = new WeakMap();
      _mediaQueryListener = new WeakMap();
      _scrollBounds = new WeakMap();
      _touchState = new WeakMap();
      _touchScrolled = new WeakMap();
      _lastVisibleRange = new WeakMap();
      _Paginator_instances = new WeakSet();
      createView_fn = function() {
        if (__privateGet(this, _view)) {
          __privateGet(this, _view).destroy();
          __privateGet(this, _container).removeChild(__privateGet(this, _view).element);
        }
        __privateSet(this, _view, new View({
          container: this,
          onExpand: () => __privateMethod(this, _Paginator_instances, scrollToAnchor_fn).call(this, __privateGet(this, _anchor))
        }));
        __privateGet(this, _container).append(__privateGet(this, _view).element);
        return __privateGet(this, _view);
      };
      beforeRender_fn = function({ vertical, rtl, background }) {
        __privateSet(this, _vertical2, vertical);
        __privateSet(this, _rtl2, rtl);
        __privateGet(this, _top).classList.toggle("vertical", vertical);
        __privateGet(this, _background).style.background = background;
        const { width, height } = __privateGet(this, _container).getBoundingClientRect();
        const size = vertical ? height : width;
        const style2 = getComputedStyle(__privateGet(this, _top));
        const maxInlineSize = parseFloat(style2.getPropertyValue("--_max-inline-size"));
        const maxColumnCount = parseInt(style2.getPropertyValue("--_max-column-count-spread"));
        const margin = parseFloat(style2.getPropertyValue("--_margin"));
        __privateSet(this, _margin, margin);
        const g = parseFloat(style2.getPropertyValue("--_gap")) / 100;
        const gap = -g / (g - 1) * size;
        const flow = this.getAttribute("flow");
        if (flow === "scrolled") {
          this.setAttribute("dir", vertical ? "rtl" : "ltr");
          __privateGet(this, _top).style.padding = "0";
          const columnWidth2 = maxInlineSize;
          this.heads = null;
          this.feet = null;
          __privateGet(this, _header).replaceChildren();
          __privateGet(this, _footer).replaceChildren();
          return { flow, margin, gap, columnWidth: columnWidth2 };
        }
        const divisor = Math.min(maxColumnCount, Math.ceil(size / maxInlineSize));
        const columnWidth = size / divisor - gap;
        this.setAttribute("dir", rtl ? "rtl" : "ltr");
        const marginalDivisor = vertical ? Math.min(2, Math.ceil(width / maxInlineSize)) : divisor;
        const marginalStyle = {
          gridTemplateColumns: "repeat(".concat(marginalDivisor, ", 1fr)"),
          gap: "".concat(gap, "px"),
          direction: this.bookDir === "rtl" ? "rtl" : "ltr"
        };
        Object.assign(__privateGet(this, _header).style, marginalStyle);
        Object.assign(__privateGet(this, _footer).style, marginalStyle);
        const heads = makeMarginals(marginalDivisor, "head");
        const feet = makeMarginals(marginalDivisor, "foot");
        this.heads = heads.map((el) => el.children[0]);
        this.feet = feet.map((el) => el.children[0]);
        __privateGet(this, _header).replaceChildren(...heads);
        __privateGet(this, _footer).replaceChildren(...feet);
        return { height, width, margin, gap, columnWidth };
      };
      onTouchStart_fn = function(e) {
        const touch = e.changedTouches[0];
        __privateSet(this, _touchState, {
          x: touch == null ? void 0 : touch.screenX,
          y: touch == null ? void 0 : touch.screenY,
          t: e.timeStamp,
          vx: 0,
          xy: 0
        });
      };
      onTouchMove_fn = function(e) {
        const state2 = __privateGet(this, _touchState);
        if (state2.pinched) return;
        state2.pinched = globalThis.visualViewport.scale > 1;
        if (this.scrolled || state2.pinched) return;
        if (e.touches.length > 1) {
          if (__privateGet(this, _touchScrolled)) e.preventDefault();
          return;
        }
        e.preventDefault();
        const touch = e.changedTouches[0];
        const x = touch.screenX, y = touch.screenY;
        const dx = state2.x - x, dy = state2.y - y;
        const dt = e.timeStamp - state2.t;
        state2.x = x;
        state2.y = y;
        state2.t = e.timeStamp;
        state2.vx = dx / dt;
        state2.vy = dy / dt;
        __privateSet(this, _touchScrolled, true);
        this.scrollBy(dx, dy);
      };
      onTouchEnd_fn = function() {
        __privateSet(this, _touchScrolled, false);
        if (this.scrolled) return;
        requestAnimationFrame(() => {
          if (globalThis.visualViewport.scale === 1)
            this.snap(__privateGet(this, _touchState).vx, __privateGet(this, _touchState).vy);
        });
      };
      // allows one to process rects as if they were LTR and horizontal
      getRectMapper_fn = function() {
        if (this.scrolled) {
          const size = this.viewSize;
          const margin = __privateGet(this, _margin);
          return __privateGet(this, _vertical2) ? ({ left, right }) => ({ left: size - right - margin, right: size - left - margin }) : ({ top, bottom }) => ({ left: top + margin, right: bottom + margin });
        }
        const pxSize = this.pages * this.size;
        return __privateGet(this, _rtl2) ? ({ left, right }) => ({ left: pxSize - right, right: pxSize - left }) : __privateGet(this, _vertical2) ? ({ top, bottom }) => ({ left: top, right: bottom }) : (f) => f;
      };
      scrollToRect_fn = async function(rect, reason) {
        if (this.scrolled) {
          const offset2 = __privateMethod(this, _Paginator_instances, getRectMapper_fn).call(this)(rect).left - __privateGet(this, _margin);
          return __privateMethod(this, _Paginator_instances, scrollTo_fn).call(this, offset2, reason);
        }
        const offset = __privateMethod(this, _Paginator_instances, getRectMapper_fn).call(this)(rect).left;
        return __privateMethod(this, _Paginator_instances, scrollToPage_fn).call(this, Math.floor(offset / this.size) + (__privateGet(this, _rtl2) ? -1 : 1), reason);
      };
      scrollTo_fn = async function(offset, reason, smooth) {
        const element = __privateGet(this, _container);
        const { scrollProp, size } = this;
        if (element[scrollProp] === offset) {
          __privateSet(this, _scrollBounds, [offset, this.atStart ? 0 : size, this.atEnd ? 0 : size]);
          __privateMethod(this, _Paginator_instances, afterScroll_fn).call(this, reason);
          return;
        }
        if (this.scrolled && __privateGet(this, _vertical2)) offset = -offset;
        if ((reason === "snap" || smooth) && this.hasAttribute("animated")) return animate(
          element[scrollProp],
          offset,
          300,
          easeOutQuad,
          (x) => element[scrollProp] = x
        ).then(() => {
          __privateSet(this, _scrollBounds, [offset, this.atStart ? 0 : size, this.atEnd ? 0 : size]);
          __privateMethod(this, _Paginator_instances, afterScroll_fn).call(this, reason);
        });
        else {
          element[scrollProp] = offset;
          __privateSet(this, _scrollBounds, [offset, this.atStart ? 0 : size, this.atEnd ? 0 : size]);
          __privateMethod(this, _Paginator_instances, afterScroll_fn).call(this, reason);
        }
      };
      scrollToPage_fn = async function(page, reason, smooth) {
        const offset = this.size * (__privateGet(this, _rtl2) ? -page : page);
        return __privateMethod(this, _Paginator_instances, scrollTo_fn).call(this, offset, reason, smooth);
      };
      scrollToAnchor_fn = async function(anchor, reason = "anchor") {
        var _a2, _b2;
        __privateSet(this, _anchor, anchor);
        const rects = (_b2 = (_a2 = uncollapse(anchor)) == null ? void 0 : _a2.getClientRects) == null ? void 0 : _b2.call(_a2);
        if (rects) {
          const rect = Array.from(rects).find((r) => r.width > 0 && r.height > 0) || rects[0];
          if (!rect) return;
          await __privateMethod(this, _Paginator_instances, scrollToRect_fn).call(this, rect, reason);
          return;
        }
        if (this.scrolled) {
          await __privateMethod(this, _Paginator_instances, scrollTo_fn).call(this, anchor * this.viewSize, reason);
          return;
        }
        const { pages } = this;
        if (!pages) return;
        const textPages = pages - 2;
        const newPage = Math.round(anchor * (textPages - 1));
        await __privateMethod(this, _Paginator_instances, scrollToPage_fn).call(this, newPage + 1, reason);
      };
      getVisibleRange_fn = function() {
        if (this.scrolled) return getVisibleRange(
          __privateGet(this, _view).document,
          this.start + __privateGet(this, _margin),
          this.end - __privateGet(this, _margin),
          __privateMethod(this, _Paginator_instances, getRectMapper_fn).call(this)
        );
        const size = __privateGet(this, _rtl2) ? -this.size : this.size;
        return getVisibleRange(
          __privateGet(this, _view).document,
          this.start - size,
          this.end - size,
          __privateMethod(this, _Paginator_instances, getRectMapper_fn).call(this)
        );
      };
      afterScroll_fn = function(reason) {
        const range = __privateMethod(this, _Paginator_instances, getVisibleRange_fn).call(this);
        __privateSet(this, _lastVisibleRange, range);
        if (reason !== "selection" && reason !== "navigation" && reason !== "anchor")
          __privateSet(this, _anchor, range);
        else __privateSet(this, _justAnchored, true);
        const index2 = __privateGet(this, _index2);
        const detail = { reason, range, index: index2 };
        if (this.scrolled) detail.fraction = this.start / this.viewSize;
        else if (this.pages > 0) {
          const { page, pages } = this;
          __privateGet(this, _header).style.visibility = page > 1 ? "visible" : "hidden";
          detail.fraction = (page - 1) / (pages - 2);
          detail.size = 1 / (pages - 2);
        }
        this.dispatchEvent(new CustomEvent("relocate", { detail }));
      };
      display_fn = async function(promise) {
        var _a2, _b2, _c;
        const { index: index2, src, anchor, onLoad, select } = await promise;
        __privateSet(this, _index2, index2);
        const hasFocus = (_b2 = (_a2 = __privateGet(this, _view)) == null ? void 0 : _a2.document) == null ? void 0 : _b2.hasFocus();
        if (src) {
          const view2 = __privateMethod(this, _Paginator_instances, createView_fn).call(this);
          const afterLoad = (doc) => {
            if (doc.head) {
              const $styleBefore = doc.createElement("style");
              doc.head.prepend($styleBefore);
              const $style = doc.createElement("style");
              doc.head.append($style);
              __privateGet(this, _styleMap).set(doc, [$styleBefore, $style]);
            }
            onLoad == null ? void 0 : onLoad({ doc, index: index2 });
          };
          const beforeRender = __privateMethod(this, _Paginator_instances, beforeRender_fn).bind(this);
          await view2.load(src, afterLoad, beforeRender);
          this.dispatchEvent(new CustomEvent("create-overlayer", {
            detail: {
              doc: view2.document,
              index: index2,
              attach: (overlayer) => view2.overlayer = overlayer
            }
          }));
          __privateSet(this, _view, view2);
        }
        await this.scrollToAnchor((_c = typeof anchor === "function" ? anchor(__privateGet(this, _view).document) : anchor) != null ? _c : 0, select);
        if (hasFocus) this.focusView();
      };
      canGoToIndex_fn = function(index2) {
        return index2 >= 0 && index2 <= this.sections.length - 1;
      };
      goTo_fn = async function({ index: index2, anchor, select }) {
        if (index2 === __privateGet(this, _index2)) await __privateMethod(this, _Paginator_instances, display_fn).call(this, { index: index2, anchor, select });
        else {
          const oldIndex = __privateGet(this, _index2);
          const onLoad = (detail) => {
            var _a2, _b2;
            (_b2 = (_a2 = this.sections[oldIndex]) == null ? void 0 : _a2.unload) == null ? void 0 : _b2.call(_a2);
            this.setStyles(__privateGet(this, _styles));
            this.dispatchEvent(new CustomEvent("load", { detail }));
          };
          await __privateMethod(this, _Paginator_instances, display_fn).call(this, Promise.resolve(this.sections[index2].load()).then((src) => ({ index: index2, src, anchor, onLoad, select })).catch((e) => {
            console.warn(e);
            console.warn(new Error("Failed to load section ".concat(index2)));
            return {};
          }));
        }
      };
      scrollPrev_fn = function(distance) {
        if (!__privateGet(this, _view)) return true;
        if (this.scrolled) {
          if (this.start > 0) return __privateMethod(this, _Paginator_instances, scrollTo_fn).call(this, Math.max(0, this.start - (distance != null ? distance : this.size)), null, true);
          return true;
        }
        if (this.atStart) return;
        const page = this.page - 1;
        return __privateMethod(this, _Paginator_instances, scrollToPage_fn).call(this, page, "page", true).then(() => page <= 0);
      };
      scrollNext_fn = function(distance) {
        if (!__privateGet(this, _view)) return true;
        if (this.scrolled) {
          if (this.viewSize - this.end > 2) return __privateMethod(this, _Paginator_instances, scrollTo_fn).call(this, Math.min(this.viewSize, distance ? this.start + distance : this.end), null, true);
          return true;
        }
        if (this.atEnd) return;
        const page = this.page + 1;
        const pages = this.pages;
        return __privateMethod(this, _Paginator_instances, scrollToPage_fn).call(this, page, "page", true).then(() => page >= pages - 1);
      };
      adjacentIndex_fn = function(dir) {
        var _a2;
        for (let index2 = __privateGet(this, _index2) + dir; __privateMethod(this, _Paginator_instances, canGoToIndex_fn).call(this, index2); index2 += dir)
          if (((_a2 = this.sections[index2]) == null ? void 0 : _a2.linear) !== "no") return index2;
      };
      turnPage_fn = async function(dir, distance) {
        if (__privateGet(this, _locked)) return;
        __privateSet(this, _locked, true);
        const prev = dir === -1;
        const shouldGo = await (prev ? __privateMethod(this, _Paginator_instances, scrollPrev_fn).call(this, distance) : __privateMethod(this, _Paginator_instances, scrollNext_fn).call(this, distance));
        if (shouldGo) await __privateMethod(this, _Paginator_instances, goTo_fn).call(this, {
          index: __privateMethod(this, _Paginator_instances, adjacentIndex_fn).call(this, dir),
          anchor: prev ? () => 1 : () => 0
        });
        if (shouldGo || !this.hasAttribute("animated")) await wait(100);
        __privateSet(this, _locked, false);
      };
      __publicField(Paginator, "observedAttributes", [
        "flow",
        "gap",
        "margin",
        "max-inline-size",
        "max-block-size",
        "max-column-count"
      ]);
      customElements.define("foliate-paginator", Paginator);
    }
  });

  // vendor/foliate/search.js
  var search_exports = {};
  __export(search_exports, {
    search: () => search,
    searchMatcher: () => searchMatcher
  });
  var CONTEXT_LENGTH, normalizeWhitespace3, makeExcerpt, simpleSearch, segmenterSearch, search, searchMatcher;
  var init_search = __esm({
    "vendor/foliate/search.js"() {
      CONTEXT_LENGTH = 50;
      normalizeWhitespace3 = (str) => str.replace(/\s+/g, " ");
      makeExcerpt = (strs, { startIndex, startOffset, endIndex, endOffset }) => {
        const start = strs[startIndex];
        const end = strs[endIndex];
        const match = start === end ? start.slice(startOffset, endOffset) : start.slice(startOffset) + strs.slice(startIndex + 1, endIndex).join("") + end.slice(0, endOffset);
        const trimmedStart = normalizeWhitespace3(start.slice(0, startOffset)).trimStart();
        const trimmedEnd = normalizeWhitespace3(end.slice(endOffset)).trimEnd();
        const ellipsisPre = trimmedStart.length < CONTEXT_LENGTH ? "" : "\u2026";
        const ellipsisPost = trimmedEnd.length < CONTEXT_LENGTH ? "" : "\u2026";
        const pre = "".concat(ellipsisPre).concat(trimmedStart.slice(-CONTEXT_LENGTH));
        const post = "".concat(trimmedEnd.slice(0, CONTEXT_LENGTH)).concat(ellipsisPost);
        return { pre, match, post };
      };
      simpleSearch = function* (strs, query, options = {}) {
        const { locales = "en", sensitivity } = options;
        const matchCase = sensitivity === "variant";
        const haystack = strs.join("");
        const lowerHaystack = matchCase ? haystack : haystack.toLocaleLowerCase(locales);
        const needle = matchCase ? query : query.toLocaleLowerCase(locales);
        const needleLength = needle.length;
        let index2 = -1;
        let strIndex = -1;
        let sum = 0;
        do {
          index2 = lowerHaystack.indexOf(needle, index2 + 1);
          if (index2 > -1) {
            while (sum <= index2) sum += strs[++strIndex].length;
            const startIndex = strIndex;
            const startOffset = index2 - (sum - strs[strIndex].length);
            const end = index2 + needleLength;
            while (sum < end && strIndex + 1 < strs.length) sum += strs[++strIndex].length;
            const endIndex = strIndex;
            const endOffset = end - (sum - strs[strIndex].length);
            const range = { startIndex, startOffset, endIndex, endOffset };
            yield { range, excerpt: makeExcerpt(strs, range) };
          }
        } while (index2 > -1);
      };
      segmenterSearch = function* (strs, query, options = {}) {
        var _a2;
        const { locales = "en", granularity = "word", sensitivity = "base" } = options;
        let segmenter, collator;
        try {
          segmenter = new Intl.Segmenter(locales, { usage: "search", granularity });
          collator = new Intl.Collator(locales, { sensitivity });
        } catch (e) {
          console.warn(e);
          segmenter = new Intl.Segmenter("en", { usage: "search", granularity });
          collator = new Intl.Collator("en", { sensitivity });
        }
        const queryLength = Array.from(segmenter.segment(query)).length;
        const substrArr = [];
        let strIndex = 0;
        let segments = segmenter.segment(strs[strIndex])[Symbol.iterator]();
        main: while (strIndex < strs.length) {
          while (substrArr.length < queryLength) {
            const { done, value } = segments.next();
            if (done) {
              strIndex++;
              if (strIndex < strs.length) {
                segments = segmenter.segment(strs[strIndex])[Symbol.iterator]();
                continue;
              } else break main;
            }
            const { index: index2, segment } = value;
            if (!/[^\u00AD\u061C\u180E\u200B-\u200F\u202A-\u202E\u2060-\u206F\uFEFF]/.test(segment)) continue;
            if (/\s/u.test(segment)) {
              if (!/\s/u.test((_a2 = substrArr[substrArr.length - 1]) == null ? void 0 : _a2.segment))
                substrArr.push({ strIndex, index: index2, segment: " " });
              continue;
            }
            value.strIndex = strIndex;
            substrArr.push(value);
          }
          const substr = substrArr.map((x) => x.segment).join("");
          if (collator.compare(query, substr) === 0) {
            const endIndex = strIndex;
            const lastSeg = substrArr[substrArr.length - 1];
            const endOffset = lastSeg.index + lastSeg.segment.length;
            const startIndex = substrArr[0].strIndex;
            const startOffset = substrArr[0].index;
            const range = { startIndex, startOffset, endIndex, endOffset };
            yield { range, excerpt: makeExcerpt(strs, range) };
          }
          substrArr.shift();
        }
      };
      search = (strs, query, options) => {
        const { granularity = "grapheme", sensitivity = "base" } = options;
        if (!(Intl == null ? void 0 : Intl.Segmenter) || granularity === "grapheme" && (sensitivity === "variant" || sensitivity === "accent"))
          return simpleSearch(strs, query, options);
        return segmenterSearch(strs, query, options);
      };
      searchMatcher = (textWalker2, opts) => {
        const { defaultLocale, matchCase, matchDiacritics, matchWholeWords, acceptNode: acceptNode2 } = opts;
        return function* (doc, query) {
          const iter = textWalker2(doc, function* (strs, makeRange2) {
            for (const result of search(strs, query, {
              locales: doc.body.lang || doc.documentElement.lang || defaultLocale || "en",
              granularity: matchWholeWords ? "word" : "grapheme",
              sensitivity: matchDiacritics && matchCase ? "variant" : matchDiacritics && !matchCase ? "accent" : !matchDiacritics && matchCase ? "case" : "base"
            })) {
              const { startIndex, startOffset, endIndex, endOffset } = result.range;
              result.range = makeRange2(startIndex, startOffset, endIndex, endOffset);
              yield result;
            }
          }, acceptNode2);
          for (const result of iter) yield result;
        };
      };
    }
  });

  // vendor/foliate/tts.js
  var tts_exports = {};
  __export(tts_exports, {
    TTS: () => TTS
  });
  function* getBlocks(doc) {
    var _a2, _b2;
    let last;
    const walker = doc.createTreeWalker(doc.body, NodeFilter.SHOW_ELEMENT);
    for (let node = walker.nextNode(); node; node = walker.nextNode()) {
      const name = node.tagName.toLowerCase();
      if (blockTags.has(name)) {
        if (last) {
          last.setEndBefore(node);
          if (!rangeIsEmpty(last)) yield last;
        }
        last = doc.createRange();
        last.setStart(node, 0);
      }
    }
    if (!last) {
      last = doc.createRange();
      last.setStart((_a2 = doc.body.firstChild) != null ? _a2 : doc.body, 0);
    }
    last.setEndAfter((_b2 = doc.body.lastChild) != null ? _b2 : doc.body);
    if (!rangeIsEmpty(last)) yield last;
  }
  var NS3, blockTags, getLang, getAlphabet, getSegmenter, fragmentToSSML, getFragmentWithMarks, rangeIsEmpty, _arr, _iter, _index3, _f, ListIterator, _list, _ranges, _lastMark, _serializer, _TTS_instances, getMarkElement_fn, speak_fn, TTS;
  var init_tts = __esm({
    "vendor/foliate/tts.js"() {
      NS3 = {
        XML: "http://www.w3.org/XML/1998/namespace",
        SSML: "http://www.w3.org/2001/10/synthesis"
      };
      blockTags = /* @__PURE__ */ new Set([
        "article",
        "aside",
        "audio",
        "blockquote",
        "caption",
        "details",
        "dialog",
        "div",
        "dl",
        "dt",
        "dd",
        "figure",
        "footer",
        "form",
        "figcaption",
        "h1",
        "h2",
        "h3",
        "h4",
        "h5",
        "h6",
        "header",
        "hgroup",
        "hr",
        "li",
        "main",
        "math",
        "nav",
        "ol",
        "p",
        "pre",
        "section",
        "tr"
      ]);
      getLang = (el) => {
        var _a2;
        const x = el.lang || ((_a2 = el == null ? void 0 : el.getAttributeNS) == null ? void 0 : _a2.call(el, NS3.XML, "lang"));
        return x ? x : el.parentElement ? getLang(el.parentElement) : null;
      };
      getAlphabet = (el) => {
        var _a2;
        const x = (_a2 = el == null ? void 0 : el.getAttributeNS) == null ? void 0 : _a2.call(el, NS3.XML, "lang");
        return x ? x : el.parentElement ? getAlphabet(el.parentElement) : null;
      };
      getSegmenter = (lang = "en", granularity = "word") => {
        const segmenter = new Intl.Segmenter(lang, { granularity });
        const granularityIsWord = granularity === "word";
        return function* (strs, makeRange2) {
          const str = strs.join("");
          let name = 0;
          let strIndex = -1;
          let sum = 0;
          for (const { index: index2, segment, isWordLike } of segmenter.segment(str)) {
            if (granularityIsWord && !isWordLike) continue;
            while (sum <= index2) sum += strs[++strIndex].length;
            const startIndex = strIndex;
            const startOffset = index2 - (sum - strs[strIndex].length);
            const end = index2 + segment.length - 1;
            if (end < str.length) while (sum <= end) sum += strs[++strIndex].length;
            const endIndex = strIndex;
            const endOffset = end - (sum - strs[strIndex].length) + 1;
            yield [
              (name++).toString(),
              makeRange2(startIndex, startOffset, endIndex, endOffset)
            ];
          }
        };
      };
      fragmentToSSML = (fragment, inherited) => {
        const ssml = document.implementation.createDocument(NS3.SSML, "speak");
        const { lang } = inherited;
        if (lang) ssml.documentElement.setAttributeNS(NS3.XML, "lang", lang);
        const convert = (node, parent, inheritedAlphabet) => {
          var _a2, _b2, _c;
          if (!node) return;
          if (node.nodeType === 3) return ssml.createTextNode(node.textContent);
          if (node.nodeType === 4) return ssml.createCDATASection(node.textContent);
          if (node.nodeType !== 1 && node.nodeType !== 11) return;
          let el;
          const nodeName = node.nodeName.toLowerCase();
          if (nodeName === "foliate-mark") {
            el = ssml.createElementNS(NS3.SSML, "mark");
            el.setAttribute("name", node.dataset.name);
          } else if (nodeName === "br")
            el = ssml.createElementNS(NS3.SSML, "break");
          else if (nodeName === "em" || nodeName === "strong")
            el = ssml.createElementNS(NS3.SSML, "emphasis");
          const lang2 = node.lang || ((_a2 = node.getAttributeNS) == null ? void 0 : _a2.call(node, NS3.XML, "lang"));
          if (lang2) {
            if (!el) el = ssml.createElementNS(NS3.SSML, "lang");
            el.setAttributeNS(NS3.XML, "lang", lang2);
          }
          const alphabet = ((_b2 = node.getAttributeNS) == null ? void 0 : _b2.call(node, NS3.SSML, "alphabet")) || inheritedAlphabet;
          if (!el) {
            const ph = (_c = node.getAttributeNS) == null ? void 0 : _c.call(node, NS3.SSML, "ph");
            if (ph) {
              el = ssml.createElementNS(NS3.SSML, "phoneme");
              if (alphabet) el.setAttribute("alphabet", alphabet);
              el.setAttribute("ph", ph);
            }
          }
          if (!el) el = parent;
          let child = node.firstChild;
          while (child) {
            const childEl = convert(child, el, alphabet);
            if (childEl && el !== childEl) el.append(childEl);
            child = child.nextSibling;
          }
          return el;
        };
        convert(fragment, ssml.documentElement, inherited.alphabet);
        return ssml;
      };
      getFragmentWithMarks = (range, textWalker2, granularity) => {
        const lang = getLang(range.commonAncestorContainer);
        const alphabet = getAlphabet(range.commonAncestorContainer);
        const segmenter = getSegmenter(lang, granularity);
        const fragment = range.cloneContents();
        const entries = [...textWalker2(range, segmenter)];
        const fragmentEntries = [...textWalker2(fragment, segmenter)];
        for (const [name, range2] of fragmentEntries) {
          const mark = document.createElement("foliate-mark");
          mark.dataset.name = name;
          range2.insertNode(mark);
        }
        const ssml = fragmentToSSML(fragment, { lang, alphabet });
        return { entries, ssml };
      };
      rangeIsEmpty = (range) => !range.toString().trim();
      ListIterator = class {
        constructor(iter, f = (x) => x) {
          __privateAdd(this, _arr, []);
          __privateAdd(this, _iter);
          __privateAdd(this, _index3, -1);
          __privateAdd(this, _f);
          __privateSet(this, _iter, iter);
          __privateSet(this, _f, f);
        }
        current() {
          if (__privateGet(this, _arr)[__privateGet(this, _index3)]) return __privateGet(this, _f).call(this, __privateGet(this, _arr)[__privateGet(this, _index3)]);
        }
        first() {
          const newIndex = 0;
          if (__privateGet(this, _arr)[newIndex]) {
            __privateSet(this, _index3, newIndex);
            return __privateGet(this, _f).call(this, __privateGet(this, _arr)[newIndex]);
          }
        }
        prev() {
          const newIndex = __privateGet(this, _index3) - 1;
          if (__privateGet(this, _arr)[newIndex]) {
            __privateSet(this, _index3, newIndex);
            return __privateGet(this, _f).call(this, __privateGet(this, _arr)[newIndex]);
          }
        }
        next() {
          const newIndex = __privateGet(this, _index3) + 1;
          if (__privateGet(this, _arr)[newIndex]) {
            __privateSet(this, _index3, newIndex);
            return __privateGet(this, _f).call(this, __privateGet(this, _arr)[newIndex]);
          }
          while (true) {
            const { done, value } = __privateGet(this, _iter).next();
            if (done) break;
            __privateGet(this, _arr).push(value);
            if (__privateGet(this, _arr)[newIndex]) {
              __privateSet(this, _index3, newIndex);
              return __privateGet(this, _f).call(this, __privateGet(this, _arr)[newIndex]);
            }
          }
        }
        find(f) {
          const index2 = __privateGet(this, _arr).findIndex((x) => f(x));
          if (index2 > -1) {
            __privateSet(this, _index3, index2);
            return __privateGet(this, _f).call(this, __privateGet(this, _arr)[index2]);
          }
          while (true) {
            const { done, value } = __privateGet(this, _iter).next();
            if (done) break;
            __privateGet(this, _arr).push(value);
            if (f(value)) {
              __privateSet(this, _index3, __privateGet(this, _arr).length - 1);
              return __privateGet(this, _f).call(this, value);
            }
          }
        }
      };
      _arr = new WeakMap();
      _iter = new WeakMap();
      _index3 = new WeakMap();
      _f = new WeakMap();
      TTS = class {
        constructor(doc, textWalker2, highlight, granularity) {
          __privateAdd(this, _TTS_instances);
          __privateAdd(this, _list);
          __privateAdd(this, _ranges);
          __privateAdd(this, _lastMark);
          __privateAdd(this, _serializer, new XMLSerializer());
          this.doc = doc;
          this.highlight = highlight;
          __privateSet(this, _list, new ListIterator(getBlocks(doc), (range) => {
            const { entries, ssml } = getFragmentWithMarks(range, textWalker2, granularity);
            __privateSet(this, _ranges, new Map(entries));
            return [ssml, range];
          }));
        }
        start() {
          var _a2;
          __privateSet(this, _lastMark, null);
          const [doc] = (_a2 = __privateGet(this, _list).first()) != null ? _a2 : [];
          if (!doc) return this.next();
          return __privateMethod(this, _TTS_instances, speak_fn).call(this, doc, (ssml) => __privateMethod(this, _TTS_instances, getMarkElement_fn).call(this, ssml, __privateGet(this, _lastMark)));
        }
        resume() {
          var _a2;
          const [doc] = (_a2 = __privateGet(this, _list).current()) != null ? _a2 : [];
          if (!doc) return this.next();
          return __privateMethod(this, _TTS_instances, speak_fn).call(this, doc, (ssml) => __privateMethod(this, _TTS_instances, getMarkElement_fn).call(this, ssml, __privateGet(this, _lastMark)));
        }
        prev(paused) {
          var _a2;
          __privateSet(this, _lastMark, null);
          const [doc, range] = (_a2 = __privateGet(this, _list).prev()) != null ? _a2 : [];
          if (paused && range) this.highlight(range.cloneRange());
          return __privateMethod(this, _TTS_instances, speak_fn).call(this, doc);
        }
        next(paused) {
          var _a2;
          __privateSet(this, _lastMark, null);
          const [doc, range] = (_a2 = __privateGet(this, _list).next()) != null ? _a2 : [];
          if (paused && range) this.highlight(range.cloneRange());
          return __privateMethod(this, _TTS_instances, speak_fn).call(this, doc);
        }
        from(range) {
          __privateSet(this, _lastMark, null);
          const [doc] = __privateGet(this, _list).find((range_) => range.compareBoundaryPoints(Range.END_TO_START, range_) <= 0);
          let mark;
          for (const [name, range_] of __privateGet(this, _ranges).entries())
            if (range.compareBoundaryPoints(Range.START_TO_START, range_) <= 0) {
              mark = name;
              break;
            }
          return __privateMethod(this, _TTS_instances, speak_fn).call(this, doc, (ssml) => __privateMethod(this, _TTS_instances, getMarkElement_fn).call(this, ssml, mark));
        }
        setMark(mark) {
          const range = __privateGet(this, _ranges).get(mark);
          if (range) {
            __privateSet(this, _lastMark, mark);
            this.highlight(range.cloneRange());
          }
        }
      };
      _list = new WeakMap();
      _ranges = new WeakMap();
      _lastMark = new WeakMap();
      _serializer = new WeakMap();
      _TTS_instances = new WeakSet();
      getMarkElement_fn = function(doc, mark) {
        if (!mark) return null;
        return doc.querySelector('mark[name="'.concat(CSS.escape(mark), '"'));
      };
      speak_fn = function(doc, getNode) {
        var _a2, _b2, _c;
        if (!doc) return;
        if (!getNode) return __privateGet(this, _serializer).serializeToString(doc);
        const ssml = document.implementation.createDocument(NS3.SSML, "speak");
        ssml.documentElement.replaceWith(ssml.importNode(doc.documentElement, true));
        let node = (_a2 = getNode(ssml)) == null ? void 0 : _a2.previousSibling;
        while (node) {
          const next = (_c = node.previousSibling) != null ? _c : (_b2 = node.parentNode) == null ? void 0 : _b2.previousSibling;
          node.parentNode.removeChild(node);
          node = next;
        }
        return __privateGet(this, _serializer).serializeToString(ssml);
      };
    }
  });

  // src/reader.js
  var import_at = __toESM(require_at3());
  var import_flat = __toESM(require_flat3());
  var import_find_last_index = __toESM(require_find_last_index3());
  var import_from_entries = __toESM(require_from_entries3());
  var import_group_by = __toESM(require_group_by3());
  var import_group_by2 = __toESM(require_group_by6());
  var import_global_this = __toESM(require_global_this4());
  var import_match_all = __toESM(require_match_all3());
  var import_replace_all = __toESM(require_replace_all3());
  var import_trim_start = __toESM(require_trim_start3());
  var import_trim_end = __toESM(require_trim_end3());
  var import_async_iterator = __toESM(require_async_iterator3());
  var import_finally = __toESM(require_finally3());

  // node_modules/.pnpm/resize-observer-polyfill@1.5.1/node_modules/resize-observer-polyfill/dist/ResizeObserver.es.js
  var MapShim = (function() {
    if (typeof Map !== "undefined") {
      return Map;
    }
    function getIndex(arr, key) {
      var result = -1;
      arr.some(function(entry, index2) {
        if (entry[0] === key) {
          result = index2;
          return true;
        }
        return false;
      });
      return result;
    }
    return (
      /** @class */
      (function() {
        function class_1() {
          this.__entries__ = [];
        }
        Object.defineProperty(class_1.prototype, "size", {
          /**
           * @returns {boolean}
           */
          get: function() {
            return this.__entries__.length;
          },
          enumerable: true,
          configurable: true
        });
        class_1.prototype.get = function(key) {
          var index2 = getIndex(this.__entries__, key);
          var entry = this.__entries__[index2];
          return entry && entry[1];
        };
        class_1.prototype.set = function(key, value) {
          var index2 = getIndex(this.__entries__, key);
          if (~index2) {
            this.__entries__[index2][1] = value;
          } else {
            this.__entries__.push([key, value]);
          }
        };
        class_1.prototype.delete = function(key) {
          var entries = this.__entries__;
          var index2 = getIndex(entries, key);
          if (~index2) {
            entries.splice(index2, 1);
          }
        };
        class_1.prototype.has = function(key) {
          return !!~getIndex(this.__entries__, key);
        };
        class_1.prototype.clear = function() {
          this.__entries__.splice(0);
        };
        class_1.prototype.forEach = function(callback, ctx) {
          if (ctx === void 0) {
            ctx = null;
          }
          for (var _i = 0, _a2 = this.__entries__; _i < _a2.length; _i++) {
            var entry = _a2[_i];
            callback.call(ctx, entry[1], entry[0]);
          }
        };
        return class_1;
      })()
    );
  })();
  var isBrowser = typeof window !== "undefined" && typeof document !== "undefined" && window.document === document;
  var global$1 = (function() {
    if (typeof global !== "undefined" && global.Math === Math) {
      return global;
    }
    if (typeof self !== "undefined" && self.Math === Math) {
      return self;
    }
    if (typeof window !== "undefined" && window.Math === Math) {
      return window;
    }
    return Function("return this")();
  })();
  var requestAnimationFrame$1 = (function() {
    if (typeof requestAnimationFrame === "function") {
      return requestAnimationFrame.bind(global$1);
    }
    return function(callback) {
      return setTimeout(function() {
        return callback(Date.now());
      }, 1e3 / 60);
    };
  })();
  var trailingTimeout = 2;
  function throttle(callback, delay) {
    var leadingCall = false, trailingCall = false, lastCallTime = 0;
    function resolvePending() {
      if (leadingCall) {
        leadingCall = false;
        callback();
      }
      if (trailingCall) {
        proxy();
      }
    }
    function timeoutCallback() {
      requestAnimationFrame$1(resolvePending);
    }
    function proxy() {
      var timeStamp = Date.now();
      if (leadingCall) {
        if (timeStamp - lastCallTime < trailingTimeout) {
          return;
        }
        trailingCall = true;
      } else {
        leadingCall = true;
        trailingCall = false;
        setTimeout(timeoutCallback, delay);
      }
      lastCallTime = timeStamp;
    }
    return proxy;
  }
  var REFRESH_DELAY = 20;
  var transitionKeys = ["top", "right", "bottom", "left", "width", "height", "size", "weight"];
  var mutationObserverSupported = typeof MutationObserver !== "undefined";
  var ResizeObserverController = (
    /** @class */
    (function() {
      function ResizeObserverController2() {
        this.connected_ = false;
        this.mutationEventsAdded_ = false;
        this.mutationsObserver_ = null;
        this.observers_ = [];
        this.onTransitionEnd_ = this.onTransitionEnd_.bind(this);
        this.refresh = throttle(this.refresh.bind(this), REFRESH_DELAY);
      }
      ResizeObserverController2.prototype.addObserver = function(observer) {
        if (!~this.observers_.indexOf(observer)) {
          this.observers_.push(observer);
        }
        if (!this.connected_) {
          this.connect_();
        }
      };
      ResizeObserverController2.prototype.removeObserver = function(observer) {
        var observers2 = this.observers_;
        var index2 = observers2.indexOf(observer);
        if (~index2) {
          observers2.splice(index2, 1);
        }
        if (!observers2.length && this.connected_) {
          this.disconnect_();
        }
      };
      ResizeObserverController2.prototype.refresh = function() {
        var changesDetected = this.updateObservers_();
        if (changesDetected) {
          this.refresh();
        }
      };
      ResizeObserverController2.prototype.updateObservers_ = function() {
        var activeObservers = this.observers_.filter(function(observer) {
          return observer.gatherActive(), observer.hasActive();
        });
        activeObservers.forEach(function(observer) {
          return observer.broadcastActive();
        });
        return activeObservers.length > 0;
      };
      ResizeObserverController2.prototype.connect_ = function() {
        if (!isBrowser || this.connected_) {
          return;
        }
        document.addEventListener("transitionend", this.onTransitionEnd_);
        window.addEventListener("resize", this.refresh);
        if (mutationObserverSupported) {
          this.mutationsObserver_ = new MutationObserver(this.refresh);
          this.mutationsObserver_.observe(document, {
            attributes: true,
            childList: true,
            characterData: true,
            subtree: true
          });
        } else {
          document.addEventListener("DOMSubtreeModified", this.refresh);
          this.mutationEventsAdded_ = true;
        }
        this.connected_ = true;
      };
      ResizeObserverController2.prototype.disconnect_ = function() {
        if (!isBrowser || !this.connected_) {
          return;
        }
        document.removeEventListener("transitionend", this.onTransitionEnd_);
        window.removeEventListener("resize", this.refresh);
        if (this.mutationsObserver_) {
          this.mutationsObserver_.disconnect();
        }
        if (this.mutationEventsAdded_) {
          document.removeEventListener("DOMSubtreeModified", this.refresh);
        }
        this.mutationsObserver_ = null;
        this.mutationEventsAdded_ = false;
        this.connected_ = false;
      };
      ResizeObserverController2.prototype.onTransitionEnd_ = function(_a2) {
        var _b2 = _a2.propertyName, propertyName = _b2 === void 0 ? "" : _b2;
        var isReflowProperty = transitionKeys.some(function(key) {
          return !!~propertyName.indexOf(key);
        });
        if (isReflowProperty) {
          this.refresh();
        }
      };
      ResizeObserverController2.getInstance = function() {
        if (!this.instance_) {
          this.instance_ = new ResizeObserverController2();
        }
        return this.instance_;
      };
      ResizeObserverController2.instance_ = null;
      return ResizeObserverController2;
    })()
  );
  var defineConfigurable = (function(target, props) {
    for (var _i = 0, _a2 = Object.keys(props); _i < _a2.length; _i++) {
      var key = _a2[_i];
      Object.defineProperty(target, key, {
        value: props[key],
        enumerable: false,
        writable: false,
        configurable: true
      });
    }
    return target;
  });
  var getWindowOf = (function(target) {
    var ownerGlobal = target && target.ownerDocument && target.ownerDocument.defaultView;
    return ownerGlobal || global$1;
  });
  var emptyRect = createRectInit(0, 0, 0, 0);
  function toFloat(value) {
    return parseFloat(value) || 0;
  }
  function getBordersSize(styles) {
    var positions = [];
    for (var _i = 1; _i < arguments.length; _i++) {
      positions[_i - 1] = arguments[_i];
    }
    return positions.reduce(function(size, position) {
      var value = styles["border-" + position + "-width"];
      return size + toFloat(value);
    }, 0);
  }
  function getPaddings(styles) {
    var positions = ["top", "right", "bottom", "left"];
    var paddings = {};
    for (var _i = 0, positions_1 = positions; _i < positions_1.length; _i++) {
      var position = positions_1[_i];
      var value = styles["padding-" + position];
      paddings[position] = toFloat(value);
    }
    return paddings;
  }
  function getSVGContentRect(target) {
    var bbox = target.getBBox();
    return createRectInit(0, 0, bbox.width, bbox.height);
  }
  function getHTMLElementContentRect(target) {
    var clientWidth = target.clientWidth, clientHeight = target.clientHeight;
    if (!clientWidth && !clientHeight) {
      return emptyRect;
    }
    var styles = getWindowOf(target).getComputedStyle(target);
    var paddings = getPaddings(styles);
    var horizPad = paddings.left + paddings.right;
    var vertPad = paddings.top + paddings.bottom;
    var width = toFloat(styles.width), height = toFloat(styles.height);
    if (styles.boxSizing === "border-box") {
      if (Math.round(width + horizPad) !== clientWidth) {
        width -= getBordersSize(styles, "left", "right") + horizPad;
      }
      if (Math.round(height + vertPad) !== clientHeight) {
        height -= getBordersSize(styles, "top", "bottom") + vertPad;
      }
    }
    if (!isDocumentElement(target)) {
      var vertScrollbar = Math.round(width + horizPad) - clientWidth;
      var horizScrollbar = Math.round(height + vertPad) - clientHeight;
      if (Math.abs(vertScrollbar) !== 1) {
        width -= vertScrollbar;
      }
      if (Math.abs(horizScrollbar) !== 1) {
        height -= horizScrollbar;
      }
    }
    return createRectInit(paddings.left, paddings.top, width, height);
  }
  var isSVGGraphicsElement = (function() {
    if (typeof SVGGraphicsElement !== "undefined") {
      return function(target) {
        return target instanceof getWindowOf(target).SVGGraphicsElement;
      };
    }
    return function(target) {
      return target instanceof getWindowOf(target).SVGElement && typeof target.getBBox === "function";
    };
  })();
  function isDocumentElement(target) {
    return target === getWindowOf(target).document.documentElement;
  }
  function getContentRect(target) {
    if (!isBrowser) {
      return emptyRect;
    }
    if (isSVGGraphicsElement(target)) {
      return getSVGContentRect(target);
    }
    return getHTMLElementContentRect(target);
  }
  function createReadOnlyRect(_a2) {
    var x = _a2.x, y = _a2.y, width = _a2.width, height = _a2.height;
    var Constr = typeof DOMRectReadOnly !== "undefined" ? DOMRectReadOnly : Object;
    var rect = Object.create(Constr.prototype);
    defineConfigurable(rect, {
      x,
      y,
      width,
      height,
      top: y,
      right: x + width,
      bottom: height + y,
      left: x
    });
    return rect;
  }
  function createRectInit(x, y, width, height) {
    return { x, y, width, height };
  }
  var ResizeObservation = (
    /** @class */
    (function() {
      function ResizeObservation2(target) {
        this.broadcastWidth = 0;
        this.broadcastHeight = 0;
        this.contentRect_ = createRectInit(0, 0, 0, 0);
        this.target = target;
      }
      ResizeObservation2.prototype.isActive = function() {
        var rect = getContentRect(this.target);
        this.contentRect_ = rect;
        return rect.width !== this.broadcastWidth || rect.height !== this.broadcastHeight;
      };
      ResizeObservation2.prototype.broadcastRect = function() {
        var rect = this.contentRect_;
        this.broadcastWidth = rect.width;
        this.broadcastHeight = rect.height;
        return rect;
      };
      return ResizeObservation2;
    })()
  );
  var ResizeObserverEntry = (
    /** @class */
    /* @__PURE__ */ (function() {
      function ResizeObserverEntry2(target, rectInit) {
        var contentRect = createReadOnlyRect(rectInit);
        defineConfigurable(this, { target, contentRect });
      }
      return ResizeObserverEntry2;
    })()
  );
  var ResizeObserverSPI = (
    /** @class */
    (function() {
      function ResizeObserverSPI2(callback, controller, callbackCtx) {
        this.activeObservations_ = [];
        this.observations_ = new MapShim();
        if (typeof callback !== "function") {
          throw new TypeError("The callback provided as parameter 1 is not a function.");
        }
        this.callback_ = callback;
        this.controller_ = controller;
        this.callbackCtx_ = callbackCtx;
      }
      ResizeObserverSPI2.prototype.observe = function(target) {
        if (!arguments.length) {
          throw new TypeError("1 argument required, but only 0 present.");
        }
        if (typeof Element === "undefined" || !(Element instanceof Object)) {
          return;
        }
        if (!(target instanceof getWindowOf(target).Element)) {
          throw new TypeError('parameter 1 is not of type "Element".');
        }
        var observations = this.observations_;
        if (observations.has(target)) {
          return;
        }
        observations.set(target, new ResizeObservation(target));
        this.controller_.addObserver(this);
        this.controller_.refresh();
      };
      ResizeObserverSPI2.prototype.unobserve = function(target) {
        if (!arguments.length) {
          throw new TypeError("1 argument required, but only 0 present.");
        }
        if (typeof Element === "undefined" || !(Element instanceof Object)) {
          return;
        }
        if (!(target instanceof getWindowOf(target).Element)) {
          throw new TypeError('parameter 1 is not of type "Element".');
        }
        var observations = this.observations_;
        if (!observations.has(target)) {
          return;
        }
        observations.delete(target);
        if (!observations.size) {
          this.controller_.removeObserver(this);
        }
      };
      ResizeObserverSPI2.prototype.disconnect = function() {
        this.clearActive();
        this.observations_.clear();
        this.controller_.removeObserver(this);
      };
      ResizeObserverSPI2.prototype.gatherActive = function() {
        var _this = this;
        this.clearActive();
        this.observations_.forEach(function(observation) {
          if (observation.isActive()) {
            _this.activeObservations_.push(observation);
          }
        });
      };
      ResizeObserverSPI2.prototype.broadcastActive = function() {
        if (!this.hasActive()) {
          return;
        }
        var ctx = this.callbackCtx_;
        var entries = this.activeObservations_.map(function(observation) {
          return new ResizeObserverEntry(observation.target, observation.broadcastRect());
        });
        this.callback_.call(ctx, entries, ctx);
        this.clearActive();
      };
      ResizeObserverSPI2.prototype.clearActive = function() {
        this.activeObservations_.splice(0);
      };
      ResizeObserverSPI2.prototype.hasActive = function() {
        return this.activeObservations_.length > 0;
      };
      return ResizeObserverSPI2;
    })()
  );
  var observers = typeof WeakMap !== "undefined" ? /* @__PURE__ */ new WeakMap() : new MapShim();
  var ResizeObserver2 = (
    /** @class */
    /* @__PURE__ */ (function() {
      function ResizeObserver3(callback) {
        if (!(this instanceof ResizeObserver3)) {
          throw new TypeError("Cannot call a class as a function.");
        }
        if (!arguments.length) {
          throw new TypeError("1 argument required, but only 0 present.");
        }
        var controller = ResizeObserverController.getInstance();
        var observer = new ResizeObserverSPI(callback, controller, this);
        observers.set(this, observer);
      }
      return ResizeObserver3;
    })()
  );
  [
    "observe",
    "unobserve",
    "disconnect"
  ].forEach(function(method) {
    ResizeObserver2.prototype[method] = function() {
      var _a2;
      return (_a2 = observers.get(this))[method].apply(_a2, arguments);
    };
  });
  var index = (function() {
    if (typeof global$1.ResizeObserver !== "undefined") {
      return global$1.ResizeObserver;
    }
    return ResizeObserver2;
  })();
  var ResizeObserver_es_default = index;

  // node_modules/.pnpm/fflate@0.8.2/node_modules/fflate/esm/browser.js
  var u8 = Uint8Array;
  var u16 = Uint16Array;
  var i32 = Int32Array;
  var fleb = new u8([
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    1,
    1,
    1,
    1,
    2,
    2,
    2,
    2,
    3,
    3,
    3,
    3,
    4,
    4,
    4,
    4,
    5,
    5,
    5,
    5,
    0,
    /* unused */
    0,
    0,
    /* impossible */
    0
  ]);
  var fdeb = new u8([
    0,
    0,
    0,
    0,
    1,
    1,
    2,
    2,
    3,
    3,
    4,
    4,
    5,
    5,
    6,
    6,
    7,
    7,
    8,
    8,
    9,
    9,
    10,
    10,
    11,
    11,
    12,
    12,
    13,
    13,
    /* unused */
    0,
    0
  ]);
  var clim = new u8([16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15]);
  var freb = function(eb, start) {
    var b = new u16(31);
    for (var i = 0; i < 31; ++i) {
      b[i] = start += 1 << eb[i - 1];
    }
    var r = new i32(b[30]);
    for (var i = 1; i < 30; ++i) {
      for (var j = b[i]; j < b[i + 1]; ++j) {
        r[j] = j - b[i] << 5 | i;
      }
    }
    return { b, r };
  };
  var _a = freb(fleb, 2);
  var fl = _a.b;
  var revfl = _a.r;
  fl[28] = 258, revfl[258] = 28;
  var _b = freb(fdeb, 0);
  var fd = _b.b;
  var revfd = _b.r;
  var rev = new u16(32768);
  for (i = 0; i < 32768; ++i) {
    x = (i & 43690) >> 1 | (i & 21845) << 1;
    x = (x & 52428) >> 2 | (x & 13107) << 2;
    x = (x & 61680) >> 4 | (x & 3855) << 4;
    rev[i] = ((x & 65280) >> 8 | (x & 255) << 8) >> 1;
  }
  var x;
  var i;
  var hMap = (function(cd, mb, r) {
    var s = cd.length;
    var i = 0;
    var l = new u16(mb);
    for (; i < s; ++i) {
      if (cd[i])
        ++l[cd[i] - 1];
    }
    var le = new u16(mb);
    for (i = 1; i < mb; ++i) {
      le[i] = le[i - 1] + l[i - 1] << 1;
    }
    var co;
    if (r) {
      co = new u16(1 << mb);
      var rvb = 15 - mb;
      for (i = 0; i < s; ++i) {
        if (cd[i]) {
          var sv = i << 4 | cd[i];
          var r_1 = mb - cd[i];
          var v = le[cd[i] - 1]++ << r_1;
          for (var m = v | (1 << r_1) - 1; v <= m; ++v) {
            co[rev[v] >> rvb] = sv;
          }
        }
      }
    } else {
      co = new u16(s);
      for (i = 0; i < s; ++i) {
        if (cd[i]) {
          co[i] = rev[le[cd[i] - 1]++] >> 15 - cd[i];
        }
      }
    }
    return co;
  });
  var flt = new u8(288);
  for (i = 0; i < 144; ++i)
    flt[i] = 8;
  var i;
  for (i = 144; i < 256; ++i)
    flt[i] = 9;
  var i;
  for (i = 256; i < 280; ++i)
    flt[i] = 7;
  var i;
  for (i = 280; i < 288; ++i)
    flt[i] = 8;
  var i;
  var fdt = new u8(32);
  for (i = 0; i < 32; ++i)
    fdt[i] = 5;
  var i;
  var flrm = /* @__PURE__ */ hMap(flt, 9, 1);
  var fdrm = /* @__PURE__ */ hMap(fdt, 5, 1);
  var max = function(a) {
    var m = a[0];
    for (var i = 1; i < a.length; ++i) {
      if (a[i] > m)
        m = a[i];
    }
    return m;
  };
  var bits = function(d, p, m) {
    var o = p / 8 | 0;
    return (d[o] | d[o + 1] << 8) >> (p & 7) & m;
  };
  var bits16 = function(d, p) {
    var o = p / 8 | 0;
    return (d[o] | d[o + 1] << 8 | d[o + 2] << 16) >> (p & 7);
  };
  var shft = function(p) {
    return (p + 7) / 8 | 0;
  };
  var slc = function(v, s, e) {
    if (s == null || s < 0)
      s = 0;
    if (e == null || e > v.length)
      e = v.length;
    return new u8(v.subarray(s, e));
  };
  var ec = [
    "unexpected EOF",
    "invalid block type",
    "invalid length/literal",
    "invalid distance",
    "stream finished",
    "no stream handler",
    ,
    "no callback",
    "invalid UTF-8 data",
    "extra field too long",
    "date not in range 1980-2099",
    "filename too long",
    "stream finishing",
    "invalid zip data"
    // determined by unknown compression method
  ];
  var err = function(ind, msg, nt) {
    var e = new Error(msg || ec[ind]);
    e.code = ind;
    if (Error.captureStackTrace)
      Error.captureStackTrace(e, err);
    if (!nt)
      throw e;
    return e;
  };
  var inflt = function(dat, st, buf, dict) {
    var sl = dat.length, dl = dict ? dict.length : 0;
    if (!sl || st.f && !st.l)
      return buf || new u8(0);
    var noBuf = !buf;
    var resize = noBuf || st.i != 2;
    var noSt = st.i;
    if (noBuf)
      buf = new u8(sl * 3);
    var cbuf = function(l2) {
      var bl = buf.length;
      if (l2 > bl) {
        var nbuf = new u8(Math.max(bl * 2, l2));
        nbuf.set(buf);
        buf = nbuf;
      }
    };
    var final = st.f || 0, pos = st.p || 0, bt = st.b || 0, lm = st.l, dm = st.d, lbt = st.m, dbt = st.n;
    var tbts = sl * 8;
    do {
      if (!lm) {
        final = bits(dat, pos, 1);
        var type = bits(dat, pos + 1, 3);
        pos += 3;
        if (!type) {
          var s = shft(pos) + 4, l = dat[s - 4] | dat[s - 3] << 8, t = s + l;
          if (t > sl) {
            if (noSt)
              err(0);
            break;
          }
          if (resize)
            cbuf(bt + l);
          buf.set(dat.subarray(s, t), bt);
          st.b = bt += l, st.p = pos = t * 8, st.f = final;
          continue;
        } else if (type == 1)
          lm = flrm, dm = fdrm, lbt = 9, dbt = 5;
        else if (type == 2) {
          var hLit = bits(dat, pos, 31) + 257, hcLen = bits(dat, pos + 10, 15) + 4;
          var tl = hLit + bits(dat, pos + 5, 31) + 1;
          pos += 14;
          var ldt = new u8(tl);
          var clt = new u8(19);
          for (var i = 0; i < hcLen; ++i) {
            clt[clim[i]] = bits(dat, pos + i * 3, 7);
          }
          pos += hcLen * 3;
          var clb = max(clt), clbmsk = (1 << clb) - 1;
          var clm = hMap(clt, clb, 1);
          for (var i = 0; i < tl; ) {
            var r = clm[bits(dat, pos, clbmsk)];
            pos += r & 15;
            var s = r >> 4;
            if (s < 16) {
              ldt[i++] = s;
            } else {
              var c = 0, n = 0;
              if (s == 16)
                n = 3 + bits(dat, pos, 3), pos += 2, c = ldt[i - 1];
              else if (s == 17)
                n = 3 + bits(dat, pos, 7), pos += 3;
              else if (s == 18)
                n = 11 + bits(dat, pos, 127), pos += 7;
              while (n--)
                ldt[i++] = c;
            }
          }
          var lt = ldt.subarray(0, hLit), dt = ldt.subarray(hLit);
          lbt = max(lt);
          dbt = max(dt);
          lm = hMap(lt, lbt, 1);
          dm = hMap(dt, dbt, 1);
        } else
          err(1);
        if (pos > tbts) {
          if (noSt)
            err(0);
          break;
        }
      }
      if (resize)
        cbuf(bt + 131072);
      var lms = (1 << lbt) - 1, dms = (1 << dbt) - 1;
      var lpos = pos;
      for (; ; lpos = pos) {
        var c = lm[bits16(dat, pos) & lms], sym = c >> 4;
        pos += c & 15;
        if (pos > tbts) {
          if (noSt)
            err(0);
          break;
        }
        if (!c)
          err(2);
        if (sym < 256)
          buf[bt++] = sym;
        else if (sym == 256) {
          lpos = pos, lm = null;
          break;
        } else {
          var add = sym - 254;
          if (sym > 264) {
            var i = sym - 257, b = fleb[i];
            add = bits(dat, pos, (1 << b) - 1) + fl[i];
            pos += b;
          }
          var d = dm[bits16(dat, pos) & dms], dsym = d >> 4;
          if (!d)
            err(3);
          pos += d & 15;
          var dt = fd[dsym];
          if (dsym > 3) {
            var b = fdeb[dsym];
            dt += bits16(dat, pos) & (1 << b) - 1, pos += b;
          }
          if (pos > tbts) {
            if (noSt)
              err(0);
            break;
          }
          if (resize)
            cbuf(bt + 131072);
          var end = bt + add;
          if (bt < dt) {
            var shift = dl - dt, dend = Math.min(dt, end);
            if (shift + bt < 0)
              err(3);
            for (; bt < dend; ++bt)
              buf[bt] = dict[shift + bt];
          }
          for (; bt < end; ++bt)
            buf[bt] = buf[bt - dt];
        }
      }
      st.l = lm, st.p = lpos, st.b = bt, st.f = final;
      if (lm)
        final = 1, st.m = lbt, st.d = dm, st.n = dbt;
    } while (!final);
    return bt != buf.length && noBuf ? slc(buf, 0, bt) : buf.subarray(0, bt);
  };
  var et = /* @__PURE__ */ new u8(0);
  var zls = function(d, dict) {
    if ((d[0] & 15) != 8 || d[0] >> 4 > 7 || (d[0] << 8 | d[1]) % 31)
      err(6, "invalid zlib data");
    if ((d[1] >> 5 & 1) == +!dict)
      err(6, "invalid zlib data: " + (d[1] & 32 ? "need" : "unexpected") + " dictionary");
    return (d[1] >> 3 & 4) + 2;
  };
  function unzlibSync(data, opts) {
    return inflt(data.subarray(zls(data, opts && opts.dictionary), -4), { i: 2 }, opts && opts.out, opts && opts.dictionary);
  }
  var td = typeof TextDecoder != "undefined" && /* @__PURE__ */ new TextDecoder();
  var tds = 0;
  try {
    td.decode(et, { stream: true });
    tds = 1;
  } catch (e) {
  }

  // vendor/foliate/epubcfi.js
  var findIndices = (arr, f) => arr.map((x, i, a) => f(x, i, a) ? i : null).filter((x) => x != null);
  var splitAt = (arr, is) => [-1, ...is, arr.length].reduce(({ xs, a }, b) => {
    var _a2;
    return { xs: (_a2 = xs == null ? void 0 : xs.concat([arr.slice(a + 1, b)])) != null ? _a2 : [], a: b };
  }, {}).xs;
  var concatArrays = (a, b) => a.slice(0, -1).concat([a[a.length - 1].concat(b[0])]).concat(b.slice(1));
  var isNumber = /\d/;
  var isCFI = /^epubcfi\((.*)\)$/;
  var escapeCFI = (str) => str.replace(/[\^[\](),;=]/g, "^$&");
  var wrap = (x) => isCFI.test(x) ? x : "epubcfi(".concat(x, ")");
  var unwrap = (x) => {
    var _a2, _b2;
    return (_b2 = (_a2 = x.match(isCFI)) == null ? void 0 : _a2[1]) != null ? _b2 : x;
  };
  var lift = (f) => (...xs) => "epubcfi(".concat(f(...xs.map((x) => {
    var _a2, _b2;
    return (_b2 = (_a2 = x.match(isCFI)) == null ? void 0 : _a2[1]) != null ? _b2 : x;
  })), ")");
  var joinIndir = lift((...xs) => xs.join("!"));
  var tokenizer = (str) => {
    const tokens = [];
    let state2, escape, value = "";
    const push = (x) => (tokens.push(x), state2 = null, value = "");
    const cat = (x) => (value += x, escape = false);
    for (const char of Array.from(str.trim()).concat("")) {
      if (char === "^" && !escape) {
        escape = true;
        continue;
      }
      if (state2 === "!") push(["!"]);
      else if (state2 === ",") push([","]);
      else if (state2 === "/" || state2 === ":") {
        if (isNumber.test(char)) {
          cat(char);
          continue;
        } else push([state2, parseInt(value)]);
      } else if (state2 === "~") {
        if (isNumber.test(char) || char === ".") {
          cat(char);
          continue;
        } else push(["~", parseFloat(value)]);
      } else if (state2 === "@") {
        if (char === ":") {
          push(["@", parseFloat(value)]);
          state2 = "@";
          continue;
        }
        if (isNumber.test(char) || char === ".") {
          cat(char);
          continue;
        } else push(["@", parseFloat(value)]);
      } else if (state2 === "[") {
        if (char === ";" && !escape) {
          push(["[", value]);
          state2 = ";";
        } else if (char === "," && !escape) {
          push(["[", value]);
          state2 = "[";
        } else if (char === "]" && !escape) push(["[", value]);
        else cat(char);
        continue;
      } else if (state2 == null ? void 0 : state2.startsWith(";")) {
        if (char === "=" && !escape) {
          state2 = ";".concat(value);
          value = "";
        } else if (char === ";" && !escape) {
          push([state2, value]);
          state2 = ";";
        } else if (char === "]" && !escape) push([state2, value]);
        else cat(char);
        continue;
      }
      if (char === "/" || char === ":" || char === "~" || char === "@" || char === "[" || char === "!" || char === ",") state2 = char;
    }
    return tokens;
  };
  var findTokens = (tokens, x) => findIndices(tokens, ([t]) => t === x);
  var parser = (tokens) => {
    var _a2, _b2;
    const parts = [];
    let state2;
    for (const [type, val] of tokens) {
      if (type === "/") parts.push({ index: val });
      else {
        const last = parts[parts.length - 1];
        if (type === ":") last.offset = val;
        else if (type === "~") last.temporal = val;
        else if (type === "@") last.spatial = ((_a2 = last.spatial) != null ? _a2 : []).concat(val);
        else if (type === ";s") last.side = val;
        else if (type === "[") {
          if (state2 === "/" && val) last.id = val;
          else {
            last.text = ((_b2 = last.text) != null ? _b2 : []).concat(val);
            continue;
          }
        }
      }
      state2 = type;
    }
    return parts;
  };
  var parserIndir = (tokens) => splitAt(tokens, findTokens(tokens, "!")).map(parser);
  var parse = (cfi) => {
    const tokens = tokenizer(unwrap(cfi));
    const commas = findTokens(tokens, ",");
    if (!commas.length) return parserIndir(tokens);
    const [parent, start, end] = splitAt(tokens, commas).map(parserIndir);
    return { parent, start, end };
  };
  var partToString = ({ index: index2, id, offset, temporal, spatial, text, side }) => {
    var _a2, _b2;
    const param = side ? ";s=".concat(side) : "";
    return "/".concat(index2) + (id ? "[".concat(escapeCFI(id)).concat(param, "]") : "") + (offset != null && index2 % 2 ? ":".concat(offset) : "") + (temporal ? "~".concat(temporal) : "") + (spatial ? "@".concat(spatial.join(":")) : "") + (text || !id && side ? "[" + ((_b2 = (_a2 = text == null ? void 0 : text.map(escapeCFI)) == null ? void 0 : _a2.join(",")) != null ? _b2 : "") + param + "]" : "");
  };
  var toInnerString = (parsed) => parsed.parent ? [parsed.parent, parsed.start, parsed.end].map(toInnerString).join(",") : parsed.map((parts) => parts.map(partToString).join("")).join("!");
  var toString = (parsed) => wrap(toInnerString(parsed));
  var collapse = (x, toEnd) => typeof x === "string" ? toString(collapse(parse(x), toEnd)) : x.parent ? concatArrays(x.parent, x[toEnd ? "end" : "start"]) : x;
  var buildRange = (from, to) => {
    if (typeof from === "string") from = parse(from);
    if (typeof to === "string") to = parse(to);
    from = collapse(from);
    to = collapse(to, true);
    const localFrom = from[from.length - 1], localTo = to[to.length - 1];
    const localParent = [], localStart = [], localEnd = [];
    let pushToParent = true;
    const len = Math.max(localFrom.length, localTo.length);
    for (let i = 0; i < len; i++) {
      const a = localFrom[i], b = localTo[i];
      pushToParent && (pushToParent = (a == null ? void 0 : a.index) === (b == null ? void 0 : b.index) && !(a == null ? void 0 : a.offset) && !(b == null ? void 0 : b.offset));
      if (pushToParent) localParent.push(a);
      else {
        if (a) localStart.push(a);
        if (b) localEnd.push(b);
      }
    }
    const parent = from.slice(0, -1).concat([localParent]);
    return toString({ parent, start: [localStart], end: [localEnd] });
  };
  var isTextNode = ({ nodeType }) => nodeType === 3 || nodeType === 4;
  var isElementNode = ({ nodeType }) => nodeType === 1;
  var getChildNodes = (node, filter3) => {
    const nodes = Array.from(node.childNodes).filter((node2) => isTextNode(node2) || isElementNode(node2));
    return filter3 ? nodes.map((node2) => {
      const accept = filter3(node2);
      if (accept === NodeFilter.FILTER_REJECT) return null;
      else if (accept === NodeFilter.FILTER_SKIP) return getChildNodes(node2, filter3);
      else return node2;
    }).flat().filter((x) => x) : nodes;
  };
  var indexChildNodes = (node, filter3) => {
    const nodes = getChildNodes(node, filter3).reduce((arr, node2) => {
      let last = arr[arr.length - 1];
      if (!last) arr.push(node2);
      else if (isTextNode(node2)) {
        if (Array.isArray(last)) last.push(node2);
        else if (isTextNode(last)) arr[arr.length - 1] = [last, node2];
        else arr.push(node2);
      } else {
        if (isElementNode(last)) arr.push(null, node2);
        else arr.push(node2);
      }
      return arr;
    }, []);
    if (isElementNode(nodes[0])) nodes.unshift("first");
    if (isElementNode(nodes[nodes.length - 1])) nodes.push("last");
    nodes.unshift("before");
    nodes.push("after");
    return nodes;
  };
  var partsToNode = (node, parts, filter3) => {
    var _a2, _b2;
    const { id } = parts[parts.length - 1];
    if (id) {
      const el = node.ownerDocument.getElementById(id);
      if (el) return { node: el, offset: 0 };
    }
    for (const { index: index2 } of parts) {
      const newNode = node ? indexChildNodes(node, filter3)[index2] : null;
      if (newNode === "first") return { node: (_a2 = node.firstChild) != null ? _a2 : node };
      if (newNode === "last") return { node: (_b2 = node.lastChild) != null ? _b2 : node };
      if (newNode === "before") return { node, before: true };
      if (newNode === "after") return { node, after: true };
      node = newNode;
    }
    const { offset } = parts[parts.length - 1];
    if (!Array.isArray(node)) return { node, offset };
    let sum = 0;
    for (const n of node) {
      const { length } = n.nodeValue;
      if (sum + length >= offset) return { node: n, offset: offset - sum };
      sum += length;
    }
  };
  var nodeToParts = (node, offset, filter3) => {
    let { id, parentNode } = node;
    while (filter3 && parentNode && parentNode !== node.ownerDocument.documentElement && filter3(parentNode) === NodeFilter.FILTER_SKIP)
      parentNode = parentNode.parentNode;
    const indexed = indexChildNodes(parentNode, filter3);
    const index2 = indexed.findIndex((x) => Array.isArray(x) ? x.some((x2) => x2 === node) : x === node);
    const chunk = indexed[index2];
    if (Array.isArray(chunk)) {
      let sum = 0;
      for (const x of chunk) {
        if (x === node) {
          sum += offset;
          break;
        } else sum += x.nodeValue.length;
      }
      offset = sum;
    }
    const part = { id, index: index2, offset };
    return (parentNode !== node.ownerDocument.documentElement ? nodeToParts(parentNode, null, filter3).concat(part) : [part]).filter((x) => x.index !== -1);
  };
  var fromRange = (range, filter3) => {
    const { startContainer, startOffset, endContainer, endOffset } = range;
    const start = nodeToParts(startContainer, startOffset, filter3);
    if (range.collapsed) return toString([start]);
    const end = nodeToParts(endContainer, endOffset, filter3);
    return buildRange([start], [end]);
  };
  var toRange = (doc, parts, filter3) => {
    const startParts = collapse(parts);
    const endParts = collapse(parts, true);
    const root = doc.documentElement;
    const start = partsToNode(root, startParts[0], filter3);
    const end = partsToNode(root, endParts[0], filter3);
    const range = doc.createRange();
    if (start.before) range.setStartBefore(start.node);
    else if (start.after) range.setStartAfter(start.node);
    else range.setStart(start.node, start.offset);
    if (end.before) range.setEndBefore(end.node);
    else if (end.after) range.setEndAfter(end.node);
    else range.setEnd(end.node, end.offset);
    return range;
  };
  var fromElements = (elements) => {
    const results = [];
    const { parentNode } = elements[0];
    const parts = nodeToParts(parentNode);
    for (const [index2, node] of indexChildNodes(parentNode).entries()) {
      const el = elements[results.length];
      if (node === el)
        results.push(toString([parts.concat({ id: el.id, index: index2 })]));
    }
    return results;
  };
  var toElement = (doc, parts) => partsToNode(doc.documentElement, collapse(parts)).node;
  var fake = {
    fromIndex: (index2) => wrap("/6/".concat((index2 + 1) * 2)),
    toIndex: (parts) => (parts == null ? void 0 : parts.at(-1).index) / 2 - 1
  };

  // vendor/foliate/epub.js
  var NS = {
    CONTAINER: "urn:oasis:names:tc:opendocument:xmlns:container",
    XHTML: "http://www.w3.org/1999/xhtml",
    OPF: "http://www.idpf.org/2007/opf",
    EPUB: "http://www.idpf.org/2007/ops",
    DC: "http://purl.org/dc/elements/1.1/",
    DCTERMS: "http://purl.org/dc/terms/",
    ENC: "http://www.w3.org/2001/04/xmlenc#",
    NCX: "http://www.daisy.org/z3986/2005/ncx/",
    XLINK: "http://www.w3.org/1999/xlink",
    SMIL: "http://www.w3.org/ns/SMIL"
  };
  var MIME = {
    XML: "application/xml",
    NCX: "application/x-dtbncx+xml",
    XHTML: "application/xhtml+xml",
    HTML: "text/html",
    CSS: "text/css",
    SVG: "image/svg+xml",
    JS: /\/(x-)?(javascript|ecmascript)/
  };
  var PREFIX = {
    a11y: "http://www.idpf.org/epub/vocab/package/a11y/#",
    dcterms: "http://purl.org/dc/terms/",
    marc: "http://id.loc.gov/vocabulary/",
    media: "http://www.idpf.org/epub/vocab/overlays/#",
    onix: "http://www.editeur.org/ONIX/book/codelists/current.html#",
    rendition: "http://www.idpf.org/vocab/rendition/#",
    schema: "http://schema.org/",
    xsd: "http://www.w3.org/2001/XMLSchema#",
    msv: "http://www.idpf.org/epub/vocab/structure/magazine/#",
    prism: "http://www.prismstandard.org/specifications/3.0/PRISM_CV_Spec_3.0.htm#"
  };
  var RELATORS = {
    art: "artist",
    aut: "author",
    clr: "colorist",
    edt: "editor",
    ill: "illustrator",
    nrt: "narrator",
    trl: "translator",
    pbl: "publisher"
  };
  var ONIX5 = {
    "02": "isbn",
    "06": "doi",
    "15": "isbn",
    "26": "doi",
    "34": "issn"
  };
  var camel = (x) => x.toLowerCase().replace(/[-:](.)/g, (_, g) => g.toUpperCase());
  var normalizeWhitespace = (str) => str ? str.replace(/[\t\n\f\r ]+/g, " ").replace(/^[\t\n\f\r ]+/, "").replace(/[\t\n\f\r ]+$/, "") : "";
  var filterAttribute = (attr, value, isList) => isList ? (el) => {
    var _a2, _b2;
    return (_b2 = (_a2 = el.getAttribute(attr)) == null ? void 0 : _a2.split(/\s/)) == null ? void 0 : _b2.includes(value);
  } : typeof value === "function" ? (el) => value(el.getAttribute(attr)) : (el) => el.getAttribute(attr) === value;
  var getAttributes = (...xs) => (el) => el ? Object.fromEntries(xs.map((x) => [camel(x), el.getAttribute(x)])) : null;
  var getElementText = (el) => normalizeWhitespace(el == null ? void 0 : el.textContent);
  var childGetter = (doc, ns) => {
    const useNS = doc.lookupNamespaceURI(null) === ns || doc.lookupPrefix(ns);
    const f = useNS ? (el, name) => (el2) => el2.namespaceURI === ns && el2.localName === name : (el, name) => (el2) => el2.localName === name;
    return {
      $: (el, name) => [...el.children].find(f(el, name)),
      $$: (el, name) => [...el.children].filter(f(el, name)),
      $$$: useNS ? (el, name) => [...el.getElementsByTagNameNS(ns, name)] : (el, name) => [...el.getElementsByTagName(name)]
    };
  };
  var resolveURL = (url, relativeTo) => {
    try {
      if (relativeTo.includes(":")) return new URL(url, relativeTo);
      const root = "https://invalid.invalid/";
      const obj = new URL(url, root + relativeTo);
      obj.search = "";
      return decodeURI(obj.href.replace(root, ""));
    } catch (e) {
      console.warn(e);
      return url;
    }
  };
  var isExternal = (uri) => /^(?!blob)\w+:/i.test(uri);
  var pathRelative = (from, to) => {
    if (!from) return to;
    const as = from.replace(/\/$/, "").split("/");
    const bs = to.replace(/\/$/, "").split("/");
    const i = (as.length > bs.length ? as : bs).findIndex((_, i2) => as[i2] !== bs[i2]);
    return i < 0 ? "" : Array(as.length - i).fill("..").concat(bs.slice(i)).join("/");
  };
  var pathDirname = (str) => str.slice(0, str.lastIndexOf("/") + 1);
  var replaceSeries = async (str, regex, f) => {
    const matches = [];
    str.replace(regex, (...args) => (matches.push(args), null));
    const results = [];
    for (const args of matches) results.push(await f(...args));
    return str.replace(regex, () => results.shift());
  };
  var regexEscape = (str) => str.replace(/[-/\\^$*+?.()|[\]{}]/g, "\\$&");
  var tidy = (obj) => {
    for (const [key, val] of Object.entries(obj))
      if (val == null) delete obj[key];
      else if (Array.isArray(val)) {
        obj[key] = val.filter((x) => x).map((x) => typeof x === "object" && !Array.isArray(x) ? tidy(x) : x);
        if (!obj[key].length) delete obj[key];
        else if (obj[key].length === 1) obj[key] = obj[key][0];
      } else if (typeof val === "object") {
        obj[key] = tidy(val);
        if (!Object.keys(val).length) delete obj[key];
      }
    const keys = Object.keys(obj);
    if (keys.length === 1 && keys[0] === "name") return obj[keys[0]];
    return obj;
  };
  var getPrefixes = (doc) => {
    const map = new Map(Object.entries(PREFIX));
    const value = doc.documentElement.getAttributeNS(NS.EPUB, "prefix") || doc.documentElement.getAttribute("prefix");
    if (value) for (const [, prefix, url] of value.matchAll(/(.+): +(.+)[ \t\r\n]*/g)) map.set(prefix, url);
    return map;
  };
  var getPropertyURL = (value, prefixes) => {
    if (!value) return null;
    const [a, b] = value.split(":");
    const prefix = b ? a : null;
    const reference = b ? b : a;
    const baseURL = prefixes.get(prefix);
    return baseURL ? baseURL + reference : null;
  };
  var getMetadata = (opf) => {
    var _a2, _b2, _c, _d, _e, _f2, _g, _h, _i, _j, _k, _l, _m, _n, _o, _p, _q, _r, _s, _t, _u, _v, _w, _x, _y, _z, _A, _B, _C, _D, _E, _F, _G, _H, _I, _J, _K;
    const { $ } = childGetter(opf, NS.OPF);
    const $metadata = $(opf.documentElement, "metadata");
    const els = Object.groupBy($metadata.children, (el) => el.namespaceURI === NS.DC ? "dc" : el.namespaceURI === NS.OPF && el.localName === "meta" ? el.hasAttribute("name") ? "legacyMeta" : "meta" : "");
    const baseLang = (_b2 = (_a2 = $metadata.getAttribute("xml:lang")) != null ? _a2 : opf.documentElement.getAttribute("xml:lang")) != null ? _b2 : "und";
    const prefixes = getPrefixes(opf);
    const parse2 = (el) => {
      var _a3, _b3;
      const property = el.getAttribute("property");
      const scheme = el.getAttribute("scheme");
      return {
        property: (_a3 = getPropertyURL(property, prefixes)) != null ? _a3 : property,
        scheme: (_b3 = getPropertyURL(scheme, prefixes)) != null ? _b3 : scheme,
        lang: el.getAttribute("xml:lang"),
        value: getElementText(el),
        props: getProperties(el),
        // `opf:` attributes from EPUB 2 & EPUB 3.1 (removed in EPUB 3.2)
        attrs: Object.fromEntries(Array.from(el.attributes).filter((attr) => attr.namespaceURI === NS.OPF).map((attr) => [attr.localName, attr.value]))
      };
    };
    const refines = Map.groupBy((_c = els.meta) != null ? _c : [], (el) => el.getAttribute("refines"));
    const getProperties = (el) => {
      const els2 = refines.get(el ? "#" + el.getAttribute("id") : null);
      if (!els2) return null;
      return Object.groupBy(els2.map(parse2), (x) => x.property);
    };
    const dc = Object.fromEntries(Object.entries(Object.groupBy(els.dc, (el) => el.localName)).map(([name, els2]) => [name, els2.map(parse2)]));
    const properties = (_d = getProperties()) != null ? _d : {};
    const legacyMeta = Object.fromEntries((_f2 = (_e = els.legacyMeta) == null ? void 0 : _e.map((el) => [el.getAttribute("name"), el.getAttribute("content")])) != null ? _f2 : []);
    const one = (x) => {
      var _a3;
      return (_a3 = x == null ? void 0 : x[0]) == null ? void 0 : _a3.value;
    };
    const prop = (x, p) => {
      var _a3;
      return one((_a3 = x == null ? void 0 : x.props) == null ? void 0 : _a3[p]);
    };
    const makeLanguageMap = (x) => {
      var _a3, _b3, _c2, _d2, _e2;
      if (!x) return null;
      const alts = (_b3 = (_a3 = x.props) == null ? void 0 : _a3["alternate-script"]) != null ? _b3 : [];
      const altRep = x.attrs["alt-rep"];
      if (!alts.length && (!x.lang || x.lang === baseLang) && !altRep) return x.value;
      const map = { [(_c2 = x.lang) != null ? _c2 : baseLang]: x.value };
      if (altRep) map[x.attrs["alt-rep-lang"]] = altRep;
      for (const y of alts) (_e2 = map[_d2 = y.lang]) != null ? _e2 : map[_d2] = y.value;
      return map;
    };
    const makeContributor = (x) => {
      var _a3, _b3, _c2, _d2, _e2, _f3, _g2, _h2, _i2;
      return x ? {
        name: makeLanguageMap(x),
        sortAs: (_c2 = makeLanguageMap((_b3 = (_a3 = x.props) == null ? void 0 : _a3["file-as"]) == null ? void 0 : _b3[0])) != null ? _c2 : x.attrs["file-as"],
        role: (_g2 = (_f3 = (_e2 = (_d2 = x.props) == null ? void 0 : _d2.role) == null ? void 0 : _e2.filter((x2) => x2.scheme === PREFIX.marc + "relators")) == null ? void 0 : _f3.map((x2) => x2.value)) != null ? _g2 : [x.attrs.role],
        code: (_h2 = prop(x, "term")) != null ? _h2 : x.attrs.term,
        scheme: (_i2 = prop(x, "authority")) != null ? _i2 : x.attrs.authority
      } : null;
    };
    const makeCollection = (x) => {
      var _a3;
      return {
        name: makeLanguageMap(x),
        // NOTE: webpub requires number but EPUB allows values like "2.2.1"
        position: one((_a3 = x.props) == null ? void 0 : _a3["group-position"])
      };
    };
    const makeAltIdentifier = (x) => {
      var _a3;
      const { value } = x;
      if (/^urn:/i.test(value)) return value;
      if (/^doi:/i.test(value)) return "urn:".concat(value);
      const type = (_a3 = x.props) == null ? void 0 : _a3["identifier-type"];
      if (!type) {
        const scheme = x.attrs.scheme;
        if (!scheme) return value;
        if (/^(doi|isbn|uuid)$/i.test(scheme)) return "urn:".concat(scheme, ":").concat(value);
        return { scheme, value };
      }
      if (type.scheme === PREFIX.onix + "codelist5") {
        const nid = ONIX5[type.value];
        if (nid) return "urn:".concat(nid, ":").concat(value);
      }
      return value;
    };
    const belongsTo = Object.groupBy(
      (_g = properties["belongs-to-collection"]) != null ? _g : [],
      (x) => prop(x, "collection-type") === "series" ? "series" : "collection"
    );
    const mainTitle = (_j = (_h = dc.title) == null ? void 0 : _h.find((x) => prop(x, "title-type") === "main")) != null ? _j : (_i = dc.title) == null ? void 0 : _i[0];
    const metadata = {
      identifier: getIdentifier(opf),
      title: makeLanguageMap(mainTitle),
      sortAs: (_o = (_n = makeLanguageMap((_l = (_k = mainTitle == null ? void 0 : mainTitle.props) == null ? void 0 : _k["file-as"]) == null ? void 0 : _l[0])) != null ? _n : (_m = mainTitle == null ? void 0 : mainTitle.attrs) == null ? void 0 : _m["file-as"]) != null ? _o : legacyMeta == null ? void 0 : legacyMeta["calibre:title_sort"],
      subtitle: (_q = (_p = dc.title) == null ? void 0 : _p.find((x) => prop(x, "title-type") === "subtitle")) == null ? void 0 : _q.value,
      language: (_r = dc.language) == null ? void 0 : _r.map((x) => x.value),
      description: one(dc.description),
      publisher: (_s = dc.publisher) == null ? void 0 : _s.map(makeContributor),
      published: (_v = (_u = (_t = dc.date) == null ? void 0 : _t.find((x) => x.attrs.event === "publication")) == null ? void 0 : _u.value) != null ? _v : one(dc.date),
      modified: (_y = one(properties[PREFIX.dcterms + "modified"])) != null ? _y : (_x = (_w = dc.date) == null ? void 0 : _w.find((x) => x.attrs.event === "modification")) == null ? void 0 : _x.value,
      subject: (_z = dc.subject) == null ? void 0 : _z.map(makeContributor),
      belongsTo: {
        collection: (_A = belongsTo.collection) == null ? void 0 : _A.map(makeCollection),
        series: ((_C = (_B = belongsTo.series) == null ? void 0 : _B.map(makeCollection)) != null ? _C : legacyMeta == null ? void 0 : legacyMeta["calibre:series"]) ? {
          name: legacyMeta == null ? void 0 : legacyMeta["calibre:series"],
          position: parseFloat(legacyMeta == null ? void 0 : legacyMeta["calibre:series_index"])
        } : null
      },
      altIdentifier: (_D = dc.identifier) == null ? void 0 : _D.map(makeAltIdentifier),
      source: (_E = dc.source) == null ? void 0 : _E.map(makeAltIdentifier),
      // NOTE: not in webpub schema
      rights: one(dc.rights),
      // NOTE: not in webpub schema
      pageBreakSource: one(properties["pageBreakSource"])
      // NOTE: not in webpub schema
    };
    const remapContributor = (defaultKey) => (x) => {
      var _a3;
      const keys = new Set((_a3 = x.role) == null ? void 0 : _a3.map((role) => {
        var _a4;
        return (_a4 = RELATORS[role]) != null ? _a4 : defaultKey;
      }));
      return [keys.size ? keys : [defaultKey], x];
    };
    for (const [keys, val] of [].concat(
      (_H = (_G = (_F = dc.creator) == null ? void 0 : _F.map(makeContributor)) == null ? void 0 : _G.map(remapContributor("author"))) != null ? _H : [],
      (_K = (_J = (_I = dc.contributor) == null ? void 0 : _I.map(makeContributor)) == null ? void 0 : _J.map(remapContributor("contributor"))) != null ? _K : []
    ))
      for (const key of keys)
        if (metadata[key]) metadata[key].push(val);
        else metadata[key] = [val];
    tidy(metadata);
    if (metadata.altIdentifier === metadata.identifier)
      delete metadata.altIdentifier;
    const rendition = {};
    const media = {};
    for (const [key, val] of Object.entries(properties)) {
      if (key.startsWith(PREFIX.rendition))
        rendition[camel(key.replace(PREFIX.rendition, ""))] = one(val);
      else if (key.startsWith(PREFIX.media))
        media[camel(key.replace(PREFIX.media, ""))] = one(val);
    }
    if (media.duration) media.duration = parseClock(media.duration);
    return { metadata, rendition, media };
  };
  var parseNav = (doc, resolve = (f) => f) => {
    var _a2, _b2;
    const { $, $$, $$$ } = childGetter(doc, NS.XHTML);
    const resolveHref = (href) => href ? decodeURI(resolve(href)) : null;
    const parseLI = (getType) => ($li) => {
      var _a3, _b3;
      const $a = (_a3 = $($li, "a")) != null ? _a3 : $($li, "span");
      const $ol = $($li, "ol");
      const href = resolveHref($a == null ? void 0 : $a.getAttribute("href"));
      const label = getElementText($a) || ($a == null ? void 0 : $a.getAttribute("title"));
      const result = { label, href, subitems: parseOL($ol) };
      if (getType) result.type = (_b3 = $a == null ? void 0 : $a.getAttributeNS(NS.EPUB, "type")) == null ? void 0 : _b3.split(/\s/);
      return result;
    };
    const parseOL = ($ol, getType) => $ol ? $$($ol, "li").map(parseLI(getType)) : null;
    const parseNav2 = ($nav, getType) => parseOL($($nav, "ol"), getType);
    const $$nav = $$$(doc, "nav");
    let toc = null, pageList = null, landmarks = null, others = [];
    for (const $nav of $$nav) {
      const type = (_b2 = (_a2 = $nav.getAttributeNS(NS.EPUB, "type")) == null ? void 0 : _a2.split(/\s/)) != null ? _b2 : [];
      if (type.includes("toc")) toc != null ? toc : toc = parseNav2($nav);
      else if (type.includes("page-list")) pageList != null ? pageList : pageList = parseNav2($nav);
      else if (type.includes("landmarks")) landmarks != null ? landmarks : landmarks = parseNav2($nav, true);
      else others.push({
        label: getElementText($nav.firstElementChild),
        type,
        list: parseNav2($nav)
      });
    }
    return { toc, pageList, landmarks, others };
  };
  var parseNCX = (doc, resolve = (f) => f) => {
    const { $, $$ } = childGetter(doc, NS.NCX);
    const resolveHref = (href) => href ? decodeURI(resolve(href)) : null;
    const parseItem = (el) => {
      const $label = $(el, "navLabel");
      const $content = $(el, "content");
      const label = getElementText($label);
      const href = resolveHref($content.getAttribute("src"));
      if (el.localName === "navPoint") {
        const els = $$(el, "navPoint");
        return { label, href, subitems: els.length ? els.map(parseItem) : null };
      }
      return { label, href };
    };
    const parseList = (el, itemName) => $$(el, itemName).map(parseItem);
    const getSingle = (container, itemName) => {
      const $container = $(doc.documentElement, container);
      return $container ? parseList($container, itemName) : null;
    };
    return {
      toc: getSingle("navMap", "navPoint"),
      pageList: getSingle("pageList", "pageTarget"),
      others: $$(doc.documentElement, "navList").map((el) => ({
        label: getElementText($(el, "navLabel")),
        list: parseList(el, "navTarget")
      }))
    };
  };
  var parseClock = (str) => {
    if (!str) return;
    const parts = str.split(":").map((x2) => parseFloat(x2));
    if (parts.length === 3) {
      const [h, m, s] = parts;
      return h * 60 * 60 + m * 60 + s;
    }
    if (parts.length === 2) {
      const [m, s] = parts;
      return m * 60 + s;
    }
    const [x, unit] = str.split(/(?=[^\d.])/);
    const n = parseFloat(x);
    const f = unit === "h" ? 60 * 60 : unit === "min" ? 60 : unit === "ms" ? 1e-3 : 1;
    return n * f;
  };
  var _entries, _lastMediaOverlayItem, _sectionIndex, _audioIndex, _itemIndex, _audio, _volume, _rate, _state, _MediaOverlay_instances, loadSMIL_fn, activeAudio_get, activeItem_get, error_fn, highlight_fn, unhighlight_fn, play_fn, stop_fn;
  var MediaOverlay = class extends EventTarget {
    constructor(book, loadXML) {
      super();
      __privateAdd(this, _MediaOverlay_instances);
      __privateAdd(this, _entries);
      __privateAdd(this, _lastMediaOverlayItem);
      __privateAdd(this, _sectionIndex);
      __privateAdd(this, _audioIndex);
      __privateAdd(this, _itemIndex);
      __privateAdd(this, _audio);
      __privateAdd(this, _volume, 1);
      __privateAdd(this, _rate, 1);
      __privateAdd(this, _state);
      this.book = book;
      this.loadXML = loadXML;
    }
    async start(sectionIndex, filter3 = () => true) {
      var _a2;
      (_a2 = __privateGet(this, _audio)) == null ? void 0 : _a2.pause();
      const section = this.book.sections[sectionIndex];
      const href = section == null ? void 0 : section.id;
      if (!href) return;
      const { mediaOverlay } = section;
      if (!mediaOverlay) return this.start(sectionIndex + 1);
      __privateSet(this, _sectionIndex, sectionIndex);
      await __privateMethod(this, _MediaOverlay_instances, loadSMIL_fn).call(this, mediaOverlay);
      for (let i = 0; i < __privateGet(this, _entries).length; i++) {
        const { items } = __privateGet(this, _entries)[i];
        for (let j = 0; j < items.length; j++) {
          if (items[j].text.split("#")[0] === href && filter3(items[j], j, items))
            return __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, i, j).catch((e) => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, e));
        }
      }
    }
    pause() {
      var _a2;
      __privateSet(this, _state, "paused");
      (_a2 = __privateGet(this, _audio)) == null ? void 0 : _a2.pause();
    }
    resume() {
      var _a2;
      __privateSet(this, _state, "playing");
      (_a2 = __privateGet(this, _audio)) == null ? void 0 : _a2.play().catch((e) => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, e));
    }
    stop() {
      __privateSet(this, _state, "stopped");
      __privateMethod(this, _MediaOverlay_instances, stop_fn).call(this);
    }
    prev() {
      if (__privateGet(this, _itemIndex) > 0) __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, __privateGet(this, _audioIndex), __privateGet(this, _itemIndex) - 1);
      else if (__privateGet(this, _audioIndex) > 0) __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, __privateGet(this, _audioIndex) - 1, __privateGet(this, _entries)[__privateGet(this, _audioIndex) - 1].items.length - 1);
      else if (__privateGet(this, _sectionIndex) > 0)
        this.start(__privateGet(this, _sectionIndex) - 1, (_, i, items) => i === items.length - 1);
    }
    next() {
      __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, __privateGet(this, _audioIndex), __privateGet(this, _itemIndex) + 1);
    }
    setVolume(volume) {
      __privateSet(this, _volume, volume);
      if (__privateGet(this, _audio)) __privateGet(this, _audio).volume = volume;
    }
    setRate(rate) {
      __privateSet(this, _rate, rate);
      if (__privateGet(this, _audio)) __privateGet(this, _audio).playbackRate = rate;
    }
  };
  _entries = new WeakMap();
  _lastMediaOverlayItem = new WeakMap();
  _sectionIndex = new WeakMap();
  _audioIndex = new WeakMap();
  _itemIndex = new WeakMap();
  _audio = new WeakMap();
  _volume = new WeakMap();
  _rate = new WeakMap();
  _state = new WeakMap();
  _MediaOverlay_instances = new WeakSet();
  loadSMIL_fn = async function(item) {
    if (__privateGet(this, _lastMediaOverlayItem) === item) return;
    const doc = await this.loadXML(item.href);
    const resolve = (href) => href ? resolveURL(href, item.href) : null;
    const { $, $$$ } = childGetter(doc, NS.SMIL);
    __privateSet(this, _audioIndex, -1);
    __privateSet(this, _itemIndex, -1);
    __privateSet(this, _entries, $$$(doc, "par").reduce((arr, $par) => {
      var _a2;
      const text = resolve((_a2 = $($par, "text")) == null ? void 0 : _a2.getAttribute("src"));
      const $audio = $($par, "audio");
      if (!text || !$audio) return arr;
      const src = resolve($audio.getAttribute("src"));
      const begin = parseClock($audio.getAttribute("clipBegin"));
      const end = parseClock($audio.getAttribute("clipEnd"));
      const last = arr.at(-1);
      if ((last == null ? void 0 : last.src) === src) last.items.push({ text, begin, end });
      else arr.push({ src, items: [{ text, begin, end }] });
      return arr;
    }, []));
    __privateSet(this, _lastMediaOverlayItem, item);
  };
  activeAudio_get = function() {
    return __privateGet(this, _entries)[__privateGet(this, _audioIndex)];
  };
  activeItem_get = function() {
    var _a2, _b2;
    return (_b2 = (_a2 = __privateGet(this, _MediaOverlay_instances, activeAudio_get)) == null ? void 0 : _a2.items) == null ? void 0 : _b2[__privateGet(this, _itemIndex)];
  };
  error_fn = function(e) {
    console.error(e);
    this.dispatchEvent(new CustomEvent("error", { detail: e }));
  };
  highlight_fn = function() {
    this.dispatchEvent(new CustomEvent("highlight", { detail: __privateGet(this, _MediaOverlay_instances, activeItem_get) }));
  };
  unhighlight_fn = function() {
    this.dispatchEvent(new CustomEvent("unhighlight", { detail: __privateGet(this, _MediaOverlay_instances, activeItem_get) }));
  };
  play_fn = async function(audioIndex, itemIndex) {
    var _a2, _b2;
    __privateMethod(this, _MediaOverlay_instances, stop_fn).call(this);
    __privateSet(this, _audioIndex, audioIndex);
    __privateSet(this, _itemIndex, itemIndex);
    const src = (_a2 = __privateGet(this, _MediaOverlay_instances, activeAudio_get)) == null ? void 0 : _a2.src;
    if (!src || !__privateGet(this, _MediaOverlay_instances, activeItem_get)) return this.start(__privateGet(this, _sectionIndex) + 1);
    const url = URL.createObjectURL(await this.book.loadBlob(src));
    const audio = new Audio(url);
    __privateSet(this, _audio, audio);
    audio.volume = __privateGet(this, _volume);
    audio.playbackRate = __privateGet(this, _rate);
    audio.addEventListener("timeupdate", () => {
      var _a3, _b3;
      if (audio.paused) return;
      const t = audio.currentTime;
      const { items } = __privateGet(this, _MediaOverlay_instances, activeAudio_get);
      if (t > ((_a3 = __privateGet(this, _MediaOverlay_instances, activeItem_get)) == null ? void 0 : _a3.end)) {
        __privateMethod(this, _MediaOverlay_instances, unhighlight_fn).call(this);
        if (__privateGet(this, _itemIndex) === items.length - 1) {
          __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, __privateGet(this, _audioIndex) + 1, 0).catch((e) => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, e));
          return;
        }
      }
      const oldIndex = __privateGet(this, _itemIndex);
      while (((_b3 = items[__privateGet(this, _itemIndex) + 1]) == null ? void 0 : _b3.begin) <= t) __privateWrapper(this, _itemIndex)._++;
      if (__privateGet(this, _itemIndex) !== oldIndex) __privateMethod(this, _MediaOverlay_instances, highlight_fn).call(this);
    });
    audio.addEventListener("error", () => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, new Error("Failed to load ".concat(src))));
    audio.addEventListener("playing", () => __privateMethod(this, _MediaOverlay_instances, highlight_fn).call(this));
    audio.addEventListener("ended", () => {
      __privateMethod(this, _MediaOverlay_instances, unhighlight_fn).call(this);
      URL.revokeObjectURL(url);
      __privateSet(this, _audio, null);
      __privateMethod(this, _MediaOverlay_instances, play_fn).call(this, audioIndex + 1, 0).catch((e) => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, e));
    });
    if (__privateGet(this, _state) === "paused") {
      __privateMethod(this, _MediaOverlay_instances, highlight_fn).call(this);
      audio.currentTime = (_b2 = __privateGet(this, _MediaOverlay_instances, activeItem_get).begin) != null ? _b2 : 0;
    } else audio.addEventListener("canplaythrough", () => {
      var _a3;
      audio.currentTime = (_a3 = __privateGet(this, _MediaOverlay_instances, activeItem_get).begin) != null ? _a3 : 0;
      __privateSet(this, _state, "playing");
      audio.play().catch((e) => __privateMethod(this, _MediaOverlay_instances, error_fn).call(this, e));
    }, { once: true });
  };
  stop_fn = function() {
    if (__privateGet(this, _audio)) {
      __privateGet(this, _audio).pause();
      URL.revokeObjectURL(__privateGet(this, _audio).src);
      __privateSet(this, _audio, null);
      __privateMethod(this, _MediaOverlay_instances, unhighlight_fn).call(this);
    }
  };
  var isUUID = /([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})/;
  var getUUID = (opf) => {
    for (const el of opf.getElementsByTagNameNS(NS.DC, "identifier")) {
      const [id] = getElementText(el).split(":").slice(-1);
      if (isUUID.test(id)) return id;
    }
    return "";
  };
  var getIdentifier = (opf) => {
    var _a2;
    return getElementText(
      (_a2 = opf.getElementById(opf.documentElement.getAttribute("unique-identifier"))) != null ? _a2 : opf.getElementsByTagNameNS(NS.DC, "identifier")[0]
    );
  };
  var deobfuscate = async (key, length, blob) => {
    const array = new Uint8Array(await blob.slice(0, length).arrayBuffer());
    length = Math.min(length, array.length);
    for (var i = 0; i < length; i++) array[i] = array[i] ^ key[i % key.length];
    return new Blob([array, blob.slice(length)], { type: blob.type });
  };
  var WebCryptoSHA1 = async (str) => {
    const data = new TextEncoder().encode(str);
    const buffer = await globalThis.crypto.subtle.digest("SHA-1", data);
    return new Uint8Array(buffer);
  };
  var deobfuscators = (sha1 = WebCryptoSHA1) => ({
    "http://www.idpf.org/2008/embedding": {
      key: (opf) => sha1(getIdentifier(opf).replaceAll(/[\u0020\u0009\u000d\u000a]/g, "")),
      decode: (key, blob) => deobfuscate(key, 1040, blob)
    },
    "http://ns.adobe.com/pdf/enc#RC": {
      key: (opf) => {
        const uuid = getUUID(opf).replaceAll("-", "");
        return Uint8Array.from({ length: 16 }, (_, i) => parseInt(uuid.slice(i * 2, i * 2 + 2), 16));
      },
      decode: (key, blob) => deobfuscate(key, 1024, blob)
    }
  });
  var _uris, _decoders, _algorithms;
  var Encryption = class {
    constructor(algorithms) {
      __privateAdd(this, _uris, /* @__PURE__ */ new Map());
      __privateAdd(this, _decoders, /* @__PURE__ */ new Map());
      __privateAdd(this, _algorithms);
      __privateSet(this, _algorithms, algorithms);
    }
    async init(encryption, opf) {
      if (!encryption) return;
      const data = Array.from(
        encryption.getElementsByTagNameNS(NS.ENC, "EncryptedData"),
        (el) => {
          var _a2, _b2;
          return {
            algorithm: (_a2 = el.getElementsByTagNameNS(NS.ENC, "EncryptionMethod")[0]) == null ? void 0 : _a2.getAttribute("Algorithm"),
            uri: (_b2 = el.getElementsByTagNameNS(NS.ENC, "CipherReference")[0]) == null ? void 0 : _b2.getAttribute("URI")
          };
        }
      );
      for (const { algorithm, uri } of data) {
        if (!__privateGet(this, _decoders).has(algorithm)) {
          const algo = __privateGet(this, _algorithms)[algorithm];
          if (!algo) {
            console.warn("Unknown encryption algorithm");
            continue;
          }
          const key = await algo.key(opf);
          __privateGet(this, _decoders).set(algorithm, (blob) => algo.decode(key, blob));
        }
        __privateGet(this, _uris).set(uri, algorithm);
      }
    }
    getDecoder(uri) {
      var _a2;
      return (_a2 = __privateGet(this, _decoders).get(__privateGet(this, _uris).get(uri))) != null ? _a2 : ((x) => x);
    }
  };
  _uris = new WeakMap();
  _decoders = new WeakMap();
  _algorithms = new WeakMap();
  var Resources = class {
    constructor({ opf, resolveHref }) {
      var _a2, _b2, _c, _d, _e, _f2, _g, _h;
      this.opf = opf;
      const { $, $$, $$$ } = childGetter(opf, NS.OPF);
      const $manifest = $(opf.documentElement, "manifest");
      const $spine = $(opf.documentElement, "spine");
      const $$itemref = $$($spine, "itemref");
      this.manifest = $$($manifest, "item").map(getAttributes("href", "id", "media-type", "properties", "media-overlay")).map((item) => {
        var _a3;
        item.href = resolveHref(item.href);
        item.properties = (_a3 = item.properties) == null ? void 0 : _a3.split(/\s/);
        return item;
      });
      this.manifestById = new Map(this.manifest.map((item) => [item.id, item]));
      this.spine = $$itemref.map(getAttributes("idref", "id", "linear", "properties")).map((item) => {
        var _a3;
        return item.properties = (_a3 = item.properties) == null ? void 0 : _a3.split(/\s/), item;
      });
      this.pageProgressionDirection = $spine.getAttribute("page-progression-direction");
      this.navPath = (_a2 = this.getItemByProperty("nav")) == null ? void 0 : _a2.href;
      this.ncxPath = (_c = (_b2 = this.getItemByID($spine.getAttribute("toc"))) != null ? _b2 : this.manifest.find((item) => item.mediaType === MIME.NCX)) == null ? void 0 : _c.href;
      const $guide = $(opf.documentElement, "guide");
      if ($guide) this.guide = $$($guide, "reference").map(getAttributes("type", "title", "href")).map(({ type, title, href }) => ({
        label: title,
        type: type.split(/\s/),
        href: resolveHref(href)
      }));
      this.cover = (_h = (_e = this.getItemByProperty("cover-image")) != null ? _e : this.getItemByID((_d = $$$(opf, "meta").find(filterAttribute("name", "cover"))) == null ? void 0 : _d.getAttribute("content"))) != null ? _h : this.getItemByHref((_g = (_f2 = this.guide) == null ? void 0 : _f2.find((ref) => ref.type.includes("cover"))) == null ? void 0 : _g.href);
      this.cfis = fromElements($$itemref);
    }
    getItemByID(id) {
      return this.manifestById.get(id);
    }
    getItemByHref(href) {
      return this.manifest.find((item) => item.href === href);
    }
    getItemByProperty(prop) {
      return this.manifest.find((item) => {
        var _a2;
        return (_a2 = item.properties) == null ? void 0 : _a2.includes(prop);
      });
    }
    resolveCFI(cfi) {
      var _a2;
      const parts = parse(cfi);
      const top = ((_a2 = parts.parent) != null ? _a2 : parts).shift();
      let $itemref = toElement(this.opf, top);
      if ($itemref && $itemref.nodeName !== "idref") {
        top.at(-1).id = null;
        $itemref = toElement(this.opf, top);
      }
      const idref = $itemref == null ? void 0 : $itemref.getAttribute("idref");
      const index2 = this.spine.findIndex((item) => item.idref === idref);
      const anchor = (doc) => toRange(doc, parts);
      return { index: index2, anchor };
    }
  };
  var _cache, _children, _refCount;
  var Loader = class {
    constructor({ loadText, loadBlob, resources }) {
      __privateAdd(this, _cache, /* @__PURE__ */ new Map());
      __privateAdd(this, _children, /* @__PURE__ */ new Map());
      __privateAdd(this, _refCount, /* @__PURE__ */ new Map());
      __publicField(this, "eventTarget", new EventTarget());
      this.loadText = loadText;
      this.loadBlob = loadBlob;
      this.manifest = resources.manifest;
      this.assets = resources.manifest;
    }
    async createURL(href, data, type, parent) {
      if (!data) return "";
      const detail = { data, type };
      Object.defineProperty(detail, "name", { value: href });
      const event = new CustomEvent("data", { detail });
      this.eventTarget.dispatchEvent(event);
      const newData = await event.detail.data;
      const newType = await event.detail.type;
      const url = URL.createObjectURL(new Blob([newData], { type: newType }));
      __privateGet(this, _cache).set(href, url);
      __privateGet(this, _refCount).set(href, 1);
      if (parent) {
        const childList = __privateGet(this, _children).get(parent);
        if (childList) childList.push(href);
        else __privateGet(this, _children).set(parent, [href]);
      }
      return url;
    }
    ref(href, parent) {
      const childList = __privateGet(this, _children).get(parent);
      if (!(childList == null ? void 0 : childList.includes(href))) {
        __privateGet(this, _refCount).set(href, __privateGet(this, _refCount).get(href) + 1);
        if (childList) childList.push(href);
        else __privateGet(this, _children).set(parent, [href]);
      }
      return __privateGet(this, _cache).get(href);
    }
    unref(href) {
      if (!__privateGet(this, _refCount).has(href)) return;
      const count = __privateGet(this, _refCount).get(href) - 1;
      if (count < 1) {
        URL.revokeObjectURL(__privateGet(this, _cache).get(href));
        __privateGet(this, _cache).delete(href);
        __privateGet(this, _refCount).delete(href);
        const childList = __privateGet(this, _children).get(href);
        if (childList) while (childList.length) this.unref(childList.pop());
        __privateGet(this, _children).delete(href);
      } else __privateGet(this, _refCount).set(href, count);
    }
    // load manifest item, recursively loading all resources as needed
    async loadItem(item, parents = []) {
      if (!item) return null;
      const { href, mediaType } = item;
      const isScript = MIME.JS.test(item.mediaType);
      const detail = { type: mediaType, isScript, allow: true };
      const event = new CustomEvent("load", { detail });
      this.eventTarget.dispatchEvent(event);
      const allow = await event.detail.allow;
      if (!allow) return null;
      const parent = parents.at(-1);
      if (__privateGet(this, _cache).has(href)) return this.ref(href, parent);
      const shouldReplace = (isScript || [MIME.XHTML, MIME.HTML, MIME.CSS, MIME.SVG].includes(mediaType)) && parents.every((p) => p !== href);
      if (shouldReplace) return this.loadReplaced(item, parents);
      const tryLoadBlob = Promise.resolve().then(() => this.loadBlob(href));
      return this.createURL(href, tryLoadBlob, mediaType, parent);
    }
    async loadHref(href, base, parents = []) {
      if (isExternal(href)) return href;
      const path = resolveURL(href, base);
      const item = this.manifest.find((item2) => item2.href === path);
      if (!item) return href;
      return this.loadItem(item, parents.concat(base));
    }
    async loadReplaced(item, parents = []) {
      var _a2, _b2, _c;
      const { href, mediaType } = item;
      const parent = parents.at(-1);
      let str = "";
      try {
        str = await this.loadText(href);
      } catch (e) {
        return this.createURL(href, Promise.reject(e), mediaType, parent);
      }
      if (!str) return null;
      if ([MIME.XHTML, MIME.HTML, MIME.SVG].includes(mediaType)) {
        let doc = new DOMParser().parseFromString(str, mediaType);
        if (mediaType === MIME.XHTML && (doc.querySelector("parsererror") || !((_a2 = doc.documentElement) == null ? void 0 : _a2.namespaceURI))) {
          console.warn((_c = (_b2 = doc.querySelector("parsererror")) == null ? void 0 : _b2.innerText) != null ? _c : "Invalid XHTML");
          item.mediaType = MIME.HTML;
          doc = new DOMParser().parseFromString(str, item.mediaType);
        }
        if ([MIME.XHTML, MIME.SVG].includes(item.mediaType)) {
          let child = doc.firstChild;
          while (child instanceof ProcessingInstruction) {
            if (child.data) {
              const replacedData = await replaceSeries(
                child.data,
                /(?:^|\s*)(href\s*=\s*['"])([^'"]*)(['"])/i,
                (_, p1, p2, p3) => this.loadHref(p2, href, parents).then((p22) => "".concat(p1).concat(p22).concat(p3))
              );
              child.replaceWith(doc.createProcessingInstruction(
                child.target,
                replacedData
              ));
            }
            child = child.nextSibling;
          }
        }
        const replace = async (el, attr) => el.setAttribute(
          attr,
          await this.loadHref(el.getAttribute(attr), href, parents)
        );
        for (const el of doc.querySelectorAll("link[href]")) await replace(el, "href");
        for (const el of doc.querySelectorAll("[src]")) await replace(el, "src");
        for (const el of doc.querySelectorAll("[poster]")) await replace(el, "poster");
        for (const el of doc.querySelectorAll("object[data]")) await replace(el, "data");
        for (const el of doc.querySelectorAll("[*|href]:not([href])"))
          el.setAttributeNS(NS.XLINK, "href", await this.loadHref(
            el.getAttributeNS(NS.XLINK, "href"),
            href,
            parents
          ));
        for (const el of doc.querySelectorAll("[srcset]"))
          el.setAttribute("srcset", await replaceSeries(
            el.getAttribute("srcset"),
            /(\s*)(.+?)\s*((?:\s[\d.]+[wx])+\s*(?:,|$)|,\s+|$)/g,
            (_, p1, p2, p3) => this.loadHref(p2, href, parents).then((p22) => "".concat(p1).concat(p22).concat(p3))
          ));
        for (const el of doc.querySelectorAll("style"))
          if (el.textContent) el.textContent = await this.replaceCSS(el.textContent, href, parents);
        for (const el of doc.querySelectorAll("[style]"))
          el.setAttribute(
            "style",
            await this.replaceCSS(el.getAttribute("style"), href, parents)
          );
        const result2 = new XMLSerializer().serializeToString(doc);
        return this.createURL(href, result2, item.mediaType, parent);
      }
      const result = mediaType === MIME.CSS ? await this.replaceCSS(str, href, parents) : await this.replaceString(str, href, parents);
      return this.createURL(href, result, mediaType, parent);
    }
    async replaceCSS(str, href, parents = []) {
      const replacedUrls = await replaceSeries(
        str,
        /url\(\s*["']?([^'"\n]*?)\s*["']?\s*\)/gi,
        (_, url) => this.loadHref(url, href, parents).then((url2) => 'url("'.concat(url2, '")'))
      );
      return replaceSeries(
        replacedUrls,
        /@import\s*["']([^"'\n]*?)["']/gi,
        (_, url) => this.loadHref(url, href, parents).then((url2) => '@import "'.concat(url2, '"'))
      );
    }
    // find & replace all possible relative paths for all assets without parsing
    replaceString(str, href, parents = []) {
      const assetMap = /* @__PURE__ */ new Map();
      const urls = this.assets.map((asset) => {
        if (asset.href === href) return;
        const relative = pathRelative(pathDirname(href), asset.href);
        const relativeEnc = encodeURI(relative);
        const rootRelative = "/" + asset.href;
        const rootRelativeEnc = encodeURI(rootRelative);
        const set = /* @__PURE__ */ new Set([relative, relativeEnc, rootRelative, rootRelativeEnc]);
        for (const url of set) assetMap.set(url, asset);
        return Array.from(set);
      }).flat().filter((x) => x);
      if (!urls.length) return str;
      const regex = new RegExp(urls.map(regexEscape).join("|"), "g");
      return replaceSeries(str, regex, async (match) => this.loadItem(
        assetMap.get(match.replace(/^\//, "")),
        parents.concat(href)
      ));
    }
    unloadItem(item) {
      this.unref(item == null ? void 0 : item.href);
    }
    destroy() {
      for (const url of __privateGet(this, _cache).values()) URL.revokeObjectURL(url);
    }
  };
  _cache = new WeakMap();
  _children = new WeakMap();
  _refCount = new WeakMap();
  var getHTMLFragment = (doc, id) => {
    var _a2;
    return (_a2 = doc.getElementById(id)) != null ? _a2 : doc.querySelector('[name="'.concat(CSS.escape(id), '"]'));
  };
  var getPageSpread = (properties) => {
    for (const p of properties) {
      if (p === "page-spread-left" || p === "rendition:page-spread-left")
        return "left";
      if (p === "page-spread-right" || p === "rendition:page-spread-right")
        return "right";
      if (p === "rendition:page-spread-center") return "center";
    }
  };
  var getDisplayOptions = (doc) => {
    if (!doc) return null;
    return {
      fixedLayout: getElementText(doc.querySelector('option[name="fixed-layout"]')),
      openToSpread: getElementText(doc.querySelector('option[name="open-to-spread"]'))
    };
  };
  var _loader, _encryption, _EPUB_instances, loadXML_fn;
  var EPUB = class {
    constructor({ loadText, loadBlob, getSize, sha1 }) {
      __privateAdd(this, _EPUB_instances);
      __publicField(this, "parser", new DOMParser());
      __privateAdd(this, _loader);
      __privateAdd(this, _encryption);
      this.loadText = loadText;
      this.loadBlob = loadBlob;
      this.getSize = getSize;
      __privateSet(this, _encryption, new Encryption(deobfuscators(sha1)));
    }
    async init() {
      var _a2, _b2, _c, _d, _e, _f2;
      const $container = await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, "META-INF/container.xml");
      if (!$container) throw new Error("Failed to load container file");
      const opfs = Array.from(
        $container.getElementsByTagNameNS(NS.CONTAINER, "rootfile"),
        getAttributes("full-path", "media-type")
      ).filter((file) => file.mediaType === "application/oebps-package+xml");
      if (!opfs.length) throw new Error("No package document defined in container");
      const opfPath = opfs[0].fullPath;
      const opf = await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, opfPath);
      if (!opf) throw new Error("Failed to load package document");
      const $encryption = await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, "META-INF/encryption.xml");
      await __privateGet(this, _encryption).init($encryption, opf);
      this.resources = new Resources({
        opf,
        resolveHref: (url) => resolveURL(url, opfPath)
      });
      __privateSet(this, _loader, new Loader({
        loadText: this.loadText,
        loadBlob: (uri) => Promise.resolve(this.loadBlob(uri)).then(__privateGet(this, _encryption).getDecoder(uri)),
        resources: this.resources
      }));
      this.transformTarget = __privateGet(this, _loader).eventTarget;
      this.sections = this.resources.spine.map((spineItem, index2) => {
        const { idref, linear, properties = [] } = spineItem;
        const item = this.resources.getItemByID(idref);
        if (!item) {
          console.warn('Could not find item with ID "'.concat(idref, '" in manifest'));
          return null;
        }
        return {
          id: item.href,
          load: () => __privateGet(this, _loader).loadItem(item),
          unload: () => __privateGet(this, _loader).unloadItem(item),
          createDocument: () => this.loadDocument(item),
          size: this.getSize(item.href),
          cfi: this.resources.cfis[index2],
          linear,
          pageSpread: getPageSpread(properties),
          resolveHref: (href) => resolveURL(href, item.href),
          mediaOverlay: item.mediaOverlay ? this.resources.getItemByID(item.mediaOverlay) : null
        };
      }).filter((s) => s);
      const { navPath, ncxPath } = this.resources;
      if (navPath) try {
        const resolve = (url) => resolveURL(url, navPath);
        const nav = parseNav(await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, navPath), resolve);
        this.toc = nav.toc;
        this.pageList = nav.pageList;
        this.landmarks = nav.landmarks;
      } catch (e) {
        console.warn(e);
      }
      if (!this.toc && ncxPath) try {
        const resolve = (url) => resolveURL(url, ncxPath);
        const ncx = parseNCX(await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, ncxPath), resolve);
        this.toc = ncx.toc;
        this.pageList = ncx.pageList;
      } catch (e) {
        console.warn(e);
      }
      (_a2 = this.landmarks) != null ? _a2 : this.landmarks = this.resources.guide;
      const { metadata, rendition, media } = getMetadata(opf);
      this.metadata = metadata;
      this.rendition = rendition;
      this.media = media;
      this.dir = this.resources.pageProgressionDirection;
      const displayOptions = getDisplayOptions(
        (_b2 = await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, "META-INF/com.apple.ibooks.display-options.xml")) != null ? _b2 : await __privateMethod(this, _EPUB_instances, loadXML_fn).call(this, "META-INF/com.kobobooks.display-options.xml")
      );
      if (displayOptions) {
        if (displayOptions.fixedLayout === "true")
          (_d = (_c = this.rendition).layout) != null ? _d : _c.layout = "pre-paginated";
        if (displayOptions.openToSpread === "false") (_f2 = (_e = this.sections.find((section) => section.linear !== "no")).pageSpread) != null ? _f2 : _e.pageSpread = this.dir === "rtl" ? "left" : "right";
      }
      return this;
    }
    async loadDocument(item) {
      const str = await this.loadText(item.href);
      return this.parser.parseFromString(str, item.mediaType);
    }
    getMediaOverlay() {
      return new MediaOverlay(this, __privateMethod(this, _EPUB_instances, loadXML_fn).bind(this));
    }
    resolveCFI(cfi) {
      return this.resources.resolveCFI(cfi);
    }
    resolveHref(href) {
      const [path, hash] = href.split("#");
      const item = this.resources.getItemByHref(decodeURI(path));
      if (!item) return null;
      const index2 = this.resources.spine.findIndex(({ idref }) => idref === item.id);
      const anchor = hash ? (doc) => getHTMLFragment(doc, hash) : () => 0;
      return { index: index2, anchor };
    }
    splitTOCHref(href) {
      var _a2;
      return (_a2 = href == null ? void 0 : href.split("#")) != null ? _a2 : [];
    }
    getTOCFragment(doc, id) {
      var _a2;
      return (_a2 = doc.getElementById(id)) != null ? _a2 : doc.querySelector('[name="'.concat(CSS.escape(id), '"]'));
    }
    isExternal(uri) {
      return isExternal(uri);
    }
    async getCover() {
      var _a2;
      const cover = (_a2 = this.resources) == null ? void 0 : _a2.cover;
      return (cover == null ? void 0 : cover.href) ? new Blob([await this.loadBlob(cover.href)], { type: cover.mediaType }) : null;
    }
    async getCalibreBookmarks() {
      const txt = await this.loadText("META-INF/calibre_bookmarks.txt");
      const magic = "encoding=json+base64:";
      if (txt == null ? void 0 : txt.startsWith(magic)) {
        const json = atob(txt.slice(magic.length));
        return JSON.parse(json);
      }
    }
    destroy() {
      var _a2;
      (_a2 = __privateGet(this, _loader)) == null ? void 0 : _a2.destroy();
    }
  };
  _loader = new WeakMap();
  _encryption = new WeakMap();
  _EPUB_instances = new WeakSet();
  loadXML_fn = async function(uri) {
    const str = await this.loadText(uri);
    if (!str) return null;
    const doc = this.parser.parseFromString(str, MIME.XML);
    if (doc.querySelector("parsererror"))
      throw new Error("XML parsing error: ".concat(uri, "\n").concat(doc.querySelector("parsererror").innerText));
    return doc;
  };

  // src/compat.mjs
  function read32BitsCompat(bytes, from) {
    let result = 0;
    for (let i = 0; i < 32; i++) {
      const position = from + i;
      result = (result << 1 | (bytes[position >>> 3] || 0) >>> 7 - (position & 7) & 1) >>> 0;
    }
    return result;
  }

  // vendor/foliate/mobi.js
  var unescapeHTML = (str) => {
    if (!str) return "";
    const textarea = document.createElement("textarea");
    textarea.innerHTML = str;
    return textarea.value;
  };
  var MIME2 = {
    XML: "application/xml",
    XHTML: "application/xhtml+xml",
    HTML: "text/html",
    CSS: "text/css",
    SVG: "image/svg+xml"
  };
  var PDB_HEADER = {
    name: [0, 32, "string"],
    type: [60, 4, "string"],
    creator: [64, 4, "string"],
    numRecords: [76, 2, "uint"]
  };
  var PALMDOC_HEADER = {
    compression: [0, 2, "uint"],
    numTextRecords: [8, 2, "uint"],
    recordSize: [10, 2, "uint"],
    encryption: [12, 2, "uint"]
  };
  var MOBI_HEADER = {
    magic: [16, 4, "string"],
    length: [20, 4, "uint"],
    type: [24, 4, "uint"],
    encoding: [28, 4, "uint"],
    uid: [32, 4, "uint"],
    version: [36, 4, "uint"],
    titleOffset: [84, 4, "uint"],
    titleLength: [88, 4, "uint"],
    localeRegion: [94, 1, "uint"],
    localeLanguage: [95, 1, "uint"],
    resourceStart: [108, 4, "uint"],
    huffcdic: [112, 4, "uint"],
    numHuffcdic: [116, 4, "uint"],
    exthFlag: [128, 4, "uint"],
    trailingFlags: [240, 4, "uint"],
    indx: [244, 4, "uint"]
  };
  var KF8_HEADER = {
    resourceStart: [108, 4, "uint"],
    fdst: [192, 4, "uint"],
    numFdst: [196, 4, "uint"],
    frag: [248, 4, "uint"],
    skel: [252, 4, "uint"],
    guide: [260, 4, "uint"]
  };
  var EXTH_HEADER = {
    magic: [0, 4, "string"],
    length: [4, 4, "uint"],
    count: [8, 4, "uint"]
  };
  var INDX_HEADER = {
    magic: [0, 4, "string"],
    length: [4, 4, "uint"],
    type: [8, 4, "uint"],
    idxt: [20, 4, "uint"],
    numRecords: [24, 4, "uint"],
    encoding: [28, 4, "uint"],
    language: [32, 4, "uint"],
    total: [36, 4, "uint"],
    ordt: [40, 4, "uint"],
    ligt: [44, 4, "uint"],
    numLigt: [48, 4, "uint"],
    numCncx: [52, 4, "uint"]
  };
  var TAGX_HEADER = {
    magic: [0, 4, "string"],
    length: [4, 4, "uint"],
    numControlBytes: [8, 4, "uint"]
  };
  var HUFF_HEADER = {
    magic: [0, 4, "string"],
    offset1: [8, 4, "uint"],
    offset2: [12, 4, "uint"]
  };
  var CDIC_HEADER = {
    magic: [0, 4, "string"],
    length: [4, 4, "uint"],
    numEntries: [8, 4, "uint"],
    codeLength: [12, 4, "uint"]
  };
  var FDST_HEADER = {
    magic: [0, 4, "string"],
    numEntries: [8, 4, "uint"]
  };
  var FONT_HEADER = {
    flags: [8, 4, "uint"],
    dataStart: [12, 4, "uint"],
    keyLength: [16, 4, "uint"],
    keyStart: [20, 4, "uint"]
  };
  var MOBI_ENCODING = {
    1252: "windows-1252",
    65001: "utf-8"
  };
  var EXTH_RECORD_TYPE = {
    100: ["creator", "string", true],
    101: ["publisher"],
    103: ["description"],
    104: ["isbn"],
    105: ["subject", "string", true],
    106: ["date"],
    108: ["contributor", "string", true],
    109: ["rights"],
    110: ["subjectCode", "string", true],
    112: ["source", "string", true],
    113: ["asin"],
    121: ["boundary", "uint"],
    122: ["fixedLayout"],
    125: ["numResources", "uint"],
    126: ["originalResolution"],
    127: ["zeroGutter"],
    128: ["zeroMargin"],
    129: ["coverURI"],
    132: ["regionMagnification"],
    201: ["coverOffset", "uint"],
    202: ["thumbnailOffset", "uint"],
    503: ["title"],
    524: ["language", "string", true],
    527: ["pageProgressionDirection"]
  };
  var MOBI_LANG = {
    1: [
      "ar",
      "ar-SA",
      "ar-IQ",
      "ar-EG",
      "ar-LY",
      "ar-DZ",
      "ar-MA",
      "ar-TN",
      "ar-OM",
      "ar-YE",
      "ar-SY",
      "ar-JO",
      "ar-LB",
      "ar-KW",
      "ar-AE",
      "ar-BH",
      "ar-QA"
    ],
    2: ["bg"],
    3: ["ca"],
    4: ["zh", "zh-TW", "zh-CN", "zh-HK", "zh-SG"],
    5: ["cs"],
    6: ["da"],
    7: ["de", "de-DE", "de-CH", "de-AT", "de-LU", "de-LI"],
    8: ["el"],
    9: [
      "en",
      "en-US",
      "en-GB",
      "en-AU",
      "en-CA",
      "en-NZ",
      "en-IE",
      "en-ZA",
      "en-JM",
      null,
      "en-BZ",
      "en-TT",
      "en-ZW",
      "en-PH"
    ],
    10: [
      "es",
      "es-ES",
      "es-MX",
      null,
      "es-GT",
      "es-CR",
      "es-PA",
      "es-DO",
      "es-VE",
      "es-CO",
      "es-PE",
      "es-AR",
      "es-EC",
      "es-CL",
      "es-UY",
      "es-PY",
      "es-BO",
      "es-SV",
      "es-HN",
      "es-NI",
      "es-PR"
    ],
    11: ["fi"],
    12: ["fr", "fr-FR", "fr-BE", "fr-CA", "fr-CH", "fr-LU", "fr-MC"],
    13: ["he"],
    14: ["hu"],
    15: ["is"],
    16: ["it", "it-IT", "it-CH"],
    17: ["ja"],
    18: ["ko"],
    19: ["nl", "nl-NL", "nl-BE"],
    20: ["no", "nb", "nn"],
    21: ["pl"],
    22: ["pt", "pt-BR", "pt-PT"],
    23: ["rm"],
    24: ["ro"],
    25: ["ru"],
    26: ["hr", null, "sr"],
    27: ["sk"],
    28: ["sq"],
    29: ["sv", "sv-SE", "sv-FI"],
    30: ["th"],
    31: ["tr"],
    32: ["ur"],
    33: ["id"],
    34: ["uk"],
    35: ["be"],
    36: ["sl"],
    37: ["et"],
    38: ["lv"],
    39: ["lt"],
    41: ["fa"],
    42: ["vi"],
    43: ["hy"],
    44: ["az"],
    45: ["eu"],
    46: ["hsb"],
    47: ["mk"],
    48: ["st"],
    49: ["ts"],
    50: ["tn"],
    52: ["xh"],
    53: ["zu"],
    54: ["af"],
    55: ["ka"],
    56: ["fo"],
    57: ["hi"],
    58: ["mt"],
    59: ["se"],
    62: ["ms"],
    63: ["kk"],
    65: ["sw"],
    67: ["uz", null, "uz-UZ"],
    68: ["tt"],
    69: ["bn"],
    70: ["pa"],
    71: ["gu"],
    72: ["or"],
    73: ["ta"],
    74: ["te"],
    75: ["kn"],
    76: ["ml"],
    77: ["as"],
    78: ["mr"],
    79: ["sa"],
    82: ["cy", "cy-GB"],
    83: ["gl", "gl-ES"],
    87: ["kok"],
    97: ["ne"],
    98: ["fy"]
  };
  var concatTypedArray = (a, b) => {
    const result = new a.constructor(a.length + b.length);
    result.set(a);
    result.set(b, a.length);
    return result;
  };
  var concatTypedArray3 = (a, b, c) => {
    const result = new a.constructor(a.length + b.length + c.length);
    result.set(a);
    result.set(b, a.length);
    result.set(c, a.length + b.length);
    return result;
  };
  var decoder = new TextDecoder();
  var getString = (buffer) => decoder.decode(buffer);
  var getUint = (buffer) => {
    if (!buffer) return;
    const l = buffer.byteLength;
    const func = l === 4 ? "getUint32" : l === 2 ? "getUint16" : "getUint8";
    return new DataView(buffer)[func](0);
  };
  var getStruct = (def, buffer) => Object.fromEntries(Array.from(Object.entries(def)).map(([key, [start, len, type]]) => [
    key,
    (type === "string" ? getString : getUint)(buffer.slice(start, start + len))
  ]));
  var getDecoder = (x) => new TextDecoder(MOBI_ENCODING[x]);
  var getVarLen = (byteArray, i = 0) => {
    let value = 0, length = 0;
    for (const byte of byteArray.subarray(i, i + 4)) {
      value = value << 7 | (byte & 127) >>> 0;
      length++;
      if (byte & 128) break;
    }
    return { value, length };
  };
  var getVarLenFromEnd = (byteArray) => {
    let value = 0;
    for (const byte of byteArray.subarray(-4)) {
      if (byte & 128) value = 0;
      value = value << 7 | byte & 127;
    }
    return value;
  };
  var countBitsSet = (x) => {
    let count = 0;
    for (; x > 0; x = x >> 1) if ((x & 1) === 1) count++;
    return count;
  };
  var countUnsetEnd = (x) => {
    let count = 0;
    while ((x & 1) === 0) x = x >> 1, count++;
    return count;
  };
  var decompressPalmDOC = (array) => {
    let output = [];
    for (let i = 0; i < array.length; i++) {
      const byte = array[i];
      if (byte === 0) output.push(0);
      else if (byte <= 8)
        for (const x of array.subarray(i + 1, (i += byte) + 1))
          output.push(x);
      else if (byte <= 127) output.push(byte);
      else if (byte <= 191) {
        const bytes = byte << 8 | array[i++ + 1];
        const distance = (bytes & 16383) >>> 3;
        const length = (bytes & 7) + 3;
        for (let j = 0; j < length; j++)
          output.push(output[output.length - distance]);
      } else output.push(32, byte ^ 128);
    }
    return Uint8Array.from(output);
  };
  var read32Bits = read32BitsCompat;
  var huffcdic = async (mobi, loadRecord) => {
    const huffRecord = await loadRecord(mobi.huffcdic);
    const { magic, offset1, offset2 } = getStruct(HUFF_HEADER, huffRecord);
    if (magic !== "HUFF") throw new Error("Invalid HUFF record");
    const table1 = Array.from({ length: 256 }, (_, i) => offset1 + i * 4).map((offset) => getUint(huffRecord.slice(offset, offset + 4))).map((x) => [x & 128, x & 31, x >>> 8]);
    const table2 = [null].concat(Array.from({ length: 32 }, (_, i) => offset2 + i * 8).map((offset) => [
      getUint(huffRecord.slice(offset, offset + 4)),
      getUint(huffRecord.slice(offset + 4, offset + 8))
    ]));
    const dictionary = [];
    for (let i = 1; i < mobi.numHuffcdic; i++) {
      const record = await loadRecord(mobi.huffcdic + i);
      const cdic = getStruct(CDIC_HEADER, record);
      if (cdic.magic !== "CDIC") throw new Error("Invalid CDIC record");
      const n = Math.min(1 << cdic.codeLength, cdic.numEntries - dictionary.length);
      const buffer = record.slice(cdic.length);
      for (let i2 = 0; i2 < n; i2++) {
        const offset = getUint(buffer.slice(i2 * 2, i2 * 2 + 2));
        const x = getUint(buffer.slice(offset, offset + 2));
        const length = x & 32767;
        const decompressed = x & 32768;
        const value = new Uint8Array(
          buffer.slice(offset + 2, offset + 2 + length)
        );
        dictionary.push([value, decompressed]);
      }
    }
    const decompress = (byteArray) => {
      let output = new Uint8Array();
      const bitLength = byteArray.byteLength * 8;
      for (let i = 0; i < bitLength; ) {
        const bits2 = Number(read32Bits(byteArray, i));
        let [found, codeLength, value] = table1[bits2 >>> 24];
        if (!found) {
          while (bits2 >>> 32 - codeLength < table2[codeLength][0])
            codeLength += 1;
          value = table2[codeLength][1];
        }
        if ((i += codeLength) > bitLength) break;
        const code = value - (bits2 >>> 32 - codeLength);
        let [result, decompressed] = dictionary[code];
        if (!decompressed) {
          result = decompress(result);
          dictionary[code] = [result, true];
        }
        output = concatTypedArray(output, result);
      }
      return output;
    };
    return decompress;
  };
  var getIndexData = async (indxIndex, loadRecord) => {
    const indxRecord = await loadRecord(indxIndex);
    const indx = getStruct(INDX_HEADER, indxRecord);
    if (indx.magic !== "INDX") throw new Error("Invalid INDX record");
    const decoder2 = getDecoder(indx.encoding);
    const tagxBuffer = indxRecord.slice(indx.length);
    const tagx = getStruct(TAGX_HEADER, tagxBuffer);
    if (tagx.magic !== "TAGX") throw new Error("Invalid TAGX section");
    const numTags = (tagx.length - 12) / 4;
    const tagTable = Array.from({ length: numTags }, (_, i) => new Uint8Array(tagxBuffer.slice(12 + i * 4, 12 + i * 4 + 4)));
    const cncx = {};
    let cncxRecordOffset = 0;
    for (let i = 0; i < indx.numCncx; i++) {
      const record = await loadRecord(indxIndex + indx.numRecords + i + 1);
      const array = new Uint8Array(record);
      for (let pos = 0; pos < array.byteLength; ) {
        const index2 = pos;
        const { value, length } = getVarLen(array, pos);
        pos += length;
        const result = record.slice(pos, pos + value);
        pos += value;
        cncx[cncxRecordOffset + index2] = decoder2.decode(result);
      }
      cncxRecordOffset += 65536;
    }
    const table = [];
    for (let i = 0; i < indx.numRecords; i++) {
      const record = await loadRecord(indxIndex + 1 + i);
      const array = new Uint8Array(record);
      const indx2 = getStruct(INDX_HEADER, record);
      if (indx2.magic !== "INDX") throw new Error("Invalid INDX record");
      for (let j = 0; j < indx2.numRecords; j++) {
        const offsetOffset = indx2.idxt + 4 + 2 * j;
        const offset = getUint(record.slice(offsetOffset, offsetOffset + 2));
        const length = getUint(record.slice(offset, offset + 1));
        const name = getString(record.slice(offset + 1, offset + 1 + length));
        const tags = [];
        const startPos = offset + 1 + length;
        let controlByteIndex = 0;
        let pos = startPos + tagx.numControlBytes;
        for (const [tag, numValues, mask, end] of tagTable) {
          if (end & 1) {
            controlByteIndex++;
            continue;
          }
          const offset2 = startPos + controlByteIndex;
          const value = getUint(record.slice(offset2, offset2 + 1)) & mask;
          if (value === mask) {
            if (countBitsSet(mask) > 1) {
              const { value: value2, length: length2 } = getVarLen(array, pos);
              tags.push([tag, null, value2, numValues]);
              pos += length2;
            } else tags.push([tag, 1, null, numValues]);
          } else tags.push([tag, value >> countUnsetEnd(mask), null, numValues]);
        }
        const tagMap = {};
        for (const [tag, valueCount, valueBytes, numValues] of tags) {
          const values = [];
          if (valueCount != null) {
            for (let i2 = 0; i2 < valueCount * numValues; i2++) {
              const { value, length: length2 } = getVarLen(array, pos);
              values.push(value);
              pos += length2;
            }
          } else {
            let count = 0;
            while (count < valueBytes) {
              const { value, length: length2 } = getVarLen(array, pos);
              values.push(value);
              pos += length2;
              count += length2;
            }
          }
          tagMap[tag] = values;
        }
        table.push({ name, tagMap });
      }
    }
    return { table, cncx };
  };
  var getNCX = async (indxIndex, loadRecord) => {
    const { table, cncx } = await getIndexData(indxIndex, loadRecord);
    const items = table.map(({ tagMap }, index2) => {
      var _a2, _b2, _c, _d, _e, _f2, _g;
      return {
        index: index2,
        offset: (_a2 = tagMap[1]) == null ? void 0 : _a2[0],
        size: (_b2 = tagMap[2]) == null ? void 0 : _b2[0],
        label: (_c = cncx[tagMap[3]]) != null ? _c : "",
        headingLevel: (_d = tagMap[4]) == null ? void 0 : _d[0],
        pos: tagMap[6],
        parent: (_e = tagMap[21]) == null ? void 0 : _e[0],
        firstChild: (_f2 = tagMap[22]) == null ? void 0 : _f2[0],
        lastChild: (_g = tagMap[23]) == null ? void 0 : _g[0]
      };
    });
    const getChildren = (item) => {
      if (item.firstChild == null) return item;
      item.children = items.filter((x) => x.parent === item.index).map(getChildren);
      return item;
    };
    return items.filter((item) => item.headingLevel === 0).map(getChildren);
  };
  var getEXTH = (buf, encoding) => {
    var _a2;
    const { magic, count } = getStruct(EXTH_HEADER, buf);
    if (magic !== "EXTH") throw new Error("Invalid EXTH header");
    const decoder2 = getDecoder(encoding);
    const results = {};
    let offset = 12;
    for (let i = 0; i < count; i++) {
      const type = getUint(buf.slice(offset, offset + 4));
      const length = getUint(buf.slice(offset + 4, offset + 8));
      if (type in EXTH_RECORD_TYPE) {
        const [name, typ, many] = EXTH_RECORD_TYPE[type];
        const data = buf.slice(offset + 8, offset + length);
        const value = typ === "uint" ? getUint(data) : decoder2.decode(data);
        if (many) {
          (_a2 = results[name]) != null ? _a2 : results[name] = [];
          results[name].push(value);
        } else results[name] = value;
      }
      offset += length;
    }
    return results;
  };
  var getFont = async (buf, unzlib) => {
    const { flags, dataStart, keyLength, keyStart } = getStruct(FONT_HEADER, buf);
    const array = new Uint8Array(buf.slice(dataStart));
    if (flags & 2) {
      const bytes = keyLength === 16 ? 1024 : 1040;
      const key = new Uint8Array(buf.slice(keyStart, keyStart + keyLength));
      const length = Math.min(bytes, array.length);
      for (var i = 0; i < length; i++) array[i] = array[i] ^ key[i % key.length];
    }
    if (flags & 1) try {
      return await unzlib(array);
    } catch (e) {
      console.warn(e);
      console.warn("Failed to decompress font");
    }
    return array;
  };
  var _file, _offsets;
  var PDB = class {
    constructor() {
      __privateAdd(this, _file);
      __privateAdd(this, _offsets);
      __publicField(this, "pdb");
    }
    async open(file) {
      __privateSet(this, _file, file);
      const pdb = getStruct(PDB_HEADER, await file.slice(0, 78).arrayBuffer());
      this.pdb = pdb;
      const buffer = await file.slice(78, 78 + pdb.numRecords * 8).arrayBuffer();
      __privateSet(this, _offsets, Array.from(
        { length: pdb.numRecords },
        (_, i) => getUint(buffer.slice(i * 8, i * 8 + 4))
      ).map((x, i, a) => [x, a[i + 1]]));
    }
    loadRecord(index2) {
      const offsets = __privateGet(this, _offsets)[index2];
      if (!offsets) throw new RangeError("Record index out of bounds");
      return __privateGet(this, _file).slice(...offsets).arrayBuffer();
    }
    async loadMagic(index2) {
      const start = __privateGet(this, _offsets)[index2][0];
      return getString(await __privateGet(this, _file).slice(start, start + 4).arrayBuffer());
    }
  };
  _file = new WeakMap();
  _offsets = new WeakMap();
  var _start, _resourceStart, _decoder, _encoder, _decompress, _removeTrailingEntries, _MOBI_instances, getHeaders_fn, setup_fn;
  var MOBI = class extends PDB {
    constructor({ unzlib }) {
      super();
      __privateAdd(this, _MOBI_instances);
      __privateAdd(this, _start, 0);
      __privateAdd(this, _resourceStart);
      __privateAdd(this, _decoder);
      __privateAdd(this, _encoder);
      __privateAdd(this, _decompress);
      __privateAdd(this, _removeTrailingEntries);
      this.unzlib = unzlib;
    }
    async open(file) {
      var _a2;
      await super.open(file);
      this.headers = __privateMethod(this, _MOBI_instances, getHeaders_fn).call(this, await super.loadRecord(0));
      __privateSet(this, _resourceStart, this.headers.mobi.resourceStart);
      let isKF8 = this.headers.mobi.version >= 8;
      if (!isKF8) {
        const boundary = (_a2 = this.headers.exth) == null ? void 0 : _a2.boundary;
        if (boundary < 4294967295) try {
          this.headers = __privateMethod(this, _MOBI_instances, getHeaders_fn).call(this, await super.loadRecord(boundary));
          __privateSet(this, _start, boundary);
          isKF8 = true;
        } catch (e) {
          console.warn(e);
          console.warn("Failed to open KF8; falling back to MOBI");
        }
      }
      await __privateMethod(this, _MOBI_instances, setup_fn).call(this);
      return isKF8 ? new KF8(this).init() : new MOBI6(this).init();
    }
    decode(...args) {
      return __privateGet(this, _decoder).decode(...args);
    }
    encode(...args) {
      return __privateGet(this, _encoder).encode(...args);
    }
    loadRecord(index2) {
      return super.loadRecord(__privateGet(this, _start) + index2);
    }
    loadMagic(index2) {
      return super.loadMagic(__privateGet(this, _start) + index2);
    }
    loadText(index2) {
      return this.loadRecord(index2 + 1).then((buf) => new Uint8Array(buf)).then(__privateGet(this, _removeTrailingEntries)).then(__privateGet(this, _decompress));
    }
    async loadResource(index2) {
      const buf = await super.loadRecord(__privateGet(this, _resourceStart) + index2);
      const magic = getString(buf.slice(0, 4));
      if (magic === "FONT") return getFont(buf, this.unzlib);
      if (magic === "VIDE" || magic === "AUDI") return buf.slice(12);
      return buf;
    }
    getNCX() {
      const index2 = this.headers.mobi.indx;
      if (index2 < 4294967295) return getNCX(index2, this.loadRecord.bind(this));
    }
    getMetadata() {
      var _a2, _b2, _c;
      const { mobi, exth } = this.headers;
      return {
        identifier: mobi.uid.toString(),
        title: unescapeHTML((exth == null ? void 0 : exth.title) || this.decode(mobi.title)),
        author: (_a2 = exth == null ? void 0 : exth.creator) == null ? void 0 : _a2.map(unescapeHTML),
        publisher: unescapeHTML(exth == null ? void 0 : exth.publisher),
        language: (_b2 = exth == null ? void 0 : exth.language) != null ? _b2 : mobi.language,
        published: exth == null ? void 0 : exth.date,
        description: unescapeHTML(exth == null ? void 0 : exth.description),
        subject: (_c = exth == null ? void 0 : exth.subject) == null ? void 0 : _c.map(unescapeHTML),
        rights: unescapeHTML(exth == null ? void 0 : exth.rights),
        contributor: exth == null ? void 0 : exth.contributor
      };
    }
    async getCover() {
      const { exth } = this.headers;
      const offset = (exth == null ? void 0 : exth.coverOffset) < 4294967295 ? exth == null ? void 0 : exth.coverOffset : (exth == null ? void 0 : exth.thumbnailOffset) < 4294967295 ? exth == null ? void 0 : exth.thumbnailOffset : null;
      if (offset != null) {
        const buf = await this.loadResource(offset);
        return new Blob([buf]);
      }
    }
  };
  _start = new WeakMap();
  _resourceStart = new WeakMap();
  _decoder = new WeakMap();
  _encoder = new WeakMap();
  _decompress = new WeakMap();
  _removeTrailingEntries = new WeakMap();
  _MOBI_instances = new WeakSet();
  getHeaders_fn = function(buf) {
    var _a2;
    const palmdoc = getStruct(PALMDOC_HEADER, buf);
    const mobi = getStruct(MOBI_HEADER, buf);
    if (mobi.magic !== "MOBI") throw new Error("Missing MOBI header");
    const { titleOffset, titleLength, localeLanguage, localeRegion } = mobi;
    mobi.title = buf.slice(titleOffset, titleOffset + titleLength);
    const lang = MOBI_LANG[localeLanguage];
    mobi.language = (_a2 = lang == null ? void 0 : lang[localeRegion >> 2]) != null ? _a2 : lang == null ? void 0 : lang[0];
    const exth = mobi.exthFlag & 64 ? getEXTH(buf.slice(mobi.length + 16), mobi.encoding) : null;
    const kf8 = mobi.version >= 8 ? getStruct(KF8_HEADER, buf) : null;
    return { palmdoc, mobi, exth, kf8 };
  };
  setup_fn = async function() {
    const { palmdoc, mobi } = this.headers;
    __privateSet(this, _decoder, getDecoder(mobi.encoding));
    __privateSet(this, _encoder, new TextEncoder());
    const { compression } = palmdoc;
    __privateSet(this, _decompress, compression === 1 ? (f) => f : compression === 2 ? decompressPalmDOC : compression === 17480 ? await huffcdic(mobi, this.loadRecord.bind(this)) : null);
    if (!__privateGet(this, _decompress)) throw new Error("Unknown compression type");
    const { trailingFlags } = mobi;
    const multibyte = trailingFlags & 1;
    const numTrailingEntries = countBitsSet(trailingFlags >>> 1);
    __privateSet(this, _removeTrailingEntries, (array) => {
      for (let i = 0; i < numTrailingEntries; i++) {
        const length = getVarLenFromEnd(array);
        array = array.subarray(0, -length);
      }
      if (multibyte) {
        const length = (array[array.length - 1] & 3) + 1;
        array = array.subarray(0, -length);
      }
      return array;
    });
  };
  var mbpPagebreakRegex = /<\s*(?:mbp:)?pagebreak[^>]*>/gi;
  var fileposRegex = /<[^<>]+filepos=['"]{0,1}(\d+)[^<>]*>/gi;
  var getIndent = (el) => {
    let x = 0;
    while (el) {
      const parent = el.parentElement;
      if (parent) {
        const tag = parent.tagName.toLowerCase();
        if (tag === "p") x += 1.5;
        else if (tag === "blockquote") x += 2;
      }
      el = parent;
    }
    return x;
  };
  function rawBytesToString(uint8Array) {
    const chunkSize = 32768;
    let result = "";
    for (let i = 0; i < uint8Array.length; i += chunkSize) {
      result += String.fromCharCode.apply(null, uint8Array.subarray(i, i + chunkSize));
    }
    return result;
  }
  var _resourceCache, _textCache, _cache2, _sections, _fileposList, _type;
  var MOBI6 = class {
    constructor(mobi) {
      __publicField(this, "parser", new DOMParser());
      __publicField(this, "serializer", new XMLSerializer());
      __privateAdd(this, _resourceCache, /* @__PURE__ */ new Map());
      __privateAdd(this, _textCache, /* @__PURE__ */ new Map());
      __privateAdd(this, _cache2, /* @__PURE__ */ new Map());
      __privateAdd(this, _sections);
      __privateAdd(this, _fileposList, []);
      __privateAdd(this, _type, MIME2.HTML);
      this.mobi = mobi;
    }
    async init() {
      var _a2;
      const recordBuffers = [];
      for (let i = 0; i < this.mobi.headers.palmdoc.numTextRecords; i++) {
        const buf = await this.mobi.loadText(i);
        recordBuffers.push(buf);
      }
      const totalLength = recordBuffers.reduce((sum, buf) => sum + buf.byteLength, 0);
      const array = new Uint8Array(totalLength);
      recordBuffers.reduce((offset, buf) => {
        array.set(new Uint8Array(buf), offset);
        return offset + buf.byteLength;
      }, 0);
      const str = rawBytesToString(array);
      __privateSet(this, _sections, [0].concat(Array.from(str.matchAll(mbpPagebreakRegex), (m) => m.index)).map((start, i, a) => {
        var _a3;
        const end = (_a3 = a[i + 1]) != null ? _a3 : array.length;
        return { book: this, raw: array.subarray(start, end) };
      }).map((section, i, arr) => {
        var _a3, _b2;
        section.start = (_b2 = (_a3 = arr[i - 1]) == null ? void 0 : _a3.end) != null ? _b2 : 0;
        section.end = section.start + section.raw.byteLength;
        return section;
      }));
      this.sections = __privateGet(this, _sections).map((section, index2) => ({
        id: index2,
        load: () => this.loadSection(section),
        createDocument: () => this.createDocument(section),
        size: section.end - section.start
      }));
      __privateSet(this, _fileposList, [...new Set(
        Array.from(str.matchAll(fileposRegex), (m) => m[1])
      )].map((filepos) => ({ filepos, number: Number(filepos) })).sort((a, b) => a.number - b.number));
      try {
        this.landmarks = await this.getGuide();
        const tocHref = (_a2 = this.landmarks.find(({ type }) => type == null ? void 0 : type.includes("toc"))) == null ? void 0 : _a2.href;
        if (tocHref) {
          const { index: index2 } = this.resolveHref(tocHref);
          const doc = await this.sections[index2].createDocument();
          let lastItem;
          let lastLevel = 0;
          let lastIndent = 0;
          const lastLevelOfIndent = /* @__PURE__ */ new Map();
          const lastParentOfLevel = /* @__PURE__ */ new Map();
          this.toc = Array.from(doc.querySelectorAll("a[filepos]")).reduce((arr, a) => {
            var _a3, _b2, _c, _d;
            const indent = getIndent(a);
            const item = {
              label: (_b2 = (_a3 = a.innerText) == null ? void 0 : _a3.trim()) != null ? _b2 : "",
              href: "filepos:".concat(a.getAttribute("filepos"))
            };
            const level = indent > lastIndent ? lastLevel + 1 : indent === lastIndent ? lastLevel : (_c = lastLevelOfIndent.get(indent)) != null ? _c : Math.max(0, lastLevel - 1);
            if (level > lastLevel) {
              if (lastItem) {
                (_d = lastItem.subitems) != null ? _d : lastItem.subitems = [];
                lastItem.subitems.push(item);
                lastParentOfLevel.set(level, lastItem);
              } else arr.push(item);
            } else {
              const parent = lastParentOfLevel.get(level);
              if (parent) parent.subitems.push(item);
              else arr.push(item);
            }
            lastItem = item;
            lastLevel = level;
            lastIndent = indent;
            lastLevelOfIndent.set(indent, level);
            return arr;
          }, []);
        }
      } catch (e) {
        console.warn(e);
      }
      this.metadata = this.mobi.getMetadata();
      this.getCover = this.mobi.getCover.bind(this.mobi);
      return this;
    }
    async getGuide() {
      const doc = await this.createDocument(__privateGet(this, _sections)[0]);
      return Array.from(doc.getElementsByTagName("reference"), (ref) => {
        var _a2;
        return {
          label: ref.getAttribute("title"),
          type: (_a2 = ref.getAttribute("type")) == null ? void 0 : _a2.split(/\s/),
          href: "filepos:".concat(ref.getAttribute("filepos"))
        };
      });
    }
    async loadResource(index2) {
      if (__privateGet(this, _resourceCache).has(index2)) return __privateGet(this, _resourceCache).get(index2);
      const raw = await this.mobi.loadResource(index2);
      const url = URL.createObjectURL(new Blob([raw]));
      __privateGet(this, _resourceCache).set(index2, url);
      return url;
    }
    async loadRecindex(recindex) {
      return this.loadResource(Number(recindex) - 1);
    }
    async replaceResources(doc) {
      for (const img of doc.querySelectorAll("img[recindex]")) {
        const recindex = img.getAttribute("recindex");
        try {
          img.src = await this.loadRecindex(recindex);
        } catch (e) {
          console.warn("Failed to load image ".concat(recindex));
        }
      }
      for (const media of doc.querySelectorAll("[mediarecindex]")) {
        const mediarecindex = media.getAttribute("mediarecindex");
        const recindex = media.getAttribute("recindex");
        try {
          media.src = await this.loadRecindex(mediarecindex);
          if (recindex) media.poster = await this.loadRecindex(recindex);
        } catch (e) {
          console.warn("Failed to load media ".concat(mediarecindex));
        }
      }
      for (const a of doc.querySelectorAll("[filepos]")) {
        const filepos = a.getAttribute("filepos");
        a.href = "filepos:".concat(filepos);
      }
    }
    async loadText(section) {
      if (__privateGet(this, _textCache).has(section)) return __privateGet(this, _textCache).get(section);
      const { raw } = section;
      const fileposList = __privateGet(this, _fileposList).filter(({ number }) => number >= section.start && number < section.end).map((obj) => __spreadProps(__spreadValues({}, obj), { offset: obj.number - section.start }));
      let arr = raw;
      if (fileposList.length) {
        arr = raw.subarray(0, fileposList[0].offset);
        fileposList.forEach(({ filepos, offset }, i) => {
          const next = fileposList[i + 1];
          const a = this.mobi.encode('<a id="filepos'.concat(filepos, '"></a>'));
          arr = concatTypedArray3(arr, a, raw.subarray(offset, next == null ? void 0 : next.offset));
        });
      }
      const str = this.mobi.decode(arr).replaceAll(mbpPagebreakRegex, "");
      __privateGet(this, _textCache).set(section, str);
      return str;
    }
    async createDocument(section) {
      const str = await this.loadText(section);
      return this.parser.parseFromString(str, __privateGet(this, _type));
    }
    async loadSection(section) {
      if (__privateGet(this, _cache2).has(section)) return __privateGet(this, _cache2).get(section);
      const doc = await this.createDocument(section);
      const style2 = doc.createElement("style");
      doc.head.append(style2);
      style2.append(doc.createTextNode("blockquote {\n            margin-block-start: 0;\n            margin-block-end: 0;\n            margin-inline-start: 1em;\n            margin-inline-end: 0;\n        }"));
      await this.replaceResources(doc);
      const result = this.serializer.serializeToString(doc);
      const url = URL.createObjectURL(new Blob([result], { type: __privateGet(this, _type) }));
      __privateGet(this, _cache2).set(section, url);
      return url;
    }
    resolveHref(href) {
      const filepos = href.match(/filepos:(.*)/)[1];
      const number = Number(filepos);
      const index2 = __privateGet(this, _sections).findIndex((section) => section.end > number);
      const anchor = (doc) => doc.getElementById("filepos".concat(filepos));
      return { index: index2, anchor };
    }
    splitTOCHref(href) {
      const filepos = href.match(/filepos:(.*)/)[1];
      const number = Number(filepos);
      const index2 = __privateGet(this, _sections).findIndex((section) => section.end > number);
      return [index2, "filepos".concat(filepos)];
    }
    getTOCFragment(doc, id) {
      return doc.getElementById(id);
    }
    isExternal(uri) {
      return /^(?!blob|filepos)\w+:/i.test(uri);
    }
    destroy() {
      for (const url of __privateGet(this, _resourceCache).values()) URL.revokeObjectURL(url);
      for (const url of __privateGet(this, _cache2).values()) URL.revokeObjectURL(url);
    }
  };
  _resourceCache = new WeakMap();
  _textCache = new WeakMap();
  _cache2 = new WeakMap();
  _sections = new WeakMap();
  _fileposList = new WeakMap();
  _type = new WeakMap();
  var kindleResourceRegex = /kindle:(flow|embed):(\w+)(?:\?mime=(\w+\/[-+.\w]+))?/;
  var kindlePosRegex = /kindle:pos:fid:(\w+):off:(\w+)/;
  var parseResourceURI = (str) => {
    const [resourceType, id, type] = str.match(kindleResourceRegex).slice(1);
    return { resourceType, id: parseInt(id, 32), type };
  };
  var parsePosURI = (str) => {
    const [fid, off] = str.match(kindlePosRegex).slice(1);
    return { fid: parseInt(fid, 32), off: parseInt(off, 32) };
  };
  var makePosURI = (fid = 0, off = 0) => "kindle:pos:fid:".concat(fid.toString(32).toUpperCase().padStart(4, "0"), ":off:").concat(off.toString(32).toUpperCase().padStart(10, "0"));
  var getFragmentSelector = (str) => {
    const match = str.match(/\s(id|name|aid)\s*=\s*['"]([^'"]*)['"]/i);
    if (!match) return;
    const [, attr, value] = match;
    return "[".concat(attr, '="').concat(CSS.escape(value), '"]');
  };
  var replaceSeries2 = async (str, regex, f) => {
    const matches = [];
    str.replace(regex, (...args) => (matches.push(args), null));
    const results = [];
    for (const args of matches) results.push(await f(...args));
    return str.replace(regex, () => results.shift());
  };
  var getPageSpread2 = (properties) => {
    for (const p of properties) {
      if (p === "page-spread-left" || p === "rendition:page-spread-left")
        return "left";
      if (p === "page-spread-right" || p === "rendition:page-spread-right")
        return "right";
      if (p === "rendition:page-spread-center") return "center";
    }
  };
  var _cache3, _fragmentOffsets, _fragmentSelectors, _tables, _sections2, _fullRawLength, _rawHead, _rawTail, _lastLoadedHead, _lastLoadedTail, _type2, _inlineMap, _KF8_instances, setFragmentSelector_fn;
  var KF8 = class {
    constructor(mobi) {
      __privateAdd(this, _KF8_instances);
      __publicField(this, "parser", new DOMParser());
      __publicField(this, "serializer", new XMLSerializer());
      __publicField(this, "transformTarget", new EventTarget());
      __privateAdd(this, _cache3, /* @__PURE__ */ new Map());
      __privateAdd(this, _fragmentOffsets, /* @__PURE__ */ new Map());
      __privateAdd(this, _fragmentSelectors, /* @__PURE__ */ new Map());
      __privateAdd(this, _tables, {});
      __privateAdd(this, _sections2);
      __privateAdd(this, _fullRawLength);
      __privateAdd(this, _rawHead, new Uint8Array());
      __privateAdd(this, _rawTail, new Uint8Array());
      __privateAdd(this, _lastLoadedHead, -1);
      __privateAdd(this, _lastLoadedTail, -1);
      __privateAdd(this, _type2, MIME2.XHTML);
      __privateAdd(this, _inlineMap, /* @__PURE__ */ new Map());
      this.mobi = mobi;
    }
    async init() {
      var _a2, _b2, _c, _d, _e, _f2;
      const loadRecord = this.mobi.loadRecord.bind(this.mobi);
      const { kf8 } = this.mobi.headers;
      try {
        const fdstBuffer = await loadRecord(kf8.fdst);
        const fdst = getStruct(FDST_HEADER, fdstBuffer);
        if (fdst.magic !== "FDST") throw new Error("Missing FDST record");
        const fdstTable = Array.from(
          { length: fdst.numEntries },
          (_, i) => 12 + i * 8
        ).map((offset) => [
          getUint(fdstBuffer.slice(offset, offset + 4)),
          getUint(fdstBuffer.slice(offset + 4, offset + 8))
        ]);
        __privateGet(this, _tables).fdstTable = fdstTable;
        __privateSet(this, _fullRawLength, fdstTable[fdstTable.length - 1][1]);
      } catch (e) {
      }
      const skelTable = (await getIndexData(kf8.skel, loadRecord)).table.map(({ name, tagMap }, index2) => ({
        index: index2,
        name,
        numFrag: tagMap[1][0],
        offset: tagMap[6][0],
        length: tagMap[6][1]
      }));
      const fragData = await getIndexData(kf8.frag, loadRecord);
      const fragTable = fragData.table.map(({ name, tagMap }) => ({
        insertOffset: parseInt(name),
        selector: fragData.cncx[tagMap[2][0]],
        index: tagMap[4][0],
        offset: tagMap[6][0],
        length: tagMap[6][1]
      }));
      __privateGet(this, _tables).skelTable = skelTable;
      __privateGet(this, _tables).fragTable = fragTable;
      __privateSet(this, _sections2, skelTable.reduce((arr, skel) => {
        var _a3, _b3;
        const last = arr[arr.length - 1];
        const fragStart = (_a3 = last == null ? void 0 : last.fragEnd) != null ? _a3 : 0, fragEnd = fragStart + skel.numFrag;
        const frags = fragTable.slice(fragStart, fragEnd);
        const length = skel.length + frags.map((f) => f.length).reduce((a, b) => a + b, 0);
        const totalLength = ((_b3 = last == null ? void 0 : last.totalLength) != null ? _b3 : 0) + length;
        return arr.concat({ skel, frags, fragEnd, length, totalLength });
      }, []));
      const resources = await this.getResourcesByMagic(["RESC", "PAGE"]);
      const pageSpreads = /* @__PURE__ */ new Map();
      if (resources.RESC) {
        const buf = await this.mobi.loadRecord(resources.RESC);
        const str = this.mobi.decode(buf.slice(16)).replace(/\0/g, "");
        const index2 = str.search(/\?>/);
        const xmlStr = "<package>".concat(str.slice(index2), "</package>");
        const opf = this.parser.parseFromString(xmlStr, MIME2.XML);
        for (const $itemref of opf.querySelectorAll("spine > itemref")) {
          const i = parseInt($itemref.getAttribute("skelid"));
          pageSpreads.set(i, getPageSpread2(
            (_b2 = (_a2 = $itemref.getAttribute("properties")) == null ? void 0 : _a2.split(" ")) != null ? _b2 : []
          ));
        }
      }
      this.sections = __privateGet(this, _sections2).map((section, index2) => section.frags.length ? {
        id: index2,
        load: () => this.loadSection(section),
        createDocument: () => this.createDocument(section),
        size: section.length,
        pageSpread: pageSpreads.get(index2)
      } : { linear: "no" });
      try {
        const ncx = await this.mobi.getNCX();
        const map = ({ label, pos, children }) => {
          const [fid, off] = pos;
          const href = makePosURI(fid, off);
          const arr = __privateGet(this, _fragmentOffsets).get(fid);
          if (arr) arr.push(off);
          else __privateGet(this, _fragmentOffsets).set(fid, [off]);
          return { label: unescapeHTML(label), href, subitems: children == null ? void 0 : children.map(map) };
        };
        this.toc = ncx == null ? void 0 : ncx.map(map);
        this.landmarks = await this.getGuide();
      } catch (e) {
        console.warn(e);
      }
      const { exth } = this.mobi.headers;
      this.dir = exth.pageProgressionDirection;
      this.rendition = {
        layout: exth.fixedLayout === "true" ? "pre-paginated" : "reflowable",
        viewport: Object.fromEntries((_f2 = (_e = (_d = (_c = exth.originalResolution) == null ? void 0 : _c.split("x")) == null ? void 0 : _d.slice(0, 2)) == null ? void 0 : _e.map((x, i) => [i ? "height" : "width", x])) != null ? _f2 : [])
      };
      this.metadata = this.mobi.getMetadata();
      this.getCover = this.mobi.getCover.bind(this.mobi);
      return this;
    }
    // is this really the only way of getting to RESC, PAGE, etc.?
    async getResourcesByMagic(keys) {
      const results = {};
      const start = this.mobi.headers.kf8.resourceStart;
      const end = this.mobi.pdb.numRecords;
      for (let i = start; i < end; i++) {
        try {
          const magic = await this.mobi.loadMagic(i);
          const match = keys.find((key) => key === magic);
          if (match) results[match] = i;
        } catch (e) {
        }
      }
      return results;
    }
    async getGuide() {
      const index2 = this.mobi.headers.kf8.guide;
      if (index2 < 4294967295) {
        const loadRecord = this.mobi.loadRecord.bind(this.mobi);
        const { table, cncx } = await getIndexData(index2, loadRecord);
        return table.map(({ name, tagMap }) => {
          var _a2, _b2, _c, _d;
          return {
            label: (_a2 = cncx[tagMap[1][0]]) != null ? _a2 : "",
            type: name == null ? void 0 : name.split(/\s/),
            href: makePosURI((_d = (_b2 = tagMap[6]) == null ? void 0 : _b2[0]) != null ? _d : (_c = tagMap[3]) == null ? void 0 : _c[0])
          };
        });
      }
    }
    async loadResourceBlob(str) {
      var _a2;
      const { resourceType, id, type } = parseResourceURI(str);
      const raw = resourceType === "flow" ? await this.loadFlow(id) : await this.mobi.loadResource(id - 1);
      const result = [MIME2.XHTML, MIME2.HTML, MIME2.CSS, MIME2.SVG].includes(type) ? await this.replaceResources(this.mobi.decode(raw)) : raw;
      const detail = { data: result, type };
      const event = new CustomEvent("data", { detail });
      this.transformTarget.dispatchEvent(event);
      const newData = await event.detail.data;
      const newType = await event.detail.type;
      const doc = newType === MIME2.SVG ? this.parser.parseFromString(newData, newType) : null;
      return [
        new Blob([newData], { newType }),
        // SVG wrappers need to be inlined
        // as browsers don't allow external resources when loading SVG as an image
        ((_a2 = doc == null ? void 0 : doc.getElementsByTagNameNS("http://www.w3.org/2000/svg", "image")) == null ? void 0 : _a2.length) ? doc.documentElement : null
      ];
    }
    async loadResource(str) {
      if (__privateGet(this, _cache3).has(str)) return __privateGet(this, _cache3).get(str);
      const [blob, inline] = await this.loadResourceBlob(str);
      const url = inline ? str : URL.createObjectURL(blob);
      if (inline) __privateGet(this, _inlineMap).set(url, inline);
      __privateGet(this, _cache3).set(str, url);
      return url;
    }
    replaceResources(str) {
      const regex = new RegExp(kindleResourceRegex, "g");
      return replaceSeries2(str, regex, this.loadResource.bind(this));
    }
    // NOTE: there doesn't seem to be a way to access text randomly?
    // how to know the decompressed size of the records without decompressing?
    // 4096 is just the maximum size
    async loadRaw(start, end) {
      const distanceHead = end - __privateGet(this, _rawHead).length;
      const distanceEnd = __privateGet(this, _fullRawLength) == null ? Infinity : __privateGet(this, _fullRawLength) - __privateGet(this, _rawTail).length - start;
      if (distanceHead < 0 || distanceHead < distanceEnd) {
        while (__privateGet(this, _rawHead).length < end) {
          const index2 = ++__privateWrapper(this, _lastLoadedHead)._;
          const data = await this.mobi.loadText(index2);
          __privateSet(this, _rawHead, concatTypedArray(__privateGet(this, _rawHead), data));
        }
        return __privateGet(this, _rawHead).slice(start, end);
      }
      while (__privateGet(this, _fullRawLength) - __privateGet(this, _rawTail).length > start) {
        const index2 = this.mobi.headers.palmdoc.numTextRecords - 1 - ++__privateWrapper(this, _lastLoadedTail)._;
        const data = await this.mobi.loadText(index2);
        __privateSet(this, _rawTail, concatTypedArray(data, __privateGet(this, _rawTail)));
      }
      const rawTailStart = __privateGet(this, _fullRawLength) - __privateGet(this, _rawTail).length;
      return __privateGet(this, _rawTail).slice(start - rawTailStart, end - rawTailStart);
    }
    loadFlow(index2) {
      if (index2 < 4294967295)
        return this.loadRaw(...__privateGet(this, _tables).fdstTable[index2]);
    }
    async loadText(section) {
      const { skel, frags, length } = section;
      const raw = await this.loadRaw(skel.offset, skel.offset + length);
      let skeleton = raw.slice(0, skel.length);
      for (const frag of frags) {
        const insertOffset = frag.insertOffset - skel.offset;
        const offset = skel.length + frag.offset;
        const fragRaw = raw.slice(offset, offset + frag.length);
        skeleton = concatTypedArray3(
          skeleton.slice(0, insertOffset),
          fragRaw,
          skeleton.slice(insertOffset)
        );
        const offsets = __privateGet(this, _fragmentOffsets).get(frag.index);
        if (offsets) for (const offset2 of offsets) {
          const str = this.mobi.decode(fragRaw.slice(offset2));
          const selector = getFragmentSelector(str);
          __privateMethod(this, _KF8_instances, setFragmentSelector_fn).call(this, frag.index, offset2, selector);
        }
      }
      return this.mobi.decode(skeleton);
    }
    async createDocument(section) {
      const str = await this.loadText(section);
      return this.parser.parseFromString(str, __privateGet(this, _type2));
    }
    async loadSection(section) {
      var _a2;
      if (__privateGet(this, _cache3).has(section)) return __privateGet(this, _cache3).get(section);
      const str = await this.loadText(section);
      const replaced = await this.replaceResources(str);
      let doc = this.parser.parseFromString(replaced, __privateGet(this, _type2));
      if (doc.querySelector("parsererror") || !((_a2 = doc.documentElement) == null ? void 0 : _a2.namespaceURI)) {
        __privateSet(this, _type2, MIME2.HTML);
        doc = this.parser.parseFromString(replaced, __privateGet(this, _type2));
      }
      for (const [url2, node] of __privateGet(this, _inlineMap)) {
        for (const el of doc.querySelectorAll('img[src="'.concat(url2, '"]')))
          el.replaceWith(node);
      }
      const url = URL.createObjectURL(
        new Blob([this.serializer.serializeToString(doc)], { type: __privateGet(this, _type2) })
      );
      __privateGet(this, _cache3).set(section, url);
      return url;
    }
    getIndexByFID(fid) {
      return __privateGet(this, _sections2).findIndex((section) => section.frags.some((frag) => frag.index === fid));
    }
    async resolveHref(href) {
      var _a2;
      const { fid, off } = parsePosURI(href);
      const index2 = this.getIndexByFID(fid);
      if (index2 < 0) return;
      const saved = (_a2 = __privateGet(this, _fragmentSelectors).get(fid)) == null ? void 0 : _a2.get(off);
      if (saved) return { index: index2, anchor: (doc) => doc.querySelector(saved) };
      const { skel, frags } = __privateGet(this, _sections2)[index2];
      const frag = frags.find((frag2) => frag2.index === fid);
      const offset = skel.offset + skel.length + frag.offset;
      const fragRaw = await this.loadRaw(offset, offset + frag.length);
      const str = this.mobi.decode(fragRaw.slice(off));
      const selector = getFragmentSelector(str);
      __privateMethod(this, _KF8_instances, setFragmentSelector_fn).call(this, fid, off, selector);
      const anchor = (doc) => doc.querySelector(selector);
      return { index: index2, anchor };
    }
    splitTOCHref(href) {
      const pos = parsePosURI(href);
      const index2 = this.getIndexByFID(pos.fid);
      return [index2, pos];
    }
    getTOCFragment(doc, { fid, off }) {
      var _a2;
      const selector = (_a2 = __privateGet(this, _fragmentSelectors).get(fid)) == null ? void 0 : _a2.get(off);
      return doc.querySelector(selector);
    }
    isExternal(uri) {
      return /^(?!blob|kindle)\w+:/i.test(uri);
    }
    destroy() {
      for (const url of __privateGet(this, _cache3).values()) URL.revokeObjectURL(url);
    }
  };
  _cache3 = new WeakMap();
  _fragmentOffsets = new WeakMap();
  _fragmentSelectors = new WeakMap();
  _tables = new WeakMap();
  _sections2 = new WeakMap();
  _fullRawLength = new WeakMap();
  _rawHead = new WeakMap();
  _rawTail = new WeakMap();
  _lastLoadedHead = new WeakMap();
  _lastLoadedTail = new WeakMap();
  _type2 = new WeakMap();
  _inlineMap = new WeakMap();
  _KF8_instances = new WeakSet();
  setFragmentSelector_fn = function(id, offset, selector) {
    const map = __privateGet(this, _fragmentSelectors).get(id);
    if (map) map.set(offset, selector);
    else {
      const map2 = /* @__PURE__ */ new Map();
      __privateGet(this, _fragmentSelectors).set(id, map2);
      map2.set(offset, selector);
    }
  };

  // vendor/foliate/fb2.js
  var normalizeWhitespace2 = (str) => str ? str.replace(/[\t\n\f\r ]+/g, " ").replace(/^[\t\n\f\r ]+/, "").replace(/[\t\n\f\r ]+$/, "") : "";
  var getElementText2 = (el) => normalizeWhitespace2(el == null ? void 0 : el.textContent);
  var NS2 = {
    XLINK: "http://www.w3.org/1999/xlink",
    EPUB: "http://www.idpf.org/2007/ops"
  };
  var MIME3 = {
    XML: "application/xml",
    XHTML: "application/xhtml+xml"
  };
  var STYLE = {
    "strong": ["strong", "self"],
    "emphasis": ["em", "self"],
    "style": ["span", "self"],
    "a": "anchor",
    "strikethrough": ["s", "self"],
    "sub": ["sub", "self"],
    "sup": ["sup", "self"],
    "code": ["code", "self"],
    "image": "image"
  };
  var TABLE = {
    "tr": ["tr", {
      "th": ["th", STYLE, ["colspan", "rowspan", "align", "valign"]],
      "td": ["td", STYLE, ["colspan", "rowspan", "align", "valign"]]
    }, ["align"]]
  };
  var POEM = {
    "epigraph": ["blockquote"],
    "subtitle": ["h2", STYLE],
    "text-author": ["p", STYLE],
    "date": ["p", STYLE],
    "stanza": "stanza"
  };
  var SECTION = {
    "title": ["header", {
      "p": ["h1", STYLE],
      "empty-line": ["br"]
    }],
    "epigraph": ["blockquote", "self"],
    "image": "image",
    "annotation": ["aside"],
    "section": ["section", "self"],
    "p": ["p", STYLE],
    "poem": ["blockquote", POEM],
    "subtitle": ["h2", STYLE],
    "cite": ["blockquote", "self"],
    "empty-line": ["br"],
    "table": ["table", TABLE],
    "text-author": ["p", STYLE]
  };
  POEM["epigraph"].push(SECTION);
  var BODY = {
    "image": "image",
    "title": ["section", {
      "p": ["h1", STYLE],
      "empty-line": ["br"]
    }],
    "epigraph": ["section", SECTION],
    "section": ["section", SECTION]
  };
  var FB2Converter = class {
    constructor(fb2) {
      this.fb2 = fb2;
      this.doc = document.implementation.createDocument(NS2.XHTML, "html");
      this.bins = new Map(Array.from(
        this.fb2.getElementsByTagName("binary"),
        (el) => [el.id, el]
      ));
    }
    getImageSrc(el) {
      const href = el.getAttributeNS(NS2.XLINK, "href");
      if (!href) return "data:,";
      const [, id] = href.split("#");
      if (!id) return href;
      const bin = this.bins.get(id);
      return bin ? "data:".concat(bin.getAttribute("content-type"), ";base64,").concat(bin.textContent) : href;
    }
    image(node) {
      const el = this.doc.createElement("img");
      el.alt = node.getAttribute("alt");
      el.title = node.getAttribute("title");
      el.setAttribute("src", this.getImageSrc(node));
      return el;
    }
    anchor(node) {
      const el = this.convert(node, { "a": ["a", STYLE] });
      el.setAttribute("href", node.getAttributeNS(NS2.XLINK, "href"));
      if (node.getAttribute("type") === "note")
        el.setAttributeNS(NS2.EPUB, "epub:type", "noteref");
      return el;
    }
    stanza(node) {
      const el = this.convert(node, {
        "stanza": ["p", {
          "title": ["header", {
            "p": ["strong", STYLE],
            "empty-line": ["br"]
          }],
          "subtitle": ["p", STYLE]
        }]
      });
      for (const child of node.children) if (child.nodeName === "v") {
        el.append(this.doc.createTextNode(child.textContent));
        el.append(this.doc.createElement("br"));
      }
      return el;
    }
    convert(node, def) {
      if (node.nodeType === 3) return this.doc.createTextNode(node.textContent);
      if (node.nodeType === 4) return this.doc.createCDATASection(node.textContent);
      if (node.nodeType === 8) return this.doc.createComment(node.textContent);
      const d = def == null ? void 0 : def[node.nodeName];
      if (!d) return null;
      if (typeof d === "string") return this[d](node);
      const [name, opts, attrs] = d;
      const el = this.doc.createElement(name);
      if (node.id) el.id = node.id;
      el.classList.add(node.nodeName);
      if (Array.isArray(attrs)) for (const attr of attrs) {
        const value = node.getAttribute(attr);
        if (value) el.setAttribute(attr, value);
      }
      const childDef = opts === "self" ? def : opts;
      let child = node.firstChild;
      while (child) {
        const childEl = this.convert(child, childDef);
        if (childEl) el.append(childEl);
        child = child.nextSibling;
      }
      return el;
    }
  };
  var parseXML = async (blob) => {
    var _a2;
    const buffer = await blob.arrayBuffer();
    const str = new TextDecoder("utf-8").decode(buffer);
    const parser2 = new DOMParser();
    const doc = parser2.parseFromString(str, MIME3.XML);
    const encoding = doc.xmlEncoding || ((_a2 = str.match(/^<\?xml\s+version\s*=\s*["']1.\d+"\s+encoding\s*=\s*["']([A-Za-z0-9._-]*)["']/)) == null ? void 0 : _a2[1]);
    if (encoding && encoding.toLowerCase() !== "utf-8") {
      const str2 = new TextDecoder(encoding).decode(buffer);
      return parser2.parseFromString(str2, MIME3.XML);
    }
    return doc;
  };
  var style = URL.createObjectURL(new Blob(['\n@namespace epub "http://www.idpf.org/2007/ops";\nbody > img, section > img {\n    display: block;\n    margin: auto;\n}\n.title h1 {\n    text-align: center;\n}\nbody > section > .title, body.notesBodyType > .title {\n    margin: 3em 0;\n}\nbody.notesBodyType > section .title h1 {\n    text-align: start;\n}\nbody.notesBodyType > section .title {\n    margin: 1em 0;\n}\np {\n    text-indent: 1em;\n    margin: 0;\n}\n:not(p) + p, p:first-child {\n    text-indent: 0;\n}\n.poem p {\n    text-indent: 0;\n    margin: 1em 0;\n}\n.text-author, .date {\n    text-align: end;\n}\n.text-author:before {\n    content: "\u2014";\n}\ntable {\n    border-collapse: collapse;\n}\ntd, th {\n    padding: .25em;\n}\na[epub|type~="noteref"] {\n    font-size: .75em;\n    vertical-align: super;\n}\nbody:not(.notesBodyType) > .title, body:not(.notesBodyType) > .epigraph {\n    margin: 3em 0;\n}\n'], { type: "text/css" }));
  var template = (html) => '<?xml version="1.0" encoding="utf-8"?>\n<html xmlns="http://www.w3.org/1999/xhtml">\n    <head><link href="'.concat(style, '" rel="stylesheet" type="text/css"/></head>\n    <body>').concat(html, "</body>\n</html>");
  var dataID = "data-foliate-id";
  var makeFB2 = async (blob) => {
    const book = {};
    const doc = await parseXML(blob);
    const converter = new FB2Converter(doc);
    const $ = (x) => doc.querySelector(x);
    const $$ = (x) => [...doc.querySelectorAll(x)];
    const getPerson = (el) => {
      const nick = getElementText2(el.querySelector("nickname"));
      if (nick) return nick;
      const first = getElementText2(el.querySelector("first-name"));
      const middle = getElementText2(el.querySelector("middle-name"));
      const last = getElementText2(el.querySelector("last-name"));
      const name = [first, middle, last].filter((x) => x).join(" ");
      const sortAs = last ? [last, [first, middle].filter((x) => x).join(" ")].join(", ") : null;
      return { name, sortAs };
    };
    const getDate = (el) => {
      var _a2;
      return (_a2 = el == null ? void 0 : el.getAttribute("value")) != null ? _a2 : getElementText2(el);
    };
    const annotation = $("title-info annotation");
    book.metadata = {
      title: getElementText2($("title-info book-title")),
      identifier: getElementText2($("document-info id")),
      language: getElementText2($("title-info lang")),
      author: $$("title-info author").map(getPerson),
      translator: $$("title-info translator").map(getPerson),
      contributor: $$("document-info author").map(getPerson).concat($$("document-info program-used").map(getElementText2)).map((x) => Object.assign(
        typeof x === "string" ? { name: x } : x,
        { role: "bkp" }
      )),
      publisher: getElementText2($("publish-info publisher")),
      published: getDate($("title-info date")),
      modified: getDate($("document-info date")),
      description: annotation ? converter.convert(
        annotation,
        { annotation: ["div", SECTION] }
      ).innerHTML : null,
      subject: $$("title-info genre").map(getElementText2)
    };
    if ($("coverpage image")) {
      const src = converter.getImageSrc($("coverpage image"));
      book.getCover = () => fetch(src).then((res) => res.blob());
    } else book.getCover = () => null;
    const bodyData = Array.from(doc.querySelectorAll("body"), (body) => {
      const converted = converter.convert(body, { body: ["body", BODY] });
      return [Array.from(converted.children, (el) => {
        const ids = [el, ...el.querySelectorAll("[id]")].map((el2) => el2.id);
        return { el, ids };
      }), converted];
    });
    const urls = [];
    const sectionData = bodyData[0][0].map(({ el, ids }) => {
      const titles = Array.from(
        el.querySelectorAll(":scope > section > .title"),
        (el2, index2) => {
          el2.setAttribute(dataID, index2);
          return { title: getElementText2(el2), index: index2 };
        }
      );
      return { ids, titles, el };
    }).concat(bodyData.slice(1).map(([sections, body]) => {
      const ids = sections.map((s) => s.ids).flat();
      body.classList.add("notesBodyType");
      return { ids, el: body, linear: "no" };
    })).map(({ ids, titles, el, linear }) => {
      var _a2, _b2;
      const str = template(el.outerHTML);
      const blob2 = new Blob([str], { type: MIME3.XHTML });
      const url = URL.createObjectURL(blob2);
      urls.push(url);
      const title = normalizeWhitespace2(
        (_b2 = (_a2 = el.querySelector(".title, .subtitle, p")) == null ? void 0 : _a2.textContent) != null ? _b2 : el.classList.contains("title") ? el.textContent : ""
      );
      return {
        ids,
        title,
        titles,
        load: () => url,
        createDocument: () => new DOMParser().parseFromString(str, MIME3.XHTML),
        // doo't count image data as it'd skew the size too much
        size: blob2.size - Array.from(
          el.querySelectorAll("[src]"),
          (el2) => {
            var _a3, _b3;
            return (_b3 = (_a3 = el2.getAttribute("src")) == null ? void 0 : _a3.length) != null ? _b3 : 0;
          }
        ).reduce((a, b) => a + b, 0),
        linear
      };
    });
    const idMap = /* @__PURE__ */ new Map();
    book.sections = sectionData.map((section, index2) => {
      const { ids, load, createDocument, size, linear } = section;
      for (const id of ids) if (id) idMap.set(id, index2);
      return { id: index2, load, createDocument, size, linear };
    });
    book.toc = sectionData.map(({ title, titles }, index2) => {
      const id = index2.toString();
      return {
        label: title,
        href: id,
        subitems: (titles == null ? void 0 : titles.length) ? titles.map(({ title: title2, index: index3 }) => ({
          label: title2,
          href: "".concat(id, "#").concat(index3)
        })) : null
      };
    }).filter((item) => item);
    book.resolveHref = (href) => {
      const [a, b] = href.split("#");
      return a ? { index: Number(a), anchor: (doc2) => doc2.querySelector("[".concat(dataID, '="').concat(b, '"]')) } : { index: idMap.get(b), anchor: (doc2) => doc2.getElementById(b) };
    };
    book.splitTOCHref = (href) => {
      var _a2, _b2;
      return (_b2 = (_a2 = href == null ? void 0 : href.split("#")) == null ? void 0 : _a2.map((x) => Number(x))) != null ? _b2 : [];
    };
    book.getTOCFragment = (doc2, id) => doc2.querySelector("[".concat(dataID, '="').concat(id, '"]'));
    book.isExternal = (uri) => /^\w+:/i.test(uri);
    book.destroy = () => {
      for (const url of urls) URL.revokeObjectURL(url);
    };
    return book;
  };

  // src/progress.mjs
  function logicalFraction(index2, offset, totalInSection, weights) {
    const total = weights.reduce((sum, value) => sum + value, 0);
    if (!total || index2 < 0 || index2 >= weights.length) return 0;
    const before = weights.slice(0, index2).reduce((sum, value) => sum + value, 0);
    const within = totalInSection > 0 ? Math.min(1, Math.max(0, offset / totalInSection)) : 0;
    return Math.min(1, Math.max(0, (before + weights[index2] * within) / total));
  }

  // vendor/foliate/progress.js
  var assignIDs = (toc) => {
    let id = 0;
    const assignID = (item) => {
      item.id = id++;
      if (item.subitems) for (const subitem of item.subitems) assignID(subitem);
    };
    for (const item of toc) assignID(item);
    return toc;
  };
  var flatten = (items) => items.map((item) => {
    var _a2;
    return ((_a2 = item.subitems) == null ? void 0 : _a2.length) ? [item, flatten(item.subitems)].flat() : item;
  }).flat();
  var TOCProgress = class {
    async init({ toc, ids, splitHref, getFragment }) {
      var _a2;
      assignIDs(toc);
      const items = flatten(toc);
      const grouped = /* @__PURE__ */ new Map();
      for (const [i, item] of items.entries()) {
        const [id, fragment] = (_a2 = await splitHref(item == null ? void 0 : item.href)) != null ? _a2 : [];
        const value = { fragment, item };
        if (grouped.has(id)) grouped.get(id).items.push(value);
        else grouped.set(id, { prev: items[i - 1], items: [value] });
      }
      const map = /* @__PURE__ */ new Map();
      for (const [i, id] of ids.entries()) {
        if (grouped.has(id)) map.set(id, grouped.get(id));
        else map.set(id, map.get(ids[i - 1]));
      }
      this.ids = ids;
      this.map = map;
      this.getFragment = getFragment;
    }
    getProgress(index2, range) {
      var _a2, _b2;
      if (!this.ids) return;
      const id = this.ids[index2];
      const obj = this.map.get(id);
      if (!obj) return null;
      const { prev, items } = obj;
      if (!items) return prev;
      if (!range || items.length === 1 && !items[0].fragment) return items[0].item;
      const doc = range.startContainer.getRootNode();
      for (const [i, { fragment }] of items.entries()) {
        const el = this.getFragment(doc, fragment);
        if (!el) continue;
        if (range.comparePoint(el, 0) > 0)
          return (_b2 = (_a2 = items[i - 1]) == null ? void 0 : _a2.item) != null ? _b2 : prev;
      }
      return items[items.length - 1].item;
    }
  };
  var _SectionProgress_instances, getSectionFractions_fn;
  var SectionProgress = class {
    constructor(sections, sizePerLoc, sizePerTimeUnit) {
      __privateAdd(this, _SectionProgress_instances);
      this.sizes = sections.map((s) => s.linear != "no" && s.size > 0 ? s.size : 0);
      this.sizePerLoc = sizePerLoc;
      this.sizePerTimeUnit = sizePerTimeUnit;
      this.sizeTotal = this.sizes.reduce((a, b) => a + b, 0);
      this.sectionFractions = __privateMethod(this, _SectionProgress_instances, getSectionFractions_fn).call(this);
    }
    // get progress given index of and fractions within a section
    getProgress(index2, fractionInSection, pageFraction = 0) {
      var _a2;
      const { sizes, sizePerLoc, sizePerTimeUnit, sizeTotal } = this;
      const sizeInSection = (_a2 = sizes[index2]) != null ? _a2 : 0;
      const sizeBefore = sizes.slice(0, index2).reduce((a, b) => a + b, 0);
      const size = sizeBefore + fractionInSection * sizeInSection;
      const nextSize = size + pageFraction * sizeInSection;
      const remainingTotal = sizeTotal - size;
      const remainingSection = (1 - fractionInSection) * sizeInSection;
      return {
        fraction: nextSize / sizeTotal,
        section: {
          current: index2,
          total: sizes.length
        },
        location: {
          current: Math.floor(size / sizePerLoc),
          next: Math.floor(nextSize / sizePerLoc),
          total: Math.ceil(sizeTotal / sizePerLoc)
        },
        time: {
          section: remainingSection / sizePerTimeUnit,
          total: remainingTotal / sizePerTimeUnit
        }
      };
    }
    // the inverse of `getProgress`
    // get index of and fraction in section based on total fraction
    getSection(fraction) {
      if (fraction <= 0) return [0, 0];
      if (fraction >= 1) return [this.sizes.length - 1, 1];
      fraction = fraction + Number.EPSILON;
      const { sizeTotal } = this;
      let index2 = this.sectionFractions.findIndex((x) => x > fraction) - 1;
      if (index2 < 0) return [0, 0];
      while (!this.sizes[index2]) index2++;
      const fractionInSection = (fraction - this.sectionFractions[index2]) / (this.sizes[index2] / sizeTotal);
      return [index2, fractionInSection];
    }
  };
  _SectionProgress_instances = new WeakSet();
  getSectionFractions_fn = function() {
    const { sizeTotal } = this;
    const results = [0];
    let sum = 0;
    for (const size of this.sizes) results.push((sum += size) / sizeTotal);
    return results;
  };

  // vendor/foliate/overlayer.js
  var createSVGElement = (tag) => document.createElementNS("http://www.w3.org/2000/svg", tag);
  var _svg, _map;
  var Overlayer = class {
    constructor() {
      __privateAdd(this, _svg, createSVGElement("svg"));
      __privateAdd(this, _map, /* @__PURE__ */ new Map());
      Object.assign(__privateGet(this, _svg).style, {
        position: "absolute",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        pointerEvents: "none"
      });
    }
    get element() {
      return __privateGet(this, _svg);
    }
    add(key, range, draw, options) {
      if (__privateGet(this, _map).has(key)) this.remove(key);
      if (typeof range === "function") range = range(__privateGet(this, _svg).getRootNode());
      const rects = range.getClientRects();
      const element = draw(rects, options);
      __privateGet(this, _svg).append(element);
      __privateGet(this, _map).set(key, { range, draw, options, element, rects });
    }
    remove(key) {
      if (!__privateGet(this, _map).has(key)) return;
      __privateGet(this, _svg).removeChild(__privateGet(this, _map).get(key).element);
      __privateGet(this, _map).delete(key);
    }
    redraw() {
      for (const obj of __privateGet(this, _map).values()) {
        const { range, draw, options, element } = obj;
        __privateGet(this, _svg).removeChild(element);
        const rects = range.getClientRects();
        const el = draw(rects, options);
        __privateGet(this, _svg).append(el);
        obj.element = el;
        obj.rects = rects;
      }
    }
    hitTest({ x, y }) {
      const arr = Array.from(__privateGet(this, _map).entries());
      for (let i = arr.length - 1; i >= 0; i--) {
        const [key, obj] = arr[i];
        for (const { left, top, right, bottom } of obj.rects)
          if (top <= y && left <= x && bottom > y && right > x)
            return [key, obj.range];
      }
      return [];
    }
    static underline(rects, options = {}) {
      const { color = "red", width: strokeWidth = 2, writingMode } = options;
      const g = createSVGElement("g");
      g.setAttribute("fill", color);
      if (writingMode === "vertical-rl" || writingMode === "vertical-lr")
        for (const { right, top, height } of rects) {
          const el = createSVGElement("rect");
          el.setAttribute("x", right - strokeWidth);
          el.setAttribute("y", top);
          el.setAttribute("height", height);
          el.setAttribute("width", strokeWidth);
          g.append(el);
        }
      else for (const { left, bottom, width } of rects) {
        const el = createSVGElement("rect");
        el.setAttribute("x", left);
        el.setAttribute("y", bottom - strokeWidth);
        el.setAttribute("height", strokeWidth);
        el.setAttribute("width", width);
        g.append(el);
      }
      return g;
    }
    static strikethrough(rects, options = {}) {
      const { color = "red", width: strokeWidth = 2, writingMode } = options;
      const g = createSVGElement("g");
      g.setAttribute("fill", color);
      if (writingMode === "vertical-rl" || writingMode === "vertical-lr")
        for (const { right, left, top, height } of rects) {
          const el = createSVGElement("rect");
          el.setAttribute("x", (right + left) / 2);
          el.setAttribute("y", top);
          el.setAttribute("height", height);
          el.setAttribute("width", strokeWidth);
          g.append(el);
        }
      else for (const { left, top, bottom, width } of rects) {
        const el = createSVGElement("rect");
        el.setAttribute("x", left);
        el.setAttribute("y", (top + bottom) / 2);
        el.setAttribute("height", strokeWidth);
        el.setAttribute("width", width);
        g.append(el);
      }
      return g;
    }
    static squiggly(rects, options = {}) {
      const { color = "red", width: strokeWidth = 2, writingMode } = options;
      const g = createSVGElement("g");
      g.setAttribute("fill", "none");
      g.setAttribute("stroke", color);
      g.setAttribute("stroke-width", strokeWidth);
      const block = strokeWidth * 1.5;
      if (writingMode === "vertical-rl" || writingMode === "vertical-lr")
        for (const { right, top, height } of rects) {
          const el = createSVGElement("path");
          const n = Math.round(height / block / 1.5);
          const inline = height / n;
          const ls = Array.from(
            { length: n },
            (_, i) => "l".concat(i % 2 ? -block : block, " ").concat(inline)
          ).join("");
          el.setAttribute("d", "M".concat(right, " ").concat(top).concat(ls));
          g.append(el);
        }
      else for (const { left, bottom, width } of rects) {
        const el = createSVGElement("path");
        const n = Math.round(width / block / 1.5);
        const inline = width / n;
        const ls = Array.from(
          { length: n },
          (_, i) => "l".concat(inline, " ").concat(i % 2 ? block : -block)
        ).join("");
        el.setAttribute("d", "M".concat(left, " ").concat(bottom).concat(ls));
        g.append(el);
      }
      return g;
    }
    static highlight(rects, options = {}) {
      const { color = "red" } = options;
      const g = createSVGElement("g");
      g.setAttribute("fill", color);
      g.style.opacity = "var(--overlayer-highlight-opacity, .3)";
      g.style.mixBlendMode = "var(--overlayer-highlight-blend-mode, normal)";
      for (const { left, top, height, width } of rects) {
        const el = createSVGElement("rect");
        el.setAttribute("x", left);
        el.setAttribute("y", top);
        el.setAttribute("height", height);
        el.setAttribute("width", width);
        g.append(el);
      }
      return g;
    }
    static outline(rects, options = {}) {
      const { color = "red", width: strokeWidth = 3, radius = 3 } = options;
      const g = createSVGElement("g");
      g.setAttribute("fill", "none");
      g.setAttribute("stroke", color);
      g.setAttribute("stroke-width", strokeWidth);
      for (const { left, top, height, width } of rects) {
        const el = createSVGElement("rect");
        el.setAttribute("x", left);
        el.setAttribute("y", top);
        el.setAttribute("height", height);
        el.setAttribute("width", width);
        el.setAttribute("rx", radius);
        g.append(el);
      }
      return g;
    }
    // make an exact copy of an image in the overlay
    // one can then apply filters to the entire element, without affecting them;
    // it's a bit silly and probably better to just invert images twice
    // (though the color will be off in that case if you do heu-rotate)
    static copyImage([rect], options = {}) {
      const { src } = options;
      const image = createSVGElement("image");
      const { left, top, height, width } = rect;
      image.setAttribute("href", src);
      image.setAttribute("x", left);
      image.setAttribute("y", top);
      image.setAttribute("height", height);
      image.setAttribute("width", width);
      return image;
    }
  };
  _svg = new WeakMap();
  _map = new WeakMap();

  // vendor/foliate/text-walker.js
  var walkRange = (range, walker) => {
    const nodes = [];
    for (let node = walker.currentNode; node; node = walker.nextNode()) {
      const compare = range.comparePoint(node, 0);
      if (compare === 0) nodes.push(node);
      else if (compare > 0) break;
    }
    return nodes;
  };
  var walkDocument = (_, walker) => {
    const nodes = [];
    for (let node = walker.nextNode(); node; node = walker.nextNode())
      nodes.push(node);
    return nodes;
  };
  var filter = NodeFilter.SHOW_ELEMENT | NodeFilter.SHOW_TEXT | NodeFilter.SHOW_CDATA_SECTION;
  var acceptNode = (node) => {
    if (node.nodeType === 1) {
      const name = node.tagName.toLowerCase();
      if (name === "script" || name === "style") return NodeFilter.FILTER_REJECT;
      return NodeFilter.FILTER_SKIP;
    }
    return NodeFilter.FILTER_ACCEPT;
  };
  var textWalker = function* (x, func, filterFunc) {
    var _a2, _b2;
    const root = (_b2 = (_a2 = x.commonAncestorContainer) != null ? _a2 : x.body) != null ? _b2 : x;
    const walker = document.createTreeWalker(root, filter, { acceptNode: filterFunc || acceptNode });
    const walk = x.commonAncestorContainer ? walkRange : walkDocument;
    const nodes = walk(x, walker);
    const strs = nodes.map((node) => {
      var _a3;
      return (_a3 = node.nodeValue) != null ? _a3 : "";
    });
    const makeRange2 = (startIndex, startOffset, endIndex, endOffset) => {
      const range = document.createRange();
      range.setStart(nodes[startIndex], startOffset);
      range.setEnd(nodes[endIndex], endOffset);
      return range;
    };
    for (const match of func(strs, makeRange2)) yield match;
  };

  // vendor/foliate/view.js
  var SEARCH_PREFIX = "foliate-search:";
  var makeBook = async () => {
    throw new Error("Use the native offline publication loader");
  };
  var _timeout, _el, _check, _state2;
  var _CursorAutohider = class _CursorAutohider {
    constructor(el, check, state2 = {}) {
      __privateAdd(this, _timeout);
      __privateAdd(this, _el);
      __privateAdd(this, _check);
      __privateAdd(this, _state2);
      __privateSet(this, _el, el);
      __privateSet(this, _check, check);
      __privateSet(this, _state2, state2);
      if (__privateGet(this, _state2).hidden) this.hide();
      __privateGet(this, _el).addEventListener("mousemove", ({ screenX, screenY }) => {
        if (screenX === __privateGet(this, _state2).x && screenY === __privateGet(this, _state2).y) return;
        __privateGet(this, _state2).x = screenX, __privateGet(this, _state2).y = screenY;
        this.show();
        if (__privateGet(this, _timeout)) clearTimeout(__privateGet(this, _timeout));
        if (check()) __privateSet(this, _timeout, setTimeout(this.hide.bind(this), 1e3));
      }, false);
    }
    cloneFor(el) {
      return new _CursorAutohider(el, __privateGet(this, _check), __privateGet(this, _state2));
    }
    hide() {
      __privateGet(this, _el).style.cursor = "none";
      __privateGet(this, _state2).hidden = true;
    }
    show() {
      __privateGet(this, _el).style.removeProperty("cursor");
      __privateGet(this, _state2).hidden = false;
    }
  };
  _timeout = new WeakMap();
  _el = new WeakMap();
  _check = new WeakMap();
  _state2 = new WeakMap();
  var CursorAutohider = _CursorAutohider;
  var _arr2, _index4;
  var History = class extends EventTarget {
    constructor() {
      super(...arguments);
      __privateAdd(this, _arr2, []);
      __privateAdd(this, _index4, -1);
    }
    pushState(x) {
      const last = __privateGet(this, _arr2)[__privateGet(this, _index4)];
      if (last === x || (last == null ? void 0 : last.fraction) && last.fraction === x.fraction) return;
      __privateGet(this, _arr2)[++__privateWrapper(this, _index4)._] = x;
      __privateGet(this, _arr2).length = __privateGet(this, _index4) + 1;
      this.dispatchEvent(new Event("index-change"));
    }
    replaceState(x) {
      const index2 = __privateGet(this, _index4);
      __privateGet(this, _arr2)[index2] = x;
    }
    back() {
      const index2 = __privateGet(this, _index4);
      if (index2 <= 0) return;
      const detail = { state: __privateGet(this, _arr2)[index2 - 1] };
      __privateSet(this, _index4, index2 - 1);
      this.dispatchEvent(new CustomEvent("popstate", { detail }));
      this.dispatchEvent(new Event("index-change"));
    }
    forward() {
      const index2 = __privateGet(this, _index4);
      if (index2 >= __privateGet(this, _arr2).length - 1) return;
      const detail = { state: __privateGet(this, _arr2)[index2 + 1] };
      __privateSet(this, _index4, index2 + 1);
      this.dispatchEvent(new CustomEvent("popstate", { detail }));
      this.dispatchEvent(new Event("index-change"));
    }
    get canGoBack() {
      return __privateGet(this, _index4) > 0;
    }
    get canGoForward() {
      return __privateGet(this, _index4) < __privateGet(this, _arr2).length - 1;
    }
    clear() {
      __privateSet(this, _arr2, []);
      __privateSet(this, _index4, -1);
    }
  };
  _arr2 = new WeakMap();
  _index4 = new WeakMap();
  var languageInfo = (lang) => {
    var _a2, _b2, _c;
    if (!lang) return {};
    try {
      const canonical = Intl.getCanonicalLocales(lang)[0];
      const locale = new Intl.Locale(canonical);
      const isCJK = ["zh", "ja", "kr"].includes(locale.language);
      const direction = (_c = (_b2 = (_a2 = locale.getTextInfo) == null ? void 0 : _a2.call(locale)) != null ? _b2 : locale.textInfo) == null ? void 0 : _c.direction;
      return { canonical, locale, isCJK, direction };
    } catch (e) {
      console.warn(e);
      return {};
    }
  };
  var _root3, _sectionProgress, _tocProgress, _pageProgress, _searchResults, _searchDraw, _searchDrawOptions, _cursorAutohider, _View_instances, emit_fn, onRelocate_fn, onLoad_fn, handleLinks_fn, getOverlayer_fn, createOverlayer_fn, searchSection_fn, searchBook_fn;
  var View2 = class extends HTMLElement {
    constructor() {
      super();
      __privateAdd(this, _View_instances);
      __privateAdd(this, _root3, this.attachShadow({ mode: "closed" }));
      __privateAdd(this, _sectionProgress);
      __privateAdd(this, _tocProgress);
      __privateAdd(this, _pageProgress);
      __privateAdd(this, _searchResults, /* @__PURE__ */ new Map());
      __privateAdd(this, _searchDraw);
      __privateAdd(this, _searchDrawOptions);
      __privateAdd(this, _cursorAutohider, new CursorAutohider(this, () => this.hasAttribute("autohide-cursor")));
      __publicField(this, "isFixedLayout", false);
      __publicField(this, "lastLocation");
      __publicField(this, "history", new History());
      this.history.addEventListener("popstate", ({ detail }) => {
        const resolved = this.resolveNavigation(detail.state);
        this.renderer.goTo(resolved);
      });
    }
    async open(book) {
      var _a2, _b2, _c, _d;
      if (typeof book === "string" || typeof book.arrayBuffer === "function" || book.isDirectory) book = await makeBook(book);
      this.book = book;
      this.language = languageInfo((_a2 = book.metadata) == null ? void 0 : _a2.language);
      if (book.splitTOCHref && book.getTOCFragment) {
        const ids = book.sections.map((s) => s.id);
        __privateSet(this, _sectionProgress, new SectionProgress(book.sections, 1500, 1600));
        const splitHref = book.splitTOCHref.bind(book);
        const getFragment = book.getTOCFragment.bind(book);
        __privateSet(this, _tocProgress, new TOCProgress());
        await __privateGet(this, _tocProgress).init({
          toc: (_b2 = book.toc) != null ? _b2 : [],
          ids,
          splitHref,
          getFragment
        });
        __privateSet(this, _pageProgress, new TOCProgress());
        await __privateGet(this, _pageProgress).init({
          toc: (_c = book.pageList) != null ? _c : [],
          ids,
          splitHref,
          getFragment
        });
      }
      this.isFixedLayout = ((_d = this.book.rendition) == null ? void 0 : _d.layout) === "pre-paginated";
      if (this.isFixedLayout) {
        await Promise.resolve().then(() => (init_fixed_layout(), fixed_layout_exports));
        this.renderer = document.createElement("foliate-fxl");
      } else {
        await Promise.resolve().then(() => (init_paginator(), paginator_exports));
        this.renderer = document.createElement("foliate-paginator");
      }
      this.renderer.setAttribute("exportparts", "head,foot,filter");
      this.renderer.addEventListener("load", (e) => __privateMethod(this, _View_instances, onLoad_fn).call(this, e.detail));
      this.renderer.addEventListener("relocate", (e) => __privateMethod(this, _View_instances, onRelocate_fn).call(this, e.detail));
      this.renderer.addEventListener("create-overlayer", (e) => e.detail.attach(__privateMethod(this, _View_instances, createOverlayer_fn).call(this, e.detail)));
      this.renderer.open(book);
      __privateGet(this, _root3).append(this.renderer);
      if (book.sections.some((section) => section.mediaOverlay)) {
        const activeClass = book.media.activeClass;
        const playbackActiveClass = book.media.playbackActiveClass;
        this.mediaOverlay = book.getMediaOverlay();
        let lastActive;
        this.mediaOverlay.addEventListener("highlight", (e) => {
          const resolved = this.resolveNavigation(e.detail.text);
          this.renderer.goTo(resolved).then(() => {
            const { doc } = this.renderer.getContents().find((x) => x.index = resolved.index);
            const el = resolved.anchor(doc);
            el.classList.add(activeClass);
            if (playbackActiveClass) el.ownerDocument.documentElement.classList.add(playbackActiveClass);
            lastActive = { deref: () => el };
          });
        });
        this.mediaOverlay.addEventListener("unhighlight", () => {
          const el = lastActive == null ? void 0 : lastActive.deref();
          if (el) {
            el.classList.remove(activeClass);
            if (playbackActiveClass) el.ownerDocument.documentElement.classList.remove(playbackActiveClass);
          }
        });
      }
    }
    close() {
      var _a2, _b2;
      (_a2 = this.renderer) == null ? void 0 : _a2.destroy();
      (_b2 = this.renderer) == null ? void 0 : _b2.remove();
      __privateSet(this, _sectionProgress, null);
      __privateSet(this, _tocProgress, null);
      __privateSet(this, _pageProgress, null);
      __privateSet(this, _searchResults, /* @__PURE__ */ new Map());
      this.lastLocation = null;
      this.history.clear();
      this.tts = null;
      this.mediaOverlay = null;
    }
    goToTextStart() {
      var _a2, _b2, _c;
      return this.goTo((_c = (_b2 = (_a2 = this.book.landmarks) == null ? void 0 : _a2.find((m) => m.type.includes("bodymatter") || m.type.includes("text"))) == null ? void 0 : _b2.href) != null ? _c : this.book.sections.findIndex((s) => s.linear !== "no"));
    }
    async init({ lastLocation: lastLocation2, showTextStart }) {
      const resolved = lastLocation2 ? this.resolveNavigation(lastLocation2) : null;
      if (resolved) {
        await this.renderer.goTo(resolved);
        this.history.pushState(lastLocation2);
      } else if (showTextStart) await this.goToTextStart();
      else {
        this.history.pushState(0);
        await this.next();
      }
    }
    async addAnnotation(annotation, remove) {
      var _a2, _b2;
      const { value } = annotation;
      if (value.startsWith(SEARCH_PREFIX)) {
        const cfi = value.replace(SEARCH_PREFIX, "");
        const { index: index3, anchor: anchor2 } = await this.resolveNavigation(cfi);
        const obj2 = __privateMethod(this, _View_instances, getOverlayer_fn).call(this, index3);
        if (obj2) {
          const { overlayer, doc } = obj2;
          if (remove) {
            overlayer.remove(value);
            return;
          }
          const range = doc ? anchor2(doc) : anchor2;
          overlayer.add(value, range, __privateGet(this, _searchDraw), __privateGet(this, _searchDrawOptions));
        }
        return;
      }
      const { index: index2, anchor } = await this.resolveNavigation(value);
      const obj = __privateMethod(this, _View_instances, getOverlayer_fn).call(this, index2);
      if (obj) {
        const { overlayer, doc } = obj;
        overlayer.remove(value);
        if (!remove) {
          const range = doc ? anchor(doc) : anchor;
          const draw = (func, opts) => overlayer.add(value, range, func, opts);
          __privateMethod(this, _View_instances, emit_fn).call(this, "draw-annotation", { draw, annotation, doc, range });
        }
      }
      const label = (_b2 = (_a2 = __privateGet(this, _tocProgress).getProgress(index2)) == null ? void 0 : _a2.label) != null ? _b2 : "";
      return { index: index2, label };
    }
    deleteAnnotation(annotation) {
      return this.addAnnotation(annotation, true);
    }
    async showAnnotation(annotation) {
      const { value } = annotation;
      const resolved = await this.goTo(value);
      if (resolved) {
        const { index: index2, anchor } = resolved;
        const { doc } = __privateMethod(this, _View_instances, getOverlayer_fn).call(this, index2);
        const range = anchor(doc);
        __privateMethod(this, _View_instances, emit_fn).call(this, "show-annotation", { value, index: index2, range });
      }
    }
    getCFI(index2, range) {
      var _a2;
      const baseCFI = (_a2 = this.book.sections[index2].cfi) != null ? _a2 : fake.fromIndex(index2);
      if (!range) return baseCFI;
      return joinIndir(baseCFI, fromRange(range));
    }
    resolveCFI(cfi) {
      var _a2;
      if (this.book.resolveCFI)
        return this.book.resolveCFI(cfi);
      else {
        const parts = parse(cfi);
        const index2 = fake.toIndex(((_a2 = parts.parent) != null ? _a2 : parts).shift());
        const anchor = (doc) => toRange(doc, parts);
        return { index: index2, anchor };
      }
    }
    resolveNavigation(target) {
      try {
        if (typeof target === "number") return { index: target };
        if (typeof target.fraction === "number") {
          const [index2, anchor] = __privateGet(this, _sectionProgress).getSection(target.fraction);
          return { index: index2, anchor };
        }
        if (isCFI.test(target)) return this.resolveCFI(target);
        return this.book.resolveHref(target);
      } catch (e) {
        console.error(e);
        console.error("Could not resolve target ".concat(target));
      }
    }
    async goTo(target) {
      const resolved = this.resolveNavigation(target);
      try {
        await this.renderer.goTo(resolved);
        this.history.pushState(target);
        return resolved;
      } catch (e) {
        console.error(e);
        console.error("Could not go to ".concat(target));
      }
    }
    async goToFraction(frac) {
      const [index2, anchor] = __privateGet(this, _sectionProgress).getSection(frac);
      await this.renderer.goTo({ index: index2, anchor });
      this.history.pushState({ fraction: frac });
    }
    async select(target) {
      try {
        const obj = await this.resolveNavigation(target);
        await this.renderer.goTo(__spreadProps(__spreadValues({}, obj), { select: true }));
        this.history.pushState(target);
      } catch (e) {
        console.error(e);
        console.error("Could not go to ".concat(target));
      }
    }
    deselect() {
      for (const { doc } of this.renderer.getContents())
        doc.defaultView.getSelection().removeAllRanges();
    }
    getSectionFractions() {
      var _a2, _b2;
      return ((_b2 = (_a2 = __privateGet(this, _sectionProgress)) == null ? void 0 : _a2.sectionFractions) != null ? _b2 : []).map((x) => x + Number.EPSILON);
    }
    getProgressOf(index2, range) {
      var _a2, _b2;
      const tocItem = (_a2 = __privateGet(this, _tocProgress)) == null ? void 0 : _a2.getProgress(index2, range);
      const pageItem = (_b2 = __privateGet(this, _pageProgress)) == null ? void 0 : _b2.getProgress(index2, range);
      return { tocItem, pageItem };
    }
    async getTOCItemOf(target) {
      try {
        const { index: index2, anchor } = await this.resolveNavigation(target);
        const doc = await this.book.sections[index2].createDocument();
        const frag = anchor(doc);
        const isRange = frag instanceof Range;
        const range = isRange ? frag : doc.createRange();
        if (!isRange) range.selectNodeContents(frag);
        return __privateGet(this, _tocProgress).getProgress(index2, range);
      } catch (e) {
        console.error(e);
        console.error("Could not get ".concat(target));
      }
    }
    async prev(distance) {
      await this.renderer.prev(distance);
    }
    async next(distance) {
      await this.renderer.next(distance);
    }
    goLeft() {
      return this.book.dir === "rtl" ? this.next() : this.prev();
    }
    goRight() {
      return this.book.dir === "rtl" ? this.prev() : this.next();
    }
    search(opts) {
      return __asyncGenerator(this, null, function* () {
        var _a2, _b2, _c;
        this.clearSearch();
        __privateSet(this, _searchDraw, (_a2 = opts.draw) != null ? _a2 : Overlayer.outline);
        __privateSet(this, _searchDrawOptions, opts.drawOptions);
        const { searchMatcher: searchMatcher2 } = yield new __await(Promise.resolve().then(() => (init_search(), search_exports)));
        const { query, index: index2 } = opts;
        const matcher = searchMatcher2(
          textWalker,
          __spreadValues({ defaultLocale: this.language }, opts)
        );
        const iter = index2 != null ? __privateMethod(this, _View_instances, searchSection_fn).call(this, matcher, query, index2) : __privateMethod(this, _View_instances, searchBook_fn).call(this, matcher, query);
        const list = [];
        __privateGet(this, _searchResults).set(index2, list);
        try {
          for (var iter2 = __forAwait(iter), more, temp, error; more = !(temp = yield new __await(iter2.next())).done; more = false) {
            const result = temp.value;
            if (result.subitems) {
              const list2 = result.subitems.map(({ cfi }) => ({ value: SEARCH_PREFIX + cfi }));
              __privateGet(this, _searchResults).set(result.index, list2);
              for (const item of list2) this.addAnnotation(item);
              yield {
                label: (_c = (_b2 = __privateGet(this, _tocProgress).getProgress(result.index)) == null ? void 0 : _b2.label) != null ? _c : "",
                subitems: result.subitems
              };
            } else {
              if (result.cfi) {
                const item = { value: SEARCH_PREFIX + result.cfi };
                list.push(item);
                this.addAnnotation(item);
              }
              yield result;
            }
          }
        } catch (temp) {
          error = [temp];
        } finally {
          try {
            more && (temp = iter2.return) && (yield new __await(temp.call(iter2)));
          } finally {
            if (error)
              throw error[0];
          }
        }
        yield "done";
      });
    }
    clearSearch() {
      for (const list of __privateGet(this, _searchResults).values())
        for (const item of list) this.deleteAnnotation(item);
      __privateGet(this, _searchResults).clear();
    }
    async initTTS(granularity = "word", highlight) {
      const doc = this.renderer.getContents()[0].doc;
      if (this.tts && this.tts.doc === doc) return;
      const { TTS: TTS2 } = await Promise.resolve().then(() => (init_tts(), tts_exports));
      this.tts = new TTS2(doc, textWalker, highlight || ((range) => this.renderer.scrollToAnchor(range, true)), granularity);
    }
    startMediaOverlay() {
      const { index: index2 } = this.renderer.getContents()[0];
      return this.mediaOverlay.start(index2);
    }
  };
  _root3 = new WeakMap();
  _sectionProgress = new WeakMap();
  _tocProgress = new WeakMap();
  _pageProgress = new WeakMap();
  _searchResults = new WeakMap();
  _searchDraw = new WeakMap();
  _searchDrawOptions = new WeakMap();
  _cursorAutohider = new WeakMap();
  _View_instances = new WeakSet();
  emit_fn = function(name, detail, cancelable) {
    return this.dispatchEvent(new CustomEvent(name, { detail, cancelable }));
  };
  onRelocate_fn = function({ reason, range, index: index2, fraction, size }) {
    var _a2, _b2, _c, _d;
    const progress = (_b2 = (_a2 = __privateGet(this, _sectionProgress)) == null ? void 0 : _a2.getProgress(index2, fraction, size)) != null ? _b2 : {};
    const tocItem = (_c = __privateGet(this, _tocProgress)) == null ? void 0 : _c.getProgress(index2, range);
    const pageItem = (_d = __privateGet(this, _pageProgress)) == null ? void 0 : _d.getProgress(index2, range);
    const cfi = this.getCFI(index2, range);
    this.lastLocation = __spreadProps(__spreadValues({}, progress), { tocItem, pageItem, cfi, range });
    if (reason === "snap" || reason === "page" || reason === "scroll")
      this.history.replaceState(cfi);
    __privateMethod(this, _View_instances, emit_fn).call(this, "relocate", this.lastLocation);
  };
  onLoad_fn = function({ doc, index: index2 }) {
    var _a2, _b2, _c, _d;
    (_b2 = doc.documentElement).lang || (_b2.lang = (_a2 = this.language.canonical) != null ? _a2 : "");
    if (!this.language.isCJK)
      (_d = doc.documentElement).dir || (_d.dir = (_c = this.language.direction) != null ? _c : "");
    __privateMethod(this, _View_instances, handleLinks_fn).call(this, doc, index2);
    __privateGet(this, _cursorAutohider).cloneFor(doc.documentElement);
    __privateMethod(this, _View_instances, emit_fn).call(this, "load", { doc, index: index2 });
  };
  handleLinks_fn = function(doc, index2) {
    const { book } = this;
    const section = book.sections[index2];
    doc.addEventListener("click", (e) => {
      var _a2, _b2, _c;
      const a = e.target.closest("a[href]");
      if (!a) return;
      e.preventDefault();
      const href_ = a.getAttribute("href");
      const href = (_b2 = (_a2 = section == null ? void 0 : section.resolveHref) == null ? void 0 : _a2.call(section, href_)) != null ? _b2 : href_;
      if ((_c = book == null ? void 0 : book.isExternal) == null ? void 0 : _c.call(book, href))
        Promise.resolve(__privateMethod(this, _View_instances, emit_fn).call(this, "external-link", { a, href_ }, true)).then((x) => x ? globalThis.open(href_, "_blank") : null).catch((e2) => console.error(e2));
      else Promise.resolve(__privateMethod(this, _View_instances, emit_fn).call(this, "link", { a, href }, true)).then((x) => x ? this.goTo(href) : null).catch((e2) => console.error(e2));
    });
  };
  getOverlayer_fn = function(index2) {
    return this.renderer.getContents().find((x) => x.index === index2 && x.overlayer);
  };
  createOverlayer_fn = function({ doc, index: index2 }) {
    const overlayer = new Overlayer();
    doc.addEventListener("click", (e) => {
      const [value, range] = overlayer.hitTest(e);
      if (value && !value.startsWith(SEARCH_PREFIX)) {
        __privateMethod(this, _View_instances, emit_fn).call(this, "show-annotation", { value, index: index2, range });
      }
    }, false);
    const list = __privateGet(this, _searchResults).get(index2);
    if (list) for (const item of list) this.addAnnotation(item);
    __privateMethod(this, _View_instances, emit_fn).call(this, "create-overlay", { index: index2 });
    return overlayer;
  };
  searchSection_fn = function(matcher, query, index2) {
    return __asyncGenerator(this, null, function* () {
      const doc = yield new __await(this.book.sections[index2].createDocument());
      for (const { range, excerpt } of matcher(doc, query))
        yield { cfi: this.getCFI(index2, range), excerpt };
    });
  };
  searchBook_fn = function(matcher, query) {
    return __asyncGenerator(this, null, function* () {
      const { sections } = this.book;
      for (const [index2, { createDocument }] of sections.entries()) {
        if (!createDocument) continue;
        const doc = yield new __await(createDocument());
        const subitems = Array.from(matcher(doc, query), ({ range, excerpt }) => ({ cfi: this.getCFI(index2, range), excerpt }));
        const progress = (index2 + 1) / sections.length;
        yield { progress };
        if (subitems.length) yield { index: index2, subitems };
      }
    });
  };
  customElements.define("foliate-view", View2);

  // src/reader.js
  window.ResizeObserver || (window.ResizeObserver = ResizeObserver_es_default);
  window.visualViewport || (window.visualViewport = { scale: 1 });
  if (!Element.prototype.toggleAttribute) Element.prototype.toggleAttribute = function(name, force) {
    const next = arguments.length > 1 ? Boolean(force) : !this.hasAttribute(name);
    if (next) this.setAttribute(name, "");
    else this.removeAttribute(name);
    return next;
  };
  for (const prototype of [Element.prototype, DocumentFragment.prototype]) {
    if (!prototype.replaceChildren) prototype.replaceChildren = function(...nodes) {
      while (this.firstChild) this.removeChild(this.firstChild);
      for (const node of nodes) this.appendChild(typeof node === "string" ? document.createTextNode(node) : node);
    };
  }
  if (!Blob.prototype.arrayBuffer) Blob.prototype.arrayBuffer = function() {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsArrayBuffer(this);
    });
  };
  if (!Blob.prototype.text) Blob.prototype.text = function() {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsText(this);
    });
  };
  var port;
  var view;
  var publication;
  var preferences = {};
  var lastLocation;
  var searchEpoch = 0;
  var navigation = Promise.resolve();
  var navigationEpoch = 0;
  var queuedNavigation = 0;
  var documentOffsets = /* @__PURE__ */ new WeakMap();
  var state = document.getElementById("state");
  var notify = (type, value = {}) => {
    let raw = JSON.stringify(__spreadValues({ type }, value));
    if (raw.length > 6e4 && Array.isArray(value.results)) {
      while (raw.length > 6e4 && value.results.length) {
        value.results.pop();
        raw = JSON.stringify(__spreadValues({ type }, value));
      }
    }
    if (raw.length <= 6e4) port == null ? void 0 : port.postMessage(raw);
  };
  function boundedToc(items) {
    let remaining = 5e4, count = 0;
    const walk = (values, depth) => {
      const result = [];
      if (depth > 10) return result;
      for (const item of values || []) {
        if (++count > 500 || remaining <= 0) break;
        const node = { label: String(item.label || "").slice(0, 200), href: String(item.href || "").slice(0, 4096) };
        remaining -= JSON.stringify(node).length + 32;
        if (remaining < 0) break;
        node.subitems = walk(item.subitems, depth + 1);
        result.push(node);
      }
      return result;
    };
    return walk(items, 0);
  }
  var fail = (error) => {
    state.style.display = "block";
    state.textContent = error.message || "This publication could not be opened.";
    notify("error", { message: state.textContent });
  };
  window.addEventListener("error", (event) => fail(event.error || new Error(event.message)));
  window.addEventListener("unhandledrejection", (event) => fail(event.reason || new Error("Reader failed")));
  var fetchLocal = async (url) => {
    const parsed = new URL(url, location.href);
    if (parsed.origin !== location.origin) throw new Error("External book resources stay offline");
    return fetch(parsed);
  };
  function applyAppearance(prefs) {
    var _a2, _b2, _c, _d;
    preferences = __spreadValues(__spreadValues({}, preferences), prefs);
    const dark = preferences.theme === "Dark";
    const sepia = preferences.theme === "Sepia";
    const background = dark ? "#141916" : sepia ? "#f4ecd8" : "#ffffff";
    const foreground = dark ? "#e4e9e5" : "#17231e";
    document.body.style.background = background;
    document.body.style.color = foreground;
    const size = Math.max(12, Math.min(40, Number(preferences.fontSize) || 18));
    const height = Math.max(1.2, Math.min(2.2, Number(preferences.lineHeight) || 1.6));
    const font = ["serif", "sans-serif", "monospace"].includes(preferences.font) ? preferences.font : "serif";
    if (((_a2 = publication == null ? void 0 : publication.rendition) == null ? void 0 : _a2.layout) === "pre-paginated") {
      (_b2 = view == null ? void 0 : view.renderer) == null ? void 0 : _b2.setAttribute("zoom", ["fit-page", "fit-width", "1", "1.5", "2"].includes(preferences.fixedZoom) ? preferences.fixedZoom : "fit-page");
      return;
    }
    (_d = (_c = view == null ? void 0 : view.renderer) == null ? void 0 : _c.setStyles) == null ? void 0 : _d.call(_c, "html{color:".concat(foreground, "!important;background:").concat(background, "!important;color-scheme:").concat(dark ? "dark" : "light", "}body{font-family:").concat(font, "!important;font-size:").concat(size, "px!important;line-height:").concat(height, "!important}a{color:").concat(dark ? "#91d5b6" : "#366a59", "}"));
    if (view == null ? void 0 : view.renderer) view.renderer.setAttribute("flow", preferences.scrolling ? "scrolled" : "paginated");
    if (view == null ? void 0 : view.renderer) view.renderer.toggleAttribute("animated", !preferences.reducedMotion);
  }
  async function open(config) {
    var _a2, _b2, _c, _d, _e;
    (_a2 = view == null ? void 0 : view.close) == null ? void 0 : _a2.call(view);
    view == null ? void 0 : view.remove();
    (_b2 = publication == null ? void 0 : publication.destroy) == null ? void 0 : _b2.call(publication);
    documentOffsets = /* @__PURE__ */ new WeakMap();
    lastLocation = null;
    if (config.format === "EPUB") {
      const metadata = await (await fetchLocal("".concat(config.base, "/manifest"))).json();
      const entryURL = (name) => "".concat(config.base, "/entry?name=").concat(encodeURIComponent(name));
      publication = await new EPUB({
        loadText: async (name) => {
          const response = await fetchLocal(entryURL(name));
          return response.ok ? response.text() : null;
        },
        loadBlob: async (name) => {
          const response = await fetchLocal(entryURL(name));
          return response.ok ? response.blob() : null;
        },
        getSize: (name) => metadata.entries[name] || 0
      }).init();
    } else if (config.format === "MOBI") {
      const file = {
        size: config.size,
        name: config.name,
        type: config.mime,
        slice(start = 0, end = config.size) {
          return { arrayBuffer: async () => {
            const response = await fetchLocal("".concat(config.base, "/range?start=").concat(start, "&end=").concat(end));
            if (!response.ok) throw new Error("A book resource exceeds the safe reading limit");
            return response.arrayBuffer();
          } };
        }
      };
      publication = await new MOBI({ unzlib: unzlibSync }).open(file);
    } else if (config.format === "FB2") {
      const response = await fetchLocal("".concat(config.base, "/file"));
      if (!response.ok) throw new Error("This FB2 needs offline normalization before reading");
      publication = await makeFB2(await response.blob());
    } else throw new Error("Unsupported reflowable format");
    view = document.createElement("foliate-view");
    document.body.append(view);
    view.addEventListener("relocate", (event) => {
      var _a3, _b3, _c2, _d2, _e2, _f2, _g, _h, _i, _j;
      const location2 = event.detail;
      const fixed = ((_a3 = publication.rendition) == null ? void 0 : _a3.layout) === "pre-paginated";
      let fraction = fixed ? (((_c2 = (_b3 = location2.section) == null ? void 0 : _b3.current) != null ? _c2 : 0) + 1) / Math.max(1, publication.sections.length) : 0;
      const range = location2.range;
      if (range && !fixed) {
        const doc = range.startContainer.ownerDocument;
        let cached = documentOffsets.get(doc);
        if (!cached) {
          cached = { offsets: /* @__PURE__ */ new WeakMap(), total: 0 };
          const walker = doc.createTreeWalker(doc.body, NodeFilter.SHOW_TEXT);
          for (let node = walker.nextNode(); node; node = walker.nextNode()) {
            cached.offsets.set(node, cached.total);
            cached.total += node.textContent.length;
          }
          documentOffsets.set(doc, cached);
        }
        let offset = cached.offsets.get(range.startContainer);
        if (offset !== void 0) offset += range.startOffset;
        else {
          const prefix = doc.createRange();
          prefix.selectNodeContents(doc.body);
          prefix.setEnd(range.startContainer, range.startOffset);
          offset = prefix.toString().length;
        }
        fraction = logicalFraction((_e2 = (_d2 = location2.section) == null ? void 0 : _d2.current) != null ? _e2 : 0, offset, cached.total, publication.sections.map((section) => section.linear === "no" ? 0 : Math.max(1, section.size || 1)));
      }
      lastLocation = {
        locator: location2.cfi || "",
        fraction,
        section: (_g = (_f2 = location2.section) == null ? void 0 : _f2.current) != null ? _g : 0,
        chapter: ((_h = location2.tocItem) == null ? void 0 : _h.label) || "",
        quote: ((_j = (_i = location2.range) == null ? void 0 : _i.toString) == null ? void 0 : _j.call(_i).slice(0, 160)) || ""
      };
      notify("location", lastLocation);
    });
    view.addEventListener("load", (event) => {
      const doc = event.detail.doc;
      let lastInteraction = 0;
      const interact = () => {
        const now = Date.now();
        if (now - lastInteraction > 1e3) {
          lastInteraction = now;
          notify("interact");
        }
      };
      for (const type of ["click", "touchstart", "touchmove", "wheel", "keydown"]) doc.addEventListener(type, interact, { passive: true });
      doc.addEventListener("selectionchange", () => {
        var _a3;
        const text = ((_a3 = doc.getSelection()) == null ? void 0 : _a3.toString()) || "";
        if (text && text.length <= 1600) notify("selection", { text });
      });
    });
    await view.open(publication);
    applyAppearance(config.preferences || {});
    if (config.locator) await view.goTo(config.locator);
    else await view.init({ lastLocation: null });
    state.style.display = "none";
    const author = (_c = publication.metadata) == null ? void 0 : _c.author;
    const authorName = typeof author === "string" ? author : Array.isArray(author) ? author.map((value) => typeof value === "string" ? value : value.name || "").join(", ") : (author == null ? void 0 : author.name) || "";
    const title = (_d = publication.metadata) == null ? void 0 : _d.title;
    notify("opened", { title: typeof title === "string" ? title : config.title || config.name, author: authorName, toc: boundedToc(publication.toc), fixedLayout: ((_e = publication.rendition) == null ? void 0 : _e.layout) === "pre-paginated", sections: publication.sections.length });
  }
  async function command(message) {
    var _a2, _b2;
    switch (message.type) {
      case "open":
        await open(message);
        break;
      case "next":
        await (view == null ? void 0 : view.next());
        notify("interact");
        break;
      case "previous":
        await (view == null ? void 0 : view.prev());
        notify("interact");
        break;
      case "goTo":
        await (view == null ? void 0 : view.goTo(message.locator));
        notify("interact");
        break;
      case "appearance":
        applyAppearance(message.preferences);
        break;
      case "bookmark":
        if (lastLocation) notify("bookmark", lastLocation);
        break;
      case "search": {
        const epoch = ++searchEpoch;
        const query = String(message.query || "").trim().slice(0, 200);
        const results = [];
        if (query) try {
          for (var iter = __forAwait(view.search({ query })), more, temp, error; more = !(temp = await iter.next()).done; more = false) {
            const found = temp.value;
            if (epoch !== searchEpoch) return;
            for (const match of found.subitems || []) {
              if (results.length >= 100) break;
              const excerpt = match.excerpt;
              results.push({ label: found.label || "", locator: match.cfi, excerpt: typeof excerpt === "string" ? excerpt : "".concat((excerpt == null ? void 0 : excerpt.pre) || "").concat((excerpt == null ? void 0 : excerpt.match) || "").concat((excerpt == null ? void 0 : excerpt.post) || "") });
            }
            if (results.length >= 100) break;
          }
        } catch (temp) {
          error = [temp];
        } finally {
          try {
            more && (temp = iter.return) && await temp.call(iter);
          } finally {
            if (error)
              throw error[0];
          }
        }
        if (epoch === searchEpoch) notify("search", { results });
        break;
      }
      case "cancelSearch":
        ++searchEpoch;
        break;
      case "close":
        ++searchEpoch;
        (_a2 = view == null ? void 0 : view.close) == null ? void 0 : _a2.call(view);
        (_b2 = publication == null ? void 0 : publication.destroy) == null ? void 0 : _b2.call(publication);
        break;
    }
  }
  window.addEventListener("message", (event) => {
    var _a2, _b2;
    if (port || event.data !== "prospacehub-reader-channel" || !((_a2 = event.ports) == null ? void 0 : _a2[0])) return;
    port = event.ports[0];
    port.onmessage = (event2) => {
      try {
        const message = JSON.parse(event2.data);
        if (message.type === "close") ++navigationEpoch;
        if (["open", "next", "previous", "goTo", "appearance"].includes(message.type)) {
          if (queuedNavigation >= 16) return;
          const epoch = navigationEpoch;
          ++queuedNavigation;
          navigation = navigation.then(() => epoch === navigationEpoch ? command(message) : void 0).catch(fail).finally(() => {
            --queuedNavigation;
          });
        } else command(message).catch(fail);
      } catch (error) {
        fail(error);
      }
    };
    (_b2 = port.start) == null ? void 0 : _b2.call(port);
    notify("ready");
  });
})();
