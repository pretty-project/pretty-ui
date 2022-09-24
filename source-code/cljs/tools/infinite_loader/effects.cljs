
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.effects
    (:require [tools.infinite-loader.events :as events]
              [x.app-core.api               :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :infinite-loader/reload-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  [:infinite-loader/reload-loader! :my-loader]
  (fn [{:keys [db]} [_ loader-id]]
       ; Az infinite-loader komponensben elhelyezett observer viewport-on kívülre
       ; helyezése, majd visszaállítása újra meghívja az infinite-loader komponens
       ; számára callback paraméterként átadott függvényt.
      {:db (r events/hide-observer! db loader-id)
       ; A túlságosan rövid ideig (pl. 5ms) a viewport-on kívülre helyezett observer
       ; nem minden esetben hívja meg a callback függvényt.
       :dispatch-later [{:ms 50 :dispatch [:infinite-loader/show-observer! loader-id]}]}))
