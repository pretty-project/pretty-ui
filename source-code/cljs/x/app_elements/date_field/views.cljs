
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.date-field.views
    (:require [x.app-core.api                       :as a]
              [x.app-elements.date-field.prototypes :as date-field.prototypes]
              [x.app-elements.text-field.views      :as text-field.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711
  ; A date-field elem alapkomponense a text-field elem.
  ; A date-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:date-from (string)(opt)
  ;    TODO ...
  ;   :date-to (string)(opt)
  ;    TODO ...}
  ;
  ; @usage
  ;  [elements/date-field {...}]
  ;
  ; @usage
  ;  [elements/date-field :my-date-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))