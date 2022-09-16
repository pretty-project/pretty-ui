
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.search-field.views
    (:require [mid-fruits.random                      :as random]
              [x.app-elements.search-field.prototypes :as search-field.prototypes]
              [x.app-elements.text-field.views        :as text-field.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711
  ; A search-field elem alapkomponense a text-field elem.
  ; A search-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;
  ; @usage
  ;  [elements/search-field {...}]
  ;
  ; @usage
  ;  [elements/search-field :my-search-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (search-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))
