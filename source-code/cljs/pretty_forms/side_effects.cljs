
(ns pretty-forms.side-effects
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [pretty-forms.env   :as env]
              [pretty-forms.state :as state]
              [re-frame.extra.api :as r]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-form-input!
  ; @description
  ; Registers an input as a form input and stores its validator functions in order to make it
  ; validable by the form validator / input validator events.
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:form-id (keyword)(opt)
  ;   Makes the inputs validable in groups by their form ID.
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)
  ;   [{:f (function)
  ;      Takes the actual value of the input as parameter.
  ;     :invalid-message (metamorphic-content)(opt)}]}
  ; @param (function) get-value-f
  ; Must return the actual value of the input.
  ;
  ; @usage
  ; (reg-form-input! :my-input {...} #(...))
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/reg-form-input! :my-input {...} #(...)])
  ;
  ; @usage
  ; (reg-form-input! :my-input
  ;                  {:validators [{:f #(-> % empty? not) :invalid-message "Please fill out this field!"}]}
  ;                  #(deref MY-ATOM))
  ;
  ; @usage
  ; (defn my-ui
  ;   []
  ;   [:<> [pretty-elements/text-field :my-text-field {...}]])
  ; (reg-form-input! :my-text-field
  ;                  {:validators [{:f #(-> % empty? not) :invalid-message "Please fill out this field!"}]}
  ;                  #(pretty-elements.plain-field.env/get-field-content :my-text-field)])
  [input-id input-props get-value-f]
  ; - Input elements have to provide a 'get-value-f' function to the form handler in order to make
  ;   their values IMMEDIATELY available for the form handler when a validation happens.
  ; - Their 'value-path' property is not suitable to get their values immediately because
  ;   some input elements store their values in the application state with a short
  ;   latency, and if a validation fires during that delay, the actual value
  ;   could differ from the value stored in the application state.
  ;   E.g., The 'plain-field', 'text-field', etc. pretty elements store their values delayed.
  ;         HACK#9910 (source-code/cljs/pretty_elements/plain_field/views.cljs)
  (let [validation-props (-> input-props (select-keys [:form-id :validate-when-change? :validate-when-leave? :validators])
                                         (assoc :get-value-f get-value-f))]
       (swap! state/FORM-INPUTS assoc input-id validation-props)))

(defn dereg-form-input!
  ; @description
  ; Removes a specific input from the form handler.
  ;
  ; @param (keyword) input-id
  ;
  ; @usage
  ; (dereg-form-input! :my-input)
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/dereg-form-input! :my-input])
  [input-id]
  (swap! state/FORM-INPUTS dissoc input-id)
  (swap! state/FORM-ERRORS dissoc input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn autovalidate-input!
  ; @description
  ; Sets the ':validate-when-change?' toggle to TRUE of the input registered with the given input ID,
  ; to ensure that the input is getting validated when its value changes.
  ;
  ; @param (keyword) input-id
  ;
  ; @usage
  ; (autovalidate-input! :my-input)
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/autovalidate-input! :my-input])
  [input-id]
  (swap! state/FORM-INPUTS assoc-in [input-id :validate-when-change?] true))

(defn autovalidate-form!
  ; @description
  ; Sets the ':validate-when-change?' toggle to TRUE of all inputs registered with the given form ID,
  ; to ensure that the inputs are getting validated when their value changes.
  ;
  ; @param (keyword) form-id
  ;
  ; @usage
  ; (autovalidate-form! :my-form)
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/autovalidate-form! :my-form])
  [form-id]
  (let [form-inputs (env/get-form-inputs form-id)]
       (doseq [input-id form-inputs]
              (autovalidate-input! input-id))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn validate-input!
  ; @description
  ; - Applies the validators of the input registered with the given input ID ...
  ;   ... and in case of ANY validator failed, it ...
  ;       ... stores the 'invalid-message' value of the first failed validator in the 'FORM-ERRORS' atom,
  ;       ... dispatches the 'on-invalid' event (if any),
  ;   ... and in case of NO validator failed, it ...
  ;       ... clears the previously stored 'invalid-message' value (if any) from the 'FORM-ERRORS' atom,
  ;       ... dispatches the 'on-valid' event (if any),
  ; - Returns TRUE in case of no validator failed, FALSE otherwise.
  ;
  ; @param (keyword) input-id
  ; @param (keyword) validation-props
  ; {:on-invalid (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the input value and the invalid message as parameters.
  ;  :on-valid (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the input value as parameter.}
  ;
  ; @usage
  ; (validate-input! :my-input {...})
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/validate-input! :my-input {...}])
  ;
  ; @return (boolean)
  [input-id {:keys [on-invalid on-valid]}]
  ; @note (#3140)
  ; When an input gets validated, the ':validate-when-change?' toggle must be set as TRUE.
  ; Otherwise, the input can stuck in an invalid state even if it's changed and not invalid anymore.
  (autovalidate-input! input-id)

  (let [validation-result (env/get-input-validation-result input-id)]
       (letfn [(f0 [_] (swap! state/FORM-ERRORS dissoc input-id))
               (f1 [%] (swap! state/FORM-ERRORS assoc  input-id (:invalid-message %)))
               (f2 [%] (pretty-build-kit/dispatch-event-handler! on-valid   (:input-value %)))
               (f3 [%] (pretty-build-kit/dispatch-event-handler! on-invalid (:input-value %) (:invalid-message %)))]
              (when (-> validation-result :input-valid?)
                    (-> validation-result f0)
                    (-> validation-result f2))
              (when (-> validation-result :input-valid? not)
                    (-> validation-result f1)
                    (-> validation-result f3))
              (-> validation-result :input-valid?))))

(defn validate-form!
  ; @description
  ; - Applies the validators of all inputs registered with the given form ID, and ...
  ;   ... it stores the 'invalid-message' values of the INVALID inputs in the 'FORM-ERRORS' atom,
  ;   ... it clears the previously stored 'invalid-message' values of the VALID inputs from the 'FORM-ERRORS' atom.
  ;   ... and in case of ALL inputs are valid, it dispatches the 'on-valid' event.
  ;   ... and in case of ANY input is invalid, it dispatches the 'on-invalid' event.
  ; - Returns TRUE in case of no validator failed, FALSE otherwise.
  ;
  ; @param (keyword) form-id
  ; @param (keyword) validation-props
  ; {:on-invalid (function or Re-Frame metamorphic-event)(opt)
  ;  :on-valid (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @usage
  ; (validate-form! :my-form {...})
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/validate-form! :my-form {...}])
  ;
  ; @return (boolean)
  [form-id {:keys [on-invalid on-valid]}]
  (let [form-inputs (env/get-form-inputs form-id)]
       (letfn [(f0 [validation-results input-id]
                   (conj validation-results {:input-id input-id :input-valid? (validate-input! input-id {})}))]
              (let [validation-results (reduce f0 [] form-inputs)]
                   (letfn [(f0 [_] (pretty-build-kit/dispatch-event-handler! on-valid))
                           (f1 [_] (pretty-build-kit/dispatch-event-handler! on-invalid))]
                          (if (-> validation-results (vector/all-items-match? :input-valid?))
                              (-> validation-results f0)
                              (-> validation-results f1)))
                   (-> validation-results (vector/all-items-match? :input-valid?))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-changed
  ; @description
  ; Calls the 'validate-input!' function if the ':validate-when-change?' toggle of the input is TRUE.
  ;
  ; @param (keyword) input-id
  ; @param (keyword) validation-props
  ; {:on-invalid (function or Re-Frame metamorphic-event)(opt)
  ;  :on-valid (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @usage
  ; (input-changed :my-input {...})
  ;
  ; @usage
  ; (ns my-namespace (:require [re-frame.core :as r]))
  ; (r/dispatch [:pretty-forms/input-changed :my-input {...}])
  [input-id validation-props]
  (if (-> @state/FORM-INPUTS input-id :validate-when-change?)
      (-> input-id (validate-input! validation-props))))

(defn input-left
  ; @note
  ; This function is also registered as a Re-Frame effect event and a Re-Frame side-effect event:
  ; [:pretty-forms/input-left ...]
  ;
  ; @description
  ; Calls the 'validate-input!' function if the ':validate-when-leave?' toggle of the input is TRUE.
  ;
  ; @param (keyword) input-id
  ; @param (keyword) validation-props
  ; {:on-invalid (function or Re-Frame metamorphic-event)(opt)
  ;  :on-valid (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @usage
  ; (input-left :my-input {...})
  [input-id validation-props]
  (if (-> @state/FORM-INPUTS input-id :validate-when-leave?)
      (-> input-id (validate-input! validation-props))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) input-id
; @param (map) input-props
; @param (function) get-value-f
;
; @usage
; [:pretty-forms/reg-form-input! :my-input {...} #(...)]
(r/reg-fx :pretty-forms/reg-form-input! reg-form-input!)

; @param (keyword) input-id
;
; @usage
; [:pretty-forms/dereg-form-input! :my-input]
(r/reg-fx :pretty-forms/dereg-form-input! dereg-form-input!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) input-id
;
; @usage
; [:pretty-forms/autovalidate-input! :my-input]
(r/reg-fx :pretty-forms/autovalidate-input! autovalidate-input!)

; @param (keyword) form-id
;
; @usage
; [:pretty-forms/autovalidate-form! :my-form]
(r/reg-fx :pretty-forms/autovalidate-form! autovalidate-form!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) input-id
; @param (map) validation-props
;
; @usage
; [:pretty-forms/validate-input! :my-input {...}]
(r/reg-fx :pretty-forms/validate-input! validate-input!)

; @param (keyword) form-id
; @param (map) validation-props
;
; @usage
; [:pretty-forms/validate-form! :my-form {...}]
(r/reg-fx :pretty-forms/validate-form! validate-form!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) input-id
; @param (map) validation-props
;
; @usage
; [:pretty-forms/input-changed :my-input {...}]
(r/reg-fx :pretty-forms/input-changed input-changed)

; @param (keyword) input-id
; @param (map) validation-props
;
; @usage
; [:pretty-forms/input-left :my-input {...}]
(r/reg-fx :pretty-forms/input-left input-left)
