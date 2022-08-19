
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.helpers
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.vector        :as vector]
              [x.server-core.api        :as a :refer [cache-control-uri]]
              [x.server-ui.body.config  :as body.config]
              [x.server-ui.core.helpers :refer [include-js]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn core-js-props
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
  (let [core-js-uri (str body.config/CORE-JS-DIR "/" core-js)]
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
