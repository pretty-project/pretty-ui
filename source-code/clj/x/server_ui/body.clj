
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.19
; Description:
; Version: v0.7.2
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body
    (:require [ring.middleware.anti-forgery]
              [mid-fruits.candy     :refer [param]]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.server-core.api    :as a :refer [cache-control-uri]]
              [x.server-ui.engine   :refer [include-js]]
              [x.server-ui.graphics :as graphics]
              [x.server-ui.shield   :refer [view] :rename {view app-shield}]
              [x.server-user.api    :as user]))



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
          (let [default-core-js-filename (a/subscribed [:core/get-config-item :default-core-js])]
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
  (let [core-js-dir (a/subscribed [:core/get-config-item :core-js-dir])]
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
                            (conj body (include-js js-props)))
                       (conj      body (include-js js-props))))
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
  (merge (a/subscribed [:core/get-destructed-configs])
         {:shield (app-shield (graphics/loading-animation))}
         (param body-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:shield (hiccup)(opt)}
  ;
  ; @return (hiccup)
  [request {:keys [shield]}]
  [:body#x-body-container
    {:data-theme (user/request->user-settings-item request :selected-theme)}


    (let [csrf-token (force ring.middleware.anti-forgery/*anti-forgery-token*)]
         [:div#sente-csrf-token {:data-csrf-token csrf-token}])


    [:div#x-app-container]
    (if (some? shield)
        (param shield))])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:app-build (string)(opt)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;   :shield (hiccup)(opt)}
  ;
  ; @usage
  ;  (ui/body {...} {:shield [:div#x-app-shield "My loading screen"]})
  ;
  ; @return (hiccup)
  ([request]
   (view request {}))

  ([request body-props]
   (let [body-props (body-props-prototype request body-props)]
        (-> (body              request body-props)
            (body<-js-includes request body-props)))))
