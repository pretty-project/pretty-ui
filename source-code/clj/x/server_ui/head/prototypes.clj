
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.prototypes
    (:require [mid-fruits.vector        :as vector]
              [x.server-core.api        :as a]
              [x.server-environment.api :as environment]
              [x.server-router.api      :as router]
              [x.server-user.api        :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn head-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) head-props
  ;  {:css-paths (maps in vector)(opt)}
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :core-js (string)
  ;   :crawler-rules (string)
  ;   :selected-language (keyword)}
  [request head-props]
  (let [app-config @(a/subscribe [:core/get-app-config])]
       (merge app-config head-props
              {:app-build         (a/app-build)
               :core-js           (router/request->core-js          request)
               :crawler-rules     (environment/crawler-rules        request)
               :selected-language (user/request->user-settings-item request :selected-language)
               ; Hozzáadja a {:css-paths [...]} paraméterként átadott útvonalakat
               ; az x.app-config.edn fájlban beállított útvonalakhoz
               :css-paths (vector/concat-items (:css-paths app-config)
                                               (:css-paths head-props))})))
