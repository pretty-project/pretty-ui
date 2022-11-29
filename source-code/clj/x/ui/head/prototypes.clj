
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.head.prototypes
    (:require [re-frame.api      :as r]
              [x.core.api        :as x.core]
              [x.environment.api :as x.environment]
              [x.router.api      :as x.router]
              [x.user.api        :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn head-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) head-props
  ;
  ; @return (map)
  ;  {:build-version (string)
  ;   :crawler-rules (string)
  ;   :js-build (keyword)
  ;   :selected-language (keyword)}
  [request head-props]
  (let [app-config @(r/subscribe [:x.core/get-app-config])]
       (merge app-config head-props
              {:build-version     (x.core/build-version)
               :js-build          (x.router/request->route-prop       request :js-build x.core/DEFAULT-JS-BUILD)
               :crawler-rules     (x.environment/crawler-rules        request)
               :selected-language (x.user/request->user-settings-item request :selected-language)})))
