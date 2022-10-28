
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.file-drop-area.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn area-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) area-props
  ;
  ; @return (map)
  ;  {:color (keyword or string)
  ;   :font-size (keyword)
  ;   :label (metamorphic-content)}
  [area-props]
  (merge {:color     :primary
          :font-size :s
          :label     :drop-files-here-to-upload}
         (param area-props)))
