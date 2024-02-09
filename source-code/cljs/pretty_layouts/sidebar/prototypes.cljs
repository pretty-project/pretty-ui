
(ns pretty-layouts.sidebar.prototypes)

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
