
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
              [x.server-core.api        :as x.core]
              [x.server-environment.api :as x.environment]
              [x.server-router.api      :as x.router]
              [x.server-user.api        :as x.user]))



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
  ;   :crawler-rules (string)
  ;   :js-build (keyword)
  ;   :selected-language (keyword)}
  [request head-props]
  (let [app-config @(r/subscribe [:core/get-app-config])]
       (merge app-config head-props
              {:app-build         (x.core/app-build)
               :js-build          (x.router/request->route-prop       request :js-build x.router/DEFAULT-JS-BUILD)
               :crawler-rules     (x.environment/crawler-rules        request)
               :selected-language (x.user/request->user-settings-item request :selected-language)})))
