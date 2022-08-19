

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.sample
    (:require [x.app-core.api  :as a]
              [x.app-tools.api :as tools]))



;; -- A komponens használata --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component
  []
  [tools/infinite-loader :my-loader {:on-viewport [:my-event]}])



;; -- Újratöltés esemény használata -------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :reload-my-loader!
  [:tools/reload-infinite-loader! :my-loader])



;; -- Leállítás esemény használata --------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :pause-my-loader!
  [:tools/pause-infinite-loader! :my-loader])



;; -- Újraindítás esemény használata ------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :restart-my-loader!
  [:tools/restart-infinite-loader! :my-loader])
