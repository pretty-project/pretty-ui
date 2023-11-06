
(ns pretty-elements.column.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :height (keyword)
  ;  :horizontal-align (keyword)
  ;  :vertical-align (keyword)
  ;  :width (keyword)}
  [{:keys [border-color] :as column-props}]
  (merge {:height           :auto
          :horizontal-align :center
          :vertical-align   :top
          :width            :content}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> column-props)))