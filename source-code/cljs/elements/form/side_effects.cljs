
(ns elements.form.side-effects
    (:require [elements.form.env   :as form.env]
              [elements.form.state :as form.state]
              [noop.api            :refer [return]]
              [re-frame.api        :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-input!
  ; @ignore
  ;
  ; @description
  ; Iterates over the validators of the registered form input with the given
  ; input ID ...
  ; ... and in case of ANY validator fails, it ...
  ;     ... stores the invalid-message in the form errors atom,
  ;     ... dispatches the on-invalid event if any,
  ;     ... returns the invalid message.
  ; ... and in case of NO validator fails, it ...
  ;     ... clears the previously set invalid-message from the form errors atom,
  ;     ... dispatches the on-valid event if any,
  ;     ... returns NIL.
  ;
  ; @param (keyword) input-id
  ; @param (keyword) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the input value and the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the input value as its last parameter.}
  ;
  ; @return (metamorphic-content)
  [input-id {:keys [on-invalid on-valid]}]
  ; After an input once validated, the :validate-when-change? toggle must be turned on,
  ; otherwise the input might stuck in an invalid state even if it's not invalid anymore.
  (swap! form.state/FORM-INPUTS assoc-in [input-id :validate-when-change?] true)

  (let [stored-value (form.env/get-input-stored-value input-id)
        validators   (form.env/get-input-validators   input-id)]
       (letfn [(use-validator-f [{:keys [f invalid-message]}]
                                (if-not (f stored-value)
                                        (or invalid-message :invalid)))]
              (if-let [invalid-message (some use-validator-f validators)]
                      (do (swap! form.state/FORM-ERRORS assoc input-id invalid-message)
                          (if on-invalid (let [on-invalid (r/metamorphic-event<-params on-invalid stored-value invalid-message)]
                                              (r/dispatch on-invalid)))
                          (return invalid-message))
                      (do (swap! form.state/FORM-ERRORS dissoc input-id)
                          (if on-valid (let [on-valid (r/metamorphic-event<-params on-valid stored-value)]
                                            (r/dispatch on-valid)))
                          (return nil))))))

(defn validate-form!
  ; @ignore
  ;
  ; @description
  ; Iterates over the registered form inputs with the same form ID and validates
  ; the inputs until one fails on one of its validators.
  ; If any input fails it dispatches the on-invalid event if any,
  ; otherwise it dispatches the on-valid event if any.
  ;
  ; @param (keyword) form-id
  ; @param (keyword) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (metamorphic-content)
  [form-id {:keys [on-invalid on-valid]}]
  (letfn [(validate-input-f [[input-id _]] (validate-input! input-id {}))]
         (if-let [invalid-message (some validate-input-f @form.state/FORM-INPUTS)]
                 (do (if on-invalid (let [on-invalid (r/metamorphic-event<-params on-invalid invalid-message)]
                                         (r/dispatch on-invalid)))
                     (return invalid-message))
                 (do (if on-valid (r/dispatch on-valid))
                     (return nil)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-form-input!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:form-id (keyword)
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)
  ;  :value-path (Re-Frame path vector)}
  [input-id input-props]
  (let [validation-props (select-keys input-props [:form-id :validators :value-path :validate-when-change? :validate-when-leave?])]
       (swap! form.state/FORM-INPUTS assoc input-id validation-props)))

(defn remove-form-input!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! form.state/FORM-INPUTS dissoc input-id)
  (swap! form.state/FORM-ERRORS dissoc input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) input-id
; @param (map) validation-props
; {:on-invalid (Re-Frame metamorphic-event)(opt)
;   This event takes the input value and the invalid message as its last parameter.
;  :on-valid (Re-Frame metamorphic-event)(opt)
;   This event takes the input value as its last parameter.}
;
; @usage
; [:elements.form/validate-input! :my-form {...}]
(r/reg-fx :elements.form/validate-input! validate-input!)

; @param (keyword) form-id
; @param (map) validation-props
; {:on-invalid (Re-Frame metamorphic-event)(opt)
;   This event takes the invalid message as its last parameter.
;  :on-valid (Re-Frame metamorphic-event)(opt)}
;
; @usage
; [:elements.form/validate-form! :my-form {...}]
(r/reg-fx :elements.form/validate-form! validate-form!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :elements.form/reg-form-input! reg-form-input!)

; @ignore
(r/reg-fx :elements.form/remove-form-input! remove-form-input!)
