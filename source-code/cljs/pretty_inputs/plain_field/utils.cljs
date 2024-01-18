
(ns pretty-inputs.plain-field.utils
    (:require [dom.api :as dom]
              [fruits.string.api :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-event->field-content
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:modifier-f (function)(opt)}
  ; @param (DOM-event) event
  ;
  ; @return (string)
  [_ {:keys [max-length modifier-f type]} event]
  (let [value (if modifier-f (-> event dom/event->value modifier-f)
                             (-> event dom/event->value))]
       ; https://stackoverflow.com/questions/9555143/html-maxlength-attribute-not-working-on-chrome-and-safari
       (if (and max-length (= type :number))
           (string/max-length value max-length)
           (->                value))))
