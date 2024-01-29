
(ns pretty-website.language-selector.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)}
  ; {:gap (keyword, px or string)}
  [_ selector-props]
  (merge {:font-size :s
          :gap       :xxs}
         (-> selector-props)))
