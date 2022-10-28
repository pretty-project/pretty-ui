
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
              [x.server-core.api   :as x.core]
              [x.server-router.api :as x.router]
              [x.server-user.api   :as x.user]))



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
  ;   :js-build (keyword)
  ;   :selected-theme (string)}
  [request body-props]
  (let [app-config @(r/subscribe [:core/get-app-config])]
       (merge app-config body-props
              {:app-build      (x.core/app-build)
               :js-build       (x.router/request->route-prop       request :js-build x.router/DEFAULT-JS-BUILD)
               :selected-theme (x.user/request->user-settings-item request :selected-theme)})))
