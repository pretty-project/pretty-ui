
(ns pretty-layouts.sidebar.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-width (keyword, px or string)
  ;  :position (keyword)
  ;  :threshold (px)}
  [_ {:keys [border-color] :as sidebar-props}]
  (merge {:position  :left
          :threshold 720}
         (if border-color {:border-width :xxs})
         (-> sidebar-props)))
; size-unit :screen
; ;(pretty-rules/auto-disable-highlight-color)) <- stay commented
; ;(pretty-rules/auto-disable-hover-color)) <- stay commented
;(pretty-rules/apply-auto-border-crop)
;(pretty-rules/auto-align-scrollable-flex) ???
