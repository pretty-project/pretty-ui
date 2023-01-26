
(ns elements.input.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
