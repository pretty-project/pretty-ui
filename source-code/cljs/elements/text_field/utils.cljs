
(ns elements.text-field.utils
    (:require [elements.plain-field.utils :as plain-field.utils]
              [elements.text-field.state  :as text-field.state]
              [re-frame.api               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-valid-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validator (map)
  ;   {:on-valid (Re-Frame metamorphic-event)}}
  ; @param (DOM-event) event
  [field-id {:keys [validator] :as field-props} event]
  (swap! text-field.state/FIELD-CONTENT-INVALID? assoc field-id false)
  (if-let [on-valid (:on-valid validator)]
          (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)
                on-valid      (r/metamorphic-event<-params on-valid field-content)]
               (r/dispatch on-valid))))

(defn on-invalid-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validator (map)
  ;   {:on-invalid (Re-Frame metamorphic-event)}}
  ; @param (DOM-event) event
  [field-id {:keys [validator] :as field-props} event]
  (swap! text-field.state/FIELD-CONTENT-INVALID? assoc field-id true)
  (if-let [on-invalid (:on-invalid validator)]
          (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)
                on-invalid    (r/metamorphic-event<-params on-invalid field-content)]
               (r/dispatch on-invalid))))

(defn field-content-validator-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:validator (map)(opt)}
  ; @param (DOM-event) event
  [field-id {:keys [validator] :as field-props} event]
  (if-let [f (:validator-f validator)]
          (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)]
               (if (f field-content)
                   (on-valid-f   field-id field-props event)
                   (on-invalid-f field-id field-props event)))))

(defn on-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (DOM-event) event
  [field-id field-props event]
  (if (field-id @text-field.state/VALIDATE-FIELD-CONTENT?) (field-content-validator-f field-id field-props event))
  (plain-field.utils/on-change-f field-id field-props event))
