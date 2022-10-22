
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.prototypes
    (:require [re-frame.api             :as r]
              [x.server-core.api        :as core]
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
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :js-build (keyword)
  ;   :crawler-rules (string)
  ;   :selected-language (keyword)}
  [request head-props]
  (let [app-config @(r/subscribe [:core/get-app-config])]
       (merge app-config head-props
              {:app-build         (core/app-build)
               :js-build          (router/request->route-prop       request :js-build router/DEFAULT-JS-BUILD)
               :crawler-rules     (environment/crawler-rules        request)
               :selected-language (user/request->user-settings-item request :selected-language)})))
