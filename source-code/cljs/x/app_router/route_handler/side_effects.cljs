
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.side-effects
    (:require [js-window.api     :as js-window]
              [mid-fruits.string :as string]
              [re-frame.api      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [_]
  (let [route-string (js-window/get-uri-path)
        route-id    @(r/subscribe [:router/match-route-id route-string])]
       (r/dispatch [:router/store-current-route! route-id route-string])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :router/read-current-route! read-current-route!)
