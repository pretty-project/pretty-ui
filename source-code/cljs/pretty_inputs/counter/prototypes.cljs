
(ns pretty-inputs.counter.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-props-prototype
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-radius (map)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :font-size (keyword, px or string)
  ;  :initial-value (integer)
  ;  :value-path (Re-Frame path vector)}
  [counter-id counter-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :font-size       :s
          :initial-value   0}
         (-> counter-props)))
