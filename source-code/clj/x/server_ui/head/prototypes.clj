

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.prototypes
    (:require [mid-fruits.vector        :as vector]
              [x.server-core.api        :as a]
              [x.server-environment.api :as environment]
              [x.server-router.api      :as router]
              [x.server-ui.head.config  :as head.config]
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
  (let [app-config           @(a/subscribe [:core/get-app-config])
        additional-css-paths @(a/subscribe [:environment/get-css-paths])]
       (merge app-config head-props
              {:app-build         (a/app-build)
               :core-js           (router/request->core-js          request)
               :crawler-rules     (environment/crawler-rules        request)
               :selected-language (user/request->user-settings-item request :selected-language)
               ; A css fájlok listája:
               ;  1. A head.config/SYSTEM-CSS-PATHS vektorban felsorolt fájlok.
               ;  2. Az x.app-config.edn fájl {:css-paths [...]} tulajdonsága.
               ;  3. A head komponens {:css-paths [...]} paramétere.
               ;  4. Az [:environment/add-css! ...] eseménnyel hozzáadott fájlok.
               :css-paths (vector/concat-items head.config/SYSTEM-CSS-PATHS
                                               (:css-paths app-config)
                                               (:css-paths head-props)
                                               additional-css-paths)})))
