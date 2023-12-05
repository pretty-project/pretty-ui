
(ns pretty-elements.counter.events
    (:require [pretty-elements.counter.subs :as counter.subs]
              [pretty-elements.input.events :as input.events]
              [re-frame.api                 :refer [r]]
              [re-frame.db.api              :as r.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-did-mount
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [db [_ counter-id counter-props]]
  (r input.events/use-initial-value! db counter-id counter-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-value!
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-decreasable? db counter-id counter-props)
      (r r.db/apply-item!                db value-path dec)
      (->                                db)))

(defn increase-value!
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ counter-id {:keys [value-path] :as counter-props}]]
  (if (r counter.subs/value-increasable? db counter-id counter-props)
      (r r.db/apply-item!                db value-path inc)
      (->                                db)))
