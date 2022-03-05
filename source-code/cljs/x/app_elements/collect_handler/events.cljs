
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.collect-handler.events
    (:require [x.app-core.api :as a :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-collectable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (r selectable/init-selectable! db input-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/init-collectable! init-collectable!)
