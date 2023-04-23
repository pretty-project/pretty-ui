
(ns elements.content-swapper.attributes
    (:require [elements.content-swapper.state :as content-swapper.state]
              [pretty-css.api                 :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [swapper-id {:keys [style] :as swapper-props}]
  (-> {:class                    :e-content-swapper--body
       :data-animation-direction (-> @content-swapper.state/SWAPPERS swapper-id :animation-direction)
       :style                    style}
      (pretty-css/indent-attributes swapper-props)))

(defn swapper-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {}
  [_ swapper-props]
  (-> {:class :e-content-swapper}
      (pretty-css/default-attributes swapper-props)
      (pretty-css/outdent-attributes swapper-props)))
