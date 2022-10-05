
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.text-editor.prototypes
    (:require [mid-fruits.candy            :refer [param]]
              [plugins.text-editor.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:buttons (keywords in vector)
  ;   :insert-as (keyword)
  ;   :min-height (px)
  ;   :placeholder (metamorphic-content)
  ;   :value-path (vector)}
  [editor-id editor-props]
  (merge {:buttons     [:bold :italic :underline]
          :insert-as   :cleared-html
          :min-height  400
          :placeholder :write-something!
          :value-path  (helpers/default-value-path editor-id)}
         (param editor-props)))
