
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.search-field.views
    (:require [elements.search-field.prototypes :as search-field.prototypes]
              [elements.text-field.views        :as text-field.views]
              [mid-fruits.random                :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0711 (elements.text-field.views)
  ; A search-field elem alapkomponense a text-field elem.
  ; A search-field elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;
  ; @usage
  ;  [search-field {...}]
  ;
  ; @usage
  ;  [search-field :my-search-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (search-field.prototypes/field-props-prototype field-id field-props)]
        [text-field.views/element field-id field-props])))