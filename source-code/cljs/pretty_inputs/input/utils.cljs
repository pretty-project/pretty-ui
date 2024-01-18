
(ns pretty-inputs.input.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-options-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:pretty-inputs :input-handler/input-options input-id])

(defn default-value-path
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:pretty-inputs :input-handler/input-values input-id])
