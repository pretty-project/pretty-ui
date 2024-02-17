
(ns pretty-layouts.plain-popup.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [on-cover] :as popup-props}]
  (merge {}
         (if on-cover {:cover-color :black})
         (-> popup-props)))
; size-unit :screen
; ;(pretty-rules/auto-disable-highlight-color)) <- stay commented
; ;(pretty-rules/auto-disable-hover-color)) <- stay commented
;(pretty-rules/apply-auto-border-crop)
;(pretty-rules/auto-align-scrollable-flex) ???
