
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multiline-field.views
    (:require [x.app-core.api                            :as a]
              [x.app-elements.multiline-field.prototypes :as multiline-field.prototypes]
              [x.app-elements.text-field.views           :as text-field.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711
  ; A multiline-field elem alapkomponense a text-field elem.
  ; A multiline-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:max-height (integer)(opt)
  ;    TODO ...
  ;    Max lines count
  ;    Default: 32
  ;   :min-height (integer)(opt)
  ;    TODO ...
  ;    Min lines count
  ;    Default: 1}
  ;
  ; @usage
  ;  [elements/multiline-field {...}]
  ;
  ; @usage
  ;  [elements/multiline-field :my-multiline-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (multiline-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
