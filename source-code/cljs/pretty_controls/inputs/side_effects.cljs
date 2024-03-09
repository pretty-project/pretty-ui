
(ns pretty-controls.inputs.side-effects)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-input!
  ; @description
  ; ...
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:on-invalid-f (function)(opt)
  ;  :on-valid-f (function)(opt)}
  ;
  ; @usage
  ; (validate-input! :my-input {:on-valid-f (fn [_] ...)})
  [id props])
  ; When an input gets validated, the autovalidation must be turned on.
  ; Otherwise, the input can stuck in an invalid state even if it's changed and not invalid anymore.
  ; pretty-inputs.engine/autovalidate-input!
  ; pretty-inputs.engine/validate-input!

(defn validate-form!
  ; @description
  ; ...
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:on-invalid-f (function)(opt)
  ;  :on-valid-f (function)(opt)}
  ;
  ; @usage
  ; (validate-form! :my-form {:on-valid-f (fn [_] ...)})
  [id props])
  ; pretty-inputs.engine/autovalidate-form!
  ; pretty-inputs.engine/validate-form!
