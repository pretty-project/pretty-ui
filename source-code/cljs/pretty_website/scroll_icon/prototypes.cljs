
(ns pretty-website.scroll-icon.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:color (keyword)}
  [_ icon-props]
  (merge {:color "#FFFFFF"}
         (-> icon-props)))
