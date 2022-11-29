
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.sample
    (:require [re-frame.api              :as r]
              [tools.infinite-loader.api :as infinite-loader]))



;; -- A komponens használata --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component
  []
  [infinite-loader/component :my-loader {:on-viewport [:my-event]}])



;; -- Újratöltés esemény használata -------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :reload-my-loader!
  [:infinite-loader/reload-loader! :my-loader])



;; -- Leállítás esemény használata --------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pause-my-loader!
  [:infinite-loader/pause-loader! :my-loader])



;; -- Újraindítás esemény használata ------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :restart-my-loader!
  [:infinite-loader/restart-loader! :my-loader])
