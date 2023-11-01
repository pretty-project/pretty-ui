
(ns pretty-website.language-selector.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:font-size (keyword)}
  ; {:gap (keyword)}
  [selector-props]
  (merge {:font-size :s
          :gap       :xxs}
         (-> selector-props)))
