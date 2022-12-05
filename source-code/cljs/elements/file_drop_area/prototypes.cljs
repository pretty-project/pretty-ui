
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.file-drop-area.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn area-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) area-props
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :font-size (keyword)
  ;  :label (metamorphic-content)}
  [area-props]
  (merge {:color     :primary
          :font-size :s
          :label     :drop-files-here-to-upload}
         (param area-props)))
