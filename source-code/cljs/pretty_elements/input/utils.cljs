
(ns pretty-elements.input.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-options-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:pretty-elements :element-handler/input-options input-id])

(defn default-value-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:pretty-elements :element-handler/input-values input-id])

(defn value-path->vector-item?
  ; @ignore
  ;
  ; @param (vector) value-path
  ;
  ; @usage
  ; (value-path->vector-item? [:my-value])
  ; =>
  ; false
  ;
  ; @usage
  ; (value-path->vector-item? [:my-value 2])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [value-path]
  (-> value-path last integer?))
