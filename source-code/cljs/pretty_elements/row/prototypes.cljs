
(ns pretty-elements.row.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (map) row-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :height (keyword)
  ;  :horizontal-align (keyword)
  ;  :vertical-align (keyword)
  ;  :width (keyword)}
  [{:keys [border-color] :as row-props}]
  (merge {:height           :auto
          :horizontal-align :left
          :vertical-align   :center
          :width            :content}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> row-props)))
