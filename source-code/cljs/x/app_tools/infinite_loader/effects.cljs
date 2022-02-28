
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.effects
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-tools.infinite-loader.events :as infinite-loader.events]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/reload-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  [:tools/reload-infinite-loader! :my-loader]
  (fn [{:keys [db]} [_ loader-id]]
       ; Az infinite-loader komponensben elhelyezett observer viewport-on kívülre
       ; helyezése, majd visszaállítása újra meghívja az infinite-loader komponens
       ; számára callback paraméterként átadott függvényt.
      {:db (r infinite-loader.events/hide-infinite-observer! db loader-id)
       ; A túlságosan rövid ideig (pl.: 5ms) a viewport-on kívülre helyezett observer
       ; nem minden esetben hívja meg a callback függvényt.
       :dispatch-later [{:ms 50 :dispatch [:tools/show-infinite-observer! loader-id]}]}))
