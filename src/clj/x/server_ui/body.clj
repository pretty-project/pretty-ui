
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.19
; Description:
; Version: v0.6.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [x.mid-ui.api       :as ui]
              [x.server-core.api  :as a :refer [cache-control-uri]]
              [x.server-ui.engine :refer [include-js]]
              [x.server-ui.shield :refer [view] :rename {view app-shield}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request->core-js-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @example
  ;  (request->core-js-filename {...})
  ;  =>
  ;  "app.js"
  ;
  ; @return (string)
  [request]
  (if-let [core-js-filename (a/request->route-param request :js)]
          (string/not-starts-with! core-js-filename  "/")
          (let [default-core-js-filename (a/subscribed [:x.server-core/get-config-item :default-core-js])]
               (string/not-starts-with! default-core-js-filename  "/"))))

(defn- request->core-js-uri-base
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @example
  ;  (request->core-js-uri-base {...})
  ;  =>
  ;  "/js/core/"
  ;
  ; @return (string)
  [request]
  (let [core-js-dir (a/subscribed [:x.server-core/get-config-item :core-js-dir])]
       (-> core-js-dir (string/starts-with! "/")
                       (string/ends-with!   "/"))))

(defn- request->core-js-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @example
  ;  (request->core-js-uri-uri {...})
  ;  =>
  ;  "/js/core/app.js"
  ;
  ; @return (string)
  [request]
  (let [core-js-uri-base (request->core-js-uri-base request)
        core-js-filename (request->core-js-filename request)]
       (str core-js-uri-base core-js-filename)))

(defn- request->core-js-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @example
  ;  (request->core-js-uri-props {...})
  ;  =>
  ;  {:cache-control? true
  ;   :uri            "/js/core/app.js"}
  ;
  ; @return (map)
  ;  {:cache-control? (boolean)
  ;   :uri (string)}
  [request]
  (let [core-js-uri (request->core-js-uri request)]
       {:cache-control? true
        :uri            core-js-uri}))

(defn body<-js-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) body
  ; @param (map) request
  ; @param (map) body-props
  ;  {:app-build (string)(opt)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [body request {:keys [app-build plugin-js-paths]}]
  (let [core-js-props (request->core-js-props request)
        js-paths      (vector/cons-item plugin-js-paths core-js-props)]
       (reduce (fn [body {:keys [cache-control? uri] :as js-props}]
                   (if cache-control?
                       (let [cache-control-uri (cache-control-uri uri app-build)
                             js-props          (assoc js-props :uri cache-control-uri)]
                            (vector/conj-item body (include-js js-props)))
                       (vector/conj-item      body (include-js js-props))))
               (param body)
               (param js-paths))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:shield (hiccup)}
  [request body-props]
  (merge (a/subscribed [:x.server-core/get-destructed-configs])
         {:shield (app-shield (ui/loading-animation-a))}
         (param body-props)))
