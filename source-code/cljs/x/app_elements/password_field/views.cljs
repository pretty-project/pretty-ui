
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.password-field.views
    (:require [mid-fruits.random                        :as random]
              [x.app-elements.password-field.prototypes :as password-field.prototypes]
              [x.app-elements.text-field.views          :as text-field.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711
  ; A password-field elem alapkomponense a text-field elem.
  ; A password-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:validate? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [elements/password-field {...}]
  ;
  ; @usage
  ;  [elements/password-field :my-password-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (password-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
