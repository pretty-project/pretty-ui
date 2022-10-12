
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.prototypes
    (:require [re-frame.api        :as r]
              [x.server-core.api   :as core]
              [x.server-router.api :as router]
              [x.server-user.api   :as user]))



;; ----------------------------------------------------------------------------
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
  ;   :selected-theme (string)}
  [request body-props]
  (let [app-config @(r/subscribe [:core/get-app-config])]
       (merge app-config body-props
              {:app-build      (core/app-build)
               :core-js        (router/request->route-prop       request :core-js router/DEFAULT-CORE-JS)
               :selected-theme (user/request->user-settings-item request :selected-theme)})))
