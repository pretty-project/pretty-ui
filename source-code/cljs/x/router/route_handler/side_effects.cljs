
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.route-handler.side-effects
    (:require [js-window.api :as js-window]
              [re-frame.api  :as r]
              [string.api    :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [_]
  (let [route-string (js-window/get-uri-path)
        route-id    @(r/subscribe [:x.router/match-route-id route-string])]
       (r/dispatch [:x.router/store-current-route! route-id route-string])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.router/read-current-route! read-current-route!)
