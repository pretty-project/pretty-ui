
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.effects
    (:require [re-frame.api                 :as r :refer [r]]
              [tools.infinite-loader.events :as events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :infinite-loader/reload-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ; [:infinite-loader/reload-loader! :my-loader]
  (fn [{:keys [db]} [_ loader-id]]
       ; Az infinite-loader komponensben elhelyezett observer viewport-on kívülre
       ; helyezése, majd visszaállítása újra meghívja az infinite-loader komponens
       ; számára callback paraméterként átadott függvényt.
      {:db (r events/hide-observer! db loader-id)
       ; A túlságosan rövid ideig (pl. 5ms) a viewport-on kívülre helyezett observer
       ; nem minden esetben hívja meg a callback függvényt.
       :dispatch-later [{:ms 50 :dispatch [:infinite-loader/show-observer! loader-id]}]}))
