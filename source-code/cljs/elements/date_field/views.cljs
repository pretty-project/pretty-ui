
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.date-field.views
    (:require [elements.date-field.prototypes :as date-field.prototypes]
              [elements.text-field.views      :as text-field.views]
              [random.api                     :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; A date-field elem alapkomponense a text-field elem.
  ; A date-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:date-from (string)(opt)
  ;   :date-to (string)(opt)}
  ;
  ; @usage
  ;  [date-field {...}]
  ;
  ; @usage
  ;  [date-field :my-date-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
