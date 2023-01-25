
(ns elements.input.helpers
    (:require [elements.input.state :as input.state]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-focused?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/FOCUSED-INPUTS input-id))

(defn input-visited?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/VISITED-INPUTS input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:options (vector)(opt)
  ;  :options-path (vector)(opt)}
  ;
  ; @return (vector)
  [_ {:keys [options options-path]}]
  ; XXX#2781
  ; (A) In selectable elements the selectable options derived from the element's
  ;     :options property.
  ; (B) If the :options property hasn't been set, the options derived from the
  ;     application state by using the :options-path property.
  (or options @(r/subscribe [:get-item options-path])))

(defn default-options-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-options input-id])

(defn default-value-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-values input-id])

(defn value-path->vector-item?
  ; @ignore
  ;
  ; @param (vector) value-path
  ;
  ; @example
  ; (value-path->vector-item? [:my-value])
  ; =>
  ; false
  ;
  ; @example
  ; (value-path->vector-item? [:my-value 2])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [value-path]
  (-> value-path last integer?))
