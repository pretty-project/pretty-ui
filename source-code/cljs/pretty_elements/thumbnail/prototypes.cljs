
(ns pretty-elements.thumbnail.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; @ignore
  ;
  ; @param (map) thumbnail-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:background-size (keyword)
  ;  :border-color (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :height (keyword, px or string)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword, px or string)}
  [{:keys [border-color] :as thumbnail-props}]
  (merge {:background-size :contain
          :height          :s
          :icon            :image
          :icon-family     :material-symbols-outlined
          :width           :s}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> thumbnail-props)))
