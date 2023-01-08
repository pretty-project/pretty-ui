
(ns elements.input.helpers
    (:require [elements.input.state :as input.state]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/FOCUSED-INPUTS assoc input-id true))

(defn unmark-input-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/FOCUSED-INPUTS dissoc input-id))

(defn input-focused?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/FOCUSED-INPUTS input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/VISITED-INPUTS assoc input-id true))

(defn unmark-input-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! input.state/VISITED-INPUTS dissoc input-id))

(defn input-visited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/VISITED-INPUTS input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (or options @(r/subscribe [:x.db/get-item options-path])))

(defn default-options-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-options input-id])

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-values input-id])

(defn value-path->vector-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
