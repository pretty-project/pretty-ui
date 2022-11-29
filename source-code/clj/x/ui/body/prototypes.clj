
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.body.prototypes
    (:require [re-frame.api :as r]
              [x.core.api   :as x.core]
              [x.router.api :as x.router]
              [x.user.api   :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:build-version (string)
  ;   :js-build (keyword)
  ;   :selected-theme (string)}
  [request body-props]
  (let [app-config @(r/subscribe [:x.core/get-app-config])]
       (merge app-config body-props
              {:build-version  (x.core/build-version)
               :js-build       (x.router/request->route-prop       request :js-build x.core/DEFAULT-JS-BUILD)
               :selected-theme (x.user/request->user-settings-item request :selected-theme)})))
