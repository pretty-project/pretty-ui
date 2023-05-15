
(ns elements.text-field.utils
    (:require [elements.plain-field.utils :as plain-field.utils]
              [elements.text-field.state  :as text-field.state]
              [noop.api                   :refer [return]]
              [re-frame.api               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-content-validator-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content and the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :validators (maps in vector)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)}]}
  ; @param (string) field-content
  [field-id {:keys [on-invalid on-valid validators] :as x} field-content]
  ; XXX#0612 (source-code/cljs/elements/text_field/side_effects.cljs)
  (letfn [(on-valid-f   []
                        (swap! text-field.state/FIELD-CONTENT-INVALID?        dissoc field-id)
                        (swap! text-field.state/FIELD-CONTENT-INVALID-MESSAGE dissoc field-id)
                        (if on-valid (let [on-valid (r/metamorphic-event<-params on-valid field-content)]
                                          (r/dispatch on-valid))))
          (on-invalid-f [{:keys [invalid-message] :as validator}]
                        (swap! text-field.state/FIELD-CONTENT-INVALID?        assoc field-id true)
                        (swap! text-field.state/FIELD-CONTENT-INVALID-MESSAGE assoc field-id invalid-message)
                        (if on-invalid (let [on-invalid (r/metamorphic-event<-params on-invalid field-content invalid-message)]
                                            (r/dispatch on-invalid))))
          (test-f       [{:keys [f] :as validator}]
                        (println validator)
                        (if f (if (f field-content)
                                  (return :validator-passed)
                                  (on-invalid-f validator))))]
         (if (every? test-f validators)
             (on-valid-f))))

(defn on-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (DOM-event) event
  [field-id field-props event]
  (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)]
       (if (field-id @text-field.state/VALIDATE-FIELD-CONTENT?)
           (field-content-validator-f field-id field-props field-content))
       (plain-field.utils/field-changed-f field-id field-props field-content)))
