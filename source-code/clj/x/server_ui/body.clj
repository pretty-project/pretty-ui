
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.19
; Description:
; Version: v0.9.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.server-core.api    :as a :refer [cache-control-uri]]
              [x.server-router.api  :as router]
              [x.server-ui.engine   :refer [include-js]]
              [x.server-ui.graphics :as graphics]
              [x.server-ui.shield   :refer [view] :rename {view app-shield}]
              [x.server-user.api    :as user]
              [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def CORE-JS-DIR "/js/core")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- core-js-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) body-props
  ;  {:core-js (string)}
  ;
  ; @example
  ;  (core-js-props {...})
  ;  =>
  ;  {:uri "/js/core/app.js"}
  ;
  ; @return (map)
  ;  {:uri (string)}
  [{:keys [core-js]}]
  (let [core-js-uri (str CORE-JS-DIR "/" core-js)]
       {:uri core-js-uri}))

(defn body<-js-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) body
  ; @param (map) request
  ; @param (map) body-props
  ;  {:app-build (string)(opt)
  ;   :core-js (string)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [body request {:keys [app-build core-js plugin-js-paths] :as body-props}]
  (letfn [(include-js? [js-props] (or (-> js-props :core-js nil?)
                                      (-> js-props :core-js (= core-js))))
          (f [body {:keys [uri] :as js-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   js-props          (assoc js-props :uri cache-control-uri)]
                  (if (include-js? js-props)
                      (conj   body (include-js js-props))
                      (return body))))]
         (let [core-js-props (core-js-props body-props)
               js-paths      (vector/cons-item plugin-js-paths core-js-props)]
              (reduce f body js-paths))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :core-js (string)
  ;   :selected-theme (string)
  ;   :shield (hiccup)}
  [request body-props]
  (merge @(a/subscribe [:core/get-app-config])
          {:app-build      (a/app-build)
           :core-js        (router/request->route-prop       request :core-js)
           :selected-theme (user/request->user-settings-item request :selected-theme)
           :shield         (app-shield (graphics/loading-animation))}
          (param body-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:selected-theme (string)
  ;   :shield (hiccup)(opt)}
  ;
  ; @return (hiccup)
  [request {:keys [selected-theme shield]}]
  [:body#x-body-container {:data-theme selected-theme}
                          (let [csrf-token (force *anti-forgery-token*)]
                               [:div#sente-csrf-token {:data-csrf-token csrf-token}])
                          [:div#x-app-container]
                          (param shield)])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:plugin-js-paths (maps in vector)
  ;    [{:core-js (string)(opt)
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
