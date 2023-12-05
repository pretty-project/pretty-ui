
(ns pretty-elements.content-swapper.attributes
    (:require [pretty-css.api                        :as pretty-css]
              [pretty-elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-animation-direction (keyword)
  ;  :style (map)}
  [swapper-id {:keys [style] :as swapper-props}]
  (-> {:class                    :pe-content-swapper--body
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
  ; {:class (keyword or keywords in vector)}
  [_ swapper-props]
  (-> {:class :pe-content-swapper}
      (pretty-css/default-attributes swapper-props)
      (pretty-css/outdent-attributes swapper-props)))
