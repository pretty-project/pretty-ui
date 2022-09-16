
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
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:buttons (keywords in vector)
  ;   :min-height (px)}
  [editor-id editor-props]
  (merge {:buttons    [:bold :italic :underline]
          :min-height 400
          :value-path (helpers/default-value-path editor-id)}
         (param editor-props)))
