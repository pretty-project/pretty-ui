
(ns pretty-elements.content-swapper.attributes
    (:require [pretty-build-kit.api                  :as pretty-build-kit]
              [pretty-elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-animation-direction (keyword)}
  [swapper-id swapper-props]
  (-> {:class                    :pe-content-swapper--body
       :data-animation-direction (-> @content-swapper.state/SWAPPERS swapper-id :animation-direction)}
      (pretty-build-kit/indent-attributes swapper-props)
      (pretty-build-kit/style-attributes  swapper-props)))

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
      (pretty-build-kit/class-attributes   swapper-props)
      (pretty-build-kit/outdent-attributes swapper-props)
      (pretty-build-kit/state-attributes   swapper-props)))
