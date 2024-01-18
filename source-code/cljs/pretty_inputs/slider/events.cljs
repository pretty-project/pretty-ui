
(ns pretty-inputs.slider.events
    (:require [pretty-inputs.input.events :as input.events]
              [re-frame.api               :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-did-mount
  ; @ignore
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
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  [db [_ slider-id {:keys [value-path] :as slider-props}]])

(defn increase-value!
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  [db [_ slider-id {:keys [value-path] :as slider-props}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-event-db :pretty-inputs.slider/decrease-value! decrease-value!)

; @ignore
(r/reg-event-db :pretty-inputs.slider/increase-value! increase-value!)
