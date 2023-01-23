
(ns elements.slider.events
    (:require [elements.input.events :as input.events]
              [noop.api              :refer [return]]
              [re-frame.api          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;
  ; @return (map)
  [db [_ slider-id slider-props]]
  (r input.events/use-initial-value! db slider-id slider-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  [db [_ slider-id {:keys [value-path] :as slider-props}]])

(defn increase-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  [db [_ slider-id {:keys [value-path] :as slider-props}]])


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.slider/decrease-value! decrease-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.slider/increase-value! increase-value!)
